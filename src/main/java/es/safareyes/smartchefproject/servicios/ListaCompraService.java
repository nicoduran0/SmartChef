package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.request.CerrarListaRequest;
import es.safareyes.smartchefproject.dtos.request.ListaDesdeRecetaRequest;
import es.safareyes.smartchefproject.dtos.response.ListaCompraResponse;
import es.safareyes.smartchefproject.excepciones.AccesoDenegadoException;
import es.safareyes.smartchefproject.excepciones.ConflictoException;
import es.safareyes.smartchefproject.excepciones.RecursoNoEncontradoException;
import es.safareyes.smartchefproject.excepciones.ReglaNegocioException;
import es.safareyes.smartchefproject.mappers.ListaCompraMapper;
import es.safareyes.smartchefproject.modelos.Despensa;
import es.safareyes.smartchefproject.modelos.EstadoLista;
import es.safareyes.smartchefproject.modelos.LineaListaCompra;
import es.safareyes.smartchefproject.modelos.ListaCompra;
import es.safareyes.smartchefproject.modelos.Receta;
import es.safareyes.smartchefproject.modelos.RecetaIngrediente;
import es.safareyes.smartchefproject.modelos.Usuario;
import es.safareyes.smartchefproject.repositorios.IDespensaRepository;
import es.safareyes.smartchefproject.repositorios.ILineaListaCompraRepository;
import es.safareyes.smartchefproject.repositorios.IListaCompraRepository;
import es.safareyes.smartchefproject.repositorios.IRecetaRepository;
import es.safareyes.smartchefproject.repositorios.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListaCompraService implements IListaCompraService {

    private final IListaCompraRepository listaRepository;
    private final ILineaListaCompraRepository lineaRepository;
    private final IRecetaRepository recetaRepository;
    private final IDespensaRepository despensaRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ListaCompraMapper listaMapper;

    @Override
    @Transactional
    public ListaCompraResponse anadirDesdeReceta(Long usuarioId, ListaDesdeRecetaRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId));
        Receta receta = recetaRepository.findById(request.recetaId())
                .filter(Receta::isActiva)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la receta con id " + request.recetaId()));

        // Solo puede haber una lista abierta por persona: se reutiliza o se crea
        ListaCompra lista = listaRepository.findByUsuarioIdAndEstado(usuarioId, EstadoLista.ABIERTA)
                .orElseGet(() -> {
                    ListaCompra nueva = new ListaCompra();
                    nueva.setUsuario(usuario);
                    return listaRepository.save(nueva);
                });

        Map<Long, BigDecimal> enDespensa = cantidadesEnDespensa(usuarioId);
        Map<Long, LineaListaCompra> lineasPorIngrediente = new HashMap<>();
        lista.getLineas().forEach(l -> lineasPorIngrediente.put(l.getIngrediente().getId(), l));

        BigDecimal factor = BigDecimal.valueOf(request.raciones())
                .divide(BigDecimal.valueOf(receta.getRaciones()), 6, RoundingMode.HALF_UP);

        for (RecetaIngrediente ri : receta.getIngredientes()) {
            Long ingredienteId = ri.getIngrediente().getId();
            BigDecimal necesaria = ri.getCantidad().multiply(factor).setScale(2, RoundingMode.HALF_UP);
            BigDecimal falta = necesaria.subtract(enDespensa.getOrDefault(ingredienteId, BigDecimal.ZERO));
            if (falta.compareTo(BigDecimal.ZERO) <= 0) {
                continue; // ya lo tiene en casa
            }
            LineaListaCompra existente = lineasPorIngrediente.get(ingredienteId);
            if (existente != null) {
                existente.setCantidad(existente.getCantidad().add(falta));
            } else {
                LineaListaCompra nueva = new LineaListaCompra();
                nueva.setLista(lista);
                nueva.setIngrediente(ri.getIngrediente());
                nueva.setCantidad(falta);
                nueva = lineaRepository.save(nueva);
                lista.getLineas().add(nueva);
            }
        }

        log.info("Lista de la compra {} actualizada con la receta {} para el usuario {}",
                lista.getId(), receta.getId(), usuarioId);
        return listaMapper.toResponse(lista);
    }

    @Override
    @Transactional
    public ListaCompraResponse cerrar(Long listaId, Long usuarioId, CerrarListaRequest request) {
        ListaCompra lista = listaRepository.findById(listaId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la lista de la compra con id " + listaId));
        if (!lista.getUsuario().getId().equals(usuarioId)) {
            throw new AccesoDenegadoException("Esta lista de la compra no es tuya");
        }
        if (lista.getEstado() == EstadoLista.CERRADA) {
            throw new ConflictoException("La lista de la compra " + listaId + " ya está cerrada");
        }

        Set<Long> marcadas = (request == null || request.lineasCompradasIds() == null)
                ? Set.of() : new HashSet<>(request.lineasCompradasIds());
        Set<Long> idsDeLaLista = new HashSet<>();
        lista.getLineas().forEach(l -> idsDeLaLista.add(l.getId()));
        if (!idsDeLaLista.containsAll(marcadas)) {
            throw new ReglaNegocioException("Alguna línea indicada no pertenece a esta lista");
        }

        for (LineaListaCompra linea : lista.getLineas()) {
            if (marcadas.contains(linea.getId())) {
                linea.setComprado(true);
            }
            if (linea.isComprado()) {
                sumarADespensa(lista.getUsuario(), linea);
            }
        }

        lista.setEstado(EstadoLista.CERRADA);
        lista.setCerradaEn(LocalDateTime.now());

        log.info("Lista de la compra {} cerrada por el usuario {}", listaId, usuarioId);
        return listaMapper.toResponse(lista);
    }

    private Map<Long, BigDecimal> cantidadesEnDespensa(Long usuarioId) {
        LocalDate hoy = LocalDate.now();
        Map<Long, BigDecimal> cantidades = new HashMap<>();
        for (Despensa d : despensaRepository.findByUsuarioIdOrderByCaducidadAsc(usuarioId)) {
            // Lo caducado no cuenta
            if (d.getCaducidad() == null || !d.getCaducidad().isBefore(hoy)) {
                cantidades.put(d.getIngrediente().getId(), d.getCantidad());
            }
        }
        return cantidades;
    }

    private void sumarADespensa(Usuario usuario, LineaListaCompra linea) {
        Despensa despensa = despensaRepository
                .findByUsuarioIdAndIngredienteId(usuario.getId(), linea.getIngrediente().getId())
                .orElseGet(() -> {
                    Despensa nueva = new Despensa();
                    nueva.setUsuario(usuario);
                    nueva.setIngrediente(linea.getIngrediente());
                    nueva.setCantidad(BigDecimal.ZERO);
                    return nueva;
                });
        boolean caducada = despensa.getCaducidad() != null
                && despensa.getCaducidad().isBefore(LocalDate.now());
        if (caducada) {
            // Lo viejo ya no sirve: se sustituye por lo recién comprado
            despensa.setCantidad(linea.getCantidad());
            despensa.setCaducidad(null);
        } else {
            despensa.setCantidad(despensa.getCantidad().add(linea.getCantidad()));
        }
        despensaRepository.save(despensa);
    }
}
package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.request.IngredienteCantidadRequest;
import es.safareyes.smartchefproject.dtos.request.PasoRequest;
import es.safareyes.smartchefproject.dtos.request.RecetaRequest;
import es.safareyes.smartchefproject.dtos.response.RecetaDetalleResponse;
import es.safareyes.smartchefproject.dtos.response.RecetaResumenResponse;
import es.safareyes.smartchefproject.excepciones.AccesoDenegadoException;
import es.safareyes.smartchefproject.excepciones.ConflictoException;
import es.safareyes.smartchefproject.excepciones.RecursoNoEncontradoException;
import es.safareyes.smartchefproject.excepciones.ReglaNegocioException;
import es.safareyes.smartchefproject.mappers.RecetaMapper;
import es.safareyes.smartchefproject.modelos.Dificultad;
import es.safareyes.smartchefproject.modelos.Ingrediente;
import es.safareyes.smartchefproject.modelos.Paso;
import es.safareyes.smartchefproject.modelos.Receta;
import es.safareyes.smartchefproject.modelos.RecetaIngrediente;
import es.safareyes.smartchefproject.modelos.RecetaIngredienteId;
import es.safareyes.smartchefproject.modelos.Rol;
import es.safareyes.smartchefproject.modelos.Usuario;
import es.safareyes.smartchefproject.repositorios.IIngredienteRepository;
import es.safareyes.smartchefproject.repositorios.IPasoRepository;
import es.safareyes.smartchefproject.repositorios.IRecetaIngredienteRepository;
import es.safareyes.smartchefproject.repositorios.IRecetaRepository;
import es.safareyes.smartchefproject.repositorios.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecetaService implements IRecetaService {

    private static final BigDecimal LIMITE_ECONOMICA = new BigDecimal("2.50");
    private static final int LIMITE_RAPIDA = 30;

    private final IRecetaRepository recetaRepository;
    private final IRecetaIngredienteRepository recetaIngredienteRepository;
    private final IPasoRepository pasoRepository;
    private final IIngredienteRepository ingredienteRepository;
    private final IUsuarioRepository usuarioRepository;
    private final RecetaMapper recetaMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<RecetaResumenResponse> listar(String titulo, Dificultad dificultad, Short tiempoMaximo,
                                              boolean vegetariana, boolean sinGluten, Pageable pageable) {
        Page<Receta> pagina;
        if (vegetariana) {
            pagina = recetaRepository.buscarVegetarianas(pageable);
        } else if (sinGluten) {
            pagina = recetaRepository.buscarSinGluten(pageable);
        } else {
            pagina = recetaRepository.findAll(
                    recetaRepository.conFiltros(titulo, dificultad, tiempoMaximo), pageable);
        }
        return pagina.map(recetaMapper::toResumen);
    }

    @Override
    @Transactional(readOnly = true)
    public RecetaDetalleResponse obtenerDetalle(Long id) {
        Receta receta = buscarActiva(id);
        log.info("Consultado el detalle de la receta {}", id);
        return construirDetalle(receta);
    }

    @Override
    @Transactional
    public RecetaDetalleResponse crear(RecetaRequest request, Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        if (usuario.getRol() == Rol.USUARIO) {
            throw new AccesoDenegadoException("Solo un chef o un administrador puede publicar recetas");
        }
        if (recetaRepository.existsByAutorIdAndTituloIgnoreCase(usuarioId, request.titulo())) {
            throw new ConflictoException("Ya tienes una receta con el título «" + request.titulo() + "»");
        }
        Map<Long, Ingrediente> ingredientes = cargarIngredientes(request);

        Receta receta = recetaMapper.toEntity(request);
        receta.setAutor(usuario);
        receta = recetaRepository.save(receta);
        guardarLineas(receta, request, ingredientes);

        log.info("Receta {} creada por el usuario {}", receta.getId(), usuarioId);
        return construirDetalle(receta);
    }

    @Override
    @Transactional
    public RecetaDetalleResponse actualizar(Long id, RecetaRequest request, Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        Receta receta = buscarActiva(id);
        comprobarPropietario(receta, usuario);
        if (recetaRepository.existsByAutorIdAndTituloIgnoreCaseAndIdNot(
                receta.getAutor().getId(), request.titulo(), id)) {
            throw new ConflictoException("El autor ya tiene otra receta con el título «" + request.titulo() + "»");
        }
        Map<Long, Ingrediente> ingredientes = cargarIngredientes(request);

        recetaMapper.actualizar(request, receta);

        // Se sustituyen ingredientes y pasos; el flush borra antes de volver a insertar
        recetaIngredienteRepository.deleteAll(receta.getIngredientes());
        pasoRepository.deleteAll(receta.getPasos());
        receta.getIngredientes().clear();
        receta.getPasos().clear();
        recetaRepository.flush();
        guardarLineas(receta, request, ingredientes);

        log.info("Receta {} modificada por el usuario {}", id, usuarioId);
        return construirDetalle(receta);
    }

    @Override
    @Transactional
    public void eliminar(Long id, Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        Receta receta = buscarActiva(id);
        comprobarPropietario(receta, usuario);
        receta.setActiva(false);
        log.info("Receta {} retirada por el usuario {}", id, usuarioId);
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId));
    }

    private Receta buscarActiva(Long id) {
        return recetaRepository.findById(id)
                .filter(Receta::isActiva)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la receta con id " + id));
    }

    private void comprobarPropietario(Receta receta, Usuario usuario) {
        boolean esAdmin = usuario.getRol() == Rol.ADMIN;
        boolean esAutor = receta.getAutor().getId().equals(usuario.getId());
        if (!esAdmin && !esAutor) {
            throw new AccesoDenegadoException("Solo el chef autor o un administrador puede modificar esta receta");
        }
    }

    private Map<Long, Ingrediente> cargarIngredientes(RecetaRequest request) {
        Set<Long> ids = new HashSet<>();
        for (IngredienteCantidadRequest linea : request.ingredientes()) {
            if (!ids.add(linea.ingredienteId())) {
                throw new ReglaNegocioException(
                        "El ingrediente con id " + linea.ingredienteId() + " aparece repetido en la receta");
            }
        }
        Map<Long, Ingrediente> encontrados = ingredienteRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Ingrediente::getId, ingrediente -> ingrediente));
        for (Long ingredienteId : ids) {
            if (!encontrados.containsKey(ingredienteId)) {
                throw new RecursoNoEncontradoException("No existe el ingrediente con id " + ingredienteId);
            }
        }
        return encontrados;
    }

    private void guardarLineas(Receta receta, RecetaRequest request, Map<Long, Ingrediente> ingredientes) {
        List<RecetaIngrediente> lineas = request.ingredientes().stream().map(l -> {
            RecetaIngrediente linea = new RecetaIngrediente();
            linea.setId(new RecetaIngredienteId(receta.getId(), l.ingredienteId()));
            linea.setReceta(receta);
            linea.setIngrediente(ingredientes.get(l.ingredienteId()));
            linea.setCantidad(l.cantidad());
            return linea;
        }).toList();

        List<Paso> pasos = request.pasos().stream()
                .sorted(Comparator.comparing(PasoRequest::numero))
                .map(p -> {
                    Paso paso = recetaMapper.toPasoEntidad(p);
                    paso.setReceta(receta);
                    return paso;
                }).toList();

        receta.getIngredientes().addAll(recetaIngredienteRepository.saveAll(lineas));
        receta.getPasos().addAll(pasoRepository.saveAll(pasos));
    }

    private RecetaDetalleResponse construirDetalle(Receta receta) {
        RecetaDetalleResponse base = recetaMapper.toDetalle(receta);

        BigDecimal raciones = BigDecimal.valueOf(receta.getRaciones());
        BigDecimal costeTotal = valorOCero(recetaIngredienteRepository.calcularCosteTotal(receta.getId()));
        BigDecimal caloriasTotales = valorOCero(recetaIngredienteRepository.calcularCaloriasTotales(receta.getId()));
        BigDecimal costePorRacion = costeTotal.divide(raciones, 2, RoundingMode.HALF_UP);
        BigDecimal caloriasPorRacion = caloriasTotales.divide(raciones, 0, RoundingMode.HALF_UP);

        boolean vegetariana = !receta.getIngredientes().isEmpty()
                && receta.getIngredientes().stream().allMatch(l -> l.getIngrediente().isVegetariano());
        boolean sinGluten = !receta.getIngredientes().isEmpty()
                && receta.getIngredientes().stream().allMatch(l -> l.getIngrediente().isSinGluten());
        boolean rapida = receta.getTiempoPreparacion() <= LIMITE_RAPIDA;
        boolean economica = costePorRacion.compareTo(LIMITE_ECONOMICA) <= 0;

        return new RecetaDetalleResponse(
                base.id(), base.titulo(), base.descripcion(), base.tiempoPreparacion(),
                base.raciones(), base.dificultad(), base.autor(),
                base.ingredientes(), base.pasos(),
                costePorRacion, caloriasPorRacion,
                vegetariana, sinGluten, rapida, economica);
    }

    private BigDecimal valorOCero(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO : valor;
    }
}
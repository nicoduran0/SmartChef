package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.request.RegistroRequest;
import es.safareyes.smartchefproject.dtos.response.RegistroResponse;
import es.safareyes.smartchefproject.dtos.response.ResumenDiaResponse;
import es.safareyes.smartchefproject.dtos.response.SemanaResponse;
import es.safareyes.smartchefproject.excepciones.ConflictoException;
import es.safareyes.smartchefproject.excepciones.RecursoNoEncontradoException;
import es.safareyes.smartchefproject.excepciones.ReglaNegocioException;
import es.safareyes.smartchefproject.mappers.RegistroMapper;
import es.safareyes.smartchefproject.modelos.Receta;
import es.safareyes.smartchefproject.modelos.RecetaIngrediente;
import es.safareyes.smartchefproject.modelos.RegistroComida;
import es.safareyes.smartchefproject.modelos.Usuario;
import es.safareyes.smartchefproject.repositorios.IDespensaRepository;
import es.safareyes.smartchefproject.repositorios.IRecetaRepository;
import es.safareyes.smartchefproject.repositorios.IRegistroComidaRepository;
import es.safareyes.smartchefproject.repositorios.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistroComidaService implements IRegistroComidaService {

    private final IRegistroComidaRepository registroRepository;
    private final IRecetaRepository recetaRepository;
    private final IDespensaRepository despensaRepository;
    private final IUsuarioRepository usuarioRepository;
    private final RegistroMapper registroMapper;

    @Override
    @Transactional
    public RegistroResponse registrar(Long usuarioId, RegistroRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId));

        if (request.fecha().isAfter(LocalDate.now())) {
            throw new ReglaNegocioException("No se pueden registrar comidas de fechas futuras");
        }
        boolean sinDescripcion = request.descripcion() == null || request.descripcion().isBlank();
        if (request.recetaId() == null && sinDescripcion) {
            throw new ReglaNegocioException("Indica una receta o una descripción de la comida libre");
        }
        if (registroRepository.existsByUsuarioIdAndFechaAndMomento(usuarioId, request.fecha(), request.momento())) {
            throw new ConflictoException("Ya has registrado el " + request.momento()
                    + " del día " + request.fecha());
        }

        Receta receta = null;
        if (request.recetaId() != null) {
            receta = recetaRepository.findById(request.recetaId())
                    .filter(Receta::isActiva)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe la receta con id " + request.recetaId()));
        }

        RegistroComida registro = registroMapper.toEntity(request);
        registro.setUsuario(usuario);
        registro.setReceta(receta);
        if (registro.getRaciones() == null) {
            registro.setRaciones((short) 1);
        }
        registro = registroRepository.save(registro);

        if (receta != null && request.descontarDespensa()) {
            descontarDespensa(usuarioId, receta, registro.getRaciones());
        }

        log.info("Registro {} creado para el usuario {} ({} {})",
                registro.getId(), usuarioId, request.fecha(), request.momento());
        return registroMapper.toResponse(registro);
    }

    @Override
    @Transactional(readOnly = true)
    public SemanaResponse semana(Long usuarioId, LocalDate fecha) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId);
        }
        LocalDate base = fecha == null ? LocalDate.now() : fecha;
        LocalDate lunes = base.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate domingo = lunes.plusDays(6);

        Map<LocalDate, BigDecimal> caloriasPorDia = new HashMap<>();
        for (Object[] fila : registroRepository.calcularCaloriasPorDia(usuarioId, lunes, domingo)) {
            caloriasPorDia.put(aFecha(fila[0]), aNumero(fila[1]).setScale(0, RoundingMode.HALF_UP));
        }

        Map<LocalDate, List<RegistroResponse>> registrosPorDia = registroRepository
                .findByUsuarioIdAndFechaBetweenOrderByFechaAsc(usuarioId, lunes, domingo).stream()
                .collect(Collectors.groupingBy(RegistroComida::getFecha,
                        Collectors.mapping(registroMapper::toResponse, Collectors.toList())));

        List<ResumenDiaResponse> dias = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < 7; i++) {
            LocalDate dia = lunes.plusDays(i);
            BigDecimal calorias = caloriasPorDia.getOrDefault(dia, BigDecimal.ZERO);
            total = total.add(calorias);
            dias.add(new ResumenDiaResponse(dia, calorias, registrosPorDia.getOrDefault(dia, List.of())));
        }
        return new SemanaResponse(lunes, domingo, total, dias);
    }

    private void descontarDespensa(Long usuarioId, Receta receta, Short racionesComidas) {
        BigDecimal factor = BigDecimal.valueOf(racionesComidas)
                .divide(BigDecimal.valueOf(receta.getRaciones()), 6, RoundingMode.HALF_UP);

        for (RecetaIngrediente ri : receta.getIngredientes()) {
            BigDecimal usada = ri.getCantidad().multiply(factor).setScale(2, RoundingMode.HALF_UP);
            despensaRepository.findByUsuarioIdAndIngredienteId(usuarioId, ri.getIngrediente().getId())
                    .ifPresent(despensa -> {
                        BigDecimal restante = despensa.getCantidad().subtract(usada);
                        if (restante.compareTo(BigDecimal.ZERO) <= 0) {
                            // La tabla exige cantidad > 0: al llegar a cero se elimina la fila
                            despensaRepository.delete(despensa);
                        } else {
                            despensa.setCantidad(restante);
                            despensaRepository.save(despensa);
                        }
                    });
        }
    }

    // La consulta nativa devuelve la fecha como java.sql.Date o LocalDate según el driver
    private LocalDate aFecha(Object valor) {
        if (valor instanceof LocalDate fecha) {
            return fecha;
        }
        if (valor instanceof java.sql.Date fecha) {
            return fecha.toLocalDate();
        }
        return LocalDate.parse(valor.toString());
    }

    private BigDecimal aNumero(Object valor) {
        return valor == null ? BigDecimal.ZERO : new BigDecimal(valor.toString());
    }
}
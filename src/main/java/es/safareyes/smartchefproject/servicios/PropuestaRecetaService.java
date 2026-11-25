package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.response.PropuestaRecetaResponse;
import es.safareyes.smartchefproject.excepciones.RecursoNoEncontradoException;
import es.safareyes.smartchefproject.excepciones.ReglaNegocioException;
import es.safareyes.smartchefproject.mappers.PropuestaRecetaMapper;
import es.safareyes.smartchefproject.modelos.Despensa;
import es.safareyes.smartchefproject.modelos.Receta;
import es.safareyes.smartchefproject.modelos.RecetaIngrediente;
import es.safareyes.smartchefproject.repositorios.IDespensaRepository;
import es.safareyes.smartchefproject.repositorios.IRecetaRepository;
import es.safareyes.smartchefproject.repositorios.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropuestaRecetaService implements IPropuestaRecetaService {

    private final IRecetaRepository recetaRepository;
    private final IDespensaRepository despensaRepository;
    private final IUsuarioRepository usuarioRepository;
    private final PropuestaRecetaMapper propuestaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PropuestaRecetaResponse> buscar(Long usuarioId, List<Long> ingredientesIds) {
        Set<Long> disponibles = obtenerDisponibles(usuarioId, ingredientesIds);
        List<Receta> recetas = recetaRepository.findByActivaTrue(Pageable.unpaged()).getContent();

        List<PropuestaRecetaResponse> propuestas = new ArrayList<>();
        for (Receta receta : recetas) {
            List<RecetaIngrediente> lineas = receta.getIngredientes();
            int total = lineas.size();
            if (total == 0) {
                continue;
            }
            List<String> faltan = lineas.stream()
                    .filter(l -> !disponibles.contains(l.getIngrediente().getId()))
                    .map(l -> l.getIngrediente().getNombre())
                    .toList();
            int tiene = total - faltan.size();

            
            if (tiene * 2 <= total) {
                continue;
            }
            BigDecimal porcentaje = BigDecimal.valueOf(tiene * 100L)
                    .divide(BigDecimal.valueOf(total), 1, RoundingMode.HALF_UP);

            PropuestaRecetaResponse base = propuestaMapper.toPropuesta(receta);
            propuestas.add(new PropuestaRecetaResponse(
                    base.id(), base.titulo(), base.tiempoPreparacion(), base.raciones(),
                    base.dificultad(), porcentaje, faltan));
        }
        propuestas.sort(Comparator.comparing(PropuestaRecetaResponse::porcentajeCoincidencia).reversed());

        log.info("Búsqueda por ingredientes: {} propuestas", propuestas.size());
        return propuestas;
    }

    private Set<Long> obtenerDisponibles(Long usuarioId, List<Long> ingredientesIds) {
        if (ingredientesIds != null && !ingredientesIds.isEmpty()) {
            return new HashSet<>(ingredientesIds);
        }
        if (usuarioId == null) {
            throw new ReglaNegocioException(
                    "Indica una lista de ingredientes o tu usuario para usar tu despensa");
        }
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId);
        }
        LocalDate hoy = LocalDate.now();

        return despensaRepository.findByUsuarioIdOrderByCaducidadAsc(usuarioId).stream()
                .filter(d -> d.getCaducidad() == null || !d.getCaducidad().isBefore(hoy))
                .map(Despensa::getIngrediente)
                .map(ingrediente -> ingrediente.getId())
                .collect(Collectors.toSet());
    }
}
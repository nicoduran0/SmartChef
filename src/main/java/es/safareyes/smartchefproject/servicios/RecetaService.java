package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.response.RecetaDetalleResponse;
import es.safareyes.smartchefproject.excepciones.RecursoNoEncontradoException;
import es.safareyes.smartchefproject.mappers.RecetaMapper;
import es.safareyes.smartchefproject.modelos.Receta;
import es.safareyes.smartchefproject.modelos.RecetaIngrediente;
import es.safareyes.smartchefproject.repositorios.IRecetaIngredienteRepository;
import es.safareyes.smartchefproject.repositorios.IRecetaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecetaService implements IRecetaService {

    private static final BigDecimal LIMITE_ECONOMICA = new BigDecimal("2.50");
    private static final int LIMITE_RAPIDA = 30;

    private final IRecetaRepository recetaRepository;
    private final IRecetaIngredienteRepository recetaIngredienteRepository;
    private final RecetaMapper recetaMapper;

    @Override
    @Transactional(readOnly = true)
    public RecetaDetalleResponse obtenerDetalle(Long id) {
        Receta receta = recetaRepository.findById(id)
                .filter(Receta::isActiva)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la receta con id " + id));

        RecetaDetalleResponse base = recetaMapper.toDetalle(receta);

        BigDecimal raciones = BigDecimal.valueOf(receta.getRaciones());
        BigDecimal costeTotal = valorOCero(recetaIngredienteRepository.calcularCosteTotal(id));
        BigDecimal caloriasTotales = valorOCero(recetaIngredienteRepository.calcularCaloriasTotales(id));
        BigDecimal costePorRacion = costeTotal.divide(raciones, 2, RoundingMode.HALF_UP);
        BigDecimal caloriasPorRacion = caloriasTotales.divide(raciones, 0, RoundingMode.HALF_UP);

        boolean vegetariana = !receta.getIngredientes().isEmpty()
                && receta.getIngredientes().stream().allMatch(l -> l.getIngrediente().isVegetariano());
        boolean sinGluten = !receta.getIngredientes().isEmpty()
                && receta.getIngredientes().stream().allMatch(l -> l.getIngrediente().isSinGluten());
        boolean rapida = receta.getTiempoPreparacion() <= LIMITE_RAPIDA;
        boolean economica = costePorRacion.compareTo(LIMITE_ECONOMICA) <= 0;

        log.info("Consultado el detalle de la receta {}", id);

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
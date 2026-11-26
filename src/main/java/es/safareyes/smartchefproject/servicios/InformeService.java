package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.response.RecetaPopularDto;
import es.safareyes.smartchefproject.dtos.response.RecetasPopularesDto;
import es.safareyes.smartchefproject.excepciones.ReglaNegocioException;
import es.safareyes.smartchefproject.mappers.InformeMapper;
import es.safareyes.smartchefproject.modelos.Receta;
import es.safareyes.smartchefproject.repositorios.IColeccionRecetaRepository;
import es.safareyes.smartchefproject.repositorios.IRegistroComidaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InformeService implements IInformeService {

    private static final int TAMANO_RANKING = 10;
    private static final int DIAS_POR_DEFECTO = 30;

    private final IRegistroComidaRepository registroRepository;
    private final IColeccionRecetaRepository coleccionRecetaRepository;
    private final InformeMapper informeMapper;

    @Override
    @Transactional(readOnly = true)
    public RecetasPopularesDto recetasPopulares(LocalDate desde, LocalDate hasta) {
        LocalDate fin = hasta == null ? LocalDate.now() : hasta;
        LocalDate inicio = desde == null ? fin.minusDays(DIAS_POR_DEFECTO) : desde;
        if (inicio.isAfter(fin)) {
            throw new ReglaNegocioException("La fecha 'desde' no puede ser posterior a 'hasta'");
        }

        List<RecetaPopularDto> masCocinadas = registroRepository.buscarRecetasMasCocinadas(inicio, fin)
                .stream().limit(TAMANO_RANKING).map(this::aPopular).toList();
        List<RecetaPopularDto> masGuardadas = coleccionRecetaRepository
                .buscarRecetasMasGuardadas(inicio.atStartOfDay(), fin.atTime(LocalTime.MAX))
                .stream().limit(TAMANO_RANKING).map(this::aPopular).toList();

        log.info("Informe de recetas populares entre {} y {}", inicio, fin);
        return new RecetasPopularesDto(inicio, fin, masCocinadas, masGuardadas);
    }

    private RecetaPopularDto aPopular(Object[] fila) {
        return informeMapper.toPopular((Receta) fila[0], ((Number) fila[1]).longValue());
    }
}
package es.safareyes.smartchefproject.dtos.response;

import java.time.LocalDate;
import java.util.List;

public record RecetasPopularesDto(
        LocalDate desde,
        LocalDate hasta,
        List<RecetaPopularDto> masCocinadas,
        List<RecetaPopularDto> masGuardadas
) {
}
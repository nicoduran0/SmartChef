package es.safareyes.smartchefproject.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SemanaResponse(
        LocalDate desde,
        LocalDate hasta,
        BigDecimal caloriasTotales,
        List<ResumenDiaResponse> dias
) {
}
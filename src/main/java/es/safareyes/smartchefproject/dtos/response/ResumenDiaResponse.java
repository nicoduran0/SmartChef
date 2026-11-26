package es.safareyes.smartchefproject.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ResumenDiaResponse(
        LocalDate fecha,
        BigDecimal calorias,
        List<RegistroResponse> registros
) {
}
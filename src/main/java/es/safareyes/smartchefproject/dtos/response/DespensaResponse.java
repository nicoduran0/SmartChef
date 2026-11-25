package es.safareyes.smartchefproject.dtos.response;

import es.safareyes.smartchefproject.modelos.UnidadMedida;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespensaResponse(
        Long ingredienteId,
        String ingrediente,
        UnidadMedida unidad,
        BigDecimal cantidad,
        LocalDate caducidad,
        boolean caducado
) {
}
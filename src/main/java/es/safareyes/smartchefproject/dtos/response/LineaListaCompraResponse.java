package es.safareyes.smartchefproject.dtos.response;

import es.safareyes.smartchefproject.modelos.UnidadMedida;

import java.math.BigDecimal;

public record LineaListaCompraResponse(
        Long id,
        Long ingredienteId,
        String ingrediente,
        UnidadMedida unidad,
        BigDecimal cantidad,
        boolean comprado
) {
}
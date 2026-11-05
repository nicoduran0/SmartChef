package es.safareyes.smartchefproject.dtos.response;

import es.safareyes.smartchefproject.modelos.UnidadMedida;

import java.math.BigDecimal;

public record IngredienteRecetaResponse(
        Long ingredienteId,
        String nombre,
        UnidadMedida unidad,
        BigDecimal cantidad
) {
}
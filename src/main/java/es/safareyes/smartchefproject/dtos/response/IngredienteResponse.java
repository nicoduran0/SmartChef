package es.safareyes.smartchefproject.dtos.response;

import es.safareyes.smartchefproject.modelos.UnidadMedida;

import java.math.BigDecimal;

public record IngredienteResponse(
        Long id,
        String nombre,
        UnidadMedida unidad,
        BigDecimal calorias,
        BigDecimal precio,
        boolean vegetariano,
        boolean sinGluten
) {
}
package es.safareyes.smartchefproject.dtos.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BuscarPorIngredientesRequest(
        List<@NotNull(message = "Los ids de ingredientes no pueden ser nulos") Long> ingredientesIds
) {
}
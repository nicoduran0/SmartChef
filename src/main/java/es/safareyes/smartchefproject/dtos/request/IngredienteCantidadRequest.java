package es.safareyes.smartchefproject.dtos.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record IngredienteCantidadRequest(
        @NotNull(message = "El ingrediente es obligatorio")
        Long ingredienteId,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor que 0")
        @Digits(integer = 8, fraction = 2, message = "La cantidad admite como máximo 2 decimales")
        BigDecimal cantidad
) {
}
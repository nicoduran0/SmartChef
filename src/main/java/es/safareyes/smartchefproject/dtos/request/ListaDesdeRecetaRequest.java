package es.safareyes.smartchefproject.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ListaDesdeRecetaRequest(
        @NotNull(message = "La receta es obligatoria")
        Long recetaId,

        @NotNull(message = "Las raciones son obligatorias")
        @Min(value = 1, message = "Mínimo 1 ración")
        @Max(value = 50, message = "Máximo 50 raciones")
        Integer raciones
) {
}
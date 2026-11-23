package es.safareyes.smartchefproject.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PasoRequest(
        @NotNull(message = "El número del paso es obligatorio")
        @Positive(message = "El número del paso debe ser mayor que 0")
        Short numero,

        @NotBlank(message = "La descripción del paso es obligatoria")
        String descripcion
) {
}
package es.safareyes.smartchefproject.dtos.request;

import es.safareyes.smartchefproject.modelos.Dificultad;
import es.safareyes.smartchefproject.validadores.PasosConsecutivos;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RecetaRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título admite como máximo 150 caracteres")
        String titulo,

        String descripcion,

        @NotNull(message = "El tiempo de preparación es obligatorio")
        @Positive(message = "El tiempo de preparación debe ser mayor que 0")
        Short tiempoPreparacion,

        @NotNull(message = "Las raciones son obligatorias")
        @Min(value = 1, message = "Mínimo 1 ración")
        @Max(value = 12, message = "Máximo 12 raciones")
        Short raciones,

        @NotNull(message = "La dificultad es obligatoria")
        Dificultad dificultad,

        @NotEmpty(message = "La receta necesita al menos un ingrediente")
        @Size(max = 30, message = "Una receta admite como máximo 30 ingredientes")
        @Valid
        List<IngredienteCantidadRequest> ingredientes,

        @NotEmpty(message = "La receta necesita al menos un paso")
        @PasosConsecutivos
        @Valid
        List<PasoRequest> pasos
) {
}
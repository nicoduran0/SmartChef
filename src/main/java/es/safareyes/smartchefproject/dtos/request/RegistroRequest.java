package es.safareyes.smartchefproject.dtos.request;

import es.safareyes.smartchefproject.modelos.MomentoComida;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegistroRequest(
        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        @NotNull(message = "El momento es obligatorio (DESAYUNO, COMIDA o CENA)")
        MomentoComida momento,

        Long recetaId,

        @Min(value = 1, message = "Mínimo 1 ración")
        @Max(value = 50, message = "Máximo 50 raciones")
        Short raciones,

        @Size(max = 255, message = "La descripción admite como máximo 255 caracteres")
        String descripcion,

        @PositiveOrZero(message = "Las calorías no pueden ser negativas")
        Integer calorias,

        Boolean descontarDespensa
) {
}
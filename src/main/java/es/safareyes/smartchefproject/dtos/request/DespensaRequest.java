package es.safareyes.smartchefproject.dtos.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespensaRequest(
        @NotNull(message = "La cantidad es obligatoria")
        @PositiveOrZero(message = "La cantidad no puede ser negativa")
        @Digits(integer = 8, fraction = 2, message = "La cantidad admite como máximo 2 decimales")
        BigDecimal cantidad,

        LocalDate caducidad
) {
}
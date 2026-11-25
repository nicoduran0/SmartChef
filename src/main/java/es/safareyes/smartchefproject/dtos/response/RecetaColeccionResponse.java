package es.safareyes.smartchefproject.dtos.response;

import java.time.LocalDateTime;

public record RecetaColeccionResponse(
        Long id,
        String titulo,
        boolean retirada,
        LocalDateTime agregadaEn
) {
}
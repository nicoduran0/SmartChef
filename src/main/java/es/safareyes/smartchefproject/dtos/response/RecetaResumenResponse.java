package es.safareyes.smartchefproject.dtos.response;

import es.safareyes.smartchefproject.modelos.Dificultad;

public record RecetaResumenResponse(
        Long id,
        String titulo,
        String autor,
        Short tiempoPreparacion,
        Short raciones,
        Dificultad dificultad
) {
}
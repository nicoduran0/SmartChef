package es.safareyes.smartchefproject.dtos.response;

import java.util.List;

public record ColeccionResponse(
        Long id,
        String nombre,
        boolean esFavoritas,
        List<RecetaColeccionResponse> recetas
) {
}
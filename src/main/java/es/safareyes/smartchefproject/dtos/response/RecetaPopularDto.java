package es.safareyes.smartchefproject.dtos.response;

public record RecetaPopularDto(
        Long recetaId,
        String titulo,
        long veces
) {
}
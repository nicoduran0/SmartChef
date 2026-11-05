package es.safareyes.smartchefproject.dtos.response;

import es.safareyes.smartchefproject.modelos.Dificultad;

import java.math.BigDecimal;
import java.util.List;

public record RecetaDetalleResponse(
        Long id,
        String titulo,
        String descripcion,
        Short tiempoPreparacion,
        Short raciones,
        Dificultad dificultad,
        String autor,
        List<IngredienteRecetaResponse> ingredientes,
        List<PasoResponse> pasos,
        BigDecimal costePorRacion,
        BigDecimal caloriasPorRacion,
        boolean vegetariana,
        boolean sinGluten,
        boolean rapida,
        boolean economica
) {
}
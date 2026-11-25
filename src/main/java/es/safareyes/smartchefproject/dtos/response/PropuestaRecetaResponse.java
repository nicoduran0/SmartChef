package es.safareyes.smartchefproject.dtos.response;

import es.safareyes.smartchefproject.modelos.Dificultad;

import java.math.BigDecimal;
import java.util.List;

public record PropuestaRecetaResponse(
        Long id,
        String titulo,
        Short tiempoPreparacion,
        Short raciones,
        Dificultad dificultad,
        BigDecimal porcentajeCoincidencia,
        List<String> ingredientesFaltantes
) {
}
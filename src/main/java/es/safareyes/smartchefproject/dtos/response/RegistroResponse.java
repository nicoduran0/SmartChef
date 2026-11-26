package es.safareyes.smartchefproject.dtos.response;

import es.safareyes.smartchefproject.modelos.MomentoComida;

import java.time.LocalDate;

public record RegistroResponse(
        Long id,
        LocalDate fecha,
        MomentoComida momento,
        Long recetaId,
        String receta,
        boolean recetaRetirada,
        Short raciones,
        String descripcion,
        Integer calorias
) {
}
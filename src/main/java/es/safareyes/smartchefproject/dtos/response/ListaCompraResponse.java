package es.safareyes.smartchefproject.dtos.response;

import es.safareyes.smartchefproject.modelos.EstadoLista;

import java.time.LocalDateTime;
import java.util.List;

public record ListaCompraResponse(
        Long id,
        EstadoLista estado,
        LocalDateTime creadaEn,
        LocalDateTime cerradaEn,
        List<LineaListaCompraResponse> lineas
) {
}
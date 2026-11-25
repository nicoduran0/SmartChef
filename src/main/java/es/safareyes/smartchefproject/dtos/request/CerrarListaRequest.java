package es.safareyes.smartchefproject.dtos.request;

import java.util.List;

public record CerrarListaRequest(
        List<Long> lineasCompradasIds
) {
}
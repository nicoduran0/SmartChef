package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.response.PropuestaRecetaResponse;

import java.util.List;

public interface IPropuestaRecetaService {

    List<PropuestaRecetaResponse> buscar(Long usuarioId, List<Long> ingredientesIds);
}
package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.response.ColeccionResponse;

import java.util.List;

public interface IColeccionService {

    List<ColeccionResponse> listar(Long usuarioId);

    ColeccionResponse guardarReceta(Long usuarioId, String nombre, Long recetaId);
}
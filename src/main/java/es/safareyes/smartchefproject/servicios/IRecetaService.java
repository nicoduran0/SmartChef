package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.response.RecetaDetalleResponse;

public interface IRecetaService {

    RecetaDetalleResponse obtenerDetalle(Long id);
}
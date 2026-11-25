package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.request.CerrarListaRequest;
import es.safareyes.smartchefproject.dtos.request.ListaDesdeRecetaRequest;
import es.safareyes.smartchefproject.dtos.response.ListaCompraResponse;

public interface IListaCompraService {

    ListaCompraResponse anadirDesdeReceta(Long usuarioId, ListaDesdeRecetaRequest request);

    ListaCompraResponse cerrar(Long listaId, Long usuarioId, CerrarListaRequest request);
}
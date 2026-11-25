package es.safareyes.smartchefproject.mappers;

import es.safareyes.smartchefproject.dtos.response.LineaListaCompraResponse;
import es.safareyes.smartchefproject.dtos.response.ListaCompraResponse;
import es.safareyes.smartchefproject.modelos.LineaListaCompra;
import es.safareyes.smartchefproject.modelos.ListaCompra;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ListaCompraMapper {

    ListaCompraResponse toResponse(ListaCompra lista);

    @Mapping(target = "ingredienteId", source = "ingrediente.id")
    @Mapping(target = "ingrediente", source = "ingrediente.nombre")
    @Mapping(target = "unidad", source = "ingrediente.unidad")
    LineaListaCompraResponse toLinea(LineaListaCompra linea);

    List<LineaListaCompraResponse> toLineas(List<LineaListaCompra> lineas);
}
package es.safareyes.smartchefproject.mappers;

import es.safareyes.smartchefproject.dtos.response.ColeccionResponse;
import es.safareyes.smartchefproject.dtos.response.RecetaColeccionResponse;
import es.safareyes.smartchefproject.modelos.Coleccion;
import es.safareyes.smartchefproject.modelos.ColeccionReceta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ColeccionMapper {

    ColeccionResponse toResponse(Coleccion coleccion);

    List<ColeccionResponse> toResponseList(List<Coleccion> colecciones);

    @Mapping(target = "id", source = "receta.id")
    @Mapping(target = "titulo", source = "receta.titulo")
    @Mapping(target = "retirada", expression = "java(!coleccionReceta.getReceta().isActiva())")
    RecetaColeccionResponse toRecetaColeccion(ColeccionReceta coleccionReceta);
}
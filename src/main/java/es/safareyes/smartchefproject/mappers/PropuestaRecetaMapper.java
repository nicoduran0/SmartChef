package es.safareyes.smartchefproject.mappers;

import es.safareyes.smartchefproject.dtos.response.PropuestaRecetaResponse;
import es.safareyes.smartchefproject.modelos.Receta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PropuestaRecetaMapper {

    @Mapping(target = "porcentajeCoincidencia", ignore = true)
    @Mapping(target = "ingredientesFaltantes", ignore = true)
    PropuestaRecetaResponse toPropuesta(Receta receta);
}
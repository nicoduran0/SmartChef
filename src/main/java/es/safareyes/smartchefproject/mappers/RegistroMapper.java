package es.safareyes.smartchefproject.mappers;

import es.safareyes.smartchefproject.dtos.request.RegistroRequest;
import es.safareyes.smartchefproject.dtos.response.RegistroResponse;
import es.safareyes.smartchefproject.modelos.RegistroComida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistroMapper {

    @Mapping(target = "recetaId", source = "receta.id")
    @Mapping(target = "receta", source = "receta.titulo")
    @Mapping(target = "recetaRetirada",
            expression = "java(registro.getReceta() != null && !registro.getReceta().isActiva())")
    RegistroResponse toResponse(RegistroComida registro);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "receta", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    RegistroComida toEntity(RegistroRequest request);
}
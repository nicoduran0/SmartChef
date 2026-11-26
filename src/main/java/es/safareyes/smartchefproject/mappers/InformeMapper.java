package es.safareyes.smartchefproject.mappers;

import es.safareyes.smartchefproject.dtos.response.RecetaPopularDto;
import es.safareyes.smartchefproject.modelos.Receta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InformeMapper {

    @Mapping(target = "recetaId", source = "receta.id")
    @Mapping(target = "titulo", source = "receta.titulo")
    @Mapping(target = "veces", source = "veces")
    RecetaPopularDto toPopular(Receta receta, long veces);
}
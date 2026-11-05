package es.safareyes.smartchefproject.mappers;

import es.safareyes.smartchefproject.dtos.response.IngredienteResponse;
import es.safareyes.smartchefproject.modelos.Ingrediente;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredienteMapper {

    IngredienteResponse toResponse(Ingrediente ingrediente);

    List<IngredienteResponse> toResponseList(List<Ingrediente> ingredientes);
}
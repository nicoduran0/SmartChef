package es.safareyes.smartchefproject.mappers;

import es.safareyes.smartchefproject.dtos.response.IngredienteRecetaResponse;
import es.safareyes.smartchefproject.dtos.response.PasoResponse;
import es.safareyes.smartchefproject.dtos.response.RecetaDetalleResponse;
import es.safareyes.smartchefproject.modelos.Paso;
import es.safareyes.smartchefproject.modelos.Receta;
import es.safareyes.smartchefproject.modelos.RecetaIngrediente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecetaMapper {

    @Mapping(target = "autor", source = "autor.nombre")
    @Mapping(target = "costePorRacion", ignore = true)
    @Mapping(target = "caloriasPorRacion", ignore = true)
    @Mapping(target = "vegetariana", ignore = true)
    @Mapping(target = "sinGluten", ignore = true)
    @Mapping(target = "rapida", ignore = true)
    @Mapping(target = "economica", ignore = true)
    RecetaDetalleResponse toDetalle(Receta receta);

    @Mapping(target = "ingredienteId", source = "ingrediente.id")
    @Mapping(target = "nombre", source = "ingrediente.nombre")
    @Mapping(target = "unidad", source = "ingrediente.unidad")
    IngredienteRecetaResponse toIngredienteReceta(RecetaIngrediente lineaReceta);

    PasoResponse toPaso(Paso paso);

    List<IngredienteRecetaResponse> toIngredientesReceta(List<RecetaIngrediente> lineas);

    List<PasoResponse> toPasos(List<Paso> pasos);
}
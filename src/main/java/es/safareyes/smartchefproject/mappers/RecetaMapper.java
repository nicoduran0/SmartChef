package es.safareyes.smartchefproject.mappers;

import es.safareyes.smartchefproject.dtos.request.PasoRequest;
import es.safareyes.smartchefproject.dtos.request.RecetaRequest;
import es.safareyes.smartchefproject.dtos.response.IngredienteRecetaResponse;
import es.safareyes.smartchefproject.dtos.response.PasoResponse;
import es.safareyes.smartchefproject.dtos.response.RecetaDetalleResponse;
import es.safareyes.smartchefproject.dtos.response.RecetaResumenResponse;
import es.safareyes.smartchefproject.modelos.Paso;
import es.safareyes.smartchefproject.modelos.Receta;
import es.safareyes.smartchefproject.modelos.RecetaIngrediente;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

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

    @Mapping(target = "autor", source = "autor.nombre")
    RecetaResumenResponse toResumen(Receta receta);

    @Mapping(target = "ingredienteId", source = "ingrediente.id")
    @Mapping(target = "nombre", source = "ingrediente.nombre")
    @Mapping(target = "unidad", source = "ingrediente.unidad")
    IngredienteRecetaResponse toIngredienteReceta(RecetaIngrediente lineaReceta);

    PasoResponse toPaso(Paso paso);

    List<IngredienteRecetaResponse> toIngredientesReceta(List<RecetaIngrediente> lineas);

    List<PasoResponse> toPasos(List<Paso> pasos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "autor", ignore = true)
    @Mapping(target = "activa", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    @Mapping(target = "actualizadoEn", ignore = true)
    @Mapping(target = "ingredientes", ignore = true)
    @Mapping(target = "pasos", ignore = true)
    Receta toEntity(RecetaRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "autor", ignore = true)
    @Mapping(target = "activa", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    @Mapping(target = "actualizadoEn", ignore = true)
    @Mapping(target = "ingredientes", ignore = true)
    @Mapping(target = "pasos", ignore = true)
    void actualizar(RecetaRequest request, @MappingTarget Receta receta);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receta", ignore = true)
    Paso toPasoEntidad(PasoRequest request);
}
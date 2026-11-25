package es.safareyes.smartchefproject.mappers;

import es.safareyes.smartchefproject.dtos.response.DespensaResponse;
import es.safareyes.smartchefproject.modelos.Despensa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DespensaMapper {

    @Mapping(target = "ingredienteId", source = "ingrediente.id")
    @Mapping(target = "ingrediente", source = "ingrediente.nombre")
    @Mapping(target = "unidad", source = "ingrediente.unidad")
    @Mapping(target = "caducado",
            expression = "java(despensa.getCaducidad() != null && despensa.getCaducidad().isBefore(java.time.LocalDate.now()))")
    DespensaResponse toResponse(Despensa despensa);

    List<DespensaResponse> toResponseList(List<Despensa> despensas);
}
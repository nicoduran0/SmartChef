package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.request.DespensaRequest;
import es.safareyes.smartchefproject.dtos.response.DespensaResponse;

import java.util.List;
import java.util.Optional;

public interface IDespensaService {

    List<DespensaResponse> listar(Long usuarioId);

    Optional<DespensaResponse> guardar(Long usuarioId, Long ingredienteId, DespensaRequest request);
}
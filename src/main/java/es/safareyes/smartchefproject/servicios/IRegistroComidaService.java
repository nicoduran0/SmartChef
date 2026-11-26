package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.request.RegistroRequest;
import es.safareyes.smartchefproject.dtos.response.RegistroResponse;
import es.safareyes.smartchefproject.dtos.response.SemanaResponse;

import java.time.LocalDate;

public interface IRegistroComidaService {

    RegistroResponse registrar(Long usuarioId, RegistroRequest request);

    SemanaResponse semana(Long usuarioId, LocalDate fecha);
}
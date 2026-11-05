package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.response.IngredienteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IIngredienteService {

    Page<IngredienteResponse> buscar(String texto, Pageable pageable);
}
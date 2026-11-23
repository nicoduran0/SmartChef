package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.response.RecetaDetalleResponse;
import es.safareyes.smartchefproject.dtos.response.RecetaResumenResponse;
import es.safareyes.smartchefproject.modelos.Dificultad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRecetaService {

    Page<RecetaResumenResponse> listar(String titulo, Dificultad dificultad, Short tiempoMaximo,
                                       boolean vegetariana, boolean sinGluten, Pageable pageable);

    RecetaDetalleResponse obtenerDetalle(Long id);
}
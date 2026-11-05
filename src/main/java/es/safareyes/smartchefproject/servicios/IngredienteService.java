package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.response.IngredienteResponse;
import es.safareyes.smartchefproject.mappers.IngredienteMapper;
import es.safareyes.smartchefproject.repositorios.IIngredienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IngredienteService implements IIngredienteService {

    private final IIngredienteRepository ingredienteRepository;
    private final IngredienteMapper ingredienteMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<IngredienteResponse> buscar(String texto, Pageable pageable) {
        String filtro = texto == null ? "" : texto;
        return ingredienteRepository
                .findByNombreContainingIgnoreCase(filtro, pageable)
                .map(ingredienteMapper::toResponse);
    }
}
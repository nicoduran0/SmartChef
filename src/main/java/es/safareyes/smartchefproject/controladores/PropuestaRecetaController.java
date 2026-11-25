package es.safareyes.smartchefproject.controladores;

import es.safareyes.smartchefproject.dtos.request.BuscarPorIngredientesRequest;
import es.safareyes.smartchefproject.dtos.response.PropuestaRecetaResponse;
import es.safareyes.smartchefproject.servicios.IPropuestaRecetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recetas")
@RequiredArgsConstructor
public class PropuestaRecetaController {

    private final IPropuestaRecetaService propuestaRecetaService;
    
    @PostMapping("/buscar-por-ingredientes")
    public List<PropuestaRecetaResponse> buscar(
            @RequestParam(required = false) Long usuarioId,
            @Valid @RequestBody(required = false) BuscarPorIngredientesRequest request) {
        List<Long> ids = request == null ? null : request.ingredientesIds();
        return propuestaRecetaService.buscar(usuarioId, ids);
    }
}
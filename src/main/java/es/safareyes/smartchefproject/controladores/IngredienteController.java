package es.safareyes.smartchefproject.controladores;

import es.safareyes.smartchefproject.dtos.response.IngredienteResponse;
import es.safareyes.smartchefproject.servicios.IIngredienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ingredientes")
@RequiredArgsConstructor
public class IngredienteController {

    private final IIngredienteService ingredienteService;

    @GetMapping
    public Page<IngredienteResponse> buscar(@RequestParam(required = false) String texto,
                                            Pageable pageable) {
        return ingredienteService.buscar(texto, pageable);
    }
}
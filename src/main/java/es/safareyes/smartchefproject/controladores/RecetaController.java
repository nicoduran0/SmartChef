package es.safareyes.smartchefproject.controladores;

import es.safareyes.smartchefproject.dtos.response.RecetaDetalleResponse;
import es.safareyes.smartchefproject.servicios.IRecetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recetas")
@RequiredArgsConstructor
public class RecetaController {

    private final IRecetaService recetaService;

    @GetMapping("/{id}")
    public RecetaDetalleResponse obtenerDetalle(@PathVariable Long id) {
        return recetaService.obtenerDetalle(id);
    }
}
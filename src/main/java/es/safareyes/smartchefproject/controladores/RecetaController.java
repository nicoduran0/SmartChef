package es.safareyes.smartchefproject.controladores;

import es.safareyes.smartchefproject.dtos.response.RecetaDetalleResponse;
import es.safareyes.smartchefproject.dtos.response.RecetaResumenResponse;
import es.safareyes.smartchefproject.modelos.Dificultad;
import es.safareyes.smartchefproject.servicios.IRecetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recetas")
@RequiredArgsConstructor
public class RecetaController {

    private final IRecetaService recetaService;

    @GetMapping
    public Page<RecetaResumenResponse> listar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Dificultad dificultad,
            @RequestParam(required = false) Short tiempoMaximo,
            @RequestParam(defaultValue = "false") boolean vegetariana,
            @RequestParam(defaultValue = "false") boolean sinGluten,
            Pageable pageable) {
        return recetaService.listar(titulo, dificultad, tiempoMaximo, vegetariana, sinGluten, pageable);
    }

    @GetMapping("/{id}")
    public RecetaDetalleResponse obtenerDetalle(@PathVariable Long id) {
        return recetaService.obtenerDetalle(id);
    }
}
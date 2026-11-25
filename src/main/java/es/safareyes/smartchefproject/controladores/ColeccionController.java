package es.safareyes.smartchefproject.controladores;

import es.safareyes.smartchefproject.dtos.response.ColeccionResponse;
import es.safareyes.smartchefproject.servicios.IColeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/colecciones")
@RequiredArgsConstructor
public class ColeccionController {

    private final IColeccionService coleccionService;

    // TODO seguridad: sustituir usuarioId por el usuario del token JWT
    @GetMapping
    public List<ColeccionResponse> listar(@RequestParam Long usuarioId) {
        return coleccionService.listar(usuarioId);
    }

    @PutMapping("/{nombre}/recetas/{recetaId}")
    public ColeccionResponse guardarReceta(@PathVariable String nombre,
                                           @PathVariable Long recetaId,
                                           @RequestParam Long usuarioId) {
        return coleccionService.guardarReceta(usuarioId, nombre, recetaId);
    }
}
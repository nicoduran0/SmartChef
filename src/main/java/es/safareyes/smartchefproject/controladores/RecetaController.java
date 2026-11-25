package es.safareyes.smartchefproject.controladores;

import es.safareyes.smartchefproject.dtos.request.RecetaRequest;
import es.safareyes.smartchefproject.dtos.response.RecetaDetalleResponse;
import es.safareyes.smartchefproject.dtos.response.RecetaResumenResponse;
import es.safareyes.smartchefproject.modelos.Dificultad;
import es.safareyes.smartchefproject.servicios.IRecetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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

    // TODO seguridad: sustituir usuarioId por el usuario del token JWT
    @PostMapping
    public ResponseEntity<RecetaDetalleResponse> crear(@Valid @RequestBody RecetaRequest request,
                                                       @RequestParam Long usuarioId) {
        RecetaDetalleResponse creada = recetaService.crear(request, usuarioId);
        URI ubicacion = URI.create("/api/v1/recetas/" + creada.id());
        return ResponseEntity.created(ubicacion).body(creada);
    }

    @PutMapping("/{id}")
    public RecetaDetalleResponse actualizar(@PathVariable Long id,
                                            @Valid @RequestBody RecetaRequest request,
                                            @RequestParam Long usuarioId) {
        return recetaService.actualizar(id, request, usuarioId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, @RequestParam Long usuarioId) {
        recetaService.eliminar(id, usuarioId);
    }
}
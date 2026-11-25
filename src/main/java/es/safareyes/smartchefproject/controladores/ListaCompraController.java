package es.safareyes.smartchefproject.controladores;

import es.safareyes.smartchefproject.dtos.request.CerrarListaRequest;
import es.safareyes.smartchefproject.dtos.request.ListaDesdeRecetaRequest;
import es.safareyes.smartchefproject.dtos.response.ListaCompraResponse;
import es.safareyes.smartchefproject.servicios.IListaCompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/listas-compra")
@RequiredArgsConstructor
public class ListaCompraController {

    private final IListaCompraService listaCompraService;

    // TODO seguridad: sustituir usuarioId por el usuario del token JWT
    @PostMapping("/desde-receta")
    public ListaCompraResponse anadirDesdeReceta(@RequestParam Long usuarioId,
                                                 @Valid @RequestBody ListaDesdeRecetaRequest request) {
        return listaCompraService.anadirDesdeReceta(usuarioId, request);
    }

    @PatchMapping("/{id}/cerrar")
    public ListaCompraResponse cerrar(@PathVariable Long id,
                                      @RequestParam Long usuarioId,
                                      @RequestBody(required = false) CerrarListaRequest request) {
        return listaCompraService.cerrar(id, usuarioId, request);
    }
}
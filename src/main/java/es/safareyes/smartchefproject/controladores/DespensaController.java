package es.safareyes.smartchefproject.controladores;

import es.safareyes.smartchefproject.dtos.request.DespensaRequest;
import es.safareyes.smartchefproject.dtos.response.DespensaResponse;
import es.safareyes.smartchefproject.servicios.IDespensaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/despensa")
@RequiredArgsConstructor
public class DespensaController {

    private final IDespensaService despensaService;

    @GetMapping
    public List<DespensaResponse> listar(@RequestParam Long usuarioId) {
        return despensaService.listar(usuarioId);
    }

    @PutMapping("/{ingredienteId}")
    public ResponseEntity<DespensaResponse> guardar(@PathVariable Long ingredienteId,
                                                    @RequestParam Long usuarioId,
                                                    @Valid @RequestBody DespensaRequest request) {
        return despensaService.guardar(usuarioId, ingredienteId, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
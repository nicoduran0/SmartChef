package es.safareyes.smartchefproject.controladores;

import es.safareyes.smartchefproject.dtos.request.RegistroRequest;
import es.safareyes.smartchefproject.dtos.response.RegistroResponse;
import es.safareyes.smartchefproject.dtos.response.SemanaResponse;
import es.safareyes.smartchefproject.servicios.IRegistroComidaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/registros")
@RequiredArgsConstructor
public class RegistroComidaController {

    private final IRegistroComidaService registroService;

    // TODO seguridad: sustituir usuarioId por el usuario del token JWT
    @PostMapping
    public ResponseEntity<RegistroResponse> registrar(@RequestParam Long usuarioId,
                                                      @Valid @RequestBody RegistroRequest request) {
        RegistroResponse creado = registroService.registrar(usuarioId, request);
        // No hay endpoint para un registro suelto: la ubicación apunta a la semana donde aparece
        URI ubicacion = URI.create("/api/v1/registros/semana?fecha=" + creado.fecha()
                + "&usuarioId=" + usuarioId);
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @GetMapping("/semana")
    public SemanaResponse semana(@RequestParam Long usuarioId,
                                 @RequestParam(required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return registroService.semana(usuarioId, fecha);
    }
}
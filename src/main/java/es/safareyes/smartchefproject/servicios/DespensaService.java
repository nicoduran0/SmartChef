package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.request.DespensaRequest;
import es.safareyes.smartchefproject.dtos.response.DespensaResponse;
import es.safareyes.smartchefproject.excepciones.RecursoNoEncontradoException;
import es.safareyes.smartchefproject.mappers.DespensaMapper;
import es.safareyes.smartchefproject.modelos.Despensa;
import es.safareyes.smartchefproject.modelos.Ingrediente;
import es.safareyes.smartchefproject.modelos.Usuario;
import es.safareyes.smartchefproject.repositorios.IDespensaRepository;
import es.safareyes.smartchefproject.repositorios.IIngredienteRepository;
import es.safareyes.smartchefproject.repositorios.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DespensaService implements IDespensaService {

    private final IDespensaRepository despensaRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IIngredienteRepository ingredienteRepository;
    private final DespensaMapper despensaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DespensaResponse> listar(Long usuarioId) {
        comprobarUsuario(usuarioId);
        return despensaMapper.toResponseList(
                despensaRepository.findByUsuarioIdOrderByCaducidadAsc(usuarioId));
    }

    @Override
    @Transactional
    public Optional<DespensaResponse> guardar(Long usuarioId, Long ingredienteId, DespensaRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId));
        Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el ingrediente con id " + ingredienteId));

        Optional<Despensa> existente = despensaRepository
                .findByUsuarioIdAndIngredienteId(usuarioId, ingredienteId);

        // Cantidad 0: se elimina de la despensa
        if (request.cantidad().compareTo(BigDecimal.ZERO) == 0) {
            existente.ifPresent(despensaRepository::delete);
            log.info("Ingrediente {} eliminado de la despensa del usuario {}", ingredienteId, usuarioId);
            return Optional.empty();
        }

        Despensa despensa = existente.orElseGet(() -> {
            Despensa nueva = new Despensa();
            nueva.setUsuario(usuario);
            nueva.setIngrediente(ingrediente);
            return nueva;
        });
        despensa.setCantidad(request.cantidad());
        despensa.setCaducidad(request.caducidad());
        despensa = despensaRepository.save(despensa);

        log.info("Despensa del usuario {} actualizada con el ingrediente {}", usuarioId, ingredienteId);
        return Optional.of(despensaMapper.toResponse(despensa));
    }

    private void comprobarUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId);
        }
    }
}
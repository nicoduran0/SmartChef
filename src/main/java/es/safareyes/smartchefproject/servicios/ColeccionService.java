package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.response.ColeccionResponse;
import es.safareyes.smartchefproject.excepciones.ConflictoException;
import es.safareyes.smartchefproject.excepciones.RecursoNoEncontradoException;
import es.safareyes.smartchefproject.excepciones.ReglaNegocioException;
import es.safareyes.smartchefproject.mappers.ColeccionMapper;
import es.safareyes.smartchefproject.modelos.Coleccion;
import es.safareyes.smartchefproject.modelos.ColeccionReceta;
import es.safareyes.smartchefproject.modelos.Receta;
import es.safareyes.smartchefproject.modelos.Usuario;
import es.safareyes.smartchefproject.repositorios.IColeccionRecetaRepository;
import es.safareyes.smartchefproject.repositorios.IColeccionRepository;
import es.safareyes.smartchefproject.repositorios.IRecetaRepository;
import es.safareyes.smartchefproject.repositorios.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ColeccionService implements IColeccionService {

    private static final String NOMBRE_FAVORITAS = "Favoritas";
    private static final int LONGITUD_MAXIMA_NOMBRE = 100;

    private final IColeccionRepository coleccionRepository;
    private final IColeccionRecetaRepository coleccionRecetaRepository;
    private final IRecetaRepository recetaRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ColeccionMapper coleccionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ColeccionResponse> listar(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId);
        }
        return coleccionMapper.toResponseList(coleccionRepository.findByUsuarioId(usuarioId));
    }

    @Override
    @Transactional
    public ColeccionResponse guardarReceta(Long usuarioId, String nombre, Long recetaId) {
        String nombreLimpio = nombre == null ? "" : nombre.trim();
        if (nombreLimpio.isEmpty() || nombreLimpio.length() > LONGITUD_MAXIMA_NOMBRE) {
            throw new ReglaNegocioException(
                    "El nombre de la colección debe tener entre 1 y " + LONGITUD_MAXIMA_NOMBRE + " caracteres");
        }
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + usuarioId));
        Receta receta = recetaRepository.findById(recetaId)
                .filter(Receta::isActiva)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la receta con id " + recetaId));

        // La colección se crea si no existe (el nombre es único por persona, sin distinguir mayúsculas)
        Coleccion coleccion = coleccionRepository
                .findByUsuarioIdAndNombreIgnoreCase(usuarioId, nombreLimpio)
                .orElseGet(() -> crearColeccion(usuario, nombreLimpio));

        if (coleccionRecetaRepository.existsByColeccionIdAndRecetaId(coleccion.getId(), recetaId)) {
            throw new ConflictoException("La receta " + recetaId + " ya está en la colección «"
                    + coleccion.getNombre() + "»");
        }

        ColeccionReceta union = new ColeccionReceta();
        union.setColeccion(coleccion);
        union.setReceta(receta);
        coleccion.getRecetas().add(coleccionRecetaRepository.save(union));

        log.info("Receta {} guardada en la colección {} del usuario {}", recetaId, coleccion.getId(), usuarioId);
        return coleccionMapper.toResponse(coleccion);
    }

    private Coleccion crearColeccion(Usuario usuario, String nombre) {
        Coleccion nueva = new Coleccion();
        nueva.setUsuario(usuario);
        nueva.setNombre(nombre);
        nueva.setEsFavoritas(NOMBRE_FAVORITAS.equalsIgnoreCase(nombre));
        return coleccionRepository.save(nueva);
    }
}
package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {

    List<Coleccion> findByUsuarioId(Long usuarioId);

    Optional<Coleccion> findByUsuarioIdAndNombreIgnoreCase(Long usuarioId, String nombre);
}
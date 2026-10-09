package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IColeccionRepository extends JpaRepository<Coleccion, Integer> {

    List<Coleccion> findByUsuarioId(Long usuarioId);
}
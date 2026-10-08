package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {
}
package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.ColeccionReceta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IColeccionRecetaRepository extends JpaRepository<ColeccionReceta, Long> {
}
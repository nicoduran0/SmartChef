package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.FotoReceta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFotoRecetaRepository extends JpaRepository<FotoReceta, Long> {
}
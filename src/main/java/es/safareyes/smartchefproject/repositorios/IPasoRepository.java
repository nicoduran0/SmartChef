package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.Paso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPasoRepository extends JpaRepository<Paso, Long> {
}
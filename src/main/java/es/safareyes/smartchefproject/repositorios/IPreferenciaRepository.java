package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.Preferencia;
import es.safareyes.smartchefproject.modelos.TipoPreferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPreferenciaRepository extends JpaRepository<Preferencia, Long> {

    Optional<Preferencia> findByTipo(TipoPreferencia tipo);
}
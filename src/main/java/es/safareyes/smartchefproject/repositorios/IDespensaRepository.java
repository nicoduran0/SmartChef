package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.Despensa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDespensaRepository extends JpaRepository<Despensa, Long> {

    List<Despensa> findByUsuarioIdOrderByCaducidadAsc(Long usuarioId);
}
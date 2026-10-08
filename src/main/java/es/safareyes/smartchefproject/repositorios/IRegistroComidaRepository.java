package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.RegistroComida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegistroComidaRepository extends JpaRepository<RegistroComida, Long> {
}
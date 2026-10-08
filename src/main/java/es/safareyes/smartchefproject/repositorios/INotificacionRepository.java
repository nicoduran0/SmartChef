package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INotificacionRepository extends JpaRepository<Notificacion, Long> {
}
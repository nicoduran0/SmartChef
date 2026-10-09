package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.ColeccionReceta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IColeccionRecetaRepository extends JpaRepository<ColeccionReceta, Integer> {

    @Query("SELECT cr.receta, COUNT(cr) FROM ColeccionReceta cr " +
            "WHERE cr.agregadaEn BETWEEN :desde AND :hasta " +
            "GROUP BY cr.receta ORDER BY COUNT(cr) DESC")
    List<Object[]> buscarRecetasMasGuardadas(@Param("desde") LocalDateTime desde,
                                             @Param("hasta") LocalDateTime hasta);
}
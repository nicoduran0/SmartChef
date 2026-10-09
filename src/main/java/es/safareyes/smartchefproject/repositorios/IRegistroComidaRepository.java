package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.RegistroComida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IRegistroComidaRepository extends JpaRepository<RegistroComida, Integer> {

    List<RegistroComida> findByUsuarioIdAndFechaBetweenOrderByFechaAsc(Long usuarioId, LocalDate desde, LocalDate hasta);

    @Query(nativeQuery = true, value = """
            SELECT DATE_TRUNC('day', rc.fecha)::date AS dia,
                   SUM(COALESCE(
                       (SELECT SUM(ri.cantidad * i.calorias / r.raciones)
                        FROM recetas_ingredientes ri
                        JOIN ingredientes i ON i.id = ri.ingrediente_id
                        JOIN recetas r ON r.id = ri.receta_id
                        WHERE ri.receta_id = rc.receta_id) * rc.raciones,
                       rc.calorias, 0)) AS calorias
            FROM registros_comida rc
            WHERE rc.usuario_id = :usuarioId
              AND rc.fecha BETWEEN :desde AND :hasta
            GROUP BY DATE_TRUNC('day', rc.fecha)
            ORDER BY dia
            """)
    List<Object[]> calcularCaloriasPorDia(@Param("usuarioId") Long usuarioId,
                                          @Param("desde") LocalDate desde,
                                          @Param("hasta") LocalDate hasta);

    @Query("SELECT rc.receta, COUNT(rc) FROM RegistroComida rc " +
            "WHERE rc.receta IS NOT NULL AND rc.fecha BETWEEN :desde AND :hasta " +
            "GROUP BY rc.receta ORDER BY COUNT(rc) DESC")
    List<Object[]> buscarRecetasMasCocinadas(@Param("desde") LocalDate desde,
                                             @Param("hasta") LocalDate hasta);
}
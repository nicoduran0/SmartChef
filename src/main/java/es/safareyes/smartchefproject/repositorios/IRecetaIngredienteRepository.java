package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.RecetaIngrediente;
import es.safareyes.smartchefproject.modelos.RecetaIngredienteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface IRecetaIngredienteRepository extends JpaRepository<RecetaIngrediente, RecetaIngredienteId> {

    @Query("SELECT SUM(ri.cantidad * i.precio) " +
            "FROM RecetaIngrediente ri JOIN ri.ingrediente i " +
            "WHERE ri.receta.id = :recetaId")
    BigDecimal calcularCosteTotal(@Param("recetaId") Long recetaId);

    @Query("SELECT SUM(ri.cantidad * i.calorias) " +
            "FROM RecetaIngrediente ri JOIN ri.ingrediente i " +
            "WHERE ri.receta.id = :recetaId")
    BigDecimal calcularCaloriasTotales(@Param("recetaId") Long recetaId);
}
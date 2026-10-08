package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.RecetaIngrediente;
import es.safareyes.smartchefproject.modelos.RecetaIngredienteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRecetaIngredienteRepository extends JpaRepository<RecetaIngrediente, RecetaIngredienteId> {
}
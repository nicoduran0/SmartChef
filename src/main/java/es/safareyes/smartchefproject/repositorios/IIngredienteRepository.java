package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.Ingrediente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IIngredienteRepository extends JpaRepository<Ingrediente, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    Page<Ingrediente> findByNombreContainingIgnoreCase(String texto, Pageable pageable);
}
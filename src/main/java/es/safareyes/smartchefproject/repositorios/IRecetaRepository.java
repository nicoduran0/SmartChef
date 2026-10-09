package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.Dificultad;
import es.safareyes.smartchefproject.modelos.Receta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IRecetaRepository extends JpaRepository<Receta, Integer>, JpaSpecificationExecutor<Receta> {

    Page<Receta> findByActivaTrue(Pageable pageable);

    @Query("SELECT r FROM Receta r WHERE r.activa = true AND NOT EXISTS (" +
            "SELECT ri FROM RecetaIngrediente ri WHERE ri.receta = r AND ri.ingrediente.vegetariano = false)")
    Page<Receta> buscarVegetarianas(Pageable pageable);

    @Query("SELECT r FROM Receta r WHERE r.activa = true AND NOT EXISTS (" +
            "SELECT ri FROM RecetaIngrediente ri WHERE ri.receta = r AND ri.ingrediente.sinGluten = false)")
    Page<Receta> buscarSinGluten(Pageable pageable);

    default Specification<Receta> conFiltros(String titulo, Dificultad dificultad, Short tiempoMaximo) {
        return (root, query, cb) -> {
            var predicado = cb.isTrue(root.get("activa"));
            if (titulo != null && !titulo.isBlank()) {
                predicado = cb.and(predicado,
                        cb.like(cb.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%"));
            }
            if (dificultad != null) {
                predicado = cb.and(predicado, cb.equal(root.get("dificultad"), dificultad));
            }
            if (tiempoMaximo != null) {
                predicado = cb.and(predicado,
                        cb.lessThanOrEqualTo(root.<Short>get("tiempoPreparacion"), tiempoMaximo));
            }
            return predicado;
        };
    }
}
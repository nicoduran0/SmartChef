package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.Receta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRecetaRepository extends JpaRepository<Receta, Long> {

    Page<Receta> findByActivaTrue(Pageable pageable);
}
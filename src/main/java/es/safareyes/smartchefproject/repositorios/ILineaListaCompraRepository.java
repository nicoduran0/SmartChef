package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.LineaListaCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILineaListaCompraRepository extends JpaRepository<LineaListaCompra, Integer> {
}
package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.ListaCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IListaCompraRepository extends JpaRepository<ListaCompra, Integer> {
}
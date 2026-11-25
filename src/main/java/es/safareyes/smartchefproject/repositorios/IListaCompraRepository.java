package es.safareyes.smartchefproject.repositorios;

import es.safareyes.smartchefproject.modelos.EstadoLista;
import es.safareyes.smartchefproject.modelos.ListaCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IListaCompraRepository extends JpaRepository<ListaCompra, Long> {

    Optional<ListaCompra> findByUsuarioIdAndEstado(Long usuarioId, EstadoLista estado);
}
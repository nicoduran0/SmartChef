package es.safareyes.smartchefproject;

import es.safareyes.smartchefproject.modelos.Dificultad;
import es.safareyes.smartchefproject.modelos.Receta;
import es.safareyes.smartchefproject.repositorios.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SmartChefProjectApplicationTests {

    private static final LocalDate DESDE = LocalDate.of(2000, 1, 1);
    private static final LocalDate HASTA = LocalDate.of(2100, 1, 1);

    @Autowired
    private IIngredienteRepository ingredienteRepository;
    @Autowired
    private IRecetaRepository recetaRepository;
    @Autowired
    private IRecetaIngredienteRepository recetaIngredienteRepository;
    @Autowired
    private IRegistroComidaRepository registroComidaRepository;
    @Autowired
    private IColeccionRecetaRepository coleccionRecetaRepository;
    @Autowired
    private IColeccionRepository coleccionRepository;
    @Autowired
    private IDespensaRepository despensaRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void ingredientesPorTextoPaginado() {
        var pagina = ingredienteRepository.findByNombreContainingIgnoreCase("PAT", PageRequest.of(0, 10));
        assertEquals(1, pagina.getTotalElements());
        assertEquals("Patata", pagina.getContent().get(0).getNombre());
    }

    @Test
    void recetasActivasPaginadas() {
        var pagina = recetaRepository.findByActivaTrue(PageRequest.of(0, 2));
        assertEquals(3, pagina.getTotalElements());
        assertEquals(2, pagina.getContent().size());
    }

    @Test
    void recetasVegetarianas() {
        var pagina = recetaRepository.buscarVegetarianas(PageRequest.of(0, 10));
        assertEquals(2, pagina.getTotalElements());
    }

    @Test
    void recetasSinGluten() {
        var pagina = recetaRepository.buscarSinGluten(PageRequest.of(0, 10));
        assertEquals(2, pagina.getTotalElements());
    }

    @Test
    void specificationPorDificultad() {
        var pagina = recetaRepository.findAll(
                recetaRepository.conFiltros(null, Dificultad.FACIL, null), PageRequest.of(0, 10));
        assertEquals(1, pagina.getTotalElements());
        assertEquals("Pasta con tomate", pagina.getContent().get(0).getTitulo());
    }

    @Test
    void specificationPorTituloYTiempo() {
        var porTitulo = recetaRepository.findAll(
                recetaRepository.conFiltros("TORTILLA", null, null), PageRequest.of(0, 10));
        assertEquals(1, porTitulo.getTotalElements());

        var porTiempo = recetaRepository.findAll(
                recetaRepository.conFiltros(null, null, (short) 30), PageRequest.of(0, 10));
        assertEquals(1, porTiempo.getTotalElements());
    }

    @Test
    void costeTotalDeLaTortilla() {
        BigDecimal coste = recetaIngredienteRepository.calcularCosteTotal(1L);
        assertEquals(0, new BigDecimal("2.925").compareTo(coste));
    }

    @Test
    void caloriasTotalesDeLaTortilla() {
        BigDecimal calorias = recetaIngredienteRepository.calcularCaloriasTotales(1L);
        assertEquals(0, new BigDecimal("1470").compareTo(calorias));
    }

    @Test
    void registrosDeUnaSemana() {
        var registros = registroComidaRepository
                .findByUsuarioIdAndFechaBetweenOrderByFechaAsc(3L, DESDE, HASTA);
        assertEquals(2, registros.size());
    }

    @Test
    void caloriasPorDiaConSqlNativo() {
        var filas = registroComidaRepository.calcularCaloriasPorDia(3L, DESDE, HASTA);
        assertEquals(1, filas.size());
        BigDecimal calorias = new BigDecimal(filas.get(0)[1].toString());
        assertEquals(0, new BigDecimal("1255").compareTo(calorias));
    }

    @Test
    void recetasMasCocinadas() {
        var filas = registroComidaRepository.buscarRecetasMasCocinadas(DESDE, HASTA);
        assertEquals(1, filas.size());
        assertEquals("Tortilla de patatas", ((Receta) filas.get(0)[0]).getTitulo());
        assertEquals(1L, filas.get(0)[1]);
    }

    @Test
    void recetasMasGuardadas() {
        var filas = coleccionRecetaRepository.buscarRecetasMasGuardadas(
                LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.of(2100, 1, 1, 0, 0));
        assertEquals(2, filas.size());
    }

    @Test
    void coleccionesDeUnUsuario() {
        assertEquals(2, coleccionRepository.findByUsuarioId(3L).size());
    }

    @Test
    void despensaOrdenadaPorCaducidad() {
        var despensa = despensaRepository.findByUsuarioIdOrderByCaducidadAsc(3L);
        assertEquals(5, despensa.size());
        assertNull(despensa.get(4).getCaducidad());
    }
}
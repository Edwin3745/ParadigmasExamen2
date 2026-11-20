package co.edu.poli.examen2.aplicacion;

import static co.edu.poli.examen2.aplicacion.Dobles.examen;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;
import co.edu.poli.examen2.aplicacion.servicio.DeserializarExamenesService;
import co.edu.poli.examen2.aplicacion.servicio.ListarExamenesService;
import co.edu.poli.examen2.aplicacion.servicio.RegistrarExamenService;
import co.edu.poli.examen2.aplicacion.servicio.SerializarExamenesService;
import co.edu.poli.examen2.dominio.excepcion.ExamenDuplicadoException;
import co.edu.poli.examen2.infraestructura.salida.persistencia.ExamenRepositorioMemoria;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiciosTest {

    private ExamenRepositorioMemoria repositorio;
    private Dobles.RespaldoFalso respaldo;

    @BeforeEach
    void preparar() {
        repositorio = new ExamenRepositorioMemoria();
        respaldo = new Dobles.RespaldoFalso();
    }

    @Test
    void registraUnExamen() {
        new RegistrarExamenService(repositorio).registrar(examen("EX1"));
        assertTrue(repositorio.existePorCodigo("EX1"));
        assertEquals(1, repositorio.listarTodos().size());
    }

    @Test
    void rechazaCodigoDuplicado() {
        RegistrarExamenService servicio = new RegistrarExamenService(repositorio);
        servicio.registrar(examen("EX1"));
        ExamenDuplicadoException e = assertThrows(ExamenDuplicadoException.class,
                () -> servicio.registrar(examen("EX1")));
        assertTrue(e.getMessage().contains("EX1"));
        assertEquals(1, repositorio.listarTodos().size());
    }

    @Test
    void rechazaExamenNulo() {
        assertThrows(NullPointerException.class, () -> new RegistrarExamenService(repositorio).registrar(null));
    }

    @Test
    void listaLosExamenesEnOrdenDeRegistro() {
        RegistrarExamenService registrar = new RegistrarExamenService(repositorio);
        registrar.registrar(examen("A"));
        registrar.registrar(examen("B"));
        assertEquals(Arrays.asList(examen("A"), examen("B")), new ListarExamenesService(repositorio).listar());
    }

    @Test
    void listaVaciaAlInicio() {
        assertTrue(new ListarExamenesService(repositorio).listar().isEmpty());
    }

    @Test
    void serializaYDeserializaConservandoLosDatos() {
        repositorio.guardar(examen("A"));
        repositorio.guardar(examen("B"));
        assertEquals(2, new SerializarExamenesService(repositorio, respaldo).serializar(TipoFormato.CSV));

        ExamenRepositorioMemoria otro = new ExamenRepositorioMemoria();
        otro.guardar(examen("VIEJO"));
        assertEquals(2, new DeserializarExamenesService(otro, respaldo).deserializar(TipoFormato.CSV));
        assertEquals(repositorio.listarTodos(), otro.listarTodos());
    }

    @Test
    void deserializarRechazaCodigosRepetidosEnElRespaldo() {
        respaldo.guardado.put(TipoFormato.BINARIO, Arrays.asList(examen("A"), examen("A")));
        repositorio.guardar(examen("ORIGINAL"));
        assertThrows(ExamenDuplicadoException.class,
                () -> new DeserializarExamenesService(repositorio, respaldo).deserializar(TipoFormato.BINARIO));
        assertEquals(1, repositorio.listarTodos().size(), "No debe modificar el repositorio si falla");
    }

    @Test
    void repositorioNoPermiteModificarLaListaDevuelta() {
        repositorio.guardar(examen("A"));
        assertThrows(UnsupportedOperationException.class, () -> repositorio.listarTodos().clear());
    }
}

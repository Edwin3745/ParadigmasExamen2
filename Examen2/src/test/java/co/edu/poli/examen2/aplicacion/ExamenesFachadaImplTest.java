package co.edu.poli.examen2.aplicacion;

import static co.edu.poli.examen2.aplicacion.Dobles.examen;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;
import co.edu.poli.examen2.aplicacion.puerto.entrada.ExamenesFachada;
import co.edu.poli.examen2.aplicacion.servicio.ExamenesFachadaImpl;
import co.edu.poli.examen2.dominio.excepcion.ExamenDuplicadoException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifica que la fachada solo delega en los casos de uso (se usan casos de uso falsos). */
class ExamenesFachadaImplTest {

    private final List<String> llamadas = new ArrayList<>();
    private final ExamenSangre ejemplo = examen("EX1");

    private ExamenesFachada fachada() {
        return new ExamenesFachadaImpl(
                e -> {
                    llamadas.add("registrar:" + e.getCodigo());
                    if ("DUP".equals(e.getCodigo())) {
                        throw new ExamenDuplicadoException("DUP");
                    }
                },
                () -> {
                    llamadas.add("listar");
                    return Collections.singletonList(ejemplo);
                },
                f -> {
                    llamadas.add("serializar:" + f);
                    return 3;
                },
                f -> {
                    llamadas.add("deserializar:" + f);
                    return 4;
                });
    }

    @Test
    void registrarDelegaEnElCasoDeUso() {
        fachada().registrar(ejemplo);
        assertEquals(Collections.singletonList("registrar:EX1"), llamadas);
    }

    @Test
    void listarDelegaYDevuelveElResultado() {
        List<ExamenSangre> resultado = fachada().listar();
        assertSame(ejemplo, resultado.get(0));
        assertEquals(Collections.singletonList("listar"), llamadas);
    }

    @Test
    void respaldarYRestaurarDelegan() {
        ExamenesFachada f = fachada();
        assertEquals(3, f.respaldar(TipoFormato.CSV));
        assertEquals(4, f.restaurar(TipoFormato.BINARIO));
        assertEquals(java.util.Arrays.asList("serializar:" + TipoFormato.CSV, "deserializar:" + TipoFormato.BINARIO),
                llamadas);
    }

    @Test
    void laFachadaNoOcultaLasExcepcionesDelDominio() {
        assertThrows(ExamenDuplicadoException.class, () -> fachada().registrar(examen("DUP")));
    }
}

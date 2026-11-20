package co.edu.poli.examen2.infraestructura;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.poli.examen2.dominio.excepcion.ExamenInvalidoException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FactorRH;
import co.edu.poli.examen2.dominio.modelo.TipoSangre;
import co.edu.poli.examen2.infraestructura.salida.archivo.FormatoBinario;
import co.edu.poli.examen2.infraestructura.salida.archivo.FormatoCsv;
import co.edu.poli.examen2.infraestructura.salida.archivo.FormatoRespaldo;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class FormatosTest {

    private static ExamenSangre e(String codigo, String paciente, double costo, TipoSangre t, FactorRH f) {
        return ExamenSangre.builder().codigo(codigo).paciente(paciente).costo(costo).tipoSangre(t).factorRH(f).build();
    }

    private static List<ExamenSangre> ejemplos() {
        return Arrays.asList(
                e("EX001", "Juan Pérez", 50000, TipoSangre.A, FactorRH.POSITIVO),
                e("EX002", "Ríos, María", 1234.56, TipoSangre.AB, FactorRH.NEGATIVO),
                e("EX003", "Pedro \"el Largo\" Gómez", 0.5, TipoSangre.O, FactorRH.POSITIVO),
                e("EX004", "Línea uno\nlínea dos, con \"comillas\"", 99.99, TipoSangre.B, FactorRH.NEGATIVO),
                e("EX005", "Ñandú Ünïcode ✓", 1e7, TipoSangre.O, FactorRH.NEGATIVO));
    }

    private static void idaYVuelta(FormatoRespaldo formato) {
        List<ExamenSangre> originales = ejemplos();
        assertEquals(originales, formato.deserializar(formato.serializar(originales)));
    }

    @Test
    void binarioConservaTodosLosDatos() {
        idaYVuelta(new FormatoBinario());
    }

    @Test
    void csvConservaTodosLosDatosIncluidosCaracteresEspeciales() {
        idaYVuelta(new FormatoCsv());
    }

    @Test
    void listaVaciaFuncionaEnAmbosFormatos() {
        for (FormatoRespaldo f : new FormatoRespaldo[]{new FormatoBinario(), new FormatoCsv()}) {
            assertTrue(f.deserializar(f.serializar(Collections.emptyList())).isEmpty());
        }
    }

    @Test
    void csvEsTextoLegibleConEncabezado() {
        String texto = new String(new FormatoCsv().serializar(ejemplos()), StandardCharsets.UTF_8);
        assertTrue(texto.startsWith("codigo,paciente,costo,tipoSangre,factorRH\n"));
        assertTrue(texto.contains("EX001,Juan Pérez,50000.0,A,POSITIVO"));
        assertTrue(texto.contains("\"Ríos, María\""));
        assertTrue(texto.contains("\"Pedro \"\"el Largo\"\" Gómez\""));
    }

    @Test
    void csvAceptaFinesDeLineaWindowsYBom() {
        String texto = "\uFEFFcodigo,paciente,costo,tipoSangre,factorRH\r\nEX1,Ana,10,A,POSITIVO\r\n\r\n";
        List<ExamenSangre> r = new FormatoCsv().deserializar(texto.getBytes(StandardCharsets.UTF_8));
        assertEquals(1, r.size());
        assertEquals("Ana", r.get(0).getPaciente());
    }

    @Test
    void csvRechazaArchivosInvalidos() {
        FormatoCsv csv = new FormatoCsv();
        String enc = "codigo,paciente,costo,tipoSangre,factorRH\n";
        assertThrows(ExamenInvalidoException.class, () -> csv.deserializar("hola\n".getBytes(StandardCharsets.UTF_8)));
        assertThrows(ExamenInvalidoException.class, () -> csv.deserializar(new byte[0]));
        assertThrows(ExamenInvalidoException.class, () -> csv.deserializar((enc + "EX1,Ana,10,A\n").getBytes(StandardCharsets.UTF_8)));
        assertThrows(ExamenInvalidoException.class, () -> csv.deserializar((enc + "EX1,Ana,abc,A,POSITIVO\n").getBytes(StandardCharsets.UTF_8)));
        assertThrows(ExamenInvalidoException.class, () -> csv.deserializar((enc + "EX1,Ana,10,Z,POSITIVO\n").getBytes(StandardCharsets.UTF_8)));
        assertThrows(ExamenInvalidoException.class, () -> csv.deserializar((enc + "EX1,,10,A,POSITIVO\n").getBytes(StandardCharsets.UTF_8)));
        assertThrows(ExamenInvalidoException.class, () -> csv.deserializar((enc + "EX1,\"Ana,10,A,POSITIVO\n").getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void binarioRechazaDatosInvalidos() {
        FormatoBinario binario = new FormatoBinario();
        assertThrows(UncheckedIOException.class, () -> binario.deserializar(new byte[]{1, 2, 3}));
    }

    @Test
    void binarioRechazaObjetosQueNoSonExamenes() throws Exception {
        java.io.ByteArrayOutputStream bytes = new java.io.ByteArrayOutputStream();
        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bytes)) {
            oos.writeObject(new java.util.ArrayList<>(Collections.singletonList(new java.util.Date())));
        }
        assertThrows(UncheckedIOException.class, () -> new FormatoBinario().deserializar(bytes.toByteArray()));
    }
}

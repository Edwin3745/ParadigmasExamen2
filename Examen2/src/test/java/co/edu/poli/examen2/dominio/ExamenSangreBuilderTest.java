package co.edu.poli.examen2.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.poli.examen2.dominio.excepcion.ExamenInvalidoException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FactorRH;
import co.edu.poli.examen2.dominio.modelo.TipoSangre;
import org.junit.jupiter.api.Test;

class ExamenSangreBuilderTest {

    private ExamenSangre.Builder valido() {
        return new ExamenSangre.Builder()
                .codigo("EX001").paciente("Juan").costo(50000)
                .tipoSangre(TipoSangre.A).factorRH(FactorRH.POSITIVO);
    }

    @Test
    void construyeExamenValido() {
        ExamenSangre e = valido().build();
        assertEquals("EX001", e.getCodigo());
        assertEquals("Juan", e.getPaciente());
        assertEquals(50000, e.getCosto());
        assertEquals(TipoSangre.A, e.getTipoSangre());
        assertEquals(FactorRH.POSITIVO, e.getFactorRH());
        assertEquals("A+", e.getGrupoCompleto());
    }

    @Test
    void builderEstaticoFunciona() {
        assertEquals("O-", ExamenSangre.builder().codigo("X").paciente("Y").costo(1)
                .tipoSangre(TipoSangre.O).factorRH(FactorRH.NEGATIVO).build().getGrupoCompleto());
    }

    @Test
    void eliminaEspaciosDeCodigoYPaciente() {
        ExamenSangre e = valido().codigo("  EX9 ").paciente(" Ana  ").build();
        assertEquals("EX9", e.getCodigo());
        assertEquals("Ana", e.getPaciente());
    }

    @Test
    void rechazaCodigoVacioONulo() {
        assertThrows(ExamenInvalidoException.class, () -> valido().codigo("   ").build());
        assertThrows(ExamenInvalidoException.class, () -> valido().codigo(null).build());
    }

    @Test
    void rechazaPacienteVacio() {
        ExamenInvalidoException e = assertThrows(ExamenInvalidoException.class, () -> valido().paciente("").build());
        assertTrue(e.getMessage().contains("paciente"));
    }

    @Test
    void rechazaCostoCeroNegativoONoNumerico() {
        assertThrows(ExamenInvalidoException.class, () -> valido().costo(0).build());
        assertThrows(ExamenInvalidoException.class, () -> valido().costo(-5).build());
        assertThrows(ExamenInvalidoException.class, () -> valido().costo(Double.NaN).build());
        assertThrows(ExamenInvalidoException.class, () -> valido().costo(Double.POSITIVE_INFINITY).build());
    }

    @Test
    void rechazaTipoSangreOFactorNulos() {
        assertThrows(ExamenInvalidoException.class, () -> valido().tipoSangre(null).build());
        assertThrows(ExamenInvalidoException.class, () -> valido().factorRH(null).build());
    }

    @Test
    void acumulaTodosLosErroresEnUnSoloMensaje() {
        ExamenInvalidoException e = assertThrows(ExamenInvalidoException.class,
                () -> new ExamenSangre.Builder().build());
        assertEquals(5, e.getErrores().size());
        for (String parte : new String[]{"código", "paciente", "costo", "tipo de sangre", "factor RH"}) {
            assertTrue(e.getMessage().contains(parte), "Falta el error de: " + parte);
        }
    }

    @Test
    void exameneIgualesPorValor() {
        assertEquals(valido().build(), valido().build());
        assertEquals(valido().build().hashCode(), valido().build().hashCode());
    }

    @Test
    void desdeTextoInterpretaTipos() {
        assertEquals(TipoSangre.AB, TipoSangre.desdeTexto(" ab "));
        assertNull(TipoSangre.desdeTexto(null));
        assertNull(TipoSangre.desdeTexto("Z"));
        assertEquals("+", FactorRH.POSITIVO.getSimbolo());
    }
}

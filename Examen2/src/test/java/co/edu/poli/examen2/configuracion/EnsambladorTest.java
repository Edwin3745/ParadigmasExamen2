package co.edu.poli.examen2.configuracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;
import co.edu.poli.examen2.aplicacion.puerto.entrada.ExamenesFachada;
import co.edu.poli.examen2.dominio.excepcion.ExamenDuplicadoException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FactorRH;
import co.edu.poli.examen2.dominio.modelo.TipoSangre;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Prueba de integración sin JavaFX: el ensamblador conecta todas las capas. */
class EnsambladorTest {

    @TempDir
    Path carpeta;

    private static ExamenSangre examen(String codigo) {
        return ExamenSangre.builder().codigo(codigo).paciente("Luis, \"Lucho\"").costo(75)
                .tipoSangre(TipoSangre.B).factorRH(FactorRH.POSITIVO).build();
    }

    @Test
    void flujoCompletoRegistrarRespaldarYRestaurarEnAmbosFormatos() {
        for (TipoFormato formato : TipoFormato.values()) {
            ExamenesFachada primera = Ensamblador.crearFachada(carpeta);
            primera.registrar(examen("EX1"));
            primera.registrar(examen("EX2"));
            assertThrows(ExamenDuplicadoException.class, () -> primera.registrar(examen("EX1")));
            assertEquals(2, primera.respaldar(formato));

            ExamenesFachada segunda = Ensamblador.crearFachada(carpeta); // aplicación "reiniciada"
            assertEquals(0, segunda.listar().size());
            assertEquals(2, segunda.restaurar(formato));
            assertEquals(primera.listar(), segunda.listar());
        }
    }
}

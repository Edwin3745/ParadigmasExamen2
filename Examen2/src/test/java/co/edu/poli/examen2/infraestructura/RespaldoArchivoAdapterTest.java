package co.edu.poli.examen2.infraestructura;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FactorRH;
import co.edu.poli.examen2.dominio.modelo.TipoSangre;
import co.edu.poli.examen2.infraestructura.salida.archivo.FormatoBinario;
import co.edu.poli.examen2.infraestructura.salida.archivo.FormatoCsv;
import co.edu.poli.examen2.infraestructura.salida.archivo.FormatoRespaldo;
import co.edu.poli.examen2.infraestructura.salida.archivo.RespaldoArchivoAdapter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RespaldoArchivoAdapterTest {

    @TempDir
    Path carpeta;

    private RespaldoArchivoAdapter adaptador() {
        Map<TipoFormato, FormatoRespaldo> formatos = new EnumMap<>(TipoFormato.class);
        formatos.put(TipoFormato.BINARIO, new FormatoBinario());
        formatos.put(TipoFormato.CSV, new FormatoCsv());
        return new RespaldoArchivoAdapter(carpeta.resolve("respaldos"), formatos);
    }

    private static List<ExamenSangre> ejemplos() {
        return Arrays.asList(
                ExamenSangre.builder().codigo("A1").paciente("Ana, la \"primera\"").costo(10.5)
                        .tipoSangre(TipoSangre.A).factorRH(FactorRH.POSITIVO).build(),
                ExamenSangre.builder().codigo("B2").paciente("Beto").costo(20)
                        .tipoSangre(TipoSangre.O).factorRH(FactorRH.NEGATIVO).build());
    }

    @Test
    void guardaYRecuperaEnBinario() {
        RespaldoArchivoAdapter a = adaptador();
        a.respaldar(ejemplos(), TipoFormato.BINARIO);
        assertTrue(Files.exists(carpeta.resolve("respaldos").resolve("examenes.ser")));
        assertEquals(ejemplos(), a.restaurar(TipoFormato.BINARIO));
    }

    @Test
    void guardaYRecuperaEnCsv() throws Exception {
        RespaldoArchivoAdapter a = adaptador();
        a.respaldar(ejemplos(), TipoFormato.CSV);
        Path archivo = carpeta.resolve("respaldos").resolve("examenes.csv");
        assertTrue(Files.readAllLines(archivo).get(0).startsWith("codigo,"));
        assertEquals(ejemplos(), a.restaurar(TipoFormato.CSV));
    }

    @Test
    void cadaFormatoUsaSuPropioArchivo() {
        RespaldoArchivoAdapter a = adaptador();
        a.respaldar(ejemplos(), TipoFormato.CSV);
        assertThrows(IllegalStateException.class, () -> a.restaurar(TipoFormato.BINARIO));
    }

    @Test
    void restaurarSinArchivoDaMensajeClaro() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> adaptador().restaurar(TipoFormato.CSV));
        assertTrue(e.getMessage().contains("No existe un respaldo"));
    }

    @Test
    void formatoSinEstrategiaRegistradaFalla() {
        RespaldoArchivoAdapter vacio = new RespaldoArchivoAdapter(carpeta, new EnumMap<>(TipoFormato.class));
        assertThrows(IllegalArgumentException.class, () -> vacio.respaldar(ejemplos(), TipoFormato.CSV));
    }
}

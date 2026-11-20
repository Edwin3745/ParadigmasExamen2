package co.edu.poli.examen2.configuracion;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;
import co.edu.poli.examen2.aplicacion.puerto.entrada.ExamenesFachada;
import co.edu.poli.examen2.aplicacion.puerto.salida.ExamenRepositorio;
import co.edu.poli.examen2.aplicacion.puerto.salida.RespaldoExamenesPuerto;
import co.edu.poli.examen2.aplicacion.servicio.DeserializarExamenesService;
import co.edu.poli.examen2.aplicacion.servicio.ExamenesFachadaImpl;
import co.edu.poli.examen2.aplicacion.servicio.ListarExamenesService;
import co.edu.poli.examen2.aplicacion.servicio.RegistrarExamenService;
import co.edu.poli.examen2.aplicacion.servicio.SerializarExamenesService;
import co.edu.poli.examen2.infraestructura.salida.archivo.FormatoBinario;
import co.edu.poli.examen2.infraestructura.salida.archivo.FormatoCsv;
import co.edu.poli.examen2.infraestructura.salida.archivo.FormatoRespaldo;
import co.edu.poli.examen2.infraestructura.salida.archivo.RespaldoArchivoAdapter;
import co.edu.poli.examen2.infraestructura.salida.persistencia.ExamenRepositorioMemoria;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;

/**
 * Ensamblador (composición manual): crea adaptadores y servicios y los conecta.
 * Es el único lugar que conoce a la vez las implementaciones concretas y los puertos.
 */
public final class Ensamblador {

    /** Carpeta donde se guardan los respaldos. */
    public static final Path DIRECTORIO_RESPALDOS = Paths.get("respaldos");

    private Ensamblador() {
    }

    /** @return fachada lista para usar, con los respaldos en {@link #DIRECTORIO_RESPALDOS} */
    public static ExamenesFachada crearFachada() {
        return crearFachada(DIRECTORIO_RESPALDOS);
    }

    /**
     * @param directorioRespaldos carpeta donde se guardan los respaldos
     * @return fachada con todas sus dependencias inyectadas
     */
    public static ExamenesFachada crearFachada(Path directorioRespaldos) {
        ExamenRepositorio repositorio = new ExamenRepositorioMemoria();

        Map<TipoFormato, FormatoRespaldo> formatos = new EnumMap<>(TipoFormato.class);
        formatos.put(TipoFormato.BINARIO, new FormatoBinario());
        formatos.put(TipoFormato.CSV, new FormatoCsv());
        RespaldoExamenesPuerto respaldo = new RespaldoArchivoAdapter(directorioRespaldos, formatos);

        return new ExamenesFachadaImpl(
                new RegistrarExamenService(repositorio),
                new ListarExamenesService(repositorio),
                new SerializarExamenesService(repositorio, respaldo),
                new DeserializarExamenesService(repositorio, respaldo));
    }
}

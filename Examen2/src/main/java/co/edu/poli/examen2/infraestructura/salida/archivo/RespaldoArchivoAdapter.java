package co.edu.poli.examen2.infraestructura.salida.archivo;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;
import co.edu.poli.examen2.aplicacion.puerto.salida.RespaldoExamenesPuerto;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Adaptador de salida y contexto del patrón Strategy.
 * <p>
 * Decide <b>dónde</b> se guarda el respaldo (un archivo local por formato dentro de un
 * directorio) y delega en la estrategia {@link FormatoRespaldo} elegida el <b>cómo</b>
 * se representan los datos.
 */
public class RespaldoArchivoAdapter implements RespaldoExamenesPuerto {

    private static final String NOMBRE_BASE = "examenes";

    private final Path directorio;
    private final Map<TipoFormato, FormatoRespaldo> formatos;

    /**
     * @param directorio carpeta donde se guardan los archivos (se crea si no existe)
     * @param formatos   estrategia asociada a cada formato disponible
     */
    public RespaldoArchivoAdapter(Path directorio, Map<TipoFormato, FormatoRespaldo> formatos) {
        this.directorio = directorio;
        this.formatos = formatos;
    }

    @Override
    public void respaldar(List<ExamenSangre> examenes, TipoFormato formato) {
        byte[] datos = estrategia(formato).serializar(examenes);
        Path archivo = rutaDe(formato);
        try {
            Files.createDirectories(directorio);
            Files.write(archivo, datos);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo escribir el respaldo en " + archivo, e);
        }
    }

    @Override
    public List<ExamenSangre> restaurar(TipoFormato formato) {
        Path archivo = rutaDe(formato);
        if (!Files.exists(archivo)) {
            throw new IllegalStateException("No existe un respaldo " + formato.getEtiqueta()
                    + " en " + archivo.toAbsolutePath());
        }
        try {
            return estrategia(formato).deserializar(Files.readAllBytes(archivo));
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer el respaldo en " + archivo, e);
        }
    }

    private Path rutaDe(TipoFormato formato) {
        return directorio.resolve(NOMBRE_BASE + "." + formato.getExtension());
    }

    private FormatoRespaldo estrategia(TipoFormato formato) {
        FormatoRespaldo estrategia = formatos.get(formato);
        if (estrategia == null) {
            throw new IllegalArgumentException("Formato de respaldo no soportado: " + formato);
        }
        return estrategia;
    }
}

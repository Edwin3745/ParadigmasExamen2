package co.edu.poli.examen2.infraestructura.salida.archivo;

import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/** Estrategia concreta: serialización binaria estándar de Java. */
public class FormatoBinario implements FormatoRespaldo {

    /** Solo se aceptan las clases que forman parte de un respaldo válido. */
    private static final ObjectInputFilter FILTRO = ObjectInputFilter.Config.createFilter(
            "java.util.ArrayList;co.edu.poli.examen2.dominio.modelo.*;java.lang.*;!*");

    @Override
    public byte[] serializar(List<ExamenSangre> examenes) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(salida)) {
            oos.writeObject(new ArrayList<>(examenes));
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo serializar los exámenes", e);
        }
        return salida.toByteArray();
    }

    @Override
    public List<ExamenSangre> deserializar(byte[] datos) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(datos))) {
            ois.setObjectInputFilter(FILTRO);
            Object leido = ois.readObject();
            if (!(leido instanceof List)) {
                throw new InvalidObjectException("El archivo no contiene una lista de exámenes");
            }
            List<ExamenSangre> examenes = new ArrayList<>();
            for (Object elemento : (List<?>) leido) {
                if (!(elemento instanceof ExamenSangre)) {
                    throw new InvalidObjectException("El archivo contiene datos que no son exámenes");
                }
                examenes.add((ExamenSangre) elemento);
            }
            return examenes;
        } catch (IOException | ClassNotFoundException e) {
            throw new UncheckedIOException("No se pudo leer el respaldo binario: archivo dañado o inválido",
                    new IOException(e));
        }
    }
}

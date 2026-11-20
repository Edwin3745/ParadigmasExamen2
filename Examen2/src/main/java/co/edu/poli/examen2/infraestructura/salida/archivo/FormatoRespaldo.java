package co.edu.poli.examen2.infraestructura.salida.archivo;

import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

import java.util.List;

/**
 * Patrón Strategy: define cómo se convierte una lista de exámenes en bytes y viceversa.
 * No sabe dónde se guardan esos bytes; eso lo decide {@link RespaldoArchivoAdapter}.
 */
public interface FormatoRespaldo {

    /**
     * @param examenes exámenes a codificar
     * @return representación en bytes
     */
    byte[] serializar(List<ExamenSangre> examenes);

    /**
     * @param datos bytes producidos por {@link #serializar(List)}
     * @return exámenes recuperados
     */
    List<ExamenSangre> deserializar(byte[] datos);
}

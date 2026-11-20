package co.edu.poli.examen2.dominio.excepcion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Se lanza cuando los datos de un examen no cumplen las reglas del dominio.
 * Puede reunir varios errores de validación en un solo mensaje.
 */
public class ExamenInvalidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final transient List<String> errores;

    /**
     * Crea la excepción con un único mensaje.
     *
     * @param mensaje descripción del error
     */
    public ExamenInvalidoException(String mensaje) {
        super(mensaje);
        this.errores = Collections.singletonList(mensaje);
    }

    /**
     * Crea la excepción con todos los errores de validación encontrados.
     *
     * @param errores lista de errores; el mensaje de la excepción los une uno por línea
     */
    public ExamenInvalidoException(List<String> errores) {
        super(unir(errores));
        this.errores = Collections.unmodifiableList(new ArrayList<>(errores));
    }

    /**
     * @return errores de validación individuales (no modificable)
     */
    public List<String> getErrores() {
        return errores;
    }

    private static String unir(List<String> errores) {
        StringBuilder sb = new StringBuilder("Datos del examen inválidos:");
        for (String error : errores) {
            sb.append("\n- ").append(error);
        }
        return sb.toString();
    }
}

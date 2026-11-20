package co.edu.poli.examen2.dominio.excepcion;

/**
 * Se lanza cuando se intenta registrar un examen con un código que ya existe.
 */
public class ExamenDuplicadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * @param codigo código de examen repetido
     */
    public ExamenDuplicadoException(String codigo) {
        super("Ya existe un examen registrado con el código: " + codigo);
    }
}

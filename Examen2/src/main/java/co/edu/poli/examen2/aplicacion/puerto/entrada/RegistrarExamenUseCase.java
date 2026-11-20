package co.edu.poli.examen2.aplicacion.puerto.entrada;

import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

/** Puerto de entrada: registrar un examen de sangre. */
public interface RegistrarExamenUseCase {

    /**
     * Registra un examen.
     *
     * @param examen examen ya validado
     * @throws co.edu.poli.examen2.dominio.excepcion.ExamenDuplicadoException si el código ya existe
     */
    void registrar(ExamenSangre examen);
}

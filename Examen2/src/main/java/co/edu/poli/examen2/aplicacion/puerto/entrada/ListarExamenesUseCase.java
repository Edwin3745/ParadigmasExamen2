package co.edu.poli.examen2.aplicacion.puerto.entrada;

import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

import java.util.List;

/** Puerto de entrada: listar los exámenes registrados. */
public interface ListarExamenesUseCase {

    /** @return exámenes registrados, en orden de registro */
    List<ExamenSangre> listar();
}

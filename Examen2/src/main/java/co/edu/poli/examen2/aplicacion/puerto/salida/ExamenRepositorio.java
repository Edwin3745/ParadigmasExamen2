package co.edu.poli.examen2.aplicacion.puerto.salida;

import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

import java.util.List;

/** Puerto de salida: almacenamiento de exámenes. */
public interface ExamenRepositorio {

    /** @param examen examen a guardar */
    void guardar(ExamenSangre examen);

    /** @return todos los exámenes, en orden de registro */
    List<ExamenSangre> listarTodos();

    /**
     * @param codigo código a consultar
     * @return {@code true} si ya existe un examen con ese código
     */
    boolean existePorCodigo(String codigo);

    /** @param examenes nuevo contenido completo del repositorio */
    void reemplazarTodos(List<ExamenSangre> examenes);
}

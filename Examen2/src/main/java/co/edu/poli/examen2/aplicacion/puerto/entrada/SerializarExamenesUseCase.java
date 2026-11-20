package co.edu.poli.examen2.aplicacion.puerto.entrada;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;

/** Puerto de entrada: guardar los exámenes en un archivo de respaldo. */
public interface SerializarExamenesUseCase {

    /**
     * Guarda todos los exámenes registrados.
     *
     * @param formato formato del archivo
     * @return cantidad de exámenes guardados
     */
    int serializar(TipoFormato formato);
}

package co.edu.poli.examen2.aplicacion.puerto.entrada;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;

/** Puerto de entrada: recuperar los exámenes desde un archivo de respaldo. */
public interface DeserializarExamenesUseCase {

    /**
     * Reemplaza los exámenes registrados por los del archivo de respaldo.
     *
     * @param formato formato del archivo
     * @return cantidad de exámenes recuperados
     */
    int deserializar(TipoFormato formato);
}

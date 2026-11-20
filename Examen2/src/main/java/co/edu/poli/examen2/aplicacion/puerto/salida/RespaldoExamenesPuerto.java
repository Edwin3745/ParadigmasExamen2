package co.edu.poli.examen2.aplicacion.puerto.salida;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

import java.util.List;

/** Puerto de salida: guardar y recuperar copias de respaldo de los exámenes. */
public interface RespaldoExamenesPuerto {

    /**
     * @param examenes exámenes a guardar
     * @param formato  formato de representación
     */
    void respaldar(List<ExamenSangre> examenes, TipoFormato formato);

    /**
     * @param formato formato de representación
     * @return exámenes guardados en el respaldo
     */
    List<ExamenSangre> restaurar(TipoFormato formato);
}

package co.edu.poli.examen2.aplicacion.puerto.entrada;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

import java.util.List;

/**
 * Patrón Facade: punto de acceso único a las operaciones de la aplicación.
 * El adaptador de entrada (controlador JavaFX) depende solo de esta interfaz
 * y no de los cuatro casos de uso individuales.
 */
public interface ExamenesFachada {

    /**
     * @param examen examen a registrar
     * @see RegistrarExamenUseCase#registrar(ExamenSangre)
     */
    void registrar(ExamenSangre examen);

    /**
     * @return exámenes registrados
     * @see ListarExamenesUseCase#listar()
     */
    List<ExamenSangre> listar();

    /**
     * @param formato formato del respaldo
     * @return cantidad de exámenes guardados
     * @see SerializarExamenesUseCase#serializar(TipoFormato)
     */
    int respaldar(TipoFormato formato);

    /**
     * @param formato formato del respaldo
     * @return cantidad de exámenes recuperados
     * @see DeserializarExamenesUseCase#deserializar(TipoFormato)
     */
    int restaurar(TipoFormato formato);
}

package co.edu.poli.examen2.aplicacion.servicio;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;
import co.edu.poli.examen2.aplicacion.puerto.entrada.DeserializarExamenesUseCase;
import co.edu.poli.examen2.aplicacion.puerto.entrada.ExamenesFachada;
import co.edu.poli.examen2.aplicacion.puerto.entrada.ListarExamenesUseCase;
import co.edu.poli.examen2.aplicacion.puerto.entrada.RegistrarExamenUseCase;
import co.edu.poli.examen2.aplicacion.puerto.entrada.SerializarExamenesUseCase;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

import java.util.List;

/**
 * Implementación de la fachada. Solo delega en los casos de uso: no contiene
 * reglas de negocio ni acceso a archivos.
 */
public class ExamenesFachadaImpl implements ExamenesFachada {

    private final RegistrarExamenUseCase registrarUC;
    private final ListarExamenesUseCase listarUC;
    private final SerializarExamenesUseCase serializarUC;
    private final DeserializarExamenesUseCase deserializarUC;

    /**
     * @param registrarUC    caso de uso de registro
     * @param listarUC       caso de uso de listado
     * @param serializarUC   caso de uso de respaldo
     * @param deserializarUC caso de uso de restauración
     */
    public ExamenesFachadaImpl(RegistrarExamenUseCase registrarUC,
                               ListarExamenesUseCase listarUC,
                               SerializarExamenesUseCase serializarUC,
                               DeserializarExamenesUseCase deserializarUC) {
        this.registrarUC = registrarUC;
        this.listarUC = listarUC;
        this.serializarUC = serializarUC;
        this.deserializarUC = deserializarUC;
    }

    @Override
    public void registrar(ExamenSangre examen) {
        registrarUC.registrar(examen);
    }

    @Override
    public List<ExamenSangre> listar() {
        return listarUC.listar();
    }

    @Override
    public int respaldar(TipoFormato formato) {
        return serializarUC.serializar(formato);
    }

    @Override
    public int restaurar(TipoFormato formato) {
        return deserializarUC.deserializar(formato);
    }
}

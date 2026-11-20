package co.edu.poli.examen2.aplicacion.servicio;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;
import co.edu.poli.examen2.aplicacion.puerto.entrada.SerializarExamenesUseCase;
import co.edu.poli.examen2.aplicacion.puerto.salida.ExamenRepositorio;
import co.edu.poli.examen2.aplicacion.puerto.salida.RespaldoExamenesPuerto;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

import java.util.List;

/** Caso de uso: guardar los exámenes en un respaldo. */
public class SerializarExamenesService implements SerializarExamenesUseCase {

    private final ExamenRepositorio repositorio;
    private final RespaldoExamenesPuerto respaldo;

    /**
     * @param repositorio origen de los exámenes
     * @param respaldo    destino del respaldo
     */
    public SerializarExamenesService(ExamenRepositorio repositorio, RespaldoExamenesPuerto respaldo) {
        this.repositorio = repositorio;
        this.respaldo = respaldo;
    }

    @Override
    public int serializar(TipoFormato formato) {
        List<ExamenSangre> examenes = repositorio.listarTodos();
        respaldo.respaldar(examenes, formato);
        return examenes.size();
    }
}

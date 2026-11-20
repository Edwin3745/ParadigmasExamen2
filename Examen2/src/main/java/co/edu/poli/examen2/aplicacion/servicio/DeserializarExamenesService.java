package co.edu.poli.examen2.aplicacion.servicio;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;
import co.edu.poli.examen2.aplicacion.puerto.entrada.DeserializarExamenesUseCase;
import co.edu.poli.examen2.aplicacion.puerto.salida.ExamenRepositorio;
import co.edu.poli.examen2.aplicacion.puerto.salida.RespaldoExamenesPuerto;
import co.edu.poli.examen2.dominio.excepcion.ExamenDuplicadoException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Caso de uso: recuperar los exámenes desde un respaldo. */
public class DeserializarExamenesService implements DeserializarExamenesUseCase {

    private final ExamenRepositorio repositorio;
    private final RespaldoExamenesPuerto respaldo;

    /**
     * @param repositorio destino de los exámenes recuperados
     * @param respaldo    origen del respaldo
     */
    public DeserializarExamenesService(ExamenRepositorio repositorio, RespaldoExamenesPuerto respaldo) {
        this.repositorio = repositorio;
        this.respaldo = respaldo;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ExamenDuplicadoException si el respaldo contiene dos exámenes con el mismo código
     */
    @Override
    public int deserializar(TipoFormato formato) {
        List<ExamenSangre> examenes = respaldo.restaurar(formato);
        Set<String> codigos = new HashSet<>();
        for (ExamenSangre examen : examenes) {
            if (!codigos.add(examen.getCodigo())) {
                throw new ExamenDuplicadoException(examen.getCodigo());
            }
        }
        repositorio.reemplazarTodos(examenes);
        return examenes.size();
    }
}

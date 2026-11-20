package co.edu.poli.examen2.aplicacion.servicio;

import co.edu.poli.examen2.aplicacion.puerto.entrada.ListarExamenesUseCase;
import co.edu.poli.examen2.aplicacion.puerto.salida.ExamenRepositorio;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

import java.util.List;

/** Caso de uso: listar los exámenes registrados. */
public class ListarExamenesService implements ListarExamenesUseCase {

    private final ExamenRepositorio repositorio;

    /** @param repositorio puerto de salida de donde se leen los exámenes */
    public ListarExamenesService(ExamenRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<ExamenSangre> listar() {
        return repositorio.listarTodos();
    }
}

package co.edu.poli.examen2.aplicacion.servicio;

import co.edu.poli.examen2.aplicacion.puerto.entrada.RegistrarExamenUseCase;
import co.edu.poli.examen2.aplicacion.puerto.salida.ExamenRepositorio;
import co.edu.poli.examen2.dominio.excepcion.ExamenDuplicadoException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

import java.util.Objects;

/** Caso de uso: registrar un examen evitando códigos duplicados. */
public class RegistrarExamenService implements RegistrarExamenUseCase {

    private final ExamenRepositorio repositorio;

    /** @param repositorio puerto de salida donde se guardan los exámenes */
    public RegistrarExamenService(ExamenRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void registrar(ExamenSangre examen) {
        Objects.requireNonNull(examen, "El examen no puede ser nulo");
        if (repositorio.existePorCodigo(examen.getCodigo())) {
            throw new ExamenDuplicadoException(examen.getCodigo());
        }
        repositorio.guardar(examen);
    }
}

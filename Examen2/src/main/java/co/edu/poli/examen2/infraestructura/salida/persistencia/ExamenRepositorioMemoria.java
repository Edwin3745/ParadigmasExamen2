package co.edu.poli.examen2.infraestructura.salida.persistencia;

import co.edu.poli.examen2.aplicacion.puerto.salida.ExamenRepositorio;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Adaptador de salida: guarda los exámenes en memoria mientras la aplicación está abierta. */
public class ExamenRepositorioMemoria implements ExamenRepositorio {

    private final List<ExamenSangre> examenes = new ArrayList<>();

    @Override
    public void guardar(ExamenSangre examen) {
        examenes.add(examen);
    }

    @Override
    public List<ExamenSangre> listarTodos() {
        return Collections.unmodifiableList(new ArrayList<>(examenes));
    }

    @Override
    public boolean existePorCodigo(String codigo) {
        for (ExamenSangre examen : examenes) {
            if (examen.getCodigo().equals(codigo)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void reemplazarTodos(List<ExamenSangre> nuevos) {
        examenes.clear();
        examenes.addAll(nuevos);
    }
}

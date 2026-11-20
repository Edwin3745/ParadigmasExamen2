package co.edu.poli.examen2.aplicacion;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;
import co.edu.poli.examen2.aplicacion.puerto.salida.RespaldoExamenesPuerto;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FactorRH;
import co.edu.poli.examen2.dominio.modelo.TipoSangre;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Utilidades de prueba: datos de ejemplo y un respaldo falso en memoria. */
final class Dobles {

    private Dobles() {
    }

    static ExamenSangre examen(String codigo) {
        return ExamenSangre.builder().codigo(codigo).paciente("Paciente " + codigo).costo(1000)
                .tipoSangre(TipoSangre.O).factorRH(FactorRH.NEGATIVO).build();
    }

    static class RespaldoFalso implements RespaldoExamenesPuerto {
        final Map<TipoFormato, List<ExamenSangre>> guardado = new EnumMap<>(TipoFormato.class);

        @Override
        public void respaldar(List<ExamenSangre> examenes, TipoFormato formato) {
            guardado.put(formato, new ArrayList<>(examenes));
        }

        @Override
        public List<ExamenSangre> restaurar(TipoFormato formato) {
            return guardado.getOrDefault(formato, new ArrayList<>());
        }
    }
}

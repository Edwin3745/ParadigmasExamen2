package co.edu.poli.examen2.dominio.modelo;

import co.edu.poli.examen2.dominio.excepcion.ExamenInvalidoException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidad principal del sistema: un examen de sangre.
 * <p>
 * Es inmutable y solo puede crearse mediante {@link Builder}, que valida todos los datos
 * antes de construir el objeto (patrón creacional Builder).
 */
public final class ExamenSangre implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String codigo;
    private final String paciente;
    private final double costo;
    private final TipoSangre tipoSangre;
    private final FactorRH factorRH;

    private ExamenSangre(Builder builder) {
        this.codigo = builder.codigo;
        this.paciente = builder.paciente;
        this.costo = builder.costo;
        this.tipoSangre = builder.tipoSangre;
        this.factorRH = builder.factorRH;
    }

    /**
     * @return un nuevo {@link Builder} vacío
     */
    public static Builder builder() {
        return new Builder();
    }

    /** @return código único del examen */
    public String getCodigo() {
        return codigo;
    }

    /** @return nombre del paciente */
    public String getPaciente() {
        return paciente;
    }

    /** @return costo del examen (siempre mayor que cero) */
    public double getCosto() {
        return costo;
    }

    /** @return tipo de sangre */
    public TipoSangre getTipoSangre() {
        return tipoSangre;
    }

    /** @return factor Rh */
    public FactorRH getFactorRH() {
        return factorRH;
    }

    /**
     * @return grupo sanguíneo completo, por ejemplo {@code A+} u {@code O-}
     */
    public String getGrupoCompleto() {
        return tipoSangre.name() + factorRH.getSimbolo();
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof ExamenSangre)) {
            return false;
        }
        ExamenSangre e = (ExamenSangre) otro;
        return Double.compare(costo, e.costo) == 0
                && codigo.equals(e.codigo)
                && paciente.equals(e.paciente)
                && tipoSangre == e.tipoSangre
                && factorRH == e.factorRH;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, paciente, costo, tipoSangre, factorRH);
    }

    @Override
    public String toString() {
        return codigo + " | " + paciente + " | " + costo + " | " + getGrupoCompleto();
    }

    /**
     * Patrón Builder: construye un {@link ExamenSangre} validando todos sus datos.
     * A diferencia de un constructor que falla en el primer error, {@link #build()}
     * acumula todos los errores y los informa juntos.
     */
    public static class Builder {

        private String codigo;
        private String paciente;
        private double costo;
        private TipoSangre tipoSangre;
        private FactorRH factorRH;

        /**
         * @param valor código del examen (se eliminan espacios al inicio y al final)
         * @return este builder
         */
        public Builder codigo(String valor) {
            this.codigo = valor == null ? null : valor.trim();
            return this;
        }

        /**
         * @param valor nombre del paciente (se eliminan espacios al inicio y al final)
         * @return este builder
         */
        public Builder paciente(String valor) {
            this.paciente = valor == null ? null : valor.trim();
            return this;
        }

        /**
         * @param valor costo del examen; debe ser un número finito mayor que cero
         * @return este builder
         */
        public Builder costo(double valor) {
            this.costo = valor;
            return this;
        }

        /**
         * @param valor tipo de sangre
         * @return este builder
         */
        public Builder tipoSangre(TipoSangre valor) {
            this.tipoSangre = valor;
            return this;
        }

        /**
         * @param valor factor Rh
         * @return este builder
         */
        public Builder factorRH(FactorRH valor) {
            this.factorRH = valor;
            return this;
        }

        /**
         * Valida los datos y construye el examen.
         *
         * @return un examen válido
         * @throws ExamenInvalidoException si algún dato incumple las reglas; el mensaje
         *                                 incluye todos los errores encontrados
         */
        public ExamenSangre build() {
            List<String> errores = new ArrayList<>();
            if (codigo == null || codigo.isEmpty()) {
                errores.add("El código del examen es obligatorio.");
            }
            if (paciente == null || paciente.isEmpty()) {
                errores.add("El nombre del paciente es obligatorio.");
            }
            if (Double.isNaN(costo) || Double.isInfinite(costo) || costo <= 0) {
                errores.add("El costo debe ser un número mayor que cero.");
            }
            if (tipoSangre == null) {
                errores.add("El tipo de sangre es obligatorio.");
            }
            if (factorRH == null) {
                errores.add("El factor RH es obligatorio.");
            }
            if (!errores.isEmpty()) {
                throw new ExamenInvalidoException(errores);
            }
            return new ExamenSangre(this);
        }
    }
}

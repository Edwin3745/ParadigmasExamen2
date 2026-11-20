package co.edu.poli.examen2.dominio.modelo;

/**
 * Factor Rh de la sangre.
 */
public enum FactorRH {
    POSITIVO("+"),
    NEGATIVO("-");

    private final String simbolo;

    FactorRH(String simbolo) {
        this.simbolo = simbolo;
    }

    /**
     * @return símbolo del factor: {@code +} o {@code -}
     */
    public String getSimbolo() {
        return simbolo;
    }
}

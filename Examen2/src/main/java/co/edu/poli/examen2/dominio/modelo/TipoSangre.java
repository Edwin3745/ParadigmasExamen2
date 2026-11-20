package co.edu.poli.examen2.dominio.modelo;

/**
 * Grupos sanguíneos del sistema ABO.
 */
public enum TipoSangre {
    A, B, AB, O;

    /**
     * Convierte un texto (por ejemplo, el valor de un ComboBox) en un tipo de sangre.
     *
     * @param texto texto a convertir; se ignoran mayúsculas, minúsculas y espacios
     * @return el tipo de sangre correspondiente, o {@code null} si el texto es nulo,
     *         está vacío o no corresponde a ningún tipo
     */
    public static TipoSangre desdeTexto(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.trim();
        for (TipoSangre tipo : values()) {
            if (tipo.name().equalsIgnoreCase(limpio)) {
                return tipo;
            }
        }
        return null;
    }
}

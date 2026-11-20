package co.edu.poli.examen2.aplicacion.puerto;

/**
 * Formatos en los que se puede guardar un respaldo. Es la opción que elige el usuario;
 * cada valor se asocia con una estrategia concreta en la infraestructura.
 */
public enum TipoFormato {
    BINARIO("Binario (.ser)", "ser"),
    CSV("CSV (.csv)", "csv");

    private final String etiqueta;
    private final String extension;

    TipoFormato(String etiqueta, String extension) {
        this.etiqueta = etiqueta;
        this.extension = extension;
    }

    /** @return texto para mostrar en la interfaz */
    public String getEtiqueta() {
        return etiqueta;
    }

    /** @return extensión del archivo, sin punto */
    public String getExtension() {
        return extension;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}

package co.edu.poli.examen2.infraestructura.salida.archivo;

import co.edu.poli.examen2.dominio.excepcion.ExamenInvalidoException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FactorRH;
import co.edu.poli.examen2.dominio.modelo.TipoSangre;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Estrategia concreta: texto CSV en UTF-8 (una línea por examen, con encabezado).
 * Los campos con comas, comillas o saltos de línea se escriben entre comillas dobles,
 * y las comillas internas se duplican.
 */
public class FormatoCsv implements FormatoRespaldo {

    private static final String ENCABEZADO = "codigo,paciente,costo,tipoSangre,factorRH";
    private static final int COLUMNAS = 5;

    @Override
    public byte[] serializar(List<ExamenSangre> examenes) {
        StringBuilder sb = new StringBuilder(ENCABEZADO).append('\n');
        for (ExamenSangre e : examenes) {
            sb.append(escapar(e.getCodigo())).append(',')
              .append(escapar(e.getPaciente())).append(',')
              .append(e.getCosto()).append(',')
              .append(e.getTipoSangre().name()).append(',')
              .append(e.getFactorRH().name()).append('\n');
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public List<ExamenSangre> deserializar(byte[] datos) {
        String texto = new String(datos, StandardCharsets.UTF_8);
        if (texto.startsWith("\uFEFF")) {
            texto = texto.substring(1);
        }
        List<List<String>> filas = leerFilas(texto);
        if (filas.isEmpty() || !ENCABEZADO.equals(String.join(",", filas.get(0)))) {
            throw new ExamenInvalidoException("El archivo CSV no tiene el encabezado esperado: " + ENCABEZADO);
        }
        List<ExamenSangre> examenes = new ArrayList<>();
        for (int i = 1; i < filas.size(); i++) {
            examenes.add(construir(filas.get(i), i + 1));
        }
        return examenes;
    }

    private static ExamenSangre construir(List<String> campos, int linea) {
        if (campos.size() != COLUMNAS) {
            throw new ExamenInvalidoException("Línea " + linea + ": se esperaban " + COLUMNAS + " columnas.");
        }
        try {
            return ExamenSangre.builder()
                    .codigo(campos.get(0))
                    .paciente(campos.get(1))
                    .costo(Double.parseDouble(campos.get(2).trim()))
                    .tipoSangre(TipoSangre.valueOf(campos.get(3).trim().toUpperCase()))
                    .factorRH(FactorRH.valueOf(campos.get(4).trim().toUpperCase()))
                    .build();
        } catch (IllegalArgumentException e) {
            // Incluye NumberFormatException y valores de enumeración desconocidos
            throw new ExamenInvalidoException("Línea " + linea + ": valor inválido (" + e.getMessage() + ").");
        } catch (ExamenInvalidoException e) {
            throw new ExamenInvalidoException("Línea " + linea + ": " + e.getErrores());
        }
    }

    private static String escapar(String valor) {
        boolean requiereComillas = valor.indexOf(',') >= 0 || valor.indexOf('"') >= 0
                || valor.indexOf('\n') >= 0 || valor.indexOf('\r') >= 0;
        if (!requiereComillas) {
            return valor;
        }
        return '"' + valor.replace("\"", "\"\"") + '"';
    }

    /** Divide el texto en filas y campos respetando las comillas. */
    private static List<List<String>> leerFilas(String texto) {
        List<List<String>> filas = new ArrayList<>();
        List<String> fila = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean entreComillas = false;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (entreComillas) {
                if (c == '"') {
                    if (i + 1 < texto.length() && texto.charAt(i + 1) == '"') {
                        campo.append('"');
                        i++;
                    } else {
                        entreComillas = false;
                    }
                } else {
                    campo.append(c);
                }
            } else if (c == '"') {
                entreComillas = true;
            } else if (c == ',') {
                fila.add(campo.toString());
                campo.setLength(0);
            } else if (c == '\n') {
                fila.add(campo.toString());
                campo.setLength(0);
                agregarSiNoEstaVacia(filas, fila);
                fila = new ArrayList<>();
            } else if (c != '\r') {
                campo.append(c);
            }
        }
        if (entreComillas) {
            throw new ExamenInvalidoException("El archivo CSV tiene comillas sin cerrar.");
        }
        if (campo.length() > 0 || !fila.isEmpty()) {
            fila.add(campo.toString());
            agregarSiNoEstaVacia(filas, fila);
        }
        return filas;
    }

    private static void agregarSiNoEstaVacia(List<List<String>> filas, List<String> fila) {
        boolean vacia = fila.size() == 1 && fila.get(0).isEmpty();
        if (!vacia) {
            filas.add(fila);
        }
    }
}

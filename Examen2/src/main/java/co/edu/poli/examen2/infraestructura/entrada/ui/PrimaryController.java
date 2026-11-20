package co.edu.poli.examen2.infraestructura.entrada.ui;

import co.edu.poli.examen2.aplicacion.puerto.TipoFormato;
import co.edu.poli.examen2.aplicacion.puerto.entrada.ExamenesFachada;
import co.edu.poli.examen2.dominio.excepcion.ExamenDuplicadoException;
import co.edu.poli.examen2.dominio.excepcion.ExamenInvalidoException;
import co.edu.poli.examen2.dominio.modelo.ExamenSangre;
import co.edu.poli.examen2.dominio.modelo.FactorRH;
import co.edu.poli.examen2.dominio.modelo.TipoSangre;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * Adaptador de entrada: recibe los eventos de la interfaz JavaFX y los traduce a
 * llamadas a la {@link ExamenesFachada}. No contiene reglas de negocio ni acceso a archivos.
 */
public class PrimaryController {

    private final ExamenesFachada fachada;

    @FXML
    private TextField txtCodigoExamen;
    @FXML
    private TextField txtNombrePaciente;
    @FXML
    private TextField txtCosto;
    @FXML
    private ComboBox<String> cmbTipoSangre;
    @FXML
    private RadioButton rbPositivo;
    @FXML
    private RadioButton rbNegativo;
    @FXML
    private ComboBox<TipoFormato> cmbFormato;
    @FXML
    private TextArea txtAreaMensajes;

    /**
     * @param fachada punto de acceso a la aplicación, inyectado por el ensamblador
     */
    public PrimaryController(ExamenesFachada fachada) {
        this.fachada = fachada;
    }

    /** Llena los selectores con los valores del dominio. Lo invoca JavaFX al cargar el FXML. */
    @FXML
    public void initialize() {
        for (TipoSangre tipo : TipoSangre.values()) {
            cmbTipoSangre.getItems().add(tipo.name());
        }
        cmbFormato.getItems().addAll(TipoFormato.values());
        cmbFormato.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleGuardar() {
        try {
            ExamenSangre examen = ExamenSangre.builder()
                    .codigo(txtCodigoExamen.getText())
                    .paciente(txtNombrePaciente.getText())
                    .costo(leerCosto())
                    .tipoSangre(TipoSangre.desdeTexto(cmbTipoSangre.getValue()))
                    .factorRH(rbPositivo.isSelected() ? FactorRH.POSITIVO : FactorRH.NEGATIVO)
                    .build();
            fachada.registrar(examen);
            mostrar("Examen " + examen.getCodigo() + " registrado correctamente.");
            limpiarCampos();
        } catch (ExamenInvalidoException | ExamenDuplicadoException e) {
            mostrar(e.getMessage());
        }
    }

    @FXML
    private void handleListar() {
        List<ExamenSangre> examenes = fachada.listar();
        if (examenes.isEmpty()) {
            mostrar("No hay exámenes registrados.");
            return;
        }
        StringBuilder sb = new StringBuilder("Exámenes registrados (" + examenes.size() + "):\n");
        for (ExamenSangre e : examenes) {
            sb.append(String.format("%s | %s | $%.2f | %s%n",
                    e.getCodigo(), e.getPaciente(), e.getCosto(), e.getGrupoCompleto()));
        }
        mostrar(sb.toString());
    }

    @FXML
    private void handleLimpiar() {
        limpiarCampos();
    }

    @FXML
    private void handleSerializar() {
        ejecutarRespaldo(() -> {
            int cantidad = fachada.respaldar(formatoElegido());
            return cantidad + " examen(es) guardado(s) en formato " + formatoElegido().getEtiqueta() + ".";
        });
    }

    @FXML
    private void handleDeserializar() {
        ejecutarRespaldo(() -> {
            int cantidad = fachada.restaurar(formatoElegido());
            return cantidad + " examen(es) recuperado(s) desde formato " + formatoElegido().getEtiqueta() + ".";
        });
    }

    private void ejecutarRespaldo(java.util.function.Supplier<String> operacion) {
        try {
            mostrar(operacion.get());
        } catch (ExamenInvalidoException | ExamenDuplicadoException e) {
            mostrar("Respaldo inválido. " + e.getMessage());
        } catch (RuntimeException e) {
            mostrar("Error: " + e.getMessage());
        }
    }

    private TipoFormato formatoElegido() {
        return cmbFormato.getValue();
    }

    /**
     * Convierte el texto del costo en número. Si está vacío o no es numérico devuelve NaN,
     * y el dominio lo informa junto con los demás errores.
     */
    private double leerCosto() {
        String texto = txtCosto.getText();
        if (texto == null || texto.trim().isEmpty()) {
            return Double.NaN;
        }
        try {
            return Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    private void limpiarCampos() {
        txtCodigoExamen.clear();
        txtNombrePaciente.clear();
        txtCosto.clear();
        cmbTipoSangre.getSelectionModel().clearSelection();
        rbPositivo.setSelected(true);
    }

    private void mostrar(String mensaje) {
        txtAreaMensajes.setText(mensaje);
    }
}

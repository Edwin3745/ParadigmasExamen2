package co.edu.poli.examen2.configuracion;

import co.edu.poli.examen2.aplicacion.puerto.entrada.ExamenesFachada;
import co.edu.poli.examen2.infraestructura.entrada.ui.PrimaryController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada de JavaFX. Arma la aplicación con el {@link Ensamblador}
 * e inyecta la fachada en el controlador.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        ExamenesFachada fachada = Ensamblador.crearFachada();

        FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/poli/examen2/primary.fxml"));
        loader.setControllerFactory(tipo -> new PrimaryController(fachada));
        Parent raiz = loader.load();

        stage.setTitle("Exámenes de Sangre");
        stage.setScene(new Scene(raiz));
        stage.setResizable(false);
        stage.show();
    }

    /** @param args argumentos de línea de comandos (no se usan) */
    public static void main(String[] args) {
        launch(args);
    }
}

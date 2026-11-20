module co.edu.poli.examen2 {
    requires javafx.controls;
    requires javafx.fxml;

    // JavaFX necesita acceso reflexivo para inyectar los campos @FXML y los manejadores
    opens co.edu.poli.examen2.infraestructura.entrada.ui to javafx.fxml;

    // App extiende Application: JavaFX debe poder instanciarla
    exports co.edu.poli.examen2.configuracion;
}

package org.amagana.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Notificacion {

    public void mostrarNotificacion(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("SuperMarket ALERT");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // Cambia el icono (reemplaza "icon.png" con tu propio archivo de imagen)
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/org/amagana/Image/logo.png"));

        // Personaliza el estilo CSS (opcional)
        alert.getDialogPane().getStylesheets().add("/org/amagana/Util/styles.css");

        alert.showAndWait();
    }
}

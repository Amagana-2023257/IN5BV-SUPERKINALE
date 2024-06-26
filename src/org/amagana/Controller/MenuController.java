package org.amagana.Controller;

// */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* IMPORTACIONES */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import org.amagana.System.Main;

/**
 * @author Leonel .--------------------------------------------------------.
 * Documentacion Nombre completo: Angel Leonel Magania Torres
 * .%%%%%%%%%%%%%%%%%%%%%%%%%. .%%%%%%%%%%%%%%%%%%%%%%%%%. Fecha de creacion:
 * .%%%%%%%%%%%%%*%%%%%%%%%%%%. 05/04/2024 .%%%%%%%%%%%%#=%%%%%%%%%%%%.
 * .%%%%%%%%%%%+==%%%%%%%%%%%%. Fecha de Modificacion:
 * .%%%%%%%%%%#==*#%%%%%%%%%%%%. 8/04 // 10/04 // 11/04 // 3/05
 * #%%%%%%%%%%#==%%%%%%%%%%%%. -%%%%%%%%%%%*=#%%%%%%%%%%+
 * .*%%%%%%%%%*====#%%%%%%%#: .=%%%%%%%#=======#%%%%%%+. =%%%%%*=========+%%%%%.
 * +*++==============++. .:================-. .-=============:. .:==========-.
 * .:======-. .:--:.  *
 */
public class MenuController implements Initializable {

    // DECLARACION DE VARIABLES
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* INYECCION */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    @FXML
    private Button btnProducto, btnProveedor, btnCliente, btnCompra, btnSalir;

    @FXML
    private MenuItem btnInfo, btnEmpleado;

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* OBJETOS DE OTRAS CLASES */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    private Main main;

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* GETTERS AND SETTERS*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    // ACCION DE LOS BUTTON MENU
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* BTN */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnInfo) {
            main.sobreView();
        } else if (event.getSource() == btnCliente) {
            main.clienteView();
        } else if (event.getSource() == btnProveedor) {
            main.proveedorView();
        } else if (event.getSource() == btnCompra) {
            main.compraView();
        } else if (event.getSource() == btnProducto) {
            main.productoView();
        } else if (event.getSource() == btnEmpleado) {
            main.empleadoView();
        } else if (event.getSource() == btnSalir) {
            System.exit(0);
        }

    }

    // METODO ABSTRACTO
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* INIZIALIZABLE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}

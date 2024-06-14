package org.amagana.Controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.amagana.Bean.Empleado;
import org.amagana.DB.Conexion;
import org.amagana.Report.GenerarReportes;
import org.amagana.System.Main;

public class EmpleadoController implements Initializable {

    private Main main;

    private enum Operaciones {
        AGREGAR, ELIMINAR, EDITAR, NINGUNO
    }

    private Operaciones tipoDeOperacion = Operaciones.NINGUNO;

    @FXML
    private TextField txtId, txtNombre, txtApellido, txtSueldo, txtDireccion, txtTurno, txtNombreCargo, txtDescripcionCargo;

    @FXML
    private Button btnMenu, btnAgregar, btnEliminar, btnEditar, btnReportes;

    @FXML
    private TableView<Empleado> tblEmpleados;

    @FXML
    private TableColumn<Empleado, Integer> colId;

    @FXML
    private TableColumn<Empleado, String> colNombre, colApellido, colDireccion, colTurno, colNombreCargo, colDescripcionCargo, colSueldo;

    private ObservableList<Empleado> listaEmpleados;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnMenu) {
            main.menuView();
        }
    }

    public void agregar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnReportes.setText("Cancelar");
                btnEditar.setDisable(true);
                btnEliminar.setDisable(true);
                tipoDeOperacion = Operaciones.AGREGAR;
                break;
            case AGREGAR:
                crearEmpleado();
                JOptionPane.showMessageDialog(null, "Empleado creado con éxito.");
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnReportes.setText("Eliminar");
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = Operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void eliminar() {
        if (tipoDeOperacion == Operaciones.AGREGAR) {
            desactivarControles();
            limpiarControles();
            tipoDeOperacion = Operaciones.NINGUNO;
        } else {
            if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Confirmar eliminación del Empleado?", "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    eliminarEmpleado(tblEmpleados.getSelectionModel().getSelectedItem().getId());
                    listaEmpleados.remove(tblEmpleados.getSelectionModel().getSelectedItem());
                    limpiarControles();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona un empleado.");
            }
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtId.setEditable(false);
                    tipoDeOperacion = Operaciones.EDITAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciona un empleado.");
                }
                break;
            case EDITAR:
                actualizarEmpleado();
                JOptionPane.showMessageDialog(null, "Empleado actualizado con éxito.");
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperacion = Operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void reporte() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                imprimirReporte();
                break;
            case AGREGAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                tipoDeOperacion = Operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void imprimirReporte(){
        
        Map parametros = new HashMap();
        parametros.put("id", null);
        GenerarReportes.mostrarRepsorters("ReporteEmpleados.jasper", "Reporte de los Empleados", parametros);
    }

    public void cargarDatos() {
        tblEmpleados.setItems(getEmpleados());
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTurno.setCellValueFactory(new PropertyValueFactory<>("turno"));
        colNombreCargo.setCellValueFactory(new PropertyValueFactory<>("nombreCargo"));
        colDescripcionCargo.setCellValueFactory(new PropertyValueFactory<>("descripcionCargo"));
    }

    public void seleccionarElementos() {
        txtId.setText(String.valueOf(tblEmpleados.getSelectionModel().getSelectedItem().getId()));
        txtNombre.setText(tblEmpleados.getSelectionModel().getSelectedItem().getNombre());
        txtApellido.setText(tblEmpleados.getSelectionModel().getSelectedItem().getApellido());
        txtSueldo.setText(String.valueOf(tblEmpleados.getSelectionModel().getSelectedItem().getSueldo()));
        txtDireccion.setText(tblEmpleados.getSelectionModel().getSelectedItem().getDireccion());
        txtTurno.setText(tblEmpleados.getSelectionModel().getSelectedItem().getTurno());
        txtNombreCargo.setText(tblEmpleados.getSelectionModel().getSelectedItem().getNombreCargo());
        txtDescripcionCargo.setText(tblEmpleados.getSelectionModel().getSelectedItem().getDescripcionCargo());
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtSueldo.setEditable(false);
        txtDireccion.setEditable(false);
        txtTurno.setEditable(false);
        txtNombreCargo.setEditable(false);
        txtDescripcionCargo.setEditable(false);
    }

    public void activarControles() {
        txtId.setEditable(true);
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtSueldo.setEditable(true);
        txtDireccion.setEditable(true);
        txtTurno.setEditable(true);
        txtNombreCargo.setEditable(true);
        txtDescripcionCargo.setEditable(true);
    }

    public void limpiarControles() {
        txtId.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtSueldo.clear();
        txtDireccion.clear();
        txtTurno.clear();
        txtNombreCargo.clear();
        txtDescripcionCargo.clear();
    }

    public void crearEmpleado() {
        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_crear_empleado_con_cargo(?, ?, ?, ?, ?, ?, ?)");
            sp.setString(1, txtNombre.getText());
            sp.setString(2, txtApellido.getText());
            sp.setBigDecimal(3, new BigDecimal(txtSueldo.getText()));
            sp.setString(4, txtDireccion.getText());
            sp.setString(5, txtTurno.getText());
            sp.setString(6, txtNombreCargo.getText());
            sp.setString(7, txtDescripcionCargo.getText());
            sp.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void actualizarEmpleado() {
        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_actualizar_empleado_con_cargo(?, ?, ?, ?, ?, ?, ?, ?)");
            sp.setInt(1, Integer.parseInt(txtId.getText()));
            sp.setString(2, txtNombre.getText());
            sp.setString(3, txtApellido.getText());
            sp.setBigDecimal(4, new BigDecimal(txtSueldo.getText()));
            sp.setString(5, txtDireccion.getText());
            sp.setString(6, txtTurno.getText());
            sp.setString(7, txtNombreCargo.getText());
            sp.setString(8, txtDescripcionCargo.getText());
            sp.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void eliminarEmpleado(int id) {
        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_eliminar_empleado_con_cargo(?)");
            sp.setInt(1, id);
            sp.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public ObservableList<Empleado> getEmpleados() {
        ArrayList<Empleado> lista = new ArrayList<>();
        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_listar_empleados_con_cargo()");
            ResultSet resultado = sp.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleado(
                        resultado.getInt("id"),
                        resultado.getString("nombre"),
                        resultado.getString("apellido"),
                        resultado.getBigDecimal("sueldo"),
                        resultado.getString("direccion"),
                        resultado.getString("turno"),
                        resultado.getString("cargo"),
                        resultado.getString("descripcion")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleados = FXCollections.observableArrayList(lista);
    }
}

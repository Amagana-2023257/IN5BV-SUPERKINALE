package org.amagana.Controller;

// */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* IMPORTACIONES */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
import java.net.URL;
import java.sql.*;
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
import org.amagana.Bean.Proveedor;
import org.amagana.System.Main;
import org.amagana.DB.Conexion;
import org.amagana.Report.GenerarReportes;

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
 * .:======-. .:--:. *
 */
public class ProveedorController implements Initializable {

    // DECLARACION DE VARIABLES
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* OBJETOS DE OTRAS CLASES */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    private Notificacion notificacion = new Notificacion();
    private Main main;

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* DECLARACION DE NUMERADORES */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*  INYECCION  */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    @FXML
    private Button btnMenu, btnAgregar, btnEliminar, btnEditar, btnReportes;
    @FXML
    private TextField txtId, txtNit, txtNombre, txtApellido, txtDireccion, txtRazonSocial, txtContacto, txtWeb, txtEmail, txtNumeroP, txtNumeroS, txtTelefonoObservaciones, txtDescripcionEmail;
    @FXML
    private TableView tblProveedores;
    @FXML
    private TableColumn colNit, colNombre, colApellido, colDireccion, colRazonSocial, colContacto, colWeb, colEmail, colDWeb, colNumeroP, colNumeroS, coltelefonoObservaciones;

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*  VARIABLE DE TABLE  */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    private ObservableList<Proveedor> listaProveedores;

    // Getters y Setters
    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    // Metodo INIT
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();

    }

    // Funcionalidad de Buttons
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* REGRESAR AL MENU */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnMenu) {
            main.menuView();
        }
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* BTN CREAR CLIENTE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/    
    public void agregar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnReportes.setText("Cancelar");
                btnEditar.setDisable(true);
                btnEliminar.setDisable(true);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                crearProveedor();
                notificacion.mostrarNotificacion("CLIENTE CREADO CON EXITO!!");
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnReportes.setText("Eliminar");
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* BTN ELIMINAR CLIENTE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void eliminar() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblProveedores.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar Si eliminar el Proveedores UwU's", "Eliminar Proeveedor", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        eliminarProveedor(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getNit());
                        listaProveedores.remove(tblProveedores.getSelectionModel().getSelectedItem());
                        limpiarControles();
                    }
                } else {
                    notificacion.mostrarNotificacion("SELECCIONA UNO BB");
                }
        }
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* BTN EDITAR CLIENTE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblProveedores.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciona a uno BB");
                }
                break;
            case ACTUALIZAR:
                actualizarProveedor();
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* BTN REPORTE CLIENTE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void reporte() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                imprimirReporte();
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void imprimirReporte(){
        
        Map parametros = new HashMap();
        parametros.put("id", null);
        GenerarReportes.mostrarRepsorters("ReporteProveedores.jasper", "Reporte de los Proveedores", parametros);
    }

    // Controles de la tabla
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* LISTA Proveedores*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void cargarDatos() {
        tblProveedores.setItems(getProveedores());
        colNit.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("nit"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("apellido"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("direccion"));
        colRazonSocial.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("razonSocial"));
        colContacto.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("contacto"));
        colWeb.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("web"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("email"));
        colDWeb.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("descripcion"));
        colNumeroP.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("numeroPrincipal"));
        colNumeroS.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("numeroSecundario"));
        coltelefonoObservaciones.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("telefonoObservaciones"));
    }

    public void seleccionarElementos() {
        txtId.setText(String.valueOf(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getId()));
        txtNit.setText(String.valueOf(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getNit()));
        txtNombre.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getNombre());
        txtApellido.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getApellido());
        txtDireccion.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getDireccion());
        txtRazonSocial.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getRazonSocial());
        txtContacto.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getContacto());
        txtWeb.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getWeb());
        txtEmail.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getEmail());
        txtNumeroP.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getNumeroPrincipal());
        txtNumeroS.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getNumeroSecundario());
        txtDescripcionEmail.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getDescripcionEmail());
        txtTelefonoObservaciones.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getTelefonoObservaciones());
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtNit.setEditable(false);
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtDireccion.setEditable(false);
        txtRazonSocial.setEditable(false);
        txtContacto.setEditable(false);
        txtWeb.setEditable(false);
        txtEmail.setEditable(false);
        txtNumeroP.setEditable(false);
        txtNumeroS.setEditable(false);
        txtDescripcionEmail.setEditable(false);
        txtTelefonoObservaciones.setEditable(false);
    }

    public void activarControles() {
        txtId.setEditable(true);
        txtNit.setEditable(true);
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtDireccion.setEditable(true);
        txtRazonSocial.setEditable(true);
        txtContacto.setEditable(true);
        txtWeb.setEditable(true);
        txtEmail.setEditable(true);
        txtNumeroP.setEditable(true);
        txtNumeroS.setEditable(true);
        txtDescripcionEmail.setEditable(true);
        txtTelefonoObservaciones.setEditable(true);
    }

    public void limpiarControles() {
        txtId.clear();
        txtNit.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtDireccion.clear();
        txtRazonSocial.clear();
        txtContacto.clear();
        txtWeb.clear();
        txtEmail.clear();
        txtNumeroP.clear();
        txtNumeroS.clear();
        txtDescripcionEmail.clear();
        txtTelefonoObservaciones.clear();
    }

    public void crearProveedor() {
        Proveedor p = new Proveedor();
        p.setId(Integer.parseInt(txtId.getText()));
        p.setNit(Integer.parseInt(txtNit.getText()));
        p.setNombre(txtNombre.getText());
        p.setApellido(txtApellido.getText());
        p.setDireccion(txtDireccion.getText());
        p.setRazonSocial(txtRazonSocial.getText());
        p.setContacto(txtContacto.getText());
        p.setWeb(txtWeb.getText());
        p.setEmail(txtEmail.getText());
        p.setNumeroPrincipal(txtNumeroP.getText());
        p.setNumeroSecundario(txtNumeroS.getText());
        p.setDescripcionEmail(txtDescripcionEmail.getText());
        p.setTelefonoObservaciones(txtTelefonoObservaciones.getText());

        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_crear_proveedor_con_contacto(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            sp.setInt(1, p.getNit());
            sp.setString(2, p.getNombre());
            sp.setString(3, p.getApellido());
            sp.setString(4, p.getDireccion());
            sp.setString(5, p.getRazonSocial());
            sp.setString(6, p.getContacto());
            sp.setString(7, p.getWeb());
            sp.setString(8, p.getEmail());
            sp.setString(9, p.getNumeroPrincipal());
            sp.setString(10, p.getNumeroSecundario());
            sp.setString(11, p.getDescripcionEmail());
            sp.setString(12, p.getTelefonoObservaciones());
            sp.execute();
            listaProveedores.add(p);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* ACTUALIZAR Proveedores */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    // se llama a el procedimiento sp_actualizar_proveedor
    public void actualizarProveedor() {
        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_actualizar_proveedor_con_contacto(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            Proveedor proveedor = (Proveedor) tblProveedores.getSelectionModel().getSelectedItem();
            proveedor.setId(Integer.parseInt(txtId.getText()));
            proveedor.setNit(Integer.parseInt(txtNit.getText()));
            proveedor.setNombre(txtNombre.getText());
            proveedor.setApellido(txtApellido.getText());
            proveedor.setDireccion(txtDireccion.getText());
            proveedor.setRazonSocial(txtRazonSocial.getText());
            proveedor.setContacto(txtContacto.getText());
            proveedor.setWeb(txtWeb.getText());
            proveedor.setEmail(txtEmail.getText());
            proveedor.setNumeroPrincipal(txtNumeroP.getText());
            proveedor.setNumeroSecundario(txtNumeroS.getText());
            proveedor.setDescripcionEmail(txtDescripcionEmail.getText());
            proveedor.setTelefonoObservaciones(txtTelefonoObservaciones.getText());

            sp.setInt(1, proveedor.getId());
            sp.setInt(2, proveedor.getNit());
            sp.setString(3, proveedor.getNombre());
            sp.setString(4, proveedor.getApellido());
            sp.setString(5, proveedor.getDireccion());
            sp.setString(6, proveedor.getRazonSocial());
            sp.setString(7, proveedor.getContacto());
            sp.setString(8, proveedor.getWeb());
            sp.setString(9, proveedor.getEmail());
            sp.setString(11, proveedor.getNumeroPrincipal());
            sp.setString(12, proveedor.getNumeroSecundario());
            sp.setString(13, proveedor.getDescripcionEmail());
            sp.setString(14, proveedor.getTelefonoObservaciones());

            sp.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* LISTAR Proveedores */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public ObservableList<Proveedor> getProveedores() {
        ArrayList<Proveedor> lista = new ArrayList<>();
        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_listar_proveedores_con_contacto()");
            ResultSet resultado = sp.executeQuery();
            while (resultado.next()) {
                lista.add(new Proveedor(
                        resultado.getInt("id"),
                        resultado.getInt("nit"),
                        resultado.getString("nombre"),
                        resultado.getString("apellido"),
                        resultado.getString("direccion"),
                        resultado.getString("razonSocial"),
                        resultado.getString("contacto"),
                        resultado.getString("web"),
                        resultado.getString("email"),
                        resultado.getString("descripcion_email"),
                        resultado.getString("numero_principal"),
                        resultado.getString("numero_secundario"),
                        resultado.getString("descripcion_email"),
                        resultado.getString("telefono_observaciones")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProveedores = FXCollections.observableArrayList(lista);
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* eliminar Proveedor */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void eliminarProveedor(int id) {
        try {
            CallableStatement cs = Conexion.getInstance().getConexion().prepareCall("CALL sp_eliminar_proveedor_con_contacto(?)");
            cs.setInt(1, id);
            cs.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

}

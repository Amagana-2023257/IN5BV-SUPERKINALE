package org.amagana.Controller;

// */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* IMPORTACIONES */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
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
import org.amagana.DB.Conexion;
import org.amagana.Bean.Cliente;
import org.amagana.Controller.Notificacion;
import org.amagana.System.Main;


/**
                                        * @author Leonel                                        
                                                                                                                .--------------------------------------------------------.      
Documentacion Nombre completo: Angel Leonel Magania Torres      .%%%%%%%%%%%%%%%%%%%%%%%%%.      
                                                                                                            .%%%%%%%%%%%%%%%%%%%%%%%%%.      
* Fecha de creacion:                                                                           .%%%%%%%%%%%%%*%%%%%%%%%%%%.      
  * 05/04/2024                                                                                      .%%%%%%%%%%%%#=%%%%%%%%%%%%.      
                                                                                                             .%%%%%%%%%%%+==%%%%%%%%%%%%.      
* Fecha de Modificacion:                                                                       .%%%%%%%%%%#==*#%%%%%%%%%%%%.      
  8/04 // 10/04 // 11/04 // 3/05                                                                  #%%%%%%%%%%#==%%%%%%%%%%%%.      
                                                                                                                 -%%%%%%%%%%%*=#%%%%%%%%%%+       
                                                                                                                    .*%%%%%%%%%*====#%%%%%%%#:       
                                                                                                                      .=%%%%%%%#=======#%%%%%%+.       
                                                                                                                         =%%%%%*=========+%%%%%.        
                                                                                                                               +*++==============++.         
                                                                                                                                  .:================-.          
                                                                                                                                      .-=============:.           
                                                                                                                                      .:==========-.             
                                                                                                                                         .:======-.               
                                                                                                                                              .:--:.                 

 */


public class ClienteController implements Initializable {
    
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
    private TextField txtNitC, txtNombreC, txtApellidoC, txtDireccionC, txtTelefonoC, txtCorreoC;
    @FXML
    private TableView tblClientes;
    @FXML
    private TableColumn colNit, colNombre, colApellido, colDireccion, colTelefono, colCorreo;
    
    

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*  VARIABLE DE TABLE  */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    private ObservableList<Cliente> listaClientes;

// GET AND SET
    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    // METODO INIT
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
                crearCliente();
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
                if (tblClientes.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar Si eliminar el Cliente UwU's", "Eliminar Cliente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        eliminarCliente(((Cliente) tblClientes.getSelectionModel().getSelectedItem()).getNit());
                        listaClientes.remove(tblClientes.getSelectionModel().getSelectedItem());
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
                if (tblClientes.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtNitC.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciona a uno BB");
                }
                break;
            case ACTUALIZAR:
                actualizarCliente();
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

    // Controles de la tabla
    
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* LISTA CLIENTES*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void cargarDatos() {
        tblClientes.setItems(getClientes());
        colNit.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("nit"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Cliente, String>("apellido"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<Cliente, String>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("direccion"));
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* SABER CUAL CLIENTE ESTA SELECCIONADO */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    
    public void seleccionarElementos() {
        txtNitC.setText(String.valueOf(((Cliente) tblClientes.getSelectionModel().getSelectedItem()).getNit()));
        txtNombreC.setText(((Cliente) tblClientes.getSelectionModel().getSelectedItem()).getNombre());
        txtApellidoC.setText(((Cliente) tblClientes.getSelectionModel().getSelectedItem()).getApellido());
        txtCorreoC.setText(((Cliente) tblClientes.getSelectionModel().getSelectedItem()).getEmail());
        txtTelefonoC.setText(((Cliente) tblClientes.getSelectionModel().getSelectedItem()).getTelefono());
        txtDireccionC.setText(((Cliente) tblClientes.getSelectionModel().getSelectedItem()).getDireccion());
    }

    //  Control de los textField
    
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* DESACTIVA LOS TXTFIELD */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void desactivarControles() {
        txtNitC.setEditable(false);
        txtNombreC.setEditable(false);
        txtApellidoC.setEditable(false);
        txtCorreoC.setEditable(false);
        txtTelefonoC.setEditable(false);
        txtDireccionC.setEditable(false);
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* ACTIVA LOS TXTFIELD */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void activarControles() {
        txtNitC.setEditable(true);
        txtNombreC.setEditable(true);
        txtApellidoC.setEditable(true);
        txtCorreoC.setEditable(true);
        txtTelefonoC.setEditable(true);
        txtDireccionC.setEditable(true);
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* LIMPIA LOS TXTFIELD */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void limpiarControles() {
        txtNitC.clear();
        txtNombreC.clear();
        txtApellidoC.clear();
        txtCorreoC.clear();
        txtTelefonoC.clear();
        txtDireccionC.clear();
    }

    // PROCEDIMIENTOS ALMACENADOS
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* CREAR CLIENTE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void crearCliente() {

        Cliente c = new Cliente();
        c.setNit(Integer.parseInt(txtNitC.getText()));
        c.setNombre(txtNombreC.getText());
        c.setApellido(txtApellidoC.getText());
        c.setDireccion(txtDireccionC.getText());
        c.setEmail(txtCorreoC.getText());
        c.setTelefono(txtTelefonoC.getText());

        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_crear_cliente(?, ?, ?, ?, ?, ?)");
            sp.setInt(1, c.getNit());
            sp.setString(2, c.getNombre());
            sp.setString(3, c.getApellido());
            sp.setString(4, c.getEmail());
            sp.setString(5, c.getTelefono());
            sp.setString(6, c.getDireccion());
            sp.execute();
            listaClientes.add(c);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* ACTUALIZAR CLIENTE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    
    // se llama a el procedimiento sp_actualizar_cliente
    
    public void actualizarCliente() {
        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_actualizar_cliente(?, ?, ?, ?, ?, ?)");
            Cliente c = (Cliente) tblClientes.getSelectionModel().getSelectedItem();
            c.setNombre(txtNombreC.getText());
            c.setApellido(txtApellidoC.getText());
            c.setEmail(txtCorreoC.getText());
            c.setTelefono(txtTelefonoC.getText());
            c.setDireccion(txtDireccionC.getText());

            sp.setInt(1, c.getNit());
            sp.setString(2, c.getNombre());
            sp.setString(3, c.getApellido());
            sp.setString(4, c.getEmail());
            sp.setString(5, c.getTelefono());
            sp.setString(6, c.getDireccion());
            sp.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* LISTAR CLIENTES */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/

    // se llama a el procedimiento sp_listar_cliente 
    
    public ObservableList<Cliente> getClientes() {
        ArrayList<Cliente> lista = new ArrayList<>();
        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_listar_clientes()");
            ResultSet resultado = sp.executeQuery();
            while (resultado.next()) {
                lista.add(new Cliente(resultado.getInt("nit"),
                        resultado.getString("nombre"),
                        resultado.getString("apellido"),
                        resultado.getString("email"),
                        resultado.getString("telefono"),
                        resultado.getString("direccion")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaClientes = FXCollections.observableArrayList(lista);
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* eliminar CLIENTE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/

    // se llama a el procedimiento sp_eliminar_cliente con un nit
    
    public void eliminarCliente(int nit) {
        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_eliminar_cliente(?)");
            sp.setInt(1, nit);
            sp.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}

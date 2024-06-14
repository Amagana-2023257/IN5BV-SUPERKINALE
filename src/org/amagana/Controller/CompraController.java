package org.amagana.Controller;

// */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* IMPORTACIONES */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.amagana.DB.Conexion;
import org.amagana.Bean.Cliente;
import org.amagana.Bean.Compra;
import org.amagana.Controller.Notificacion;
import org.amagana.Report.GenerarReportes;
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
 * .:======-. .:--:. *
 */
public class CompraController implements Initializable {

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
    private TextField txtId, txtDescripcion, txtTotal, txtDetalleProducto, txtIdProducto, txtCantidad, txtPrecioUnitario;
    @FXML
    private DatePicker dpFechaCompra;
    @FXML
    private TableView tblCompras;
    @FXML
    private TableColumn colId, colDescripcion, colTotal, colDetalleProducto, colFechaCompra, colIdDetalle, colCostoUnitario, colCantidad, colNombreProducto;

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*  VARIABLE DE TABLE  */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    private ObservableList<Compra> listaCompras;

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
                crearCompra();
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
                if (tblCompras.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar Si eliminar el Cliente UwU's", "Eliminar Cliente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        eliminarCompra(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getId());
                        listaCompras.remove(tblCompras.getSelectionModel().getSelectedItem());
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
                if (tblCompras.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtId.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciona a uno BB");
                }
                break;
            case ACTUALIZAR:
                actualizarCompra();
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
        GenerarReportes.mostrarRepsorters("ReporteCompras.jasper", "Reporte de los clientes", parametros);
    }

    // Controles de la tabla
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* LISTA COMPRAS*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void cargarDatos() {
        tblCompras.setItems(getCompras());
        colId.setCellValueFactory(new PropertyValueFactory<Compra, Integer>("id"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Compra, String>("descripcion"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Compra, Double>("total"));
        colDetalleProducto.setCellValueFactory(new PropertyValueFactory<Compra, String>("detalleProducto"));
        colFechaCompra.setCellValueFactory(new PropertyValueFactory<Compra, LocalDate>("fechaCompra"));
        // Agregar las columnas faltantes aquí
        colIdDetalle.setCellValueFactory(new PropertyValueFactory<Compra, Integer>("idDetalleCompra"));
        colCostoUnitario.setCellValueFactory(new PropertyValueFactory<Compra, Double>("costoU"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Compra, Integer>("cantidad"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Compra, String>("nombreProducto"));
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* SABER CUAL CLIENTE ESTA SELECCIONADO */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void seleccionarElementos() {
        txtId.setText(String.valueOf(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getId()));
        txtDescripcion.setText(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getDescripcion());
        txtTotal.setText(String.valueOf(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getTotal()));
        txtDetalleProducto.setText(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getProductoS());
        txtIdProducto.setText(String.valueOf(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getProducto()));
        txtCantidad.setText(String.valueOf(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getCantidad()));
        txtPrecioUnitario.setText(String.valueOf(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getCostoU()));
        dpFechaCompra.setValue(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getFecha());
    }

    //  Control de los textField
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* DESACTIVA LOS TXTFIELD */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void desactivarControles() {
        txtId.setEditable(false);
        txtDescripcion.setEditable(false);
        txtTotal.setEditable(false);
        txtDetalleProducto.setEditable(false);
        txtIdProducto.setEditable(false);
        txtCantidad.setEditable(false);
        txtPrecioUnitario.setEditable(false);
        dpFechaCompra.setDisable(true);
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* ACTIVA LOS TXTFIELD */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void activarControles() {
        txtId.setEditable(true);
        txtDescripcion.setEditable(true);
        txtTotal.setEditable(true);
        txtDetalleProducto.setEditable(true);
        txtIdProducto.setEditable(true);
        txtCantidad.setEditable(true);
        txtPrecioUnitario.setEditable(true);
        dpFechaCompra.setDisable(false);
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* LIMPIA LOS TXTFIELD */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void limpiarControles() {
        txtId.clear();
        txtDescripcion.clear();
        txtTotal.clear();
        txtDetalleProducto.clear();
        txtIdProducto.clear();
        txtCantidad.clear();
        txtPrecioUnitario.clear();
        dpFechaCompra.setValue(null);
    }

    // PROCEDIMIENTOS ALMACENADOS
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* CREAR CLIENTE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void crearCompra() {
        // Crear un objeto Compra y establecer sus atributos
        Compra compra = new Compra();
        compra.setFecha(dpFechaCompra.getValue());
        compra.setDescripcion(txtDescripcion.getText());
        compra.setTotal(Double.parseDouble(txtTotal.getText()));
        compra.setFecha(dpFechaCompra.getValue());
        compra.setProducto(Integer.parseInt(txtIdProducto.getText()));
        compra.setCantidad(Integer.parseInt(txtCantidad.getText()));
        compra.setCostoU(Integer.parseInt(txtPrecioUnitario.getText()));

        try {
            // Preparar la llamada al procedimiento almacenado para crear una compra con detalle
            CallableStatement sp = Conexion.getInstance().getConexion().prepareCall("{CALL sp_crear_compra_con_detalle(?, ?, ?, ?, ?, ?)}");
            sp.setDate(1, java.sql.Date.valueOf(compra.getFecha()));
            sp.setString(2, compra.getDescripcion());
            sp.setDouble(3, compra.getTotal());
            sp.setInt(4, compra.getProducto());
            sp.setInt(5, compra.getCantidad());
            sp.setDouble(6, compra.getCostoU());

            // Ejecutar el procedimiento almacenado
            sp.execute();
            cargarDatos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* ACTUALIZAR CLIENTE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    // se llama a el procedimiento sp_actualizar_cliente
    public void actualizarCompra() {
        Compra compra = new Compra();
        compra.setId(Integer.parseInt(txtId.getText()));
        compra.setFecha(dpFechaCompra.getValue());
        compra.setDescripcion(txtDescripcion.getText());
        compra.setTotal(Double.parseDouble(txtTotal.getText()));
        compra.setFecha(dpFechaCompra.getValue());
        compra.setProducto(Integer.parseInt(txtId.getText()));
        compra.setCantidad(Integer.parseInt(txtCantidad.getText()));
        compra.setCostoU(Double.parseDouble(txtPrecioUnitario.getText()));

        try {
            Connection conexion = Conexion.getInstance().getConexion(); // Obtener conexión
            // Llamar al procedimiento almacenado
            PreparedStatement procedimiento = conexion.prepareStatement("{call sp_actualizar_compra(?, ?, ?, ?, ?, ?, ?)}");
            // Establecer los parámetros del procedimiento almacenado
            procedimiento.setInt(1, compra.getId());
            procedimiento.setDate(2, java.sql.Date.valueOf(compra.getFecha()));
            procedimiento.setString(3, compra.getDescripcion());
            procedimiento.setDouble(4, compra.getTotal());
            procedimiento.setDouble(5, compra.getCostoU());
            procedimiento.setInt(6, compra.getCantidad());
            procedimiento.setInt(7, compra.getProducto());
            // Ejecutar el procedimiento almacenado
            procedimiento.executeUpdate();
            System.out.println("Compra actualizada correctamente.");
            cargarDatos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* LISTAR CLIENTES */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    // se llama a el procedimiento sp_listar_cliente 
    public ObservableList<Compra> getCompras() {
        ArrayList<Compra> lista = new ArrayList<>();
        try {
            CallableStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listar_compras_con_detalle()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Compra(
                        resultado.getInt("id_compra"),
                        resultado.getDate("fecha").toLocalDate(),
                        resultado.getString("descripcion_compra"),
                        resultado.getDouble("total"),
                        resultado.getDouble("costoU"),
                        resultado.getInt("cantidad"),
                        resultado.getInt("id_detalle_compra"),
                        resultado.getString("nombre_producto")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(lista);
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* eliminar CLIENTE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    // se llama a el procedimiento sp_eliminar_cliente con un nit
    public void eliminarCompra(int nit) {
        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_eliminar_compra(?)");
            sp.setInt(1, nit);
            sp.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}

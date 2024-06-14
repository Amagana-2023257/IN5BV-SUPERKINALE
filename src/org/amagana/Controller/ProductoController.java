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
import org.amagana.Bean.Producto;
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
public class ProductoController implements Initializable {

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
    private TextField txtId, txtDescripcion, txtPrecioU, txtPrecioM, txtPrecioD, txtImagenP, txtExistencia, txtProveedor, txtTipoProducto;
    @FXML
    private TableView tblProductos;
    @FXML
    private TableColumn colId, colDescripcion, colPrecioU, colPrecioM, colImagenP, colExistencia, colProveedor, colTipoProducto;

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*  VARIABLE DE TABLE  */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    private ObservableList<Producto> listaProductos;

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

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* BTN CREAR PRODUCTO */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/    
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
                crearProducto();
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

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* BTN ELIMINAR PRODUCTO */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void eliminar() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar Si eliminar el Proveedores UwU's", "Eliminar Productos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        eliminarProducto(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getId());
                        listaProductos.remove(tblProductos.getSelectionModel().getSelectedItem());
                        limpiarControles();
                    }
                } else {
                    notificacion.mostrarNotificacion("SELECCIONA UNO BB");
                }
        }
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* BTN EDITAR PRODUCTO */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
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
                actualizarProducto();
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

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* BTN REPORTE PRODUCTO */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
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
        GenerarReportes.mostrarRepsorters("ReporteProductos.jasper", "Reporte de los Productos", parametros);
    }

    // Controles de la tabla
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* LISTA PRODUCTO*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void cargarDatos() {
        tblProductos.setItems(getProducto());
        colId.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("id"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto, String>("descripcion"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioUnitario"));
        colPrecioM.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioMayorista"));
        colImagenP.setCellValueFactory(new PropertyValueFactory<Producto, String>("imagenProducto"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("existencia"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("proveedorId"));
        colTipoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("tipoProductoId"));
    }

    public void seleccionarElementos() {
        txtId.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getId()));
        txtDescripcion.setText(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getDescripcion());
        txtPrecioU.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        txtPrecioM.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayorista()));
        txtImagenP.setText(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getImagenProducto());
        txtExistencia.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
        txtProveedor.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getProveedor()));
        txtTipoProducto.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getTipoProducto()));
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtDescripcion.setEditable(false);
        txtPrecioU.setEditable(false);
        txtPrecioM.setEditable(false);
        txtImagenP.setEditable(false);
        txtExistencia.setEditable(false);
        txtProveedor.setEditable(false);
        txtTipoProducto.setEditable(false);
    }

    public void activarControles() {
        txtId.setEditable(true);
        txtDescripcion.setEditable(true);
        txtPrecioU.setEditable(true);
        txtPrecioM.setEditable(true);
        txtImagenP.setEditable(true);
        txtExistencia.setEditable(true);
        txtProveedor.setEditable(true);
        txtTipoProducto.setEditable(true);
    }

    public void limpiarControles() {
        txtId.clear();
        txtDescripcion.clear();
        txtPrecioU.clear();
        txtPrecioM.clear();
        txtImagenP.clear();
        txtExistencia.clear();
        txtProveedor.clear();
        txtTipoProducto.clear();
    }

    public void crearProducto() {

        Producto p = new Producto();
        p.setDescripcion(txtDescripcion.getText());
        p.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
        p.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
        p.setPrecioMayorista(Double.parseDouble(txtPrecioM.getText()));
        p.setImagenProducto(txtImagenP.getText());
        p.setExistencia(Integer.parseInt(txtExistencia.getText()));
        p.setProveedor(txtProveedor.getText());
        p.setTipoProducto(txtTipoProducto.getText());
        
        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_crear_producto_con_tipo(?, ?, ?, ?, ?, ?, ?, ?)");
            sp.setString(1, p.getDescripcion());
            sp.setDouble(2, p.getPrecioUnitario());
            sp.setDouble(3, p.getPrecioDocena());
            sp.setDouble(4, p.getPrecioMayorista());
            sp.setString(5, p.getImagenProducto());
            sp.setInt(6, p.getExistencia());
            sp.setString(7, p.getProveedor());
            sp.setString(8, p.getTipoProducto());
            sp.execute();
            
            cargarDatos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* ACTUALIZAR PRODUCTO */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    // se llama a el procedimiento sp_actualizar_proveedor
    public void actualizarProducto() {
        try {
            CallableStatement cs = Conexion.getInstance().getConexion().prepareCall("CALL sp_actualizar_producto_con_tipo(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            Producto producto = (Producto) tblProductos.getSelectionModel().getSelectedItem();
            producto.setId(Integer.parseInt(txtId.getText()));
            producto.setDescripcion(txtDescripcion.getText());
            producto.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
            producto.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
            producto.setPrecioMayorista(Double.parseDouble(txtPrecioM.getText()));
            producto.setImagenProducto(txtImagenP.getText());
            producto.setExistencia(Integer.parseInt(txtExistencia.getText()));
            producto.setProveedor(txtProveedor.getText());
            producto.setTipoProducto(txtTipoProducto.getText());

            cs.setInt(1, producto.getId());
            cs.setString(2, producto.getDescripcion());
            cs.setDouble(3, producto.getPrecioUnitario());
            cs.setDouble(4, producto.getPrecioDocena());
            cs.setDouble(5, producto.getPrecioMayorista());
            cs.setString(6, producto.getImagenProducto());
            cs.setInt(7, producto.getExistencia());
            cs.setString(8, producto.getProveedor());
            cs.setString(9, producto.getTipoProducto());

            cs.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* LISTAR PRODUCTO */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public ObservableList<Producto> getProducto() {
        ArrayList<Producto> lista = new ArrayList<>();
        try {
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_listar_productos_con_tipo()");
            ResultSet resultado = sp.executeQuery();
            while (resultado.next()) {
                lista.add(new Producto(
                        resultado.getInt("id"),
                        resultado.getString("Descripcion"),
                        resultado.getInt("existencia"),
                        resultado.getString("imagenP"),
                        resultado.getString("proveedor"),
                        resultado.getString("tipo_producto"),
                        resultado.getDouble("precioU"),
                        resultado.getDouble("precioD"),
                        resultado.getDouble("precioM")
                ));
            }
        } catch (SQLException e) {
            // Manejar la excepción de manera adecuada, como registrarla en un archivo de registro
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableArrayList(lista);
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* eliminar PRODUCTO */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    public void eliminarProducto(int id) {
        try {
            CallableStatement cs = Conexion.getInstance().getConexion().prepareCall("CALL sp_eliminar_producto_con_tipo(?)");
            cs.setInt(1, id);
            cs.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

}

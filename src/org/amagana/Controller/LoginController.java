package org.amagana.Controller;

// */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* IMPORTACIONES */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.awt.AWTException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.amagana.Bean.Usuario;
import org.amagana.DB.Conexion;
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

public class LoginController implements Initializable {

     // DECLARACION DE VARIABLES

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* INYECCION */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasenia;
    @FXML
    private Button btnLogin;

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* OBJETOS DE OTRAS CLASES */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    
    private Main main;
    private final Notificacion notificacion = new Notificacion();
    
     // ACCION DE BUTTON LOGIN

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* BTN */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/

    @FXML
    private void handleButtonAction(ActionEvent event) throws AWTException {
        
        if (event.getSource() == btnLogin) {
            boolean usuarioValido = false;

            String nombreUsuario = txtUsuario.getText().trim();
            String contrasena = txtContrasenia.getText().trim();

            try {
                // Verifica si el usuario con el nombre y la contraseña ingresados existe
                usuarioValido = verificarUsuario(nombreUsuario, contrasena);
            } catch (AWTException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (usuarioValido) {
                
                main.menuView();
                
            } else {
                // Si el usuario no existe, muestra un mensaje de error
                Notificacion nuevaNotificacion = new Notificacion();
                nuevaNotificacion.mostrarNotificacion("Error de Credenciales");
            }
        }
    }
    
    // METODOS DE LA LOGICA DE LOGIN

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* METODO DE VERIFICACION DEL USUARIO */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/

    public boolean verificarUsuario(String nombreInput, String contraseniaInput) throws AWTException {
        // Obtiene la lista de usuarios que coinciden con el nombre y la contraseña ingresados
        List<Usuario> usuarios = buscarUsuariosPorNombreYContrasenia(nombreInput, contraseniaInput);

        // Recorre cada usuario en la lista
        for (Usuario usuarioItem : usuarios) {
            if (sonCredencialesValidas(usuarioItem, nombreInput, contraseniaInput)) {
                notificacion.mostrarNotificacion("Credenciales correctas");
                
                return true;
            }
        }
        return false;
    }

    private boolean sonCredencialesValidas(Usuario usuarioItem, String nombreInput, String contraseniaInput) {
        return usuarioItem.getNombre().trim().equalsIgnoreCase(nombreInput.trim())
                && usuarioItem.getContrasenia().trim().equalsIgnoreCase(contraseniaInput.trim());
    }
    
    // PROCEDIMIENTO ALMACENADO
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* BUSCAR CREDENCIALES DE  USUARIO */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    
    public List<Usuario> buscarUsuariosPorNombreYContrasenia(String nombre, String contrasenia) {
        List<Usuario> usuarios = new ArrayList<>();
        try (
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("CALL sp_login(?, ?)")) {
            sp.setString(1, nombre);
            sp.setString(2, contrasenia); // Corregido: usar contrasenia en lugar de puesto
            ResultSet resultado = sp.executeQuery();
            while (resultado.next()) {
                Usuario usuario = Usuario.getInstancia();
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setContrasenia(resultado.getString("contrasenia"));
                System.out.println(usuario.toString());
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return usuarios;
    }
    
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* METODO ABSTRACTO DE INIZIALIZABLE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* GETTERS AND SETTERS*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    
    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
    
    
}

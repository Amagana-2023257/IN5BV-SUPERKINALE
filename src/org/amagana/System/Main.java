package org.amagana.System;

// */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*  IMPORTACIONES  */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/

import java.io.InputStream;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.amagana.Controller.ClienteController;
import org.amagana.Controller.CompraController;
import org.amagana.Controller.EmpleadoController;
import org.amagana.Controller.LoginController;
import org.amagana.Controller.MenuController;
import org.amagana.Controller.ProductoController;
import org.amagana.Controller.ProveedorController;
import org.amagana.Controller.SobreController;


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

public class Main extends Application {
    
    // DECLARACION DE VARIABLES

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* DECLARACION DE LA ESCENA PRINPAL ( VENTANA PRINCIPAL) */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    
    private Stage escenarioPrincipal;
    private Scene escena;
    
    // */*/*/*/*/*/*/*/*/* DECLARACION DE LA RUTA DE LOS ARCHIVOS DE FXML */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    
    private final String URLVIEW = "/org/amagana/View/";

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* METODO ABSTRACTO DE FX APLICATTION /  MAIN  */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    // UNICO PARAMETRO EL ESCENARIO DECLARADO EN LAS VARIABLES 0W0
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Super Kinal");
        escenarioPrincipal.getIcons().add(new Image(Main.class.getResourceAsStream("/org/amagana/Image/logo.png")));
        loginView();
        escenarioPrincipal.show();
    }
    
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* METODO CAMBIO DE VENTANA */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    // parametros de entrada el view
    // tamanio

    public Initializable cambiarEscena(String fxmlName, int width, int heigth) throws Exception {
        Initializable resultado;
        FXMLLoader loader = new FXMLLoader();

        InputStream file = Main.class.getResourceAsStream(URLVIEW + fxmlName);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(URLVIEW + fxmlName));

        escena = new Scene((AnchorPane) loader.load(file), width, heigth);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();

        resultado = (Initializable) loader.getController();

        return resultado;
    }
    
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* ESCENA LOGIN */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    // SIN PARAMETROS

    public void loginView() {
        try {
            LoginController loginView = (LoginController) cambiarEscena("LoginView.fxml", 900, 600);
            loginView.setMain(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* ESCENA MENU */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    // SIN PARAMETROS
    
    public void menuView() {
        try {
            MenuController menuPrincipalView = (MenuController) cambiarEscena("MenuView.fxml", 700, 500);
            menuPrincipalView.setMain(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* ESCENA CLIENTE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    // SIN PARAMETROS
    
    public void clienteView() {
        try {
            ClienteController clienteView = (ClienteController) cambiarEscena("ClientesView.fxml", 760, 820);
            clienteView.setMain(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* ESCENA SOBRE */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    // SIN PARAMETROS
    
    public void sobreView() {
        try {
            SobreController sobreView = (SobreController) cambiarEscena("SobreView.fxml", 600, 200);
            sobreView.setMain(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* ESCENA PRODUCTO */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    // SIN PARAMETROS
    
    public void productoView() {
        try {
            ProductoController ProductoView = (ProductoController) cambiarEscena("ProductoView.fxml", 1121, 594);
            ProductoView.setMain(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* ESCENA PROVEEDOR */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    // SIN PARAMETROS
    
    public void proveedorView() {
        try {
            ProveedorController proveedorView = (ProveedorController) cambiarEscena("ProveedorView.fxml", 800, 830);
            proveedorView.setMain(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* ESCENA COMPRA */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    // SIN PARAMETROS
    
    public void compraView() {
        try {
            CompraController compraView = (CompraController) cambiarEscena("CompraView.fxml", 1121, 594);
            compraView.setMain(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void empleadoView() {
        try {
            EmpleadoController sobreView = (EmpleadoController) cambiarEscena("EmpleadoView.fxml", 1200, 600);
            sobreView.setMain(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* METODO MAIN  */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
    
    public static void main(String[] args) {
        launch(args);
    }
    
}

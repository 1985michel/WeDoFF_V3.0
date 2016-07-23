package com.michel1985.wedoffv3.view;

/**
 * Sample Skeleton for 'LoginOverview.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;

import com.michel1985.wedoffv3.MainApp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class LoginOverviewController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    public static String location = "view/LoginOverview.fxml";

    
    /**
     * Identificando elementos da Gui
     * */
    @FXML
    private ImageView wedoffLogoImageView;
    
    @FXML
    private Label wedoffTituloLabel;
    
    @FXML
    private Label versaoTituloLabel;
    
    @FXML
    private Label subtituloWeDoNotForgetLabel;
    
    @FXML
    private TextField loginTextField;
    
    @FXML
    private PasswordField senhaPasswordField;
    
    @FXML
    private Button logarButton;
    
    @FXML
    private Button cadastrarNovoUsuarioButton;
    
    //Referencia ao Main
    private MainApp mainApp;
    
    /**
	 * setMainApp - é usado pelo MainApp para para se referenciar
	 * */
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		
		//Em casos de tabela,aqui é o local para solitiar o povoamento
		//someTable.setItems(mainApp.getClienteData());
	}
    
    //Contrutor. É chamado antes do método Initialize
    public LoginOverviewController(){}
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }
    
    
}


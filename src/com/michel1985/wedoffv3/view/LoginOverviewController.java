package com.michel1985.wedoffv3.view;

import java.util.ResourceBundle;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.login.LoginMiddle;
import com.michel1985.wedoffv3.model.Usuario;

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
	 */
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

	// Referencia ao Main
	private MainApp mainApp;

	/**
	 * setMainApp - � usado pelo MainApp para para se referenciar
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Em casos de tabela,aqui � o local para solitiar o povoamento
		// someTable.setItems(mainApp.getClienteData());
	}

	// Contrutor. � chamado antes do m�todo Initialize
	public LoginOverviewController() {
	}

	@FXML // This method is called by the FXMLLoader when initialization is
			// complete
	void initialize() {

	}

	/**
	 * DECIS�O ARQUITETURAL
	 * 
	 * Neste ponto devo implementar os m�todos para interagir com a view e a
	 * L�gica de Login. H� duas possibilidades: deixar a l�gica de login nesta
	 * classe OU Criar uma nova classe para conter a L�gica de Login.
	 * 
	 * A principal estrutura de login j� � contida na classe login.Logadora
	 * por�m h� uma s�rie de peculiaridades e valida��es que precisam ser
	 * realizadas pela aplica��o espec�fica ( essa aplica��o ) antes de invocar
	 * os m�todoas da classe login.Logadora.
	 * 
	 * 
	 * Em um primeiro momento pensei que como ser� uma classe simples - com
	 * poucos m�todos - eu poderia deixa-la aqui no controller. Por�m, como
	 * planejo amadurecer essa aplica��o, tomarei a decis�o de criar uma classe
	 * para gerenciar o login dessa vers�o.
	 * 
	 * Assim crio a classe "login.LoginMiddle" para deixar a ideia de que � uma classe
	 * meio que liga esse controller � classe Logadora.
	 */
	
	
	/**
	 * Reconhecendo o usu�rio ativo
	 * */
	public static Usuario usuarioAtivo;
	
	//Delegando a tarefa de logar
	@FXML
	private void handleLogar(){
		LoginMiddle middle = new LoginMiddle(this);
		try {
			middle.logar(loginTextField.getText(), senhaPasswordField.getText());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//Informando ao mainApp que o login ocorreu e que a aplica��o deve ser liberada
	public void loginConfirmado(){
		this.mainApp.setUsuarioAtivo(usuarioAtivo);
		this.mainApp.carregaHistoricoDeClientes(); // Carrega o banco de dados para a aplica��o		
		this.mainApp.showAtendendoClienteOverview();
	}
	
	@FXML
	private void handleCadastrarNovoUsuario(){
		LoginMiddle middle = new LoginMiddle(this);
		middle.cadastrarUsuario(loginTextField.getText(), senhaPasswordField.getText());
	}
	

}

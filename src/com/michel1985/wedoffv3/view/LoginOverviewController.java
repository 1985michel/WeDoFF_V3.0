package com.michel1985.wedoffv3.view;

import java.util.ResourceBundle;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.login.LoginMiddle;
import com.michel1985.wedoffv3.model.Usuario;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
	
	//Componentes do wait
	@FXML 
	private AnchorPane waitAnchorPane;
	
	@FXML
	private Pane faixaBackgroundPane;
	
	@FXML
	private ImageView imagemImageView;
	
	@FXML
	private VBox fundoPrincipalVBox;

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
	public LoginOverviewController() {}

	@FXML // This method is called by the FXMLLoader when initialization is
			// complete
	void initialize(){}

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
		showWait();
		LoginMiddle middle = new LoginMiddle(this);
		try {
			middle.logar(loginTextField.getText().trim(), senhaPasswordField.getText());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//Informando ao mainApp que o login ocorreu e que a aplica��o deve ser liberada
	public void loginConfirmado(){
		waitSomeTime();
		this.mainApp.setUsuarioAtivo(usuarioAtivo);
		this.mainApp.carregaHistoricoDeClientes(); // Carrega o banco de dados para a aplica��o		
		
		
	}
	
	public void entrar(){
		
		this.mainApp.showAtendendoClienteOverview();
	}
	
	@FXML
	private void handleCadastrarNovoUsuario(){
		LoginMiddle middle = new LoginMiddle(this);
		middle.cadastrarUsuario(loginTextField.getText(), senhaPasswordField.getText());
	}
	
	//Mostra o gif do wait
	private void showWait(){
		faixaBackgroundPane.setVisible(true);
		imagemImageView.setVisible(true);
		faixaBackgroundPane.setStyle("-fx-background-color: #000000;");
		//Image img = new Image("file:resources/images/load04.gif");
		//imagemImageView.setImage(img);
		waitAnchorPane.toFront();
	}
	
	private void waitSomeTime(){
		showWait();
		Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
            	entrar();
            }
        });
        new Thread(sleeper).start();
	}
	
	public void hideWait(){
		waitAnchorPane.toBack();
		faixaBackgroundPane.setVisible(false);
		imagemImageView.setVisible(false);
		
		fundoPrincipalVBox.toFront();
		//imagemImageView.setImage(null);
		
	}

	

}

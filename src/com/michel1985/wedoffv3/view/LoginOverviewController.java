package com.michel1985.wedoffv3.view;

import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.login.LoginMiddle;
import com.michel1985.wedoffv3.model.Usuario;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	private TextField driverDbTextField;

	@FXML
	private PasswordField senhaPasswordField;

	@FXML
	private Button logarButton;

	@FXML
	private Button cadastrarNovoUsuarioButton;

	// Componentes do wait

	@FXML
	private Pane faixaBackgroundPane;

	@FXML
	private ImageView imagemImageView;

	@FXML
	private VBox fundoPrincipalVBox;

	@FXML
	private ProgressBar carregandoLoginProgressBar;

	static Task<?> copyWorker;

	// Referencia ao Main
	private MainApp mainApp;

	/**
	 * setMainApp - é usado pelo MainApp para para se referenciar
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		InputStream url = this.getClass().getResourceAsStream("/security-app-shield-icon.png");
		wedoffLogoImageView.setImage(new Image(url));

		// Em casos de tabela,aqui é o local para solitiar o povoamento
		// someTable.setItems(mainApp.getClienteData());
	}

	// Contrutor. É chamado antes do método Initialize
	public LoginOverviewController() {
	}

	@FXML // This method is called by the FXMLLoader when initialization is
			// complete
	void initialize() {
	}

	/**
	 * Reconhecendo o usuário ativo
	 */
	public static Usuario usuarioAtivo;

	// Delegando a tarefa de logar
	@FXML
	private void handleLogar() {

		System.out.println("feijao!!!!!!!!!!!!!!!");

		if (driverDbTextField.getText().trim() != null && driverDbTextField.getText().trim().length() > 0) {
			String local = driverDbTextField.getText().trim();
			String teste = "/";
			String invertendoBarras = local.replaceAll("\\\\", teste);
			System.out.println("tentando acesso a >>>> " + invertendoBarras);
			MainApp.diretorioDb = invertendoBarras;
		} else {
			System.out.println("veio no eulse da linha 124 do loginoverviewcontroller");
			MainApp.diretorioDb = "C:/Program Files/wedoffSecurity/hsqldb-2.3.3/hsqldb/db/";
		}

		LoginMiddle middle = new LoginMiddle(this);
		try {
			middle.logar(loginTextField.getText().trim(), senhaPasswordField.getText());

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@FXML
	private void handleCadastrarNovoUsuario() {
		LoginMiddle middle = new LoginMiddle(this);
		middle.cadastrarUsuario(loginTextField.getText(), senhaPasswordField.getText());
	}

	public void mostraGif() {
		// Faz o fundo ficar preto
		faixaBackgroundPane.setStyle("-fx-background-color: #000000;");

		InputStream url = this.getClass().getResourceAsStream("/load04.gif");
		imagemImageView.setImage(new Image(url));
		// imagemImageView.setImage(new Image("file:resources/images/load04.gif"));
		// imagemImageView.setImage(new Image(url));

		imagemImageView.toFront();
		imagemImageView.setVisible(true);
		fundoPrincipalVBox.setVisible(false);
	}

	/**
	 * Método responsável pelo carregamento da interface de abertura abrangendo 1 -
	 * A exibição da imagem de abertura; 2 - O carregamento de progresssBar confrome
	 * a quantidade de Clientes sendo carregados
	 */
	private void carregaInterfaceDeAbertura() {

		// Obtendo a quantidade de clientes e criando uma Task
		// int qtd = getQtdDeClientes();
		// copyWorker = createWorker(qtd < 100 ? 100 : qtd);
		copyWorker = createWorker(100);

		// Dizendo a ProgressBar que ela deve observar o percentual de execução
		// da thread e exibi-lo
		carregandoLoginProgressBar.progressProperty().unbind();
		carregandoLoginProgressBar.progressProperty().bind(copyWorker.progressProperty());

		// Passando a tarefa para a Thread e começando o processamento
		new Thread(copyWorker).start();

		// Quando o copyWorkerTerminar, carregue a tela principal
		copyWorker.setOnSucceeded((event) -> {
			this.mainApp.showAtendendoClienteOverview();
		});
	}

	/**
	 * Método que retorna uma Task
	 */
	public Task createWorker(int qtd) {
		carregandoLoginProgressBar.setProgress(0);

		return new Task() {
			@Override
			protected Object call() throws Exception {
				for (int i = 0; i < qtd; i++) {
					// pausando a thread
					seguraTempo(qtd);
					// atualizando o progresso da thread
					updateProgress(i + 1, qtd);
				}
				return true;
			}
		};

	}

	private void showAtendendo() {
		this.mainApp.showAtendendoClienteOverview();
	}

	public void seguraTempo(int qtd) throws InterruptedException {
		// A minha idéia é esperar 5 segundos
		Thread.sleep(60);
	}

	// Informando ao mainApp que o login ocorreu e que a aplicação deve ser
	// liberada
	public void loginConfirmado() {
		// mostraGif();
		this.mainApp.setUsuarioAtivo(usuarioAtivo);

		carregandoLoginProgressBar.progressProperty().unbind();
		waitSomeTime();

		// carregaInterfaceDeAbertura();
		// this.mainApp.carregaHistoricoDeClientes();
		// this.mainApp.carregaHistoricoDeAtendimentos();
		// this.mainApp.showAtendendoClienteOverview();
	}

	private void carregaListas() {
		this.mainApp.carregaHistoricoDeClientes();
		this.mainApp.carregaHistoricoDeAtendimentos();
		this.mainApp.carregaHistoricoDeNotasAvulstas();
	}

	private void waitSomeTime() {

		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					carregaListas();
					// Thread.sleep(2300);

				} catch (Exception e) {
				}
				return null;
			}
		};
		carregandoLoginProgressBar.progressProperty().bind(sleeper.progressProperty());
		sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				showAtendendo();
			}
		});
		new Thread(sleeper).start();
	}

	// Método que retorna a quantidade de clientes
	private int getQtdDeClientes() {
		int qtd = 0;
		ResultSet resultSet = null;
		try {
			resultSet = new CRUD(mainApp.getUsuarioAtivo()).getResultSet("SELECT idCliente FROM CLIENTES");
			while (resultSet.next()) {
				qtd++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return qtd;
	}
}

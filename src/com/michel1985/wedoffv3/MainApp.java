package com.michel1985.wedoffv3;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.model.Usuario;
import com.michel1985.wedoffv3.seguranca.Cripto;
import com.michel1985.wedoffv3.view.AtendendoClienteOverviewController;
import com.michel1985.wedoffv3.view.DialogJustImageOverviewController;
import com.michel1985.wedoffv3.view.EditarClienteOverviewController;
import com.michel1985.wedoffv3.view.HistoricoDeClientesOverviewController;
import com.michel1985.wedoffv3.view.LoginOverviewController;
import com.michel1985.wedoffv3.view.RootLayoutController;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private AtendendoClienteOverviewController atendendoClienteController;

	private ObservableList<Cliente> clienteData = FXCollections.observableArrayList();

	private Usuario usuarioAtivo;

	public MainApp() {

		// Cliente newCli = new Cliente("1","Jose Um","87487813983","um notas");
		// clienteData.add(newCli);

	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("WeDoFF V3.0");

		// Set the application icon.
		this.primaryStage.getIcons().add(new Image("file:resources/images/security-app-shield-icon.png"));

		initRootLayout();

		showLoginOverview();
		// showAtendendoClienteOverview();
	}

	/**
	 * Inicializa o rootLayout(base)
	 */
	public void initRootLayout() {
		try {
			// carrega root fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Give the controller access to the main app.
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);

			// Colocando o RootLayout em cena
			primaryStage.setScene(new Scene(rootLayout));
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Colocando o LoginOverview dentro do RootLayout
	 */
	public void showLoginOverview() {
		try {
			// Passo 1 - Carregando FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(LoginOverviewController.location));
			AnchorPane loginOverview = (AnchorPane) loader.load();

			// Colocando dentro do root
			rootLayout.setCenter(loginOverview);

			// Dando acesso para o controller acessar o main
			LoginOverviewController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Apresentando o DialogJustImageOverviewController
	 */
	public void showDialogJustImageOverviewController() {
		try {
			// Passo 1 - Carregando FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(DialogJustImageOverviewController.location));
			DialogPane page = (DialogPane) loader.load();

			// Criando o dialogStage
			Stage dialogStage = new Stage();
			dialogStage.initStyle(StageStyle.UNDECORATED);
			
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/edit.png"));
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Show
			
			dialogStage.showAndWait();

			// Dando acesso para o controller acessar o main
			LoginOverviewController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Colocando o AtendendoCLiente dentro do RootLayout
	 */
	public void showAtendendoClienteOverview() {
		try {
			// Passo 1 - Carregandoo FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/AtendendoClienteOverview.fxml"));
			AnchorPane atendendoClienteOverview = (AnchorPane) loader.load();

			// Colocando dentro do root
			rootLayout.setCenter(atendendoClienteOverview);

			// Dando acesso para o controller acessar a MainApp
			atendendoClienteController = loader.getController();
			atendendoClienteController.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Recupera o controle do atendendo cliente para ser invocado por interações
	// solicitadas pelas telas secundárias
	public AtendendoClienteOverviewController getAtendendoClienteController() {
		return this.atendendoClienteController;
	}

	public void showAboutDialog() {
		try {
			// Load o FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/AboutDialog.fxml"));
			DialogPane page = (DialogPane) loader.load();

			// Criando o dialogStage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Sobre o WeDoFF");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(false);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/edit.png"));
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Show
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retorna o palco principal
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Obterndo as ObservableList
	 */
	public ObservableList<Cliente> getClienteData() {
		return this.clienteData;
	}

	/**
	 * Setando o usuário ativo
	 */
	public void setUsuarioAtivo(Usuario user) {
		this.usuarioAtivo = user;

	}

	/**
	 * O método abaixo carrega todos os clientes para a ObservableList
	 * dataCliente
	 * 
	 * O método é chamado pelo loginController quando o login é efetuado
	 */
	public void carregaHistoricoDeClientes() {
		if (this.usuarioAtivo != null) {
			ResultSet resultSet = null;

			try {
				resultSet = new CRUD(this.usuarioAtivo).getResultSet("SELECT * FROM clientes ORDER BY IDCLIENTE DESC");
				adicionaTodosClientesFromDBNaDataClientes(resultSet);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					resultSet.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void adicionaTodosClientesFromDBNaDataClientes(ResultSet resultSet) throws SQLException {
		ArrayList<Cliente> clientes = new ArrayList<>();
		while (resultSet.next()) {

			clientes.add(
					new Cliente(resultSet.getString("idcliente"), descriptografa(resultSet.getString("nomeCliente")),
							descriptografa(resultSet.getString("cpfCliente")),
							descriptografa(resultSet.getString("notassobrecliente"))));
		}

		clienteData.addAll(FXCollections.observableArrayList(clientes));
	}

	private String descriptografa(String texto) {
		Cripto cripto = new Cripto();
		return cripto.descriptografa(texto, this.usuarioAtivo.getSenha());
	}

	public Usuario getUsuarioAtivo() {
		return this.usuarioAtivo;
	}

	/**
	 * Mostra o HistoricoDeClienteOverview
	 */
	public void showHistoricoDeClientesOverview() {
		try {

			// Load o FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/HistoricoDeClientesOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Dá ao controlador acesso ao MainApp
			HistoricoDeClientesOverviewController controller = loader.getController();
			controller.setMainApp(this);

			/**
			 * Reordenando a clienteData Utilizando lambda - Comparablea
			 */
			clienteData.sort((o1, o2) -> Integer.parseInt(o2.getIdCliente()) - Integer.parseInt(o1.getIdCliente()));

			// Criando o dialogStage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Histórico de Clientes");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(true);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/edit.png"));
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Dando ao controlador poderes sobre seu próprio dialogStage
			controller.setDialogStage(dialogStage);

			// Show
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Apresenta o dialog para edição do cliente
	 */
	public boolean showEditarClienteOverview(Cliente cliente) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/EditarClienteOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Cria o palco
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editar Cliente");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Passa o cliente a ser editado ao controller
			EditarClienteOverviewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCliente(cliente);

			// Apresenta o dialog
			dialogStage.showAndWait();

			return controller.isOkCLicked();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}

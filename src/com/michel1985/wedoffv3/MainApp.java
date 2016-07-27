package com.michel1985.wedoffv3;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.model.Usuario;
import com.michel1985.wedoffv3.view.AtendendoClienteOverviewController;
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

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	private ObservableList<Cliente> clienteData = FXCollections.observableArrayList();

	private Usuario usuarioAtivo;

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
			AtendendoClienteOverviewController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
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
	 * Mostra o HistoricoDeClienteOverview 
	 * */
	public void showHistoricoDeClientesOverview() {
		try {
			// Load o FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/HistoricoDeClientesOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

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

			// Show
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}

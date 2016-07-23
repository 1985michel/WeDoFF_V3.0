package com.michel1985.wedoffv3;

import java.io.IOException;

import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.view.AtendendoClienteOverviewController;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private ObservableList<Cliente> clienteData = FXCollections.observableArrayList();

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("WeDoFF V3.0");
		
		initRootLayout();
		
		showAtendendoClienteOverview();
	}
	
	/**
	 * Inicializa o rootLayout(base)
	 * */
	public void initRootLayout(){
		try{
			//carrega root fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			//Colocando o RootLayout em cena
			primaryStage.setScene( new Scene(rootLayout));
			primaryStage.show();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Colocando o AtendendoCLiente dentro do RootLayout
	 * */
	public void showAtendendoClienteOverview(){
		try{
			//Passo 1 - Carregandoo FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/AtendendoClienteOverview.fxml"));
			AnchorPane atendendoClienteOverview = (AnchorPane) loader.load();
			
			//Colocando dentro do root
			rootLayout.setCenter(atendendoClienteOverview);
			
			//Dando acesso para o controller acessar a MainApp
			AtendendoClienteOverviewController controller = loader.getController();
			controller.setMainApp(this);
		
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Retorna o palco principal
	 * */
	public Stage getPrimaryStage(){
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Obterndo as ObservableList
	 * */
	public ObservableList<Cliente> getClienteData(){
		return this.clienteData;
	}
	
	
}

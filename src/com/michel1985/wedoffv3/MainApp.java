package com.michel1985.wedoffv3;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;

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
}

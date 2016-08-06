package com.michel1985.wedoffv3.view;


import com.michel1985.wedoffv3.MainApp;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class DialogJustImageOverviewController {

    @FXML
    private DialogPane baseDialogPane;

    @FXML
    private ImageView imagemImageView;
    
    @FXML // URL location of the FXML file that was given to the FXMLLoader
	public static String location = "view/DialogJustImageOverview.fxml";
    
	// Referencia ao Main
	private MainApp mainApp;

	/**
	 * setMainApp - é usado pelo MainApp para para se referenciar
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Em casos de tabela,aqui é o local para solitiar o povoamento
		// someTable.setItems(mainApp.getClienteData());
	}

	// Contrutor. É chamado antes do método Initialize
	public DialogJustImageOverviewController(){}

	@FXML // This method is called by the FXMLLoader when initialization is
			// complete
	void initialize(){
		
		
		
	}

}

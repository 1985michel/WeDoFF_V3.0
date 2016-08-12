package com.michel1985.wedoffv3.view;

import com.michel1985.wedoffv3.MainApp;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;

public class RootLayoutController {

	private MainApp mainApp;

	/**
	 * setMainApp - é usado pelo MainApp para para se referenciar
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Em casos de tabela,aqui é o local para solitiar o povoamento
		//someTable.setItems(mainApp.getClienteData());
	}

	// Contrutor. É chamado antes do método Initialize
	public RootLayoutController() {
		// TODO Auto-generated constructor stub
	}

	@FXML // This method is called by the FXMLLoader when initialization is
			// complete
	void initialize() {

	}

	@FXML
	private Menu configuracoesMenu;

	/**
	 * Abre uma Janela com informações sobre o Sistema
	 */
	@FXML
	private void handleAbout() {
		mainApp.showAboutDialog();
	}

	/**
	 * Fecha a aplicação
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
	}
	
	/**
	 * Exibe o dialog com o histórico de clientes
	 * */
	@FXML
	private void handleHistoricoDeClientes(){
		mainApp.showHistoricoDeClientesOverview();
	}
	
	/**
	 * Exibe o dialog com o histórico de atendimentos
	 * */
	@FXML
	private void handleHistoricoDeAtendimentos(){
		mainApp.showHistoricoDeAtendimentosOverview();
	}

}

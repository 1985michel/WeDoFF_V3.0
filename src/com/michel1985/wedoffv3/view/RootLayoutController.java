package com.michel1985.wedoffv3.view;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.model.NotaAvulsa;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;

public class RootLayoutController {

	private MainApp mainApp;

	/**
	 * setMainApp - � usado pelo MainApp para para se referenciar
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Em casos de tabela,aqui � o local para solitiar o povoamento
		//someTable.setItems(mainApp.getClienteData());
	}

	// Contrutor. � chamado antes do m�todo Initialize
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
	 * Abre uma Janela com informa��es sobre o Sistema
	 */
	@FXML
	private void handleAbout() {
		mainApp.showAboutDialog();
	}

	/**
	 * Fecha a aplica��o
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
	}
	
	/**
	 * Exibe o dialog com o hist�rico de clientes
	 * */
	@FXML
	private void handleHistoricoDeClientes(){
		mainApp.showHistoricoDeClientesOverview();
	}
	
	/**
	 * Exibe o dialog com o hist�rico de atendimentos
	 * */
	@FXML
	private void handleHistoricoDeAtendimentos(){
		mainApp.showHistoricoDeAtendimentosOverview();
	}
	
	/**
	 * Exibe o dialog com o hist�rico de atendimentos
	 * */
	@FXML
	private void handleConsultasIntegradas(){
		System.out.println("chamou aqui");		
	}
	
    @FXML
    void handleGravarNovaNota() {
    	System.out.println("Chamando Gravar Nova Nota");
    }
    
    @FXML
    void handleConsultarNotas() {
    	System.out.println("Chamando Consultar Notas");
    	mainApp.showHistoricoDeNotasAvulsasOverview();

    }
    
    @FXML
    void handlePesquisaIntegrada() {
    	mainApp.showPesquisaIntegradaOverview();
    }
    
    @FXML
    void handleAtendimentosPendentes(){
    	mainApp.showHistoricoDeAtendimentosPendentesOverview();
    }
    
       
    

}

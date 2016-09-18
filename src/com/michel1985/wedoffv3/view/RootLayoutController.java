package com.michel1985.wedoffv3.view;

import com.michel1985.wedoffv3.MainApp;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;

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
	
	@FXML
	private RadioMenuItem optionModernaLight;

	@FXML
	private RadioMenuItem optionModernaDark;
	
	@FXML
	private RadioMenuItem optionModernaDarkPink;
	
	@FXML
	private RadioMenuItem optionModernaPink;
	
	@FXML private void handleSelectCSS(){
		if(optionModernaLight.isSelected()){
			optionModernaDark.setSelected(false);
			optionModernaPink.setSelected(false);
			optionModernaDarkPink.setSelected(false);
			MainApp.selectedCss = "modenaLight";
		}			
		else if(optionModernaDark.isSelected()){
			optionModernaLight.setSelected(false);
			optionModernaPink.setSelected(false);
			optionModernaDarkPink.setSelected(false);
			MainApp.selectedCss = "modenaDark";
		}
		else if(optionModernaPink.isSelected()){
			optionModernaLight.setSelected(false);
			optionModernaDark.setSelected(false);
			optionModernaDarkPink.setSelected(false);
			MainApp.selectedCss = "modenaPink";
		}
		else if(optionModernaDarkPink.isSelected()){
			optionModernaLight.setSelected(false);
			optionModernaDark.setSelected(false);
			optionModernaPink.setSelected(false);
			MainApp.selectedCss = "modenaDarkPink";
		}
		
		this.mainApp.setRootPersonalStyle();
	}

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
	
	/**
	 * Exibe o dialog com o histórico de atendimentos
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
    
    @FXML
    void handleShowStatistics(){
    	mainApp.showAtendimentoDiarioStatistics();
    }
    
    @FXML
    void handleShowStatisticsMensal(){
    	mainApp.showAtendimentoDiarioStatisticsMensal();
    }
       
    

}

/**
 * 
 */
package com.michel1985.wedoffv3.view;

import com.michel1985.wedoffv3.MainApp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * @author michel
 * 
 */
public class AtendendoClienteOverviewController {
	
	/**
	 * Identificando os elementos da GUI
	 * 
	 * 1 - Lado esquerdo >>> CLIENTE
	 * */
	@FXML
	private Label tituloClienteLabel;
	
	@FXML
	private TextField cpfTextField;
	
	@FXML
	private Button consultarClientePeloCpfButton;
	
	@FXML
	private TextField nomeClienteTextField;
	
	@FXML
	private Button consultarClientePeloNomeButton;
	
	@FXML
	private TextArea notasClienteTextArea;
	
	@FXML
	private Button receberSatButton;
	
	@FXML
	private Button verHistoricoDeAtendimentosDoClienteButton;
	
	
	/**
	 * Elementos do lado direito >>>> ATENDIMENTO
	 * */
	@FXML
	private Label tituloAtendimentoLabel;
	
	@FXML
	private TextField nbTextField;
	
	@FXML
	private Button consultarAtendimentoPeloNbButton;
	
	@FXML
	private CheckBox isAgendamentoCheckBox;
	
	@FXML
	private DatePicker dataDoAtendimentoDatePicker;
	
	@FXML
	private TextArea notasSobreAtendimentoTextArea;
	
	@FXML
	private CheckBox isPendenteCheckBox;
	
	@FXML
	private DatePicker dataParaSolucionarPendenciaDatePicker;
	
	@FXML
	private Button registrarAtendimentoButton;
	
	@FXML
	private Button cancelarAtendimentoButton;
	
	//Referência ao Main
	private MainApp mainApp;
	
	//Crontrutor. É chamado antes do método initialize
	public AtendendoClienteOverviewController(){}
	
	/**
	 * Initialize - é chamado ao carregar o fxml
	 * */
	@FXML
	private void initialize(){
		
		//Utilizado para inicializar tabelas e tals
		//Lambda: "em cada célula dessa coluna, coloquei o valor do firstNameProperty" 
		// firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
	}

	/**
	 * setMainApp - é usado pelo MainApp para para se referenciar
	 * */
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		
		//Em casos de tabela,aqui é o local para solitiar o povoamento
		//someTable.setItems(mainApp.getClienteData());
	}
}

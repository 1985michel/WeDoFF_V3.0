package com.michel1985.wedoffv3.view;


import java.util.Optional;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.model.Atendimento;
import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.model.ConsultaIntegradaObject;
import com.michel1985.wedoffv3.util.EstruturaData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ConsultasIntegradasOverviewController {
	
	public ConsultasIntegradasOverviewController(){}

    @FXML
    private Label buscaAvancadaTituloLabel;

    @FXML
    private TableView<ConsultaIntegradaObject> resultadoTableView;

    @FXML
    private TableColumn<ConsultaIntegradaObject, String> idAtendimentoTableColumn;

    @FXML
    private TableColumn<ConsultaIntegradaObject, String> idClienteTableColumn;

    @FXML
    private TableColumn<ConsultaIntegradaObject, String> nomeClienteTableColumn;

    @FXML
    private TableColumn<ConsultaIntegradaObject, String> nbTableColumn;

    @FXML
    private TableColumn<ConsultaIntegradaObject, String> dataAtendimentoTableColumn;

    @FXML
    private TableColumn<ConsultaIntegradaObject, Boolean> isAgendamentoTableColumn;

    @FXML
    private TableColumn<ConsultaIntegradaObject, Boolean> isPendenteTableColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;

    @FXML
    private Label notasSobreAtendimentoTituloLabel;

    @FXML
    private Label notasSobreAtendimentoTextArea;

    @FXML
    private HBox acoesSobreAtendimentoHBox;

    @FXML
    private Button verClienteAtendidoButton;

    @FXML
    private MenuButton acoesMenuButton;

    @FXML
    private MenuItem editarAtendimentoMenuItem;

    @FXML
    private MenuItem excluirAtendimentoMenuItem;
    
	// Observable list que conterá o resultado das pesquisas
	public ObservableList<ConsultaIntegradaObject> result = FXCollections.observableArrayList();

	// Palco desse dialog
	private Stage dialogStage;

	MainApp mainApp;
    
    /**
	 * Inicializa a classe controller. Método chamado ao carregar o fxml
	 */
	@FXML
	private void initialize() {

		idAtendimentoTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAtd().idAtendimentoProperty());
		idClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAtd().idClienteProperty());
		nomeClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().getCliente().nomeProperty());
		nbTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAtd().nbProperty());
		// Informando o foramto de datas que quero que seja apresentado na
		// tabela
		dataAtendimentoTableColumn.setCellValueFactory(
				cellData -> EstruturaData.estruturaData(cellData.getValue().getAtd().dataAtendimentoProperty()));
		isAgendamentoTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAtd().isAgendamentoProperty());
		isPendenteTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAtd().isPendenteProperty());

		// limpa os detalhes do atendimento
		showConsultaIntegradaDetails(null);

		// Detecta mudanças de seleção e mostra os detalhes do Atendimento
		// quando
		// algum é selecionado
		resultadoTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showConsultaIntegradaDetails(newValue));

		// Detecta mudanças de seleção e habilita e desabilita as ações do HBox
		resultadoTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> permitirAcoes(newValue));

		// Detecta mudanças no campo de busca e se ele ficar vazio, apresenta
				// todo os histórico
				searchTextField.setOnKeyPressed((event) -> {
					if (searchTextField.getText().length() == 0)
						resultadoTableView.setItems(null);
				});

		// Detecta o duplo click do mouse e apresenta o alert perguntando se
		// quer atender aquele cliente.
		// Caso ok, o cliente é carregado no formulário
				resultadoTableView.setOnMousePressed((event) -> {
			if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Necessária confirmação");
				alert.setHeaderText("Você deseja continuar com esse atendimento?");
				alert.setContentText(
						"Ao clicar em \"Ok\" os dados desse atendimento serão carregados na tela principal sobrepondo os dados atuais.");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					// Obtem o id do cliente selecionado
					String idCli = resultadoTableView.getSelectionModel().getSelectedItem().getAtd().getIdCliente();
					String idAte = resultadoTableView.getSelectionModel().getSelectedItem().getAtd().getIdAtendimento();
					// Passa o id para o controller do AtendendoCliente
					this.mainApp.getAtendendoClienteController().ConsultarClientePeloId(idCli);
					this.mainApp.getAtendendoClienteController().ConsultarAtendimentoPeloId(idAte);
					// fecha o dialog do histórico
					this.dialogStage.close();
				}
			}
		});

	}

	/**
	 * Ligando ao main
	 */
	public void setMainApp(MainApp main) {
		this.mainApp = main;

		// Adiciona os dados da observable list à tabela
		//resultadoTableView.setItems(list);

	}
	
	/**
	 * Passando uma nova observableList para trabalho
	 */
	public void setObservableList(ObservableList<ConsultaIntegradaObject> lista) {
		this.result = lista;
		resultadoTableView.setItems(this.result);
	}
	
	/**
	 * Define o palco deste dialog. Usado para fecha-lo, por exemplo
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void handleShowHistoricoDeClientes() {
		mainApp.showHistoricoDeClientesOverview();
	}
	
	private void showConsultaIntegradaDetails(ConsultaIntegradaObject consulta) {

		if (consulta != null) {
			notasSobreAtendimentoTextArea.setText(consulta.getAtd().getNotasSobreAtendimento());
		} else {
			notasSobreAtendimentoTextArea.setText("");
		}
	}
	
	/**
	 * Método que habilitará e desabilitará as ações sobre o cliente Se houver
	 * ou não um cliente selecionado na tabela
	 */
	private void permitirAcoes(ConsultaIntegradaObject consultaObj) {

		if (consultaObj != null)
			acoesSobreAtendimentoHBox.setDisable(false);
		else
			acoesSobreAtendimentoHBox.setDisable(true);
	}
	
	@FXML
	private void handleConsultarConsultaIntegrada(){
		
		ConsultaIntegradaSearch search = new ConsultaIntegradaSearch(this);
		
		result.clear();
		String termoBase = searchTextField.getText();
		if (!termoBase.contains("+"))
			search.consultarConsultaIntegradaBuscaSimples(termoBase);
		else
			search.consultarConsultaIntegradaBuscaAvancada(termoBase);
		resultadoTableView.setItems(result);
	}
	
	/*
    @FXML
    void handleAtualizaAtendimento(ActionEvent event) {

    }

    @FXML
    void handleConsultarAtendimento(ActionEvent event) {

    }

    @FXML
    void handleDeleteAtendimento(ActionEvent event) {

    }
	*/
}


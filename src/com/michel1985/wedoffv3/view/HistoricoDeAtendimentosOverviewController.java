package com.michel1985.wedoffv3.view;

import java.util.Optional;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.model.Atendimento;
import com.michel1985.wedoffv3.model.Cliente;

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
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class HistoricoDeAtendimentosOverviewController {

	@FXML
	private Label historicoDeAtendimentosTituloLabel;

	@FXML
	private TableView<Atendimento> atendimentosTableView;

	@FXML
	private TableColumn<Atendimento, String> idAtendimentoTableColumn;

	@FXML
	private TableColumn<Atendimento, String> idClienteTableColumn;

	@FXML
	private TableColumn<Atendimento, String> nbTableColumn;

	@FXML
	private TableColumn<Atendimento, String> dataAtendimentoTableColumn;

	@FXML
	private TableColumn<Atendimento, Boolean> isAgendamentoTableColumn;

	@FXML
	private TableColumn<Atendimento, Boolean> isPendenteTableColumn;

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

	/**
	 * Buscas
	 */

	// Observable list que conter� o resultado das pesquisas
	public ObservableList<Atendimento> result = FXCollections.observableArrayList();

	// Palco desse dialog
	private Stage dialogStage;

	private MainApp mainApp;

	public HistoricoDeAtendimentosOverviewController() {
	}

	/**
	 * Inicializa a classe controller. M�todo chamado ao carregar o fxml
	 */
	@FXML
	private void initialize() {
		
		idAtendimentoTableColumn.setCellValueFactory(cellData -> cellData.getValue().idAtendimentoProperty());
		idClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().idClienteProperty());
		nbTableColumn.setCellValueFactory(cellData -> cellData.getValue().nbProperty());
		dataAtendimentoTableColumn.setCellValueFactory(cellData -> cellData.getValue().dataAtendimentoProperty());
		isAgendamentoTableColumn.setCellValueFactory(cellData -> cellData.getValue().isAgendamentoProperty());
		isPendenteTableColumn.setCellValueFactory(cellData -> cellData.getValue().isPendenteProperty());

		// limpa os detalhes do atendimento
		showAtendimentoDetails(null);

		// Detecta mudan�as de sele��o e mostra os detalhes do Atendimento quando
		// algum � selecionado
		atendimentosTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showAtendimentoDetails(newValue));

		// Detecta mudan�as de sele��o e habilita e desabilita as a��es do HBox
		atendimentosTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> permitirAcoes(newValue));

		// Detecta mudan�as no campo de busca e se ele ficar vazio, apresenta
		// todo os hist�rico
		/*searchTextField.setOnKeyPressed((event) -> {
			if (searchTextField.getText().length() == 0)
				clientesTableView.setItems(mainApp.getClienteData());
		});*/

		// Detecta o duplo click do mouse e apresenta o alert perguntando se
		// quer atender aquele cliente.
		// Caso ok, o cliente � carregado no formul�rio
		atendimentosTableView.setOnMousePressed((event) -> {
			if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Necess�ria confirma��o");
				alert.setHeaderText("Voc� deseja continuar com esse atendimento?");
				alert.setContentText(
						"Ao clicar em \"Ok\" os dados desse atendimento ser�o carregados na tela principal sobrepondo os dados atuais.");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					// Obtem o id do cliente selecionado
					String id = atendimentosTableView.getSelectionModel().getSelectedItem().getIdCliente();
					// Passa o id para o controller do AtendendoCliente
					this.mainApp.getAtendendoClienteController().ConsultarClientePeloId(id);
					// fecha o dialog do hist�rico
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

		// Adiciona os dados da observable list � tabela
		atendimentosTableView.setItems(main.getAtendiemntoData());

	}
	
	 /**
     * Define o palco deste dialog.
     * Usado para fecha-lo, por exemplo
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

	public void handleShowHistoricoDeClientes() {
		mainApp.showHistoricoDeClientesOverview();
	}
	
	private void showAtendimentoDetails(Atendimento atendimento) {

		if (atendimento != null) {
			notasSobreAtendimentoTextArea.setText(atendimento.getNotasSobreAtendimento());
		} else {
			notasSobreAtendimentoTextArea.setText("");
		}
	}
	
	/**
	 * M�todo que habilitar� e desabilitar� as a��es sobre o cliente Se houver
	 * ou n�o um cliente selecionado na tabela
	 */
	private void permitirAcoes(Atendimento atendimento) {
		// TODO Auto-generated method stub
		if (atendimento != null)
			acoesSobreAtendimentoHBox.setDisable(false);
		else
			acoesSobreAtendimentoHBox.setDisable(true);
	}

}

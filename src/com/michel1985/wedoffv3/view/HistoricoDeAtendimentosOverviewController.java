package com.michel1985.wedoffv3.view;

import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.model.Atendimento;
import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.seguranca.Cripto;
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
		dataAtendimentoTableColumn.setCellValueFactory(cellData -> EstruturaData.estruturaData(cellData.getValue().dataAtendimentoProperty()));
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
		atendimentosTableView.setItems(main.getAtendimentoData());

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
	
	/**
	 * Dele��o de clientes
	 */
	@FXML
	private void handleDeleteAtendimento() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Deletar Atendimento?");
		alert.setHeaderText("Deseja apagar este atendimento?");
		alert.setContentText(
				"Ao clicar em \"Ok\" voc� estar� APAGANDO TODOS OS DADOS DESSE ATENDIMENTO SEM POSSIBILIDADE DE RECUPERA-LOS.\n"
						+ "\nVoc� tem certeza desta dele��o?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			deletarAtendimentoDoBancoDeDados();

			// Removendo o cliente da observableList
			deletarAtendimentoDaAtendimentoData();
		}
	}

	private void deletarAtendimentoDaAtendimentoData() {
		try {
			mainApp.getAtendimentoData().remove(atendimentosTableView.getSelectionModel().getSelectedItem());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deletarAtendimentoDoBancoDeDados() {
		
		String selectedId = atendimentosTableView.getSelectionModel().getSelectedItem().getIdAtendimento();
		
		ResultSet resultSet = null;
		try {
			CRUD crud = new CRUD(mainApp.getUsuarioAtivo());
			resultSet = crud.getResultSet("DELETE FROM ATENDIMENTOS WHERE idATENDIMENTO = '" + selectedId + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Atualizando {@link Atendimento} Chamado quando o usu�rio clica em "editar cliente"
	 */
	@FXML
	private void handleAtualizaAtendimento() {
		Atendimento selectedAtendimento = atendimentosTableView.getSelectionModel().getSelectedItem();

		if (selectedAtendimento != null) {

			boolean okClicked = mainApp.showEditarAtendimentoOverview(selectedAtendimento);
			if (okClicked) {
				showAtendimentoDetails(selectedAtendimento);
				atualizaNoBanco(selectedAtendimento);
			}
		}
	}

	private void atualizaNoBanco(Atendimento atendimento) {

		ResultSet resultSet = null;
		try {
			CRUD crud = new CRUD(mainApp.getUsuarioAtivo());

			resultSet = crud.getResultSet("UPDATE atendimentos SET nb= '" + criptografa(atendimento.getNb())
			+ "', notassobreatendimento= '" + criptografa(atendimento.getNotasSobreAtendimento()) + "', datasolucao= '" + atendimento.getDataSolucao()
			+ "', dataatendimento= '" + atendimento.getDataAtendimento() + "', ispendente= '" + atendimento.getIsPendente()+ "', isagendamento= '"
			+ atendimento.getIsAgendamento() + "' WHERE idatendimento='" + atendimento.getIdAtendimento() + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Nota: Como trata-se de uma observableList a atualiza��o j� foi feita
		// na lista
	}

	public String criptografa(String texto) {
		Cripto cripto = new Cripto();
		return cripto.criptografa(texto, mainApp.getUsuarioAtivo().getSenha());
	}


}

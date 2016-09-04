package com.michel1985.wedoffv3.view;

import java.sql.ResultSet;
import java.util.Optional;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.model.Atendimento;
import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.model.PesquisaIntegradaObject;
import com.michel1985.wedoffv3.seguranca.Cripto;
import com.michel1985.wedoffv3.util.EstruturaData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PesquisaIntegradaOverviewController {

	@FXML
	private Label buscaAvancadaTituloLabel;

	@FXML
	private TableView<PesquisaIntegradaObject> resultadoTableView;

	@FXML
	private TableColumn<PesquisaIntegradaObject, String> idAtendimentoTableColumn;

	@FXML
	private TableColumn<PesquisaIntegradaObject, String> idClienteTableColumn;

	@FXML
	private TableColumn<PesquisaIntegradaObject, String> nomeClienteTableColumn;

	@FXML
	private TableColumn<PesquisaIntegradaObject, String> nbTableColumn;

	@FXML
	private TableColumn<PesquisaIntegradaObject, String> dataAtendimentoTableColumn;

	@FXML
	private TableColumn<PesquisaIntegradaObject, Boolean> isAgendamentoTableColumn;

	@FXML
	private TableColumn<PesquisaIntegradaObject, Boolean> isPendenteTableColumn;

	@FXML
	private TextField searchTextField;

	@FXML
	private Button searchButton;

	@FXML
	private Label notasSobreAtendimentoTituloLabel;

	@FXML
	private TextArea notasSobreClienteTextArea;

	@FXML
	private TextArea notasSobreAtendimentoTextArea;

	@FXML
	private HBox acoesSobreAtendimentoHBox;

	@FXML
	private Button verClienteAtendidoButton;

	@FXML
	private Button verAtendimento;

	MainApp mainApp;

	// Observable list que conterá o resultado das pesquisas
	public ObservableList<PesquisaIntegradaObject> resultFull = FXCollections.observableArrayList();
	public ObservableList<PesquisaIntegradaObject> result = FXCollections.observableArrayList();

	// Palco desse dialog
	private Stage dialogStage;

	/**
	 * Inicializa a classe controller. Método chamado ao carregar o fxml
	 */
	@FXML
	private void initialize() {

		idAtendimentoTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAtd().idAtendimentoProperty());
		idClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAtd().idClienteProperty());
		nbTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAtd().nbProperty());
		nomeClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().getCliente().nomeProperty());
		// Informando o foramto de datas que quero que seja apresentado na
		// tabela
		dataAtendimentoTableColumn.setCellValueFactory(
				cellData -> EstruturaData.estruturaData(cellData.getValue().getAtd().dataAtendimentoProperty()));
		isAgendamentoTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAtd().isAgendamentoProperty());
		isPendenteTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAtd().isPendenteProperty());

		// Detecta mudanças no campo de busca e se ele ficar vazio, apresenta
		// todo os histórico
		searchTextField.setOnKeyPressed((event) -> {
			if (searchTextField.getText().length() == 0) {
				resultadoTableView.setItems(this.resultFull);
				showPIODetails(null);
			}

		});

		// Detecta mudanças de seleção e habilita e desabilita as ações do HBox
		resultadoTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> permitirAcoes(newValue));

		// Detecta mudanças de seleção e mostra os detalhes do cliente quando
		// algum é selecionado
		resultadoTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPIODetails(newValue));

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
					String idCli = resultadoTableView.getSelectionModel().getSelectedItem().getCliente().getIdCliente();
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

	private void showPIODetails(PesquisaIntegradaObject pio) {
		if (pio != null) {
			if (pio.getCliente() != null) {
				notasSobreClienteTextArea.setText(pio.getCliente().getNotasSobreCLiente());
				notasSobreAtendimentoTextArea.setText(pio.getAtd().getNotasSobreAtendimento());
			}
		} else {
			notasSobreClienteTextArea.setText("");
			notasSobreAtendimentoTextArea.setText("");
		}
	}

	public void buildPioListComTodosOsAtendimentosEClientes() {
		for (Atendimento atd : mainApp.getAtendimentoData()) {
			this.resultFull.add(new PesquisaIntegradaObject(
					this.getClientePeloId(this.mainApp.getClienteData(), atd.getIdCliente()), atd));
		}
	}

	@FXML
	void handleConsultarConsultaIntegrada() {
		String termo = this.searchTextField.getText();
		System.out.println("Estamos tentando consultar o termo " + termo);
		PesquisaIntegradaSearch piSearch = new PesquisaIntegradaSearch(this.mainApp, this);
		resultadoTableView.setItems(piSearch.buscaAvancada(termo));
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		buildPioListComTodosOsAtendimentosEClientes();
		resultadoTableView.setItems(resultFull);
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public Cliente getClientePeloId(ObservableList<Cliente> list, String id) {

		for (Cliente cliente : list) {
			if (cliente.getIdCliente().equalsIgnoreCase(id)) {
				return cliente;
			}

		}

		return null;
	}

	// Trabalhando sobre a possibilidade de editar os atendimentos e clientes
	// resultantes da pesquisa

	/**
	 * Atualizando {@link Atendimento} Chamado quando o usuário clica em "editar
	 * cliente"
	 */
	@FXML
	public void handleAtualizaAtendimento() {
		System.out.println("Estamos tentando abrir a nova janela de edição");
		PesquisaIntegradaObject pio = resultadoTableView.getSelectionModel().getSelectedItem();
		Atendimento selectedAtendimento = pio.getAtd();

		if (selectedAtendimento != null) {

			boolean okClicked = mainApp.showEditarAtendimentoOverview(selectedAtendimento);
			if (okClicked) {
				showPIODetails(pio);
				atualizaAtendimentoNoBanco(selectedAtendimento);
			}
		}
	}

	@FXML
	private void handleAtualizaCliente() {
		PesquisaIntegradaObject pio = resultadoTableView.getSelectionModel().getSelectedItem();
		Cliente selectedCliente = pio.getCliente();

		if (selectedCliente != null) {

			boolean okClicked = mainApp.showEditarClienteOverview(selectedCliente);
			if (okClicked) {
				showPIODetails(pio);
				atualizaClienteNoBanco(selectedCliente);
			}
		}
	}
	
	private void atualizaClienteNoBanco(Cliente cliente) {

		ResultSet resultSet = null;
		try {
			CRUD crud = new CRUD(mainApp.getUsuarioAtivo());

			resultSet = crud.getResultSet("UPDATE clientes SET cpfcliente= '" + criptografa(cliente.getCpf())
					+ "', nomecliente= '" + criptografa(cliente.getNome()) + "', notassobrecliente= '"
					+ criptografa(cliente.getNotasSobreCLiente()) + "' WHERE idcliente='" + cliente.getIdCliente()
					+ "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Nota: Como trata-se de uma observableList a atualização já foi feita
		// na lista
	}

	private void atualizaAtendimentoNoBanco(Atendimento atendimento) {

		ResultSet resultSet = null;
		try {
			CRUD crud = new CRUD(mainApp.getUsuarioAtivo());

			resultSet = crud.getResultSet("UPDATE atendimentos SET nb= '" + criptografa(atendimento.getNb())
					+ "', notassobreatendimento= '" + criptografa(atendimento.getNotasSobreAtendimento())
					+ "', datasolucao= '" + atendimento.getDataSolucao() + "', dataatendimento= '"
					+ atendimento.getDataAtendimento() + "', ispendente= '" + atendimento.getIsPendente()
					+ "', isagendamento= '" + atendimento.getIsAgendamento() + "' WHERE idatendimento='"
					+ atendimento.getIdAtendimento() + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Nota: Como trata-se de uma observableList a atualização já foi feita
		// na lista
	}

	public String criptografa(String texto) {
		Cripto cripto = new Cripto();
		return cripto.criptografa(texto, mainApp.getUsuarioAtivo().getSenha());
	}

	/**
	 * Método que habilitará e desabilitará as ações sobre o cliente Se houver
	 * ou não um cliente selecionado na tabela
	 */
	private void permitirAcoes(PesquisaIntegradaObject pio) {

		if (pio != null)
			acoesSobreAtendimentoHBox.setDisable(false);
		else
			acoesSobreAtendimentoHBox.setDisable(true);
	}
}

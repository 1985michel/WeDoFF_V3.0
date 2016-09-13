package com.michel1985.wedoffv3.view;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Optional;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.model.Atendimento;
import com.michel1985.wedoffv3.seguranca.Cripto;
import com.michel1985.wedoffv3.util.EstruturaData;
import com.michel1985.wedoffv3.util.RemoveCaracteresEspeciais;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HistoricoDeAtendimentosPendentesOverviewController extends HistoricoDeAtendimentosOverviewController {

	@FXML
	private Label historicoDeAtendimentosTituloLabel;

	@FXML
	TableView<Atendimento> atendimentosTableView;

	@FXML
	private TableColumn<Atendimento, String> idAtendimentoTableColumn;

	@FXML
	private TableColumn<Atendimento, String> idClienteTableColumn;

	@FXML
	private TableColumn<Atendimento, String> nbTableColumn;

	@FXML
	private TableColumn<Atendimento, String> dataAtendimentoTableColumn;

	@FXML
	private TableColumn<Atendimento, String> dataSolucaoTableColumn;

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
	@FXML
	private TextField searchTextField;

	@FXML
	private Button searchButton;

	// Observable list que conterá o resultado das pesquisas
	public ObservableList<Atendimento> result = FXCollections.observableArrayList();

	// Lista com todos atendimentos pendentes
	public ObservableList<Atendimento> pendentesList = FXCollections.observableArrayList();

	// Palco desse dialog
	private Stage dialogStage;

	MainApp mainApp;
	RemoveCaracteresEspeciais removedora;

	public HistoricoDeAtendimentosPendentesOverviewController() {
		this.removedora = new RemoveCaracteresEspeciais();
	}

	/**
	 * Inicializa a classe controller. Método chamado ao carregar o fxml
	 */
	@FXML
	private void initialize() {
		
		atendimentosTableView.setStyle(" .table-row-cell:selected { -fx-background-color: steelblue;}.table-row-cell:selected .text {-fx-fill: red ;}");

		idAtendimentoTableColumn.setCellValueFactory(cellData -> cellData.getValue().idAtendimentoProperty());
		idClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().idClienteProperty());
		nbTableColumn.setCellValueFactory(cellData -> cellData.getValue().nbProperty());
		// Informando o foramto de datas que quero que seja apresentado na
		// tabela

		dataAtendimentoTableColumn.setCellValueFactory(
				cellData -> EstruturaData.estruturaData(cellData.getValue().dataAtendimentoProperty()));
		dataSolucaoTableColumn.setCellValueFactory(
				cellData -> EstruturaData.estruturaData(cellData.getValue().dataSolucaoProperty()));

		dataSolucaoTableColumn.setCellFactory(column -> {
			return new TableCell<Atendimento, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					if (item != null && item != "") {
						super.updateItem(item, empty);

						LocalDate data = geraData(item);

						setText(item);

						TableRow<Atendimento> currentRow = getTableRow();

						if (currentRow != null) {
							setRowColorDoAtendimentoPendente(currentRow, data);
						}

					}
				}
			};
		});

		isAgendamentoTableColumn.setCellValueFactory(cellData -> cellData.getValue().isAgendamentoProperty());
		// isPendenteTableColumn.setCellValueFactory(cellData ->
		// cellData.getValue().isPendenteProperty());

		// limpa os detalhes do atendimento
		showAtendimentoDetails(null);

		// Detecta mudanças de seleção e mostra os detalhes do Atendimento
		// quando
		// algum é selecionado
		atendimentosTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showAtendimentoDetails(newValue));

		// Detecta mudanças de seleção e habilita e desabilita as ações do HBox
		atendimentosTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> permitirAcoes(newValue));

		// Detecta mudanças no campo de busca e se ele ficar vazio, apresenta
		// todo os histórico
		searchTextField.setOnKeyPressed((event) -> {
			if (searchTextField.getText().length() == 0)
				atendimentosTableView.setItems(pendentesList);
		});

		// Detecta o duplo click do mouse e apresenta o alert perguntando se
		// quer atender aquele cliente.
		// Caso ok, o cliente é carregado no formulário
		atendimentosTableView.setOnMousePressed((event) -> {
			if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Necessária confirmação");
				alert.setHeaderText("Você deseja continuar com esse atendimento?");
				alert.setContentText(
						"Ao clicar em \"Ok\" os dados desse atendimento serão carregados na tela principal sobrepondo os dados atuais.");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					// Obtem o id do cliente selecionado
					String idCli = atendimentosTableView.getSelectionModel().getSelectedItem().getIdCliente();
					String idAte = atendimentosTableView.getSelectionModel().getSelectedItem().getIdAtendimento();
					// Passa o id para o controller do AtendendoCliente
					this.mainApp.getAtendendoClienteController().ConsultarClientePeloId(idCli);
					this.mainApp.getAtendendoClienteController().ConsultarAtendimentoPeloId(idAte);
					// fecha o dialog do histórico
					this.dialogStage.close();
				}
			}
		});

	}

	public void setRowColorDoAtendimentoPendente(TableRow<Atendimento> currentRow, LocalDate data) {
		
		
		
		LocalDate hoje = LocalDate.now();
		if (data.isBefore(hoje)) {// ou seja, se for data
			// vencida
			currentRow.setStyle("-fx-background-color: #FE7B51;");

		} else if (data.equals(hoje)) {// vencendo hoje
			currentRow.setStyle("-fx-background-color: #ffff33;-fx-light-text-color: steelblue ;");
			
			

		} else if (data.isAfter(hoje)) {
			currentRow.setStyle("");
			// A linha acima apesar de aparentemente não está fazendo nada
			// É necessária para que a coloração das rows ficam inalteradas
		}
	}

	/**
	 * Ligando ao main
	 */
	@Override
	public void setMainApp(MainApp main, ObservableList<Atendimento> list) {
		this.mainApp = main;

		// Adiciona os dados da observable list à tabela
		buildListPendentes();
		atendimentosTableView.setItems(pendentesList);

	}

	/**
	 * Construindo a lista só com os atendimentos pendentes
	 */
	public void buildListPendentes() {
		pendentesList  = FXCollections.observableArrayList(); 
		for (Atendimento atd : mainApp.getAtendimentoData()) {
			if (atd.getIsPendente())
				pendentesList.add(atd);
		}
		
		mainApp.ordenaListaDeAtendimentosPendentes(pendentesList);
	}

	/**
	 * Passando uma nova observableList para trabalho
	 */
	@Override
	public void setObservableList(ObservableList<Atendimento> OLAtendimentos) {
		this.result = OLAtendimentos;
		atendimentosTableView.setItems(this.result);
	}

	/**
	 * Define o palco deste dialog. Usado para fecha-lo, por exemplo
	 * 
	 * @param dialogStage
	 */
	@Override
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	@Override
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
	 * Método que habilitará e desabilitará as ações sobre o cliente Se houver
	 * ou não um cliente selecionado na tabela
	 */
	private void permitirAcoes(Atendimento atendimento) {

		if (atendimento != null)
			acoesSobreAtendimentoHBox.setDisable(false);
		else
			acoesSobreAtendimentoHBox.setDisable(true);
	}

	/**
	 * Deleção de Atendimento
	 */
	@FXML
	private void handleDeleteAtendimento() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Deletar Atendimento?");
		alert.setHeaderText("Deseja apagar este atendimento?");
		alert.setContentText(
				"Ao clicar em \"Ok\" você estará APAGANDO TODOS OS DADOS DESSE ATENDIMENTO SEM POSSIBILIDADE DE RECUPERA-LOS.\n"
						+ "\nVocê tem certeza desta deleção?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			deletarAtendimentoDoBancoDeDados();

			// Removendo o cliente da observableList
			deletarAtendimentoDaListaCorrente();

		}

	}

	private void deletarAtendimentoDaListaCorrente() {
		try {
			String id = atendimentosTableView.getSelectionModel().getSelectedItem().getIdAtendimento();
			System.out.println("estamos aqui e queremos deletar o atd d id " + id);
			// Atendimento atd = ;

			// mainApp.getAtendimentoData().remove(atendimentosTableView.getSelectionModel().getSelectedItem());

			// Deletando atendimento da lista especifica ( de histórico de
			// atendimentos do cliente, por exemplo)
			result.remove(atendimentosTableView.getSelectionModel().getSelectedItem());
			pendentesList.remove(atendimentosTableView.getSelectionModel().getSelectedItem());
			deletarAtendimentoDaListaPrincipal(id);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Não gosto desse método mas o lambda não obteve sucesso
	private void deletarAtendimentoDaListaPrincipal(String id) {
		Atendimento aRemover = null;
		for (Iterator iterator = mainApp.getAtendimentoData().iterator(); iterator.hasNext();) {
			Atendimento atd = (Atendimento) iterator.next();
			if (atd.getIdAtendimento().equalsIgnoreCase(id))
				aRemover = atd;
		}
		mainApp.getAtendimentoData().remove(aRemover);

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
	 * Atualizando {@link Atendimento} Chamado quando o usuário clica em "editar
	 * cliente"
	 */
	@FXML
	private void handleAtualizaAtendimento() {
		Atendimento selectedAtendimento = atendimentosTableView.getSelectionModel().getSelectedItem();

		if (selectedAtendimento != null) {

			boolean okClicked = mainApp.showEditarAtendimentoOverview(selectedAtendimento);
			if (okClicked) {
				showAtendimentoDetails(selectedAtendimento);
				atualizaNoBanco(selectedAtendimento);
				// ToDO
				// O código abaixo ainda não me parece redondo... pensar funcionalidades
				buildListPendentes();
				atendimentosTableView.setItems(pendentesList);
				initialize();
			}
			
			
			
		}
	}

	private void atualizaNoBanco(Atendimento atendimento) {

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

	@Override
	public String criptografa(String texto) {
		Cripto cripto = new Cripto();
		return cripto.criptografa(texto, mainApp.getUsuarioAtivo().getSenha());
	}



	// metodo que recebe uma String baseadas em LocalDate e retorna o ano, mes e
	// dia
	public int[] estruturaData(String data) {

		String[] dataStr = data.split("-");
		int ano = Integer.parseInt(dataStr[0]);
		int mes = Integer.parseInt(dataStr[1]);
		int dia = Integer.parseInt(dataStr[2]);

		return new int[] { ano, mes, dia };
	}

	public LocalDate geraData(String data) {
		System.out.println("data: " + data);
		if (data != null && data.length() == 10) {

			String[] estru = data.split("/");
			int ano = Integer.parseInt(estru[2]);
			int mes = Integer.parseInt(estru[1]);
			int dia = Integer.parseInt(estru[0]);
			LocalDate date = LocalDate.of(ano, mes, dia);
			return date;
		} else
			return null;

	}
	
	@FXML
	private void handleConsultarAtendimento() {

		
		//result.clear();
		String termoBase = searchTextField.getText();
		if (!termoBase.contains("+"))
			consultarAtendimentoBuscaSimples(termoBase);
		else
			consultarAtendimentoBuscaAvancada(termoBase);
		initialize();
		atendimentosTableView.setItems(result);
	}

	void consultarAtendimentoBuscaSimples(String termoBase) {
		// Limpando a lista que conterá os resultados
		result = FXCollections.observableArrayList();
		termoBase = removedora.clean(termoBase);
		consultarAtendimentoPorNB(termoBase);
		consultarAtendimentoPorNotas(termoBase);
	}

	private void consultarAtendimentoPorNB(String nb) {
		pendentesList.forEach(atd -> {
			if (removedora.clean(atd.getNb().toLowerCase()).contains(nb.toLowerCase())) {
				if (!this.result.contains(atd))
					this.result.add(atd);
			}
		});
		// clientesTableView.setItems(result);
	}

	private void consultarAtendimentoPorNotas(String termo) {
		pendentesList.forEach(atd -> {
			if (removedora.clean(atd.getNotasSobreAtendimento().toLowerCase()).contains(termo.toLowerCase())) {
				if (!this.result.contains(atd))
					this.result.add(atd);
			}
		});
		// clientesTableView.setItems(result);
	}

	void consultarAtendimentoBuscaAvancada(String termoBase) {

		// Limpando a lista que conterá os resultados
		result = FXCollections.observableArrayList();

		result.addAll(this.pendentesList);

		termoBase = removedora.clean(termoBase);

		termoBase = termoBase.replaceAll("[+]", "+");
		String[] termos = termoBase.split("[+]");

		for (int i = 0; i < termos.length; i++) {
			RealizandoBuscaAvancadaDoTermo(termos[i].trim());
		}

	}

	private void RealizandoBuscaAvancadaDoTermo(String termo) {

		ObservableList<Atendimento> busca = FXCollections.observableArrayList();

		result.forEach(atd -> {
			if (isNbTemTermo(atd, termo))
				busca.add(atd);
			else if (isNotasTemTermo(atd, termo))
				busca.add(atd);
		});
		result = busca;
	}

	public boolean isNbTemTermo(Atendimento atd, String termo) {
		return removedora.clean(atd.getNb().toLowerCase()).contains(termo.toLowerCase());
	}

	public boolean isNotasTemTermo(Atendimento atd, String termo) {
		return removedora.clean(atd.getNotasSobreAtendimento().toLowerCase()).contains(termo.toLowerCase());
	}

}

package com.michel1985.wedoffv3.view;

import java.sql.ResultSet;
import java.util.Optional;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.model.NotaAvulsa;
import com.michel1985.wedoffv3.seguranca.Cripto;
import com.michel1985.wedoffv3.util.OpenWebPage;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HistoricoDeNotasAvulsasOverviewController {

	@FXML
	private Label historicoDeNotasAvulsasTituloLabel;

	@FXML
	private TableView<NotaAvulsa> notasAvulsasTableView;

	@FXML
	private TableColumn<NotaAvulsa, String> idNotasAvulsasTableColumn;

	@FXML
	private TableColumn<NotaAvulsa, String> tituloNotaAvulsaTableColumn;

	@FXML
	private TextField searchTextField;

	@FXML
	private Button searchButton;

	@FXML
	private Label descricaoDaNotaAvulsaTituloLabel;

	@FXML
	private HBox acoesSobreNotasHBox;

	@FXML
	private MenuButton acoesMenuButton;

	@FXML
	private MenuItem editarNotaAvulsaMenuItem;

	@FXML
	private MenuItem excluirNotaAvulsaMenuItem;

	@FXML
	private TextArea descricaoDaNotaAvulsaTextArea;
	/**
	 * Buscas
	 */

	// Observable list que conterá o resultado das pesquisas
	public ObservableList<NotaAvulsa> result = FXCollections.observableArrayList();

	// Palco desse dialog
	private Stage dialogStage;

	MainApp mainApp;

	public HistoricoDeNotasAvulsasOverviewController() {}

	/**
	 * Inicializa a classe controller. Método chamado ao carregar o fxml
	 */
	@FXML
	private void initialize() {

		idNotasAvulsasTableColumn.setCellValueFactory(cellData -> cellData.getValue().idNotaAvulsaProperty());
		tituloNotaAvulsaTableColumn.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());

		// limpa os detalhes do cliente
		showNotaAvulsaDetails(null);

		// Detecta mudanças de seleção e mostra os detalhes do cliente quando
		// algum é selecionado
		notasAvulsasTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showNotaAvulsaDetails(newValue));

		// Detecta mudanças de seleção e habilita e desabilita as ações do HBox
		notasAvulsasTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> permitirAcoes(newValue));

		// Detecta mudanças no campo de busca e se ele ficar vazio, apresenta
		// todo os histórico
		searchTextField.setOnKeyPressed((event) -> {
			if (searchTextField.getText().length() == 0)
				notasAvulsasTableView.setItems(mainApp.getNotaAvulsaData());
		});

		// Detecta o duplo click do mouse e apresenta o alert perguntando se
		// quer atender aquele cliente.
		// Caso ok, o cliente é carregado no formulário
		notasAvulsasTableView.setOnMousePressed((event) -> {
			
			if (event.isPrimaryButtonDown() && event.getClickCount() == 2 && temLink(notasAvulsasTableView.getSelectionModel().getSelectedItem())) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Necessária confirmação");
				alert.setHeaderText("Abrir o link da Nota? ");
				alert.setContentText(
						"Ao clicar em \"Ok\" o link vínculado à nota selecionada será aberta em seu navegador.");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					// Obtem o id da nota selecionada
					String link = notasAvulsasTableView.getSelectionModel().getSelectedItem().getLink();
					// Abre a página do link no navegador
					OpenWebPage.openUrl(link);
					// fecha o dialog do histórico
					this.dialogStage.close();
				}
			}
		});

	}
	
	private boolean temLink(NotaAvulsa notaAvulsa){
		if(notaAvulsa.getLink()!= null && !notaAvulsa.getLink().equalsIgnoreCase(""))
			return true;
		
		return false;
	}

	/**
	 * Define o palco deste dialog. Usado para fecha-lo, por exemplo
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Método que habilitará e desabilitará as ações sobre o cliente Se houver
	 * ou não um cliente selecionado na tabela
	 */
	private void permitirAcoes(NotaAvulsa notaAvulsa) {
		if (notaAvulsa != null)
			acoesSobreNotasHBox.setDisable(false);
		else
			acoesSobreNotasHBox.setDisable(true);
	}

	/**
	 * Ligando ao main
	 */
	public void setMainApp(MainApp main) {
		this.mainApp = main;

		// Adiciona os dados da observable list à tabela
		notasAvulsasTableView.setItems(main.getNotaAvulsaData());

	}

	public void handleShowHistoricoDeNotasAvulsas() {
		mainApp.showHistoricoDeNotasAvulsasOverview();
	}

	/**
	 * Preenche os dados da pessoa
	 */
	private void showNotaAvulsaDetails(NotaAvulsa notaAvulsa) {

		if (notaAvulsa != null) {
			descricaoDaNotaAvulsaTextArea.setText(notaAvulsa.getDescricao());
		} else {
			descricaoDaNotaAvulsaTextArea.setText("");
		}
	}

	/***/

	/**
	 * Deleção de clientes
	 */
	@FXML
	private void handleDeleteNotaAvulsa() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Deletar Nota Avulsa?");
		alert.setHeaderText("Apagar todos os dados da Nota?");
		alert.setContentText(
				"Ao clicar em \"Ok\" você estará APAGANDO TODOS OS DADOS DESSA Nota Avulsa SEM POSSIBILIDADE DE RECUPERA-LOS.\n\n\n"
						+ "Você tem certeza desta deleção?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

			deletarNotaAvulsaDoBancoDeDados();

			// Removendo o cliente da observableList
			deletarNotaAvulsaDaNotaAvulsaData();

		}
	}

	private void deletarNotaAvulsaDaNotaAvulsaData() {
		try {
			mainApp.getNotaAvulsaData().remove(notasAvulsasTableView.getSelectionModel().getSelectedItem());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deletarNotaAvulsaDoBancoDeDados() {
		String selectedId = notasAvulsasTableView.getSelectionModel().getSelectedItem().getIdNotaAvulsa();
		ResultSet resultSet = null;
		try {
			CRUD crud = new CRUD(mainApp.getUsuarioAtivo());
			resultSet = crud.getResultSet("DELETE FROM NOTASAVULSAS WHERE idNotaAvulsa = '" + selectedId + "'");
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
	 * Atualizando cliente Chamado quando o usuário clica em "editar cliente"
	 */
	@FXML
	private void handleAtualizaNotaAvulsa() {
		NotaAvulsa selectedNotaAvulsa = notasAvulsasTableView.getSelectionModel().getSelectedItem();

		if (selectedNotaAvulsa != null) {

			boolean okClicked = mainApp.showEditarNotaAvulsaOverview(selectedNotaAvulsa);
			if (okClicked) {
				showNotaAvulsaDetails(selectedNotaAvulsa);
				atualizaNoBanco(selectedNotaAvulsa);
			}
		}
	}
	
	
	

	private void atualizaNoBanco(NotaAvulsa notaAvulsa) {

		ResultSet resultSet = null;
		try {
			CRUD crud = new CRUD(mainApp.getUsuarioAtivo());

			resultSet = crud.getResultSet("UPDATE NOTASAVULSAS SET titulo= '" + criptografa(notaAvulsa.getTitulo())
					+ "', link= '" + criptografa(notaAvulsa.getLink()) + "', descricao= '"
					+ criptografa(notaAvulsa.getDescricao()) + "' WHERE idNotaAvulsa='" + notaAvulsa.getIdNotaAvulsa()
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

	public String criptografa(String texto) {
		Cripto cripto = new Cripto();
		return cripto.criptografa(texto, mainApp.getUsuarioAtivo().getSenha());
	}

	/**
	 * Métodos relativos a consulta
	 */
	@FXML
	private void handleConsultarNotaAvulsa() {

		NotaAvulsaSearch search = new NotaAvulsaSearch(this);

		result.clear();
		String termoBase = searchTextField.getText();
		if (!termoBase.contains("+"))
			search.consultarNotaAvulsaBuscaSimples(termoBase);
		else
			search.consultarNotaAvulsaBuscaAvancada(termoBase);

		notasAvulsasTableView.setItems(result);
	}
	
	

	//@FXML
	public void handleNovaNotaAvulsa() {		

			NotaAvulsa notaAvulsa = new NotaAvulsa("","","");
			boolean okClicked = mainApp.showEditarNotaAvulsaOverview(notaAvulsa);
			if (okClicked) {
				showNotaAvulsaDetails(notaAvulsa);
				gravarNovaNotaAvulsa(notaAvulsa);
			}
		
	}
	

	private void gravarNovaNotaAvulsa(NotaAvulsa nota) {

		System.out.println("Estamos tentando gravar a seguinte nota: ");
		System.out.println(nota.getTitulo());
		System.out.println(nota.getLink());
		System.out.println(nota.getDescricao());
		
		
		int id = 0;

		// Gravando o cliente ao banco
		ResultSet resultSet = null;
		try {			
					
			
			CRUD crud = new CRUD(mainApp.getUsuarioAtivo());
			resultSet = crud.getResultSet(
					"INSERT INTO NOTASAVULSAS (titulo, link, descricao) VALUES ('" + criptografa(nota.getTitulo())
							+ "','" + criptografa(nota.getLink()) + "','" + criptografa(nota.getDescricao()) + "');CALL IDENTITY();");

			if (resultSet.next())
				id = resultSet.getInt(1);// obtendo o idretornado CALL
			
			
			// IDENTITY();
			String idString = id+"";
			NotaAvulsa notaA = new NotaAvulsa(idString, nota.getLink(), nota.getTitulo(), nota.getDescricao());
			mainApp.getNotaAvulsaData().add(notaA);
			
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
	
	

}

package com.michel1985.wedoffv3.view;

import java.sql.ResultSet;
import java.util.Optional;
import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.seguranca.Cripto;
import com.michel1985.wedoffv3.util.ValidaCPF;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HistoricoDeClientesOverviewController {

	@FXML
	private TableView<Cliente> clientesTableView;

	@FXML
	private TableColumn<Cliente, String> idClienteTableColumn;

	@FXML
	private TableColumn<Cliente, String> cpfClienteTableColumn;

	@FXML
	private TableColumn<Cliente, String> nomeClienteTableColumn;

	@FXML
	private Label historicoDeClientesTituloLabel;

	@FXML
	private Label notasSobreClienteTituloLabel;

	@FXML
	private TextArea notasSobreClienteTextArea;

	@FXML
	private Button verAtendimentosDoClienteButton;

	@FXML
	private HBox acoesSobreClienteHBox;

	@FXML
	private MenuButton acoesMenuButton;

	@FXML
	private MenuItem editarClienteMenuItem;

	@FXML
	private MenuItem excluirClienteMenuItem;
	

	/**
	 * Buscas
	 */

	// Observable list que conter� o resultado das pesquisas
	public ObservableList<Cliente> result = FXCollections.observableArrayList();

	@FXML
	private TextField searchTextField;

	@FXML
	private Button searchButton;
	
	//Palco desse dialog
	private Stage dialogStage;

	private MainApp mainApp;

	public HistoricoDeClientesOverviewController() {
	}

	/**
	 * Inicializa a classe controller. M�todo chamado ao carregar o fxml
	 */
	@FXML
	private void initialize() {

		idClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().idClienteProperty());
		nomeClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
		cpfClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().cpfProperty());

		// limpa os detalhes do cliente
		showClienteDetails(null);

		// Detecta mudan�as de sele��o e mostra os detalhes do cliente quando
		// algum � selecionado
		clientesTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showClienteDetails(newValue));

		// Detecta mudan�as de sele��o e habilita e desabilita as a��es do HBox
		clientesTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> permitirAcoes(newValue));

		// Detecta mudan�as no campo de busca e se ele ficar vazio, apresenta
		// todo os hist�rico
		searchTextField.setOnKeyPressed((event) -> {
			if (searchTextField.getText().length() == 0)
				clientesTableView.setItems(mainApp.getClienteData());
		});
		
		//Detecta o duplo click do mouse e apresenta o alert perguntando se quer atender aquele cliente.
		//Caso ok, o cliente � carregado no formul�rio
		clientesTableView.setOnMousePressed((event) -> {
			if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Necess�ria confirma��o");
				alert.setHeaderText("Voc� deseja atender esse cliente?");
				alert.setContentText("Ao clicar em \"Ok\" os dados desse cliente ser�o carregados para atendimento sobrepondo os dados atuais.");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					//Obtem o id do cliente selecionado
					String id = clientesTableView.getSelectionModel().getSelectedItem().getIdCliente();
					//Passa o id para o controller do AtendendoCliente
					this.mainApp.getAtendendoClienteController().ConsultarClientePeloId(id);
					//fecha o dialog do hist�rico
					this.dialogStage.close();
				}                  
	        }
		});

		
	}
	
	 /**
     * Define o palco deste dialog.
     * Usado para fecha-lo, por exemplo
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

	/**
	 * M�todo que habilitar� e desabilitar� as a��es sobre o cliente Se houver
	 * ou n�o um cliente selecionado na tabela
	 */
	private void permitirAcoes(Cliente cliente) {
		// TODO Auto-generated method stub
		if (cliente != null)
			acoesSobreClienteHBox.setDisable(false);
		else
			acoesSobreClienteHBox.setDisable(true);
	}

	/**
	 * Ligando ao main
	 */
	public void setMainApp(MainApp main) {
		this.mainApp = main;

		// Adiciona os dados da observable list � tabela
		clientesTableView.setItems(main.getClienteData());

	}

	public void handleShowHistoricoDeClientes() {
		mainApp.showHistoricoDeClientesOverview();
	}

	/**
	 * Preenche os dados da pessoa
	 */
	private void showClienteDetails(Cliente cliente) {

		if (cliente != null) {
			notasSobreClienteTextArea.setText(cliente.getNotasSobreCLiente());
		} else {
			notasSobreClienteTextArea.setText("");
		}
	}

	/***/

	/**
	 * Dele��o de clientes
	 */
	@FXML
	private void handleDeleteCliente() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Deletar cliente?");
		alert.setHeaderText("Apagar todos os dados do cliente?");
		alert.setContentText(
				"Ao clicar em \"Ok\" voc� estar� APAGANDO TODOS OS DADOS DESSE CLIENTE SEM POSSIBILIDADE DE RECUPERA-LOS.\n"
						+ "\nOs atendimentos desse cliente tamb�m ser�o todos apagados.\n\n"
						+ "Voc� tem certeza desta dele��o?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			deletarClienteDoBancoDeDados();

			// Removendo o cliente da observableList
			deletarClienteDaClienteData();
		}
	}

	private void deletarClienteDaClienteData() {
		try {
			mainApp.getClienteData().remove(clientesTableView.getSelectionModel().getSelectedItem());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deletarClienteDoBancoDeDados() {
		String selectedId = clientesTableView.getSelectionModel().getSelectedItem().getIdCliente();
		ResultSet resultSet = null;
		try {
			CRUD crud = new CRUD(mainApp.getUsuarioAtivo());
			resultSet = crud.getResultSet("DELETE FROM CLIENTES WHERE idcliente = '" + selectedId + "'");
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
	 * Atualizando cliente Chamado quando o usu�rio clica em "editar cliente"
	 */
	@FXML
	private void handleAtualizaCliente() {
		Cliente selectedCliente = clientesTableView.getSelectionModel().getSelectedItem();

		if (selectedCliente != null) {

			boolean okClicked = mainApp.showEditarClienteOverview(selectedCliente);
			if (okClicked) {
				showClienteDetails(selectedCliente);
				atualizaNoBanco(selectedCliente);
			}
		}
	}

	private void atualizaNoBanco(Cliente cliente) {

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
		// Nota: Como trata-se de uma observableList a atualiza��o j� foi feita
		// na lista
	}

	public String criptografa(String texto) {
		Cripto cripto = new Cripto();
		return cripto.criptografa(texto, mainApp.getUsuarioAtivo().getSenha());
	}

	/**
	 * M�todos relativos a consulta
	 */
	@FXML
	private void handleConsultarCliente() {

		result.clear();
		String termo = searchTextField.getText();
		if (new ValidaCPF().validarCPF(termo))
			consultarClientePorCpf(termo);
		else
			consultarClientePorNome(termo);
		consultarClientePorNotas(termo);
		clientesTableView.setItems(result);
	}

	/**
	 * O m�todo abaixo realiza pesquisa do termo fornecido no campo nome
	 */
	private void consultarClientePorNome(String nome) {
		mainApp.getClienteData().forEach(cli -> {
			if (cli.getNome().toLowerCase().contains(nome.toLowerCase())) {
				if (!result.contains(cli))
					result.add(cli);
			}
		});
		// clientesTableView.setItems(result);
	}

	/**
	 * O m�todo abaixo implementa pesquisa por cpf
	 */
	private void consultarClientePorCpf(String termo) {
		mainApp.getClienteData().forEach(cli -> {
			if (cli.getCpf().equals(termo))
				if (!result.contains(cli))
					result.add(cli);
		});
		// clientesTableView.setItems(result);
	}

	/**
	 * O m�todo abaixo implementa pesquisa nas notas sobre cliente
	 */
	private void consultarClientePorNotas(String nome) {
		mainApp.getClienteData().forEach(cli -> {
			if (cli.getNotasSobreCLiente().toLowerCase().contains(nome.toLowerCase())) {
				if (!result.contains(cli))
					result.add(cli);
			}
		});
		// clientesTableView.setItems(result);
	}

}

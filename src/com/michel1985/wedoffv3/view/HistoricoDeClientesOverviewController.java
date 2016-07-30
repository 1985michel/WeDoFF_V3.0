package com.michel1985.wedoffv3.view;

import java.sql.ResultSet;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.seguranca.Cripto;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

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

		// Removendo o cliente da observableList
		try {
			mainApp.getClienteData().remove(clientesTableView.getSelectionModel().getSelectedItem());
		} catch (Exception e) {
			e.printStackTrace();
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
		} else {
			// N�o se aplica pois se n�o houver sele��o o bot�o fica
			// desabilitado
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
		//Nota: Como trata-se de uma observableList a atualiza��o j� foi feita na lista
	}

	public String criptografa(String texto) {
		Cripto cripto = new Cripto();
		return cripto.criptografa(texto, mainApp.getUsuarioAtivo().getSenha());
	}

}

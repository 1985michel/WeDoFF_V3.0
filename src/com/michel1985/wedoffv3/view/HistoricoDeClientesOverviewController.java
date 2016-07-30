package com.michel1985.wedoffv3.view;

import java.sql.ResultSet;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.model.Cliente;

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
	 * Inicializa a classe controller. Método chamado ao carregar o fxml
	 */
	@FXML
	private void initialize() {

		idClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().idClienteProperty());
		nomeClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
		cpfClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().cpfProperty());

		// limpa os detalhes do cliente
		showClienteDetails(null);

		// Detecta mudanças de seleção e mostra os detalhes do cliente quando
		// algum é selecionado
		clientesTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showClienteDetails(newValue));

		// Detecta mudanças de seleção e habilita e desabilita as ações do HBox
		clientesTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> permitirAcoes(newValue));

	}

	/**
	 * Método que habilitará e desabilitará as ações sobre o cliente Se houver
	 * ou não um cliente selecionado na tabela
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

		// Adiciona os dados da observable list à tabela
		clientesTableView.setItems(main.getClienteData());

	}

	public void handleShowHistoricoDeClientes() {

		// a linha abaixo deve ser excluida posteriormente

		mainApp.showHistoricoDeClientesOverview();
	}

	/**
	 * Preenche os dados da pessoa
	 */
	private void showClienteDetails(Cliente cliente) {
		if (cliente != null) {
			// preenche o label das notas
			notasSobreClienteTextArea.setText(cliente.getNotasSobreCLiente());
		} else {
			notasSobreClienteTextArea.setText("");
		}
	}

	/***/

	/**
	 * Deleção de clientes
	 */
	@FXML
	private void handleDeleteCliente() {
		
		//Remove da Tabela
		//int selectedIndex = clientesTableView.getSelectionModel().getFocusedIndex();
		//clientesTableView.getItems().remove(selectedIndex);
		
		String selectedId = clientesTableView.getSelectionModel().getSelectedItem().getIdCliente();
		//Gravando o cliente ao banco
		ResultSet resultSet = null;
		try {
						
			CRUD crud = new CRUD(mainApp.getUsuarioAtivo());
			//DELETE FROM Customers	WHERE CustomerName='Alfreds Futterkiste'
			
			resultSet = crud.getResultSet("DELETE FROM CLIENTES WHERE idcliente = '"+selectedId+"'");
			/*
			resultSet = crud.getResultSet("INSERT INTO CLIENTES (nomeCliente, cpfCliente, notasSobreCliente) VALUES ('"
							+ criptografa(nome) + "','" + criptografa(cpf) + "','" + criptografa(notas) + "');CALL IDENTITY();");
			*/
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//Adicionando o cliente
		try{			
			System.out.println("Antes: "+mainApp.getClienteData().size());
			//String idString = ""+id;
			//Cliente newCli = new Cliente(idString,nome, cpf, notas);
			mainApp.getClienteData().remove(clientesTableView.getSelectionModel().getSelectedItem());
			System.out.println("Depois: "+mainApp.getClienteData().size());
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	
	}

}

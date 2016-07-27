package com.michel1985.wedoffv3.view;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.model.Cliente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
	private Label NotasSobreClienteTituloLabel;

	@FXML
	private Label notasSobreClienteLabel;

	@FXML
	private Button verAtendimentosDoClienteButton;

	@FXML
	private MenuItem editarClienteMenuItem;

	@FXML
	private MenuItem excluirClienteMenuItem;

	private MainApp mainApp;

	public HistoricoDeClientesOverviewController() {
		super();
	}

	/**
	 * Inicializa a classe controller. Método chamado ao carregar o fxml
	 */
	@FXML
	private void initialize() {

		idClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().idClienteProperty());
		nomeClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
		cpfClienteTableColumn.setCellValueFactory(cellData -> cellData.getValue().cpfProperty());

	}

	/**
	 * Ligando ao main
	 */
	public void setMainApp(MainApp main) {
		this.mainApp = main;

		// Adiciona os dados da observable list à tabela
		clientesTableView.setItems(main.getClienteData());

	}
	
	public void handleShowHistoricoDeClientes(){
		mainApp.showHistoricoDeClientesOverview();
	}

}

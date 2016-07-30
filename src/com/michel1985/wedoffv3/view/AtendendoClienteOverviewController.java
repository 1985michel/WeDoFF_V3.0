/**
 * 
 */
package com.michel1985.wedoffv3.view;

import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.seguranca.Cripto;
import com.michel1985.wedoffv3.util.ValidaCliente;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author michel
 * 
 */
public class AtendendoClienteOverviewController {
	
	/**
	 * Identificando os elementos da GUI
	 * 
	 * 1 - Lado esquerdo >>> CLIENTE
	 * */
	@FXML
	private AnchorPane fullAnchorPane;
	
	@FXML
	private AnchorPane clienteAnchorPane;
	
	@FXML
	private AnchorPane atendimentoAnchorPane;
	
	@FXML
	private Label tituloClienteLabel;
	
	@FXML
	private TextField cpfTextField;
	
	@FXML
	private Button consultarClientePeloCpfButton;
	
	@FXML
	private TextField nomeClienteTextField;
	
	@FXML
	private Button consultarClientePeloNomeButton;
	
	@FXML
	private TextArea notasClienteTextArea;
	
	@FXML
	private VBox acoesClienteVBox;
	
	@FXML
	private Button receberSatButton;
	
	@FXML
	private Button verHistoricoDeAtendimentosDoClienteButton;
	
	
	/**
	 * Elementos do lado direito >>>> ATENDIMENTO
	 * */
	@FXML
	private Label tituloAtendimentoLabel;
	
	@FXML
	private TextField nbTextField;
	
	@FXML
	private Button consultarAtendimentoPeloNbButton;
	
	@FXML
	private CheckBox isAgendamentoCheckBox;
	
	@FXML
	private DatePicker dataDoAtendimentoDatePicker;
	
	@FXML
	private TextArea notasSobreAtendimentoTextArea;
	
	@FXML
	private CheckBox isPendenteCheckBox;
	
	@FXML
	private DatePicker dataParaSolucionarPendenciaDatePicker;
	
	@FXML
	private Button registrarAtendimentoButton;
	
	@FXML
	private Button cancelarAtendimentoButton;
	
	//Referência ao Main
	private MainApp mainApp;
	
	//Id do cliente em atendimento
	static String idClienteAtual;
	
	//Crontrutor. É chamado antes do método initialize
	public AtendendoClienteOverviewController(){}
	
	/**
	 * Initialize - é chamado ao carregar o fxml
	 * */
	@FXML
	private void initialize(){
		
		//Utilizado para inicializar tabelas e tals
		//Lambda: "em cada célula dessa coluna, coloquei o valor do firstNameProperty" 
		// firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
	}

	/**
	 * setMainApp - é usado pelo MainApp para para se referenciar
	 * */
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		
		//Em casos de tabela,aqui é o local para solitiar o povoamento
		//someTable.setItems(mainApp.getClienteData());
	}
	
	/**
	 * Método chamado quando o usuário consulta a existência de um cliente 
	 * pelo cpf
	 * */
	@FXML
	private void handleConsultarClientePeloCPF(){		
		
		String cpf = cpfTextField.getText();
		if(!ValidaCliente.validaCPF(cpf)){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("");
			alert.setHeaderText("CPF inválido!");
			alert.setContentText("Por favor, verifique o CPF digitado e tente novamente");
			alert.showAndWait();
			return;
		}
		ResultSet resultSet = null;
		try{
			resultSet = new CRUD(mainApp.getUsuarioAtivo()).getResultSet("SELECT * FROM CLIENTES");
			boolean achou = false;
			while(resultSet.next()){
				String cpfDesc = descriptografa(resultSet.getString("cpfCliente"));
				if(cpfDesc.equalsIgnoreCase(cpf)){
					showCliente(resultSet);
					achou = true;
					try{
						resultSet.close();
					}catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
			}
			if(!achou){
				clienteNaoLocalizado();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Método que notifica se tratar de um novo cliente
	 * */
	private void clienteNaoLocalizado(){
		habilitarAcoesClienteVBox(false);
	}
	
	/**
	 * Método que carrega o cliente no AtendendoClienteOverview para permitir realiza o atendimento
	 * @throws SQLException 
	 * */
	private void showCliente(ResultSet resultSet) throws SQLException{
		
				
		//Seta idClienteAtual
		idClienteAtual = resultSet.getString("idcliente");
				
		//Carrega campos
		nomeClienteTextField.setText(descriptografa(resultSet.getString("nomeCliente")));
		notasClienteTextArea.setText(descriptografa(resultSet.getString("notasSobreCliente")));		
		
		//Habilitando funções do menu
		habilitarAcoesClienteVBox(true);
	}
	
	/**
	 * Habilita [true] e desabilita[false] o HBox com os botões de ações de cliente
	 * */
	private void habilitarAcoesClienteVBox(boolean value){
		if(!value){
			//desabilita ver histórico
			verHistoricoDeAtendimentosDoClienteButton.setDisable(true);
			
			//Alterando texto do botão receberSat
			receberSatButton.setText("Gravar Cliente");
			
			//Alterando o método do botão [com lambda!]
			receberSatButton.setOnAction(event -> gravarCliente());
		}
		else{			
			
			//habilita ver histórico
			verHistoricoDeAtendimentosDoClienteButton.setDisable(false);
			
			//Alterando texto do botão receberSat
			receberSatButton.setText("Atualizar Cliente");
			
			//Alterando o método do botão [com lambda!]
			receberSatButton.setOnAction(event -> handleAtualizaCliente());
			
		}
	}
	
	private void gravarCliente(){
				
		String cpf = "";
		String nome = "";
		String notas ="";
		int id=0;
		
		//Gravando o cliente ao banco
		ResultSet resultSet = null;
		try {
			cpf = cpfTextField.getText();
			nome = nomeClienteTextField.getText();
			notas =  notasClienteTextArea.getText();
			
			CRUD crud = new CRUD(mainApp.getUsuarioAtivo());
			
			resultSet = crud.getResultSet("INSERT INTO CLIENTES (nomeCliente, cpfCliente, notasSobreCliente) VALUES ('"
							+ criptografa(nome) + "','" + criptografa(cpf) + "','" + criptografa(notas) + "');CALL IDENTITY();");
			
			if (resultSet.next()) id = resultSet.getInt(1);// obtendo o idretornado CALL IDENTITY();
			habilitarAcoesClienteVBox(true);
			
			setIdClienteAtual(id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//Adicionando o cliente à ObservableLIst
		try{			
			
			String idString = ""+id;
			Cliente newCli = new Cliente(idString,nome, cpf, notas);
			mainApp.getClienteData().add(newCli);
						
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * O método abaixo seta o Id do cliente atual.
	 * 
	 * */	
	private void setIdClienteAtual(int id){
		idClienteAtual = id+"";
		
	}
	

	/**
	 * Atualizando cliente Chamado quando o usuário clica em "editar cliente"
	 */
	@FXML
	private void handleAtualizaCliente() {
				
		String cpf = cpfTextField.getText();
		String nome = nomeClienteTextField.getText();
		String notas = notasClienteTextArea.getText();
		
		if (idClienteAtual != null && idClienteAtual!="") {
						
			
			//for com lambda
			mainApp.getClienteData().forEach(u -> {
				if(idClienteAtual.equalsIgnoreCase(u.getIdCliente())){
										
					//Primeiro atualizando na lista
					u.setCpf(cpf);
					u.setNome(nome);
					u.setNotasSobreCLiente(notas);
					
					//Depois atualizando no banco		
					try {
						atualizaNoBanco(u);
						return; //interromp o método pois tudo já foi feito
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});			
			
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Quem estamos atendendo?");
			alert.setHeaderText("Ocorreu um erro na aplicação.");
			alert.setContentText("A aplicação não sabe qual cliente está sendo atendido. Por favor feche a aplicação e tente nomvamente.\nFavor relatar o problema ao suporte técnico.");
			alert.showAndWait();
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
		
	}

	
	public String criptografa(String texto) {
		Cripto cripto = new Cripto();
		return cripto.criptografa(texto, mainApp.getUsuarioAtivo().getSenha());
	}

	private String descriptografa(String texto) {
		Cripto cripto = new Cripto();
		return cripto.descriptografa(texto, mainApp.getUsuarioAtivo().getSenha());
	}
	
	
	
	 
}

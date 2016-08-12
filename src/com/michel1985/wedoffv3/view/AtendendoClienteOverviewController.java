/**
 * 
 */
package com.michel1985.wedoffv3.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.model.Atendimento;
import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.seguranca.Cripto;
import com.michel1985.wedoffv3.util.ValidaCliente;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * @author michel
 * 
 */
public class AtendendoClienteOverviewController {

	@FXML
	TextFieldLimited cpfTextField;

	@FXML
	Button consultarClientePeloCpfButton;

	@FXML
	TextField nomeClienteTextField;

	@FXML
	TextArea notasClienteTextArea;

	@FXML
	VBox acoesClienteVBox;

	@FXML
	Button receberSatButton;

	@FXML
	Button verHistoricoDeAtendimentosDoClienteButton;

	/**
	 * Elementos do lado direito >>>> ATENDIMENTO
	 */
	@FXML
	Label tituloAtendimentoLabel;

	@FXML
	TextField nbTextField;

	@FXML
	Button consultarAtendimentoPeloNbButton;

	@FXML
	CheckBox isAgendamentoCheckBox;

	@FXML
	DatePicker dataDoAtendimentoDatePicker;

	@FXML
	TextArea notasSobreAtendimentoTextArea;

	@FXML
	CheckBox isPendenteCheckBox;

	@FXML
	DatePicker dataParaSolucionarPendenciaDatePicker;

	@FXML
	Button registrarAtendimentoButton;

	@FXML
	Button cancelarAtendimentoButton;

	// Componentes do wait
	@FXML
	AnchorPane waitAnchorPane;

	@FXML
	Pane faixaBackgroundPane;

	@FXML
	ImageView imagemImageView;

	// Refer�ncia ao Main
	private MainApp mainApp;

	// Refer�ncia ao ManagerUI
	private AtendendoClienteOverviewUIManager uiManager;

	// Id do cliente em atendimento
	private static String idClienteAtual;

	// Id do atendimento em andamento
	private static String idAtendimentoAtual;

	// Crontrutor. � chamado antes do m�todo initialize
	public AtendendoClienteOverviewController() {
		uiManager = new AtendendoClienteOverviewUIManager(this);
	}

	/**
	 * Initialize - � chamado ao carregar o fxml
	 */
	@FXML
	private void initialize() {

		setaDataAtendimentoHoje();

		faixaBackgroundPane.setStyle("-fx-background-color: #181A1C;");

		// Tonando o dataPicker desabilitado inicialmente
		dataParaSolucionarPendenciaDatePicker.setDisable(true);

		// Tornando o dataPicker habilitado somente se o atendimento estiver
		// marcado como pendente
		isPendenteCheckBox.setOnAction((event) -> {
			dataParaSolucionarPendenciaDatePicker.setDisable(!isPendenteCheckBox.isSelected());
			if (isPendenteCheckBox.isSelected())
				setaDataSolucacaoPendenciaParaDaqui30Dias();
		});
		
	}

	/**
	 * setMainApp - � usado pelo MainApp para para se referenciar
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * M�todo chamado quando o usu�rio consulta a exist�ncia de um cliente pelo
	 * cpf
	 */
	@FXML
	void handleConsultarClientePeloCPF() {

		String cpf = cpfTextField.getText();
		if (!ValidaCliente.validaCPF(cpf)) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("");
			alert.setHeaderText("CPF inv�lido!");
			alert.setContentText("Por favor, verifique o CPF digitado e tente novamente");
			alert.showAndWait();
			return;
		}
		ResultSet resultSet = null;
		try {
			resultSet = new CRUD(mainApp.getUsuarioAtivo()).getResultSet("SELECT * FROM CLIENTES");
			boolean achou = false;
			while (resultSet.next()) {
				String cpfDesc = descriptografa(resultSet.getString("cpfCliente"));
				if (cpfDesc.equalsIgnoreCase(cpf)) {

					showCliente(resultSet);
					achou = true;
					try {
						resultSet.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
			}
			if (!achou) {

				setStatusDoFormCliente(AtendendoClienteOverviewUIManager.NOVO_CLIENTE);
			}
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

	public void ConsultarClientePeloId(String id) {

		ResultSet resultSet = null;
		try {
			resultSet = new CRUD(mainApp.getUsuarioAtivo())
					.getResultSet("SELECT * FROM CLIENTES where idCliente ='" + id + "'");
			boolean achou = false;
			while (resultSet.next()) {
				showCliente(resultSet);
				showCpf(resultSet);
				achou = true;
				try {
					resultSet.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;

			}
			if (!achou) {
				setStatusDoFormCliente(AtendendoClienteOverviewUIManager.NOVO_CLIENTE);
			}
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

	@FXML
	void handleReceberSat() {
		// TODO Auto-generated method stub
	}

	@FXML
	void handleVerAtendimentosDoCliente() {
		// TODO Auto-generated method stub
	}

	/**
	 * O formul�rio de clientes ter� tr�s status poss�veis: 1 - Inicial ( vazio
	 * ); 2 - Cadastrando Cliente ( j� pesquisamos e o cliente n�o foi
	 * encontrado, ent�o deve-se preencher o form) 3 - Cliente localizado e os
	 * dados s�o exibidos em seus referiso campos
	 */

	private void setStatusDoFormCliente(int status) {
		switch (status) {
		case 1:
			uiManager.setFormClienteStatusInicial();
			break;
		case 2:
			uiManager.setFormClienteStatusNovoCliente();
			break;
		case 3:
			uiManager.setFormClienteStatusAtendendo();
			break;
		}
	}
	
	
	private void setStatusDoFormAtendimento(int status) {
		//ToDo
		switch (status) {
		case 1:
			//uiManager.setFormClienteStatusInicial();
			break;
		case 2:
			//uiManager.setFormClienteStatusNovoCliente();
			break;
		case 3:
			//uiManager.setFormClienteStatusAtendendo();
			break;
		}
	}

	@FXML
	void handleCancelarCadastramentoDoCliente() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cancelar e Limpar");
		alert.setHeaderText("Deseja cancelar o atendimento ao Cliente?");
		alert.setContentText("Limpar dados?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

			// habilitarAcoesClienteVBox(false);
			setStatusDoFormCliente(AtendendoClienteOverviewUIManager.INICIAL);

			// limpa idClienteAtual
			setIdClienteAtual(0);

			// Limpa os campos do formul�rio de cliente
			nomeClienteTextField.setText("");
			cpfTextField.setText("");
			notasClienteTextArea.setText("");
		}
	}

	/**
	 * M�todo que carrega o cliente no AtendendoClienteOverview para permitir
	 * realiza o atendimento
	 * 
	 * @throws SQLException
	 */
	private void showCliente(ResultSet resultSet) throws SQLException {

		// Seta idClienteAtual
		idClienteAtual = resultSet.getString("idcliente");

		// Carrega campos
		nomeClienteTextField.setText(descriptografa(resultSet.getString("nomeCliente")));
		notasClienteTextArea.setText(descriptografa(resultSet.getString("notasSobreCliente")));

		// Habilitando fun��es do menu
		setStatusDoFormCliente(AtendendoClienteOverviewUIManager.ATENDENDO);

	}

	/**
	 * O m�todo showCliente n�o precisa setar o campo CPF, pois ele j� est� no
	 * textField apropriado no nomento da consulta. Ent�o, estou criando um
	 * m�todo showCpf para ser invoncado por m�todos secund�rios que utilizam
	 * essa funcionalidade
	 * 
	 */
	private void showCpf(ResultSet resultSet) throws SQLException {
		cpfTextField.setText(descriptografa(resultSet.getString("cpfCliente")));
	}

	void handleCancelarAtendimentoDoCliente() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cancelar e Limpar");
		alert.setHeaderText("Deseja cancelar o atendimento ao Cliente?");
		alert.setContentText("Limpar dados?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

			setStatusDoFormCliente(AtendendoClienteOverviewUIManager.INICIAL);

			// limpa idClienteAtual
			setIdClienteAtual(0);

			// Limpa os campos do formul�rio de cliente
			nomeClienteTextField.setText("");
			cpfTextField.setText("");
			notasClienteTextArea.setText("");
		}

	}

	void gravarCliente() {

		String cpf = "";
		String nome = "";
		String notas = "";
		int id = 0;

		// Gravando o cliente ao banco
		ResultSet resultSet = null;
		try {
			cpf = cpfTextField.getText();
			nome = nomeClienteTextField.getText();
			notas = notasClienteTextArea.getText();

			CRUD crud = new CRUD(mainApp.getUsuarioAtivo());
			resultSet = crud.getResultSet(
					"INSERT INTO CLIENTES (nomeCliente, cpfCliente, notasSobreCliente) VALUES ('" + criptografa(nome)
							+ "','" + criptografa(cpf) + "','" + criptografa(notas) + "');CALL IDENTITY();");

			if (resultSet.next())
				id = resultSet.getInt(1);// obtendo o idretornado CALL
											// IDENTITY();
			setStatusDoFormCliente(AtendendoClienteOverviewUIManager.ATENDENDO);

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

		// Adicionando o cliente � ObservableLIst
		try {

			String idString = "" + id;
			Cliente newCli = new Cliente(idString, nome, cpf, notas);
			mainApp.getClienteData().add(newCli);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * O m�todo abaixo seta o Id do cliente atual.
	 * 
	 */
	private void setIdClienteAtual(int id) {
		idClienteAtual = id + "";
	}
	
	/**
	 * O m�todo abaixo seta o Id do cliente atual.
	 * 
	 */
	private void setIdAtendimentoAtual(int id) {
		idAtendimentoAtual = id + "";
	}

	/**
	 * Atualizando cliente Chamado quando o usu�rio clica em "editar cliente"
	 */
	@FXML
	void handleAtualizaCliente() {

		String cpf = cpfTextField.getText();
		String nome = nomeClienteTextField.getText();
		String notas = notasClienteTextArea.getText();

		if (idClienteAtual != null && idClienteAtual != "") {

			// for com lambda
			mainApp.getClienteData().forEach(u -> {
				if (idClienteAtual.equalsIgnoreCase(u.getIdCliente())) {
					atualizaClienteNaLista(cpf, nome, notas, u);

					try {
						atualizaClienteNoBanco(u);
						return; // interromp o m�todo pois tudo j� foi feito
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Quem estamos atendendo?");
			alert.setHeaderText("Ocorreu um erro na aplica��o.");
			alert.setContentText(
					"A aplica��o n�o sabe qual cliente est� sendo atendido. Por favor feche a aplica��o e tente nomvamente.\nFavor relatar o problema ao suporte t�cnico.");
			alert.showAndWait();
		}
	}

	private void atualizaClienteNaLista(String cpf, String nome, String notas, Cliente u) {
		u.setCpf(cpf);
		u.setNome(nome);
		u.setNotasSobreCLiente(notas);
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

	}

	@FXML
	private void handleGravarAtendimento() {

		ResultSet resultSet = null;
		try {
			// Primeiro vamos captar os dados informados
			String nb = nbTextField.getText();
			String notas = notasSobreAtendimentoTextArea.getText();

			String nbCripto = criptografa(nb);
			String notasCripto = criptografa(notas);

			boolean isPendente = isPendenteCheckBox.isSelected();
			boolean isAgendamento = isAgendamentoCheckBox.isSelected();

			// Se o atendimento estiver pendente, ser� obrigat�rio informar uma
			// data de solu��o
			String dataSolucao = "";
			if (isPendente) {
				try {
					if (dataParaSolucionarPendenciaDatePicker.getValue() == null
							|| dataParaSolucionarPendenciaDatePicker.getValue().toString().equalsIgnoreCase("")) {
						alertarWarning("Data de Solu��o?", "Quando este atendimento deve ser conclu�do?",
								"Voc� informou que o atendimento ficou pendente.\nQuando ele deve ser concluido?");
						return;
					} else {
						dataSolucao = dataParaSolucionarPendenciaDatePicker.getValue().toString();
					}

				} catch (Exception e) {
					alertarWarning("Data de Solu��o?", "Quando este atendimento deve ser conclu�do?",
							"Voc� informou que o atendimento ficou pendente.\nQuando ele deve ser concluido?");
					return;
				}

			}

			// A data do atendimento s�ra sempre obrigat�ria
			String data = "";
			try {
				if (dataDoAtendimentoDatePicker.getValue() == null
						|| dataDoAtendimentoDatePicker.getValue().toString().equalsIgnoreCase("")) {
					alertarWarning("Data do Atendimento?", "O cliente est� sendo atendido hoje?",
							"Favor informar a data do atendimento.");
					return;
				} else {
					data = dataDoAtendimentoDatePicker.getValue().toString();
				}
			} catch (Exception e) {
				return;
			}

			CRUD crud = new CRUD(mainApp.getUsuarioAtivo());
			resultSet = crud.getResultSet(
					"INSERT INTO atendimentos (idCliente,isPendente,isAgendamento,nb,dataatendimento,notassobreatendimento,datasolucao) VALUES ('"
							+ idClienteAtual + "','" + isPendente + "','" + isAgendamento + "','" + nbCripto + "','"
							+ data + "','" + notasCripto + "','" + dataSolucao + "');CALL IDENTITY();");

			int id =0;
			if (resultSet.next())
				id = resultSet.getInt(1);// obtendo o idretornado CALL
											// IDENTITY();
			
			// Adicionando o cliente � ObservableLIst
			try {

//public Atendimento(String id, String idCli, boolean agendamento,  boolean pendente, String nb, String notas, String data, String datasolu){

				
				String idString = "" + id;
				Atendimento newAtendimento = new Atendimento(idString,idClienteAtual,isAgendamento,isPendente,nb,notas,data,dataSolucao);
				mainApp.getAtendiementoData().add(newAtendimento);

			} catch (Exception e) {
				e.printStackTrace();
			}
			setStatusDoFormAtendimento(AtendendoClienteOverviewUIManager.ATENDENDO);

			setIdAtendimentoAtual(id);
			
			waitSomeTime();
		} catch (Exception e) {
			if (idClienteAtual == "") {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Quem estamos atendendo?");
				alert.setHeaderText("Ocorreu um erro na aplica��o.");
				alert.setContentText(
						"A aplica��o n�o sabe qual cliente est� sendo atendido. Por favor feche a aplica��o e tente nomvamente.\nFavor relatar o problema ao suporte t�cnico.");
				alert.showAndWait();
			}
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	

	private void alertarWarning(String title, String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	// Mostra o gif do wait
	private void showWait() {
		Image img = new Image("file:resources/images/arquivadoCentralizado.gif");
		imagemImageView.setImage(img);
		waitAnchorPane.toFront();
	}

	private void waitSomeTime() {
		showWait();
		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					Thread.sleep(2300);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				hideWait();
			}
		});
		new Thread(sleeper).start();
	}

	// Oculta o gif do wait
	private void hideWait() {
		waitAnchorPane.toBack();
		imagemImageView.setImage(null);

	}

	/**
	 * Limpando todos os campos ao salvar um atendimento
	 */

	private void limparCamposAoConcluirAtendiemnto() {

		setStatusDoFormCliente(AtendendoClienteOverviewUIManager.INICIAL);

		// limpa idClienteAtual
		setIdClienteAtual(0);

		// Limpa os campos do formul�rio de cliente
		nomeClienteTextField.setText("");
		cpfTextField.setText("");
		notasClienteTextArea.setText("");

		// Limpa os campos do formul�rio de Atendimento
		nbTextField.setText("");
		isAgendamentoCheckBox.setSelected(false);
		setaDataAtendimentoHoje();
		isPendenteCheckBox.setSelected(false);
		dataParaSolucionarPendenciaDatePicker.setDisable(true);
		// N�o � preciso setar a data da solu��o pois ela � autom�tica
		notasSobreAtendimentoTextArea.setText("");

	}

	@FXML
	private void handleCancelarAtendimento() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cancelar e Limpar");
		alert.setHeaderText("Deseja cancelar esse atendimento?");
		alert.setContentText("As informa��es n�o salvas ser�o perdidas.\nConfirma o cancelamento?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			limparCamposAoConcluirAtendiemnto();
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

	private void setaDataAtendimentoHoje() {
		LocalDate date = LocalDate.now();
		dataDoAtendimentoDatePicker.setValue(date);
	}

	private void setaDataSolucacaoPendenciaParaDaqui30Dias() {
		LocalDate date = LocalDate.now();
		dataParaSolucionarPendenciaDatePicker.setValue(date.plusMonths(1));
	}

}

/**
 * 
 */
package com.michel1985.wedoffv3.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.crud.CascadeDeDelecao;
import com.michel1985.wedoffv3.model.Atendimento;
import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.seguranca.Cripto;
import com.michel1985.wedoffv3.util.ValidaCliente;

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

	// Referência ao Main
	private MainApp mainApp;

	// Referência ao ManagerUI
	private AtendendoClienteOverviewUIManager uiManager;

	// Id do cliente em atendimento
	private static String idClienteAtual;

	// Id do atendimento em andamento
	private static String idAtendimentoAtual;

	// Crontrutor. É chamado antes do método initialize
	public AtendendoClienteOverviewController() {
		uiManager = new AtendendoClienteOverviewUIManager(this);
	}

	/**
	 * Initialize - é chamado ao carregar o fxml
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
				setDataSolucacaoPendenciaParaDaqui30Dias();
		});
	}

	/**
	 * setMainApp - é usado pelo MainApp para para se referenciar
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	void handleReceberSat() {
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 * 
	 * Operações sobre o cliente e seus campos no formulário
	 * 
	 * 
	 * 
	 */

	/**
	 * Método que gerencia status do formulário de clientes
	 */

	private void setStatusDoFormCliente(int status) {
		uiManager.setStatusDoFormCliente(status);
	}

	@FXML
	void handleConsultarClientePeloCPF() {

		String cpf = cpfTextField.getText();
		if (!ValidaCliente.validaCPF(cpf)) {
			AtendendoClienteOverViewAlertManagers.alertaDeCPFInvalido();
			return;
		}
		ResultSet resultSet = null;
		try {
			resultSet = new CRUD(mainApp.getUsuarioAtivo()).getResultSet("SELECT * FROM CLIENTES");
			boolean achou = false;
			while (resultSet.next()) {
				String cpfDesc = descriptografa(resultSet.getString("cpfCliente"));
				if (cpfDesc.equalsIgnoreCase(cpf)) {

					showClienteNoFormularioDeAtendimento(resultSet);
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
				showClienteNoFormularioDeAtendimento(resultSet);
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
	void handleGravarCliente() {

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
			
			if(!ValidaCliente.validaNome(nome)){
				AtendendoClienteOverViewAlertManagers.alertaDeNomeInvalido();
				return;
			}
				
			

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

		// Adicionando o cliente à ObservableLIst
		try {

			String idString = "" + id;
			Cliente newCli = new Cliente(idString, nome, cpf, notas);
			mainApp.getClienteData().add(newCli);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	void handleVerHistoricoDeAtendimentosDoCliente() {
		mainApp.showHistoricoDeAtendimentosDoClienteOverview(idClienteAtual);
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

			// Limpa os campos do formulário de cliente
			nomeClienteTextField.setText("");
			cpfTextField.setText("");
			notasClienteTextArea.setText("");
		}
	}

	private void showClienteNoFormularioDeAtendimento(ResultSet resultSet) throws SQLException {

		// Seta idClienteAtual
		idClienteAtual = resultSet.getString("idcliente");

		// Carrega campos
		nomeClienteTextField.setText(descriptografa(resultSet.getString("nomeCliente")));
		notasClienteTextArea.setText(descriptografa(resultSet.getString("notasSobreCliente")));

		
		cpfTextField.setText(descriptografa(resultSet.getString("cpfCliente")));

		// Habilitando funções do menu
		setStatusDoFormCliente(AtendendoClienteOverviewUIManager.ATENDENDO);

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

			// Limpa os campos do formulário de cliente
			nomeClienteTextField.setText("");
			cpfTextField.setText("");
			notasClienteTextArea.setText("");
		}

	}

	/**
	 * O método abaixo seta o Id do cliente atual.
	 * 
	 */
	public void setIdClienteAtual(int id) {
		idClienteAtual = id + "";
	}
	public void setIdClienteAtual(String id){
		idClienteAtual=id;
	}

	/**
	 * O método abaixo seta o Id do cliente atual.
	 * 
	 */
	void setIdAtendimentoAtual(int id) {
		idAtendimentoAtual = id + "";
	}
	void setIdAtendimentoAtual(String id) {
		idAtendimentoAtual = id;
	}

	public void limparClienteEAtendimentoAtual() {
		setIdAtendimentoAtual(null);
		setIdClienteAtual(null);
	}

	/**
	 * Atualizando cliente Chamado quando o usuário clica em "editar cliente"
	 */
	@FXML
	void handleAtualizaCliente() {

		String cpf = cpfTextField.getText();
		String nome = nomeClienteTextField.getText();
		String notas = notasClienteTextArea.getText();

		if (isClienteConhecido()) {

			// for com lambda
			mainApp.getClienteData().forEach(u -> {
				if (idClienteAtual.equalsIgnoreCase(u.getIdCliente())) {
					atualizaClienteNaLista(cpf, nome, notas, u);

					try {
						atualizaClienteNoBanco(u);
						return; // interromp o método pois tudo já foi feito
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		} else {
			AtendendoClienteOverViewAlertManagers.alertaClienteAtualDesconhecido();

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

	/**
	 * 
	 * 
	 * Operações sobre o Atendimento
	 * 
	 * 
	 */

	@FXML
	void handleGravarAtendimento() {

		// Se for um novo cliente já pesquisado
		if (consultarClientePeloCpfButton.getTooltip().getText().equalsIgnoreCase("Novo Cliente")) {
			handleGravarCliente();
		}
		if (!isClienteConhecido()) {
			AtendendoClienteOverViewAlertManagers.alertaClienteAtualDesconhecido();
			return;
		}

		// Atualizando o cliente automaticamente
		if (receberSatButton.getText().equalsIgnoreCase("Atualizar Cliente"))
			handleAtualizaCliente();

		ResultSet resultSet = null;
		try {
			// Primeiro vamos captar os dados informados
			String nb = nbTextField.getText();
			String notas = notasSobreAtendimentoTextArea.getText();

			if (!isNotasSobreAtendimentoInformada(notas)) {
				AtendendoClienteOverViewAlertManagers.alertaNotasSobreAtendimentoNaoInformada();
				return;
			}

			String nbCripto = criptografa(nb);
			String notasCripto = criptografa(notas);

			boolean isPendente = isPendenteCheckBox.isSelected();
			boolean isAgendamento = isAgendamentoCheckBox.isSelected();

			// Se o atendimento estiver pendente, será obrigatório informar uma
			// data de solução
			String dataSolucao = "";
			if (isPendente) {
				try {
					if (!isDataParaSolucionarPendenciaInformada()) {
						AtendendoClienteOverViewAlertManagers.alertDataDeSolucaoNaoInformada();
						return;
					} else {
						dataSolucao = dataParaSolucionarPendenciaDatePicker.getValue().toString();
					}

				} catch (Exception e) {
					AtendendoClienteOverViewAlertManagers.alertDataDeSolucaoNaoInformada();
					return;
				}

			}

			// A data do atendimento séra sempre obrigatória
			String data = "";
			try {
				if (!isDataDoAtendimentoInformada()) {
					AtendendoClienteOverViewAlertManagers.alertaDataDoAtendimentoNaoInformada();
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

			int id = 0;
			if (resultSet.next())
				id = resultSet.getInt(1);// obtendo o idretornado CALL
											// IDENTITY();

			// Adicionando o cliente à ObservableLIst
			try {
				String idString = "" + id;
				Atendimento newAtendimento = new Atendimento(idString, idClienteAtual, isAgendamento, isPendente, nb,
						notas, data, dataSolucao);
				mainApp.getAtendimentoData().add(newAtendimento);

			} catch (Exception e) {
				e.printStackTrace();
			}
			// setStatusDoFormAtendimento(AtendendoClienteOverviewUIManager.ATENDENDO);
			uiManager.atendimentoConcluido();

			uiManager.showGifCRUDConfirmado();
		} catch (Exception e) {
			if (!isClienteConhecido()) {
				AtendendoClienteOverViewAlertManagers.alertaClienteAtualDesconhecido();
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

	@FXML
	void ConsultarAtendimentoPeloId(String id) {

		ResultSet resultSet = null;
		try {
			resultSet = new CRUD(mainApp.getUsuarioAtivo())
					.getResultSet("SELECT * FROM atendimentos where idatendimento ='" + id + "'");

			while (resultSet.next()) {
				showAtendimentoNoFormularioDeAtendimento(resultSet);

				try {
					resultSet.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;

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

	private void showAtendimentoNoFormularioDeAtendimento(ResultSet resultSet) {
		// idString,idClienteAtual,isAgendamento,isPendente,nb,notas,data,dataSolucao
		try {
			nbTextField.setText(descriptografa(resultSet.getString("nb")));
			isAgendamentoCheckBox.setSelected(resultSet.getBoolean("isAgendamento"));
			dataDoAtendimentoDatePicker.setValue(LocalDate.parse(resultSet.getString("dataatendimento")));
			isPendenteCheckBox.setSelected(resultSet.getBoolean("isPendente"));
			if (isPendenteCheckBox.isSelected()) {
				dataParaSolucionarPendenciaDatePicker.setDisable(false);
				dataParaSolucionarPendenciaDatePicker.setValue(LocalDate.parse(resultSet.getString("dataSolucao")));
			}

			notasSobreAtendimentoTextArea.setText(descriptografa(resultSet.getString("notassobreatendimento")));
			registrarAtendimentoButton.setOnAction((event) -> handleAtualizaAtendimento());
			registrarAtendimentoButton.setText("Atualizar Atendimento");
			setIdAtendimentoAtual(resultSet.getString("idAtendimento"));

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Falha ao carregar o atendimento para o form");
		}

	}

	@FXML
	void handleAtualizaAtendimento() {

		// Se for um novo cliente já pesquisado
		if (consultarClientePeloCpfButton.getTooltip().getText().equalsIgnoreCase("Novo Cliente")) {
			handleGravarCliente();
		}
		if (!isClienteConhecido()) {
			AtendendoClienteOverViewAlertManagers.alertaClienteAtualDesconhecido();
			return;
		}

		// Atualizando o cliente automaticamente
		if (receberSatButton.getText().equalsIgnoreCase("Atualizar Cliente"))
			handleAtualizaCliente();

		ResultSet resultSet = null;
		try {
			// Primeiro vamos captar os dados informados
			String nb = nbTextField.getText();
			String notas = notasSobreAtendimentoTextArea.getText();

			if (!isNotasSobreAtendimentoInformada(notas)) {
				AtendendoClienteOverViewAlertManagers.alertaNotasSobreAtendimentoNaoInformada();
				return;
			}

			String nbCripto = criptografa(nb);
			String notasCripto = criptografa(notas);

			boolean isPendente = isPendenteCheckBox.isSelected();
			boolean isAgendamento = isAgendamentoCheckBox.isSelected();

			// Se o atendimento estiver pendente, será obrigatório informar uma
			// data de solução
			String dataSolucao = "";
			if (isPendente) {
				try {
					if (!isDataParaSolucionarPendenciaInformada()) {
						AtendendoClienteOverViewAlertManagers.alertDataDeSolucaoNaoInformada();
						return;
					} else {
						dataSolucao = dataParaSolucionarPendenciaDatePicker.getValue().toString();
					}

				} catch (Exception e) {
					AtendendoClienteOverViewAlertManagers.alertDataDeSolucaoNaoInformada();
					return;
				}

			}

			// A data do atendimento séra sempre obrigatória
			String data = "";
			try {
				if (!isDataDoAtendimentoInformada()) {
					AtendendoClienteOverViewAlertManagers.alertaDataDoAtendimentoNaoInformada();
					return;
				} else {
					data = dataDoAtendimentoDatePicker.getValue().toString();
				}
			} catch (Exception e) {
				return;
			}

			CRUD crud = new CRUD(mainApp.getUsuarioAtivo());
			
			
			System.out.println("ID DE ATENDIMENTO ATUAL DE TRABALHO: "+ idAtendimentoAtual);
			
			resultSet = crud.getResultSet(
					"UPDATE atendimentos SET nb= '" + nbCripto
					+ "', notassobreatendimento= '" + notasCripto + "', datasolucao= '" + dataSolucao
					+ "', dataatendimento= '" + data + "', ispendente= '" + isPendente + "', isagendamento= '"
					+ isAgendamento + "' WHERE idatendimento='" + idAtendimentoAtual + "'");

			
			
			atualizaAtendimentoNaLista(new Atendimento(idAtendimentoAtual, idClienteAtual, isAgendamento, isPendente, nb, notas, data, dataSolucao));

			uiManager.showGifCRUDConfirmado();
		} catch (Exception e) {
			if (!isClienteConhecido()) {
				AtendendoClienteOverViewAlertManagers.alertaClienteAtualDesconhecido();
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

	private void atualizaAtendimentoNaLista(Atendimento atendimento) {
		mainApp.getAtendimentoData().forEach(atd ->{
			if(atd.getIdAtendimento().equalsIgnoreCase(atendimento.getIdAtendimento())){
				atd.setDataAtendimento(atendimento.getDataAtendimento());
				atd.setIsAgendamento(atendimento.getIsAgendamento());
				atd.setNb(atendimento.getNb());
				atd.setNotasSobreAtendimento(atendimento.getNotasSobreAtendimento());
				atd.setIsPendente(atendimento.getIsPendente());
				if(atd.getIsPendente()) atd.setDataSolucao(atendimento.getDataSolucao());
			}
		});
		
	}

	/**
	 * Validacoes
	 */

	private boolean isClienteConhecido() {
		return idClienteAtual != null && idClienteAtual != "" && idAtendimentoAtual != "0";
	}

	private boolean isDataDoAtendimentoInformada() {
		return dataDoAtendimentoDatePicker.getValue() != null
				&& !dataDoAtendimentoDatePicker.getValue().toString().equalsIgnoreCase("");
	}

	private boolean isDataParaSolucionarPendenciaInformada() {
		return dataParaSolucionarPendenciaDatePicker.getValue() != null
				&& !dataParaSolucionarPendenciaDatePicker.getValue().toString().equalsIgnoreCase("");
	}

	private boolean isNotasSobreAtendimentoInformada(String notas) {
		return notas != "" && notas != null && notas.length() >= 2;
	}

	private boolean isCpfTextFieldVazio() {
		return cpfTextField.getText().equalsIgnoreCase("") || cpfTextField.getText() == null;
	}


	@FXML
	private void handleCancelarAtendimento() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cancelar e Limpar");
		alert.setHeaderText("Deseja cancelar esse atendimento?");
		alert.setContentText("As informações não salvas serão perdidas.\nConfirma o cancelamento?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			uiManager.atendimentoConcluido();
		}

	}
	
	//void deletarTodosOsAtendimentosDeUmCliente(String idCliente){
	//	new HistoricoDeAtendimentosOverviewController().deletarTodosOsAtendimentosDeUmCliente(idCliente);
	//}

	public String criptografa(String texto) {
		Cripto cripto = new Cripto();
		return cripto.criptografa(texto, mainApp.getUsuarioAtivo().getSenha());
	}

	private String descriptografa(String texto) {
		Cripto cripto = new Cripto();
		return cripto.descriptografa(texto, mainApp.getUsuarioAtivo().getSenha());
	}

	public void setaDataAtendimentoHoje() {
		LocalDate date = LocalDate.now();
		dataDoAtendimentoDatePicker.setValue(date);
	}

	private void setDataSolucacaoPendenciaParaDaqui30Dias() {
		LocalDate date = LocalDate.now();
		dataParaSolucionarPendenciaDatePicker.setValue(date.plusMonths(1));
	}
	
		
	/**
	 * Método invocado ao deletar um cliente
	 * */	
	void deletarTodosOsAtendimentosDeUmCliente(String idCliente){
		//System.out.println("No historico de atendimentos chegou a ordem para deletar os atendimentos do cliente"+idCliente);
		new CascadeDeDelecao().deletarTodosOsAtendimentosDeUmCliente(idCliente, mainApp.getAtendimentoData(),mainApp.getUsuarioAtivo());
	}
	
	

}

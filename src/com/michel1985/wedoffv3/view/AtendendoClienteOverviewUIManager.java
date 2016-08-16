package com.michel1985.wedoffv3.view;

import java.io.InputStream;

import com.michel1985.wedoffv3.model.ObjetoSAT;
import com.michel1985.wedoffv3.util.ManipuladoraDeClipBoard;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

public class AtendendoClienteOverviewUIManager {

	private AtendendoClienteOverviewController controller;

	// Possíveis status do formulário cliente
	public static final int INICIAL = 1;
	public static final int NOVO_CLIENTE = 2;
	public static final int ATENDENDO = 3;

	// Possíveis status do formulário de Atendimento
	public static final int HA_CLIENTE = 1;
	public static final int NAO_HA_CLIENTE = 0;

	public AtendendoClienteOverviewUIManager(AtendendoClienteOverviewController controller) {
		this.controller = controller;
	}
	
	public void atendimentoConcluido(){
		// limpando os ids de Cliente e Atendimento
		this.controller.limparClienteEAtendimentoAtual();
		setFormClienteStatusInicial();
		limparFormAtendimento();
		
	}

	public void setStatusDoFormCliente(int status) {
		switch (status) {
		case 1:
			setFormClienteStatusInicial();
			break;
		case 2:
			setFormClienteStatusNovoCliente();
			break;
		case 3:
			setFormClienteStatusAtendendo();
			break;
		}
	}

	// Tela inicial quando a aplicação é carregada
	private void setFormClienteStatusInicial() {

		

		controller.setIdClienteAtual(null);
		controller.cpfTextField.setText("");
		controller.cpfTextField.setEditable(true);
		controller.consultarClientePeloCpfButton.setDisable(false);
		controller.consultarClientePeloCpfButton.setText("?");
		controller.consultarClientePeloCpfButton.setOnAction((event) -> controller.handleConsultarClientePeloCPF());
		controller.nomeClienteTextField.setText("");
		controller.notasClienteTextArea.setText("");
		controller.receberSatButton.setText("Receber SAT");
		controller.receberSatButton.setOnAction((event) -> controller.handleReceberSat());
		controller.verHistoricoDeAtendimentosDoClienteButton.setDisable(true);
		controller.verHistoricoDeAtendimentosDoClienteButton.setText("Ver Atendimentos do Cliente");
		controller.verHistoricoDeAtendimentosDoClienteButton
				.setOnAction((event) -> controller.handleVerHistoricoDeAtendimentosDoCliente());

	}

	// Quando foi pesquisado o CPF mas não foi encontrado
	private void setFormClienteStatusNovoCliente() {

		// A pesquisa foi feita para aquela cpf
		// então vamos garantir que ele não será alterado
		controller.cpfTextField.setEditable(false);

		controller.consultarClientePeloCpfButton.setDisable(true);
		controller.consultarClientePeloCpfButton.setText("new");
		controller.consultarClientePeloCpfButton.setMinWidth(50);
		controller.consultarClientePeloCpfButton.setOnAction(null);
		controller.consultarClientePeloCpfButton.setTooltip(new Tooltip("Novo Cliente"));

		// desabilita ver histórico
		controller.verHistoricoDeAtendimentosDoClienteButton.setDisable(false);
		controller.verHistoricoDeAtendimentosDoClienteButton.setText("Cancelar Cadastro");
		controller.verHistoricoDeAtendimentosDoClienteButton
				.setOnAction((event) -> controller.handleCancelarCadastramentoDoCliente());

		// Alterando texto do botão receberSat
		controller.receberSatButton.setText("Gravar Cliente");

		// Alterando o método do botão [com lambda!]
		controller.receberSatButton.setOnAction(event -> controller.handleGravarCliente());

		// Desabilita o textField de edição do CPF
		controller.cpfTextField.setEditable(false);

	}

	private void setFormClienteStatusAtendendo() {

		controller.cpfTextField.setEditable(false);

		controller.consultarClientePeloCpfButton.setDisable(false);
		controller.consultarClientePeloCpfButton.setText("X");
		controller.consultarClientePeloCpfButton.setOnAction(event -> controller.handleCancelarAtendimentoDoCliente());
		controller.consultarClientePeloCpfButton.setTooltip(new Tooltip("Cancelar e Limpar Campos"));

		// Alterando texto do botão receberSat
		controller.receberSatButton.setText("Atualizar Cliente");

		// Alterando o método do botão ReceberSat [com lambda!]
		controller.receberSatButton.setOnAction(event -> controller.handleAtualizaCliente());

		// habilita ver histórico
		controller.verHistoricoDeAtendimentosDoClienteButton.setDisable(false);
		controller.verHistoricoDeAtendimentosDoClienteButton.setText("Ver Atendimentos do Cliente");
		controller.verHistoricoDeAtendimentosDoClienteButton
				.setOnAction((event) -> controller.handleVerHistoricoDeAtendimentosDoCliente());

	}
	
	public void limparFormAtendimento(){
		controller.nbTextField.setText("");
		controller.isAgendamentoCheckBox.setSelected(false);
		controller.setaDataAtendimentoHoje();
		controller.notasSobreAtendimentoTextArea.setText("");
		controller.isPendenteCheckBox.setSelected(false);
		controller.dataParaSolucionarPendenciaDatePicker.setDisable(true);
		controller.setIdAtendimentoAtual(null);
		controller.registrarAtendimentoButton.setOnAction((event)-> controller.handleGravarAtendimento());
		controller.registrarAtendimentoButton.setText("Registrar Atendimento");
	}


	// Mostra o gif do wait
	public void showGifCRUDConfirmado() {
		waitSomeTime();
	}
	
	public void showGifCRUDConfirmadoCliente(){
		waitSomeTimeCliente();
	}

	private void showWait() {
		//Image img = new Image("file:resources/images/arquivadoCentralizado.gif");
		InputStream url = this.getClass().getResourceAsStream("/arquivadoCentralizado.gif");
		//imagemImageView.setImage(new Image(url));
		controller.imagemImageView.setImage(new Image(url));
		controller.waitAnchorPane.toFront();
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
		controller.waitAnchorPane.toBack();
		controller.imagemImageView.setImage(null);

	}
	
	private void showWaitCliente() {
		//Image img = new Image("file:resources/images/arquivadoCentralizado.gif");
		InputStream url = this.getClass().getResourceAsStream("/arquivadoCentralizado.gif");
		//imagemImageView.setImage(new Image(url));
		controller.imagemImageViewCliente.setImage(new Image(url));
		controller.waitClienteGridPane.toFront();
	}

	private void waitSomeTimeCliente() {
		showWaitCliente();
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
				hideWaitCliente();
			}
		});
		new Thread(sleeper).start();
	}

	// Oculta o gif do wait
	private void hideWaitCliente() {
		controller.waitClienteGridPane.toBack();
		controller.imagemImageViewCliente.setImage(null);

	}

	public void receberSat() {
		ObjetoSAT obSat = ManipuladoraDeClipBoard.getSAT();
		controller.cpfTextField.setText(obSat.getCpf());
		controller.nomeClienteTextField.setText(obSat.getNome());
		controller.notasClienteTextArea.setText(obSat.getNit());
		controller.isAgendamentoCheckBox.setSelected(obSat.isAgendamento());
		
	}

}
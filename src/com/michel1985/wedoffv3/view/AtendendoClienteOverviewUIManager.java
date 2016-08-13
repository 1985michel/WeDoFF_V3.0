package com.michel1985.wedoffv3.view;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

public class AtendendoClienteOverviewUIManager {

	private AtendendoClienteOverviewController controller;
	
	//Possíveis status do formulário cliente
	public static final int INICIAL = 1;
	public static final int NOVO_CLIENTE =2;
	public static final int ATENDENDO = 3;
	

	public AtendendoClienteOverviewUIManager(AtendendoClienteOverviewController controller) {
		this.controller = controller;
	}

	// Tela inicial quando a aplicação é carregada
	public void setFormClienteStatusInicial() {

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
		controller.verHistoricoDeAtendimentosDoClienteButton.setOnAction((event) -> controller.handleVerAtendimentosDoCliente());

	}

	// Quando foi pesquisado o CPF mas não foi encontrado
	public void setFormClienteStatusNovoCliente() {

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
		controller.receberSatButton.setOnAction(event -> controller.gravarCliente());

		// Desabilita o textField de edição do CPF
		controller.cpfTextField.setEditable(false);

	}

	public void setFormClienteStatusAtendendo() {

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
		controller.verHistoricoDeAtendimentosDoClienteButton.setOnAction((event) -> controller.handleVerAtendimentosDoCliente());

	}
	
	public void limparAtendendoClienteOverview(){
		
		//limpando os ids de Cliente e Atendimento
		this.controller.limparClienteEAtendimentoAtual();
		
		// Limpando lado cliente
		setFormClienteStatusInicial();
		
		//Limpando lado Atendimento
		this.controller.cpfTextField.setText("");
		this.controller.notasSobreAtendimentoTextArea.setText("");
		this.controller.nbTextField.setText("");
		this.controller.isAgendamentoCheckBox.setSelected(false);
		this.controller.isPendenteCheckBox.setSelected(false);
		
	}

}
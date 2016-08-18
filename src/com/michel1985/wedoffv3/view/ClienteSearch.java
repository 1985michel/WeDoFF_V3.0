package com.michel1985.wedoffv3.view;

import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.util.ValidaCPF;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClienteSearch {
	
	HistoricoDeClientesOverviewController controller;
	public ClienteSearch(HistoricoDeClientesOverviewController controller){
		this.controller = controller;
	}

	public void consultarClienteBuscaAvancada(String termo) {
		System.out.println("buscando "+termo);
		ObservableList<Cliente> busca = FXCollections.observableArrayList();
				
		controller.result.forEach(cliente ->{
			 if(isNomeTemTermo(cliente, termo)) busca.add(cliente);
			 else if(isNotasSobreClienteTemTermo(cliente, termo)) busca.add(cliente);
			 else if(new ValidaCPF().validarCPF(termo)){
				 isCpfTemTermo(cliente, termo);
			 }		 
			 
		});
		controller.result = busca;
	}
	
	private boolean isNomeTemTermo(Cliente cliente, String termo){
		return cliente.getNome().toLowerCase().contains(termo.toLowerCase());
	}
	private boolean isNotasSobreClienteTemTermo(Cliente cliente, String termo){
		return cliente.getNotasSobreCLiente().toLowerCase().contains(termo.toLowerCase());
	}
	private boolean isCpfTemTermo(Cliente cliente, String termo){
		return cliente.getCpf().contains(termo);
	}

	public void buscaSimples(String termoBase) {
		if (new ValidaCPF().validarCPF(termoBase))
			consultarClientePorCpf(termoBase);
		else
			consultarClientePorNome(termoBase);
		consultarClientePorNotas(termoBase);
		//controller.clientesTableView.setItems(controller.result);
	}

	/**
	 * O método abaixo realiza pesquisa do termo fornecido no campo nome
	 */
	private void consultarClientePorNome(String nome) {
		controller.mainApp.getClienteData().forEach(cli -> {
			if (cli.getNome().toLowerCase().contains(nome.toLowerCase())) {
				if (!controller.result.contains(cli))
					controller.result.add(cli);
			}
		});
		// clientesTableView.setItems(result);
	}

	/**
	 * O método abaixo implementa pesquisa por cpf
	 */
	private void consultarClientePorCpf(String termo) {
		controller.mainApp.getClienteData().forEach(cli -> {
			if (cli.getCpf().equals(termo))
				if (!controller.result.contains(cli))
					controller.result.add(cli);
		});
		// clientesTableView.setItems(result);
	}

	/**
	 * O método abaixo implementa pesquisa nas notas sobre cliente
	 */
	private void consultarClientePorNotas(String nome) {
		controller.mainApp.getClienteData().forEach(cli -> {
			if (cli.getNotasSobreCLiente().toLowerCase().contains(nome.toLowerCase())) {
				if (!controller.result.contains(cli))
					controller.result.add(cli);
			}
		});
		// clientesTableView.setItems(result);
	}

	


}

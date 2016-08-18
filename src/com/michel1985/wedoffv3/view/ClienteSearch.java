package com.michel1985.wedoffv3.view;

import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.util.RemoveCaracteresEspeciais;
import com.michel1985.wedoffv3.util.ValidaCPF;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClienteSearch {

	HistoricoDeClientesOverviewController controller;
	RemoveCaracteresEspeciais removedora;

	public ClienteSearch(HistoricoDeClientesOverviewController controller) {
		this.controller = controller;
		this.removedora = new RemoveCaracteresEspeciais();
	}

	public void consultarClienteBuscaAvancada(String termoBase) {

		termoBase = removedora.clean(termoBase);

		controller.result.addAll(controller.mainApp.getClienteData());

		termoBase = termoBase.replaceAll("[+]", "+");

		String[] termos = termoBase.split("[+]");

		for (int i = 0; i < termos.length; i++) {
			realizandoBuscaAvancadaDoTermo(termos[i].trim());
		}

	}

	private void realizandoBuscaAvancadaDoTermo(String termo) {
		ObservableList<Cliente> busca = FXCollections.observableArrayList();

		controller.result.forEach(cliente -> {
			if (isNomeTemTermo(cliente, termo))
				busca.add(cliente);
			else if (isNotasSobreClienteTemTermo(cliente, termo))
				busca.add(cliente);
			else if (new ValidaCPF().validarCPF(termo)) {
				isCpfTemTermo(cliente, termo);
			}

		});
		controller.result = busca;
	}

	private boolean isNomeTemTermo(Cliente cliente, String termo) {
		return removedora.clean(cliente.getNome().toLowerCase()).contains(termo.toLowerCase());
	}

	private boolean isNotasSobreClienteTemTermo(Cliente cliente, String termo) {
		return removedora.clean(cliente.getNotasSobreCLiente().toLowerCase()).contains(termo.toLowerCase());
	}

	private boolean isCpfTemTermo(Cliente cliente, String termo) {
		return cliente.getCpf().contains(termo);
	}

	public void buscaSimples(String termoBase) {
		termoBase = removedora.clean(termoBase);

		if (new ValidaCPF().validarCPF(termoBase))
			consultarClientePorCpf(termoBase);
		else
			consultarClientePorNome(termoBase);
		consultarClientePorNotas(termoBase);

	}

	private void consultarClientePorNome(String nome) {
		controller.mainApp.getClienteData().forEach(cli -> {
			if (removedora.clean(cli.getNome().toLowerCase()).contains(nome.toLowerCase())) {
				if (!controller.result.contains(cli))
					controller.result.add(cli);
			}
		});

	}

	private void consultarClientePorCpf(String termo) {
		controller.mainApp.getClienteData().forEach(cli -> {
			if (cli.getCpf().equals(termo))
				if (!controller.result.contains(cli))
					controller.result.add(cli);
		});

	}

	private void consultarClientePorNotas(String nome) {
		controller.mainApp.getClienteData().forEach(cli -> {
			if (removedora.clean(cli.getNotasSobreCLiente().toLowerCase()).contains(nome.toLowerCase())) {
				if (!controller.result.contains(cli))
					controller.result.add(cli);
			}
		});

	}

}

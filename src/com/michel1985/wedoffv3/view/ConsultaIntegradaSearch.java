package com.michel1985.wedoffv3.view;

import java.sql.ResultSet;
import java.util.Iterator;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.model.Atendimento;
import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.model.ConsultaIntegradaObject;
import com.michel1985.wedoffv3.util.RemoveCaracteresEspeciais;
import com.michel1985.wedoffv3.util.ValidaCPF;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConsultaIntegradaSearch {

	ConsultasIntegradasOverviewController controller;
	RemoveCaracteresEspeciais removedora;

	public ConsultaIntegradaSearch(ConsultasIntegradasOverviewController controller) {
		this.controller = controller;
		this.removedora = new RemoveCaracteresEspeciais();
	}

	void consultarConsultaIntegradaBuscaAvancada(String termoBase) {
		
		this.construindoAConsultaIntegradaData();
		
		termoBase = removedora.clean(termoBase);

		termoBase = termoBase.replaceAll("[+]", "+");
		String[] termos = termoBase.split("[+]");

		for (int i = 0; i < termos.length; i++) {
			RealizandoBuscaAvancadaDoTermo(termos[i].trim());
		}
		
	}

	private void RealizandoBuscaAvancadaDoTermo(String termo) {

		ObservableList<ConsultaIntegradaObject> busca = FXCollections.observableArrayList();

		controller.result.forEach(consInt -> {
			if (isNbTemTermo(consInt.getAtd(), termo))
				busca.add(consInt);
			else if (isNotasTemTermo(consInt.getAtd(), termo))
				busca.add(consInt);
			else if (isNomeTemTermo(consInt.getCliente(), termo))
				busca.add(consInt);
			else if (isNotasSobreClienteTemTermo(consInt.getCliente(), termo))
				busca.add(consInt);
			else if (new ValidaCPF().validarCPF(termo)) {
				if(isCpfTemTermo(consInt.getCliente(), termo))
					busca.add(consInt);
			}
		});
		controller.result = busca;
	}

	private boolean isNbTemTermo(Atendimento atd, String termo) {
		return removedora.clean(atd.getNb().toLowerCase()).contains(termo.toLowerCase());
	}

	private boolean isNotasTemTermo(Atendimento atd, String termo) {
		return removedora.clean(atd.getNotasSobreAtendimento().toLowerCase()).contains(termo.toLowerCase());
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

	private void construindoAConsultaIntegradaData() {
		
		controller.mainApp.getAtendimentoData().forEach(atd -> {
			
			Cliente cli = getClientePeloId(atd.getIdCliente());
			if(cli!=null) controller.result.add(new ConsultaIntegradaObject(cli,atd));
			
		});
	}

	private Cliente getClientePeloId(String id) {
		for (Iterator iterator = controller.mainApp.getClienteData().iterator(); iterator.hasNext();) {
			Cliente cli = (Cliente) iterator.next();
			if(cli.getIdCliente().equalsIgnoreCase(id))
				return cli;
		}
		return null;
	}
	
	void consultarConsultaIntegradaBuscaSimples(String termoBase) {		
		termoBase = removedora.clean(termoBase);		
		consultarAtendimentoPorNB(termoBase);
		consultarAtendimentoPorNotas(termoBase);
		if (new ValidaCPF().validarCPF(termoBase))
			consultarClientePorCpf(termoBase);
		consultarClientePorNome(termoBase);
		consultarClientePorNotas(termoBase);
	}
	
	private void consultarAtendimentoPorNB(String nb) {
		controller.mainApp.getAtendimentoData().forEach(atd -> {
			if (removedora.clean(atd.getNb().toLowerCase()).contains(nb.toLowerCase())) {
				if (!controller.result.contains(atd))
					controller.result.add(new ConsultaIntegradaObject(getClientePeloId(atd.getIdCliente()), atd));
			}
		});
		// clientesTableView.setItems(result);
	}
	private void consultarAtendimentoPorNotas(String termo) {
		controller.mainApp.getAtendimentoData().forEach(atd -> {
			if (removedora.clean(atd.getNotasSobreAtendimento().toLowerCase()).contains(termo.toLowerCase())) {
				if (!controller.result.contains(atd))
					controller.result.add(new ConsultaIntegradaObject(getClientePeloId(atd.getIdCliente()), atd));
			}
		});
		// clientesTableView.setItems(result);
	}
	
	private void consultarClientePorNome(String nome) {
		controller.mainApp.getClienteData().forEach(cli -> {
			if (removedora.clean(cli.getNome().toLowerCase()).contains(nome.toLowerCase())) {
				if (!controller.result.contains(cli))
					controller.result.add(new ConsultaIntegradaObject(cli));
			}
		});

	}

	private void consultarClientePorCpf(String termo) {
		controller.mainApp.getClienteData().forEach(cli -> {
			if (cli.getCpf().equals(termo))
				if (!controller.result.contains(cli))
					controller.result.add(new ConsultaIntegradaObject(cli));
		});

	}

	private void consultarClientePorNotas(String nome) {
		controller.mainApp.getClienteData().forEach(cli -> {
			if (removedora.clean(cli.getNotasSobreCLiente().toLowerCase()).contains(nome.toLowerCase())) {
				if (!controller.result.contains(cli))
					controller.result.add(new ConsultaIntegradaObject(cli));
			}
		});

	}

}

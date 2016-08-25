package com.michel1985.wedoffv3.view;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.model.Atendimento;
import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.model.PesquisaIntegradaObject;
import com.michel1985.wedoffv3.util.RemoveCaracteresEspeciais;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PesquisaIntegradaSearch {
	
	MainApp mainApp;
	ClienteSearch cliSearch;
	AtendimentoSearch atdSearch;
	PesquisaIntegradaOverviewController controller;
	RemoveCaracteresEspeciais removedora;
	ObservableList<PesquisaIntegradaObject> pioResult;
	
	public PesquisaIntegradaSearch(MainApp mainApp,PesquisaIntegradaOverviewController controller){
		this.mainApp = mainApp;
		this.removedora = new RemoveCaracteresEspeciais();
		this.pioResult = controller.resultFull;
		this.controller = controller;
	}
	
	public ObservableList<PesquisaIntegradaObject> buscaAvancada(String termoBase){
		
		System.out.println("Aqui chegou "+termoBase);
		termoBase = removedora.clean(termoBase);
		
		System.out.println("Removidos os acentos, ficou: "+termoBase);
		
		termoBase = termoBase.replaceAll("[+]", "+");
		
		System.out.println("Trocado o mais, ficou: "+termoBase);

		String[] termos = termoBase.split("[+]");
		
		System.out.println("Dividido, ficou: ");
		for (int i = 0; i < termos.length; i++) {
			System.out.println(termos[i]);
		}
		
				
		//Agora vamos filtrar
		ObservableList<PesquisaIntegradaObject> list = FXCollections.observableArrayList();
		for(PesquisaIntegradaObject pio :pioResult){
			if(isPesquisaIntegradaObjectTemTODOSosTemos(pio, termos)) list.add(pio);
		}
		pioResult = list;
		return pioResult;
	}
	
	public ObservableList<PesquisaIntegradaObject> removeDuplicidade(){
		ObservableList<PesquisaIntegradaObject> list = FXCollections.observableArrayList();
		for(PesquisaIntegradaObject pio : this.pioResult){
			if(!list.contains(pio))
				list.add(pio);
		}
		
		return list;
	}
	
	public boolean isPesquisaIntegradaObjectTemTODOSosTemos(PesquisaIntegradaObject pio, String[] termos){
		
		for (int i = 0; i < termos.length; i++) {
			if(!isPesquisaIntegradaObjectTemTermo(pio, termos[i]))
				return false;
		}
		return true;
		
	}
	
	public boolean isPesquisaIntegradaObjectTemTermo(PesquisaIntegradaObject pio, String termo){
		
		if (isNbTemTermo(pio.getAtd(), termo)) return true;
		else if(isNotasTemTermo(pio.getAtd(), termo)) return true;
		if(pio.getCliente()!=null){
			if(isCpfTemTermo(pio.getCliente(), termo)) return true;
			else if(isNomeTemTermo(pio.getCliente(), termo)) return true;
			else if(isNotasSobreClienteTemTermo(pio.getCliente(), termo)) return true;		
		}
		
		return false;
	}

	public Cliente getClientePeloId(ObservableList<Cliente> list, String id){
		for (Cliente cliente : list) {
			if(cliente.getIdCliente().equalsIgnoreCase(id))
				return cliente;
		}
		return null;
	}
	
	public boolean isInThePesquisaIntegradaList(String id){
		boolean tem = false;
		for (PesquisaIntegradaObject pio : pioResult) {
			if(pio.getCliente().getIdCliente().equalsIgnoreCase(id))
				tem = true;
		}
				
		return tem;
	}
	
	public ObservableList<Atendimento> getAllAtendimentosDeUmCliente(String idCli){
		ObservableList<Atendimento> atdList =  FXCollections.observableArrayList();
		for(Atendimento atd : this.mainApp.getAtendimentoData()){
			if(atd.getIdCliente().equalsIgnoreCase(idCli))
				atdList.add(atd);
		}
		return atdList;
	}
	
		
	public boolean isNbTemTermo(Atendimento atd, String termo){
		return removedora.clean(atd.getNb().toLowerCase()).contains(termo.toLowerCase());
	}
	public boolean isNotasTemTermo(Atendimento atd, String termo){
		return removedora.clean(atd.getNotasSobreAtendimento().toLowerCase()).contains(termo.toLowerCase());
	}
	
	public boolean isNomeTemTermo(Cliente cliente, String termo) {
		return removedora.clean(cliente.getNome().toLowerCase()).contains(termo.toLowerCase());
	}

	public boolean isNotasSobreClienteTemTermo(Cliente cliente, String termo) {
		return removedora.clean(cliente.getNotasSobreCLiente().toLowerCase()).contains(termo.toLowerCase());
	}

	public boolean isCpfTemTermo(Cliente cliente, String termo) {
		
		if(cliente.getCpf()!=null)
			return cliente.getCpf().contains(termo);
		return false;
	}
	
	

}

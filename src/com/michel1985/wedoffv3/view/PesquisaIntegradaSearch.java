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
	HistoricoDeClientesOverviewController histCliController;
	HistoricoDeAtendimentosOverviewController histAtdController;
	RemoveCaracteresEspeciais removedora;
	ObservableList<PesquisaIntegradaObject> pioResult;
	
	public PesquisaIntegradaSearch(MainApp mainApp){
		this.mainApp = mainApp;
		this.histCliController = new HistoricoDeClientesOverviewController();
		this.histAtdController = new HistoricoDeAtendimentosOverviewController();
		this.cliSearch = new ClienteSearch(histCliController);
		this.atdSearch = new AtendimentoSearch(histAtdController);
		
		this.pioResult = FXCollections.observableArrayList();
	}
	
	public ObservableList<PesquisaIntegradaObject> buscaAvancada(String termoBase){
		
		
		termoBase = removedora.clean(termoBase);
		termoBase = termoBase.replaceAll("[+]", "+");

		String[] termos = termoBase.split("[+]");
		
		buscaSimplesEConstrucaoDaLista(termos);
		
		this.pioResult = removeDuplicidade();		
		
		for(PesquisaIntegradaObject pio : this.pioResult){
			//fazer a pesquisa avancada aqui
			if(!isPesquisaIntegradaObjectTemTODOSosTemos(pio,termos)){
				this.pioResult.remove(pio);
			}			
		}
		
		return null;
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
		
		if (this.atdSearch.isNbTemTermo(pio.getAtd(), termo)) return true;
		else if(this.atdSearch.isNotasTemTermo(pio.getAtd(), termo)) return true;
		else if(this.cliSearch.isCpfTemTermo(pio.getCliente(), termo)) return true;
		else if(this.cliSearch.isNomeTemTermo(pio.getCliente(), termo)) return true;
		else if(this.cliSearch.isNotasSobreClienteTemTermo(pio.getCliente(), termo)) return true;
				
		return false;
	}

	public void buscaSimplesEConstrucaoDaLista(String[] termos) {
		//pesquisando cada um dos termos isoladamente
		for (int i = 0; i < termos.length; i++) {
			this.cliSearch.consultarClienteBuscaSimples(termos[i]);
			this.atdSearch.consultarAtendimentoBuscaSimples(termos[i]);
		}
		
		
		//Montando a primeira lista bruta com todos os atendimentos e seus clientes
		this.atdSearch.controller.result.forEach(atd->{
			pioResult.add(new PesquisaIntegradaObject(getClientePeloId(this.mainApp.getClienteData(), atd.getIdCliente())));
		});
		
		
		//Verificando se há algum cliente que tem algum dos termos mas que não está na lista já levantada
		//E ADICIONANDO TODOS OS ATENDIMENTOS DESSE CLIENTE NA LISTA 
		for(Cliente cli : this.histCliController.result){
			if(!isInThePesquisaIntegradaList(cli.getIdCliente())){
				for(Atendimento atd : getAllAtendimentosDeUmCliente(cli.getIdCliente())){
					pioResult.add(new PesquisaIntegradaObject(getClientePeloId(this.mainApp.getClienteData(), cli.getIdCliente()),atd));
				}
			}
				
		}
		
		//Essa lista resultado é composta por todos os atendimentos que possam estar relacionados mas ainda não passou de fato por uma filtragem avançada,
		//somente pela filtragem simples e construção da lista
		
		//return this.pioResult;
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
	
	

}

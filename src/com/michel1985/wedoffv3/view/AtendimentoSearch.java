package com.michel1985.wedoffv3.view;

import com.michel1985.wedoffv3.model.Atendimento;
import com.michel1985.wedoffv3.util.RemoveCaracteresEspeciais;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AtendimentoSearch {

	HistoricoDeAtendimentosOverviewController controller;
	RemoveCaracteresEspeciais removedora;
	
	public AtendimentoSearch(HistoricoDeAtendimentosOverviewController controller){
		this.controller = controller;
		this.removedora = new RemoveCaracteresEspeciais();
	}
	
	void consultarAtendimentoBuscaAvancada(String termoBase) {
		
		controller.result.addAll(controller.mainApp.getAtendimentoData());
		
		termoBase = removedora.clean(termoBase);
		
		termoBase = termoBase.replaceAll("[+]", "+");
		String[] termos = termoBase.split("[+]");		
		
		for (int i = 0; i < termos.length; i++) {
			RealizandoBuscaAvancadaDoTermo(termos[i].trim());
		}
		
	}

	private void RealizandoBuscaAvancadaDoTermo(String termo) {
		
		ObservableList<Atendimento> busca = FXCollections.observableArrayList();
		
		controller.result.forEach(atd ->{
			 if(isNbTemTermo(atd, termo)) busca.add(atd);
			 else if(isNotasTemTermo(atd, termo)) busca.add(atd);			 			 
		});
		controller.result = busca;
	}
	
	public boolean isNbTemTermo(Atendimento atd, String termo){
		return removedora.clean(atd.getNb().toLowerCase()).contains(termo.toLowerCase());
	}
	public boolean isNotasTemTermo(Atendimento atd, String termo){
		return removedora.clean(atd.getNotasSobreAtendimento().toLowerCase()).contains(termo.toLowerCase());
	}

	void consultarAtendimentoBuscaSimples(String termoBase) {		
		termoBase = removedora.clean(termoBase);		
		consultarAtendimentoPorNB(termoBase);
		consultarAtendimentoPorNotas(termoBase);		
	}
	
	private void consultarAtendimentoPorNB(String nb) {
		controller.mainApp.getAtendimentoData().forEach(atd -> {
			if (removedora.clean(atd.getNb().toLowerCase()).contains(nb.toLowerCase())) {
				if (!controller.result.contains(atd))
					controller.result.add(atd);
			}
		});
		// clientesTableView.setItems(result);
	}
	private void consultarAtendimentoPorNotas(String termo) {
		controller.mainApp.getAtendimentoData().forEach(atd -> {
			if (removedora.clean(atd.getNotasSobreAtendimento().toLowerCase()).contains(termo.toLowerCase())) {
				if (!controller.result.contains(atd))
					controller.result.add(atd);
			}
		});
		// clientesTableView.setItems(result);
	}
}

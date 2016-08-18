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
		
		controller.OLAtendimentos.addAll(controller.mainApp.getAtendimentoData());
		
		termoBase = removedora.clean(termoBase);
		
		termoBase = termoBase.replaceAll("[+]", "+");
		String[] termos = termoBase.split("[+]");		
		
		for (int i = 0; i < termos.length; i++) {
			RealizandoBuscaAvancadaDoTermo(termos[i].trim());
		}
		
	}

	private void RealizandoBuscaAvancadaDoTermo(String termo) {
		
		ObservableList<Atendimento> busca = FXCollections.observableArrayList();
		
		controller.OLAtendimentos.forEach(atd ->{
			 if(isNbTemTermo(atd, termo)) busca.add(atd);
			 else if(isNotasTemTermo(atd, termo)) busca.add(atd);			 			 
		});
		controller.OLAtendimentos = busca;
	}
	
	private boolean isNbTemTermo(Atendimento atd, String termo){
		return removedora.clean(atd.getNb().toLowerCase()).contains(termo.toLowerCase());
	}
	private boolean isNotasTemTermo(Atendimento atd, String termo){
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
				if (!controller.OLAtendimentos.contains(atd))
					controller.OLAtendimentos.add(atd);
			}
		});
		// clientesTableView.setItems(result);
	}
	private void consultarAtendimentoPorNotas(String termo) {
		controller.mainApp.getAtendimentoData().forEach(atd -> {
			if (removedora.clean(atd.getNotasSobreAtendimento().toLowerCase()).contains(termo.toLowerCase())) {
				if (!controller.OLAtendimentos.contains(atd))
					controller.OLAtendimentos.add(atd);
			}
		});
		// clientesTableView.setItems(result);
	}
}

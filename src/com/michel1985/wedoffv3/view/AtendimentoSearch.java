package com.michel1985.wedoffv3.view;

import com.michel1985.wedoffv3.model.Atendimento;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AtendimentoSearch {

	HistoricoDeAtendimentosOverviewController controller;
	
	public AtendimentoSearch(HistoricoDeAtendimentosOverviewController controller){
		this.controller = controller;
	}
	
	void consultarAtendimentoBuscaAvancada(String termo) {
		System.out.println("buscando "+termo);
		ObservableList<Atendimento> busca = FXCollections.observableArrayList();
				
		controller.OLAtendimentos.forEach(atd ->{
			 if(isNbTemTermo(atd, termo)) busca.add(atd);
			 else if(isNotasTemTermo(atd, termo)) busca.add(atd);
			 			 
		});
		controller.OLAtendimentos = busca;
	}
	
	private boolean isNbTemTermo(Atendimento atd, String termo){
		return atd.getNb().toLowerCase().contains(termo.toLowerCase());
	}
	private boolean isNotasTemTermo(Atendimento atd, String termo){
		return atd.getNotasSobreAtendimento().toLowerCase().contains(termo.toLowerCase());
	}

	void buscaSimples(String termoBase) {
		//O termo base pode ser um protocolo, por exemplo, então não filtrar só dados numéricos
		consultarAtendimentoPorNB(termoBase);
		consultarAtendimentoPorNotas(termoBase);
		controller.atendimentosTableView.setItems(controller.OLAtendimentos);
	}
	
	private void consultarAtendimentoPorNB(String nb) {
		controller.mainApp.getAtendimentoData().forEach(atd -> {
			if (atd.getNb().toLowerCase().contains(nb.toLowerCase())) {
				if (!controller.OLAtendimentos.contains(atd))
					controller.OLAtendimentos.add(atd);
			}
		});
		// clientesTableView.setItems(result);
	}
	private void consultarAtendimentoPorNotas(String termo) {
		controller.mainApp.getAtendimentoData().forEach(atd -> {
			if (atd.getNotasSobreAtendimento().toLowerCase().contains(termo.toLowerCase())) {
				if (!controller.OLAtendimentos.contains(atd))
					controller.OLAtendimentos.add(atd);
			}
		});
		// clientesTableView.setItems(result);
	}
}

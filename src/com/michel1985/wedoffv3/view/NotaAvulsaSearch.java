package com.michel1985.wedoffv3.view;

import com.michel1985.wedoffv3.model.NotaAvulsa;
import com.michel1985.wedoffv3.util.RemoveCaracteresEspeciais;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NotaAvulsaSearch {

	HistoricoDeNotasAvulsasOverviewController controller;
	RemoveCaracteresEspeciais removedora;

	public NotaAvulsaSearch(HistoricoDeNotasAvulsasOverviewController controller) {
		this.controller = controller;
		this.removedora = new RemoveCaracteresEspeciais();
	}

	public void consultarNotaAvulsaBuscaAvancada(String termoBase) {

		termoBase = removedora.clean(termoBase);

		controller.result.addAll(controller.mainApp.getNotaAvulsaData());

		termoBase = termoBase.replaceAll("[+]", "+");

		String[] termos = termoBase.split("[+]");

		for (int i = 0; i < termos.length; i++) {
			realizandoBuscaAvancadaDoTermo(termos[i].trim());
		}

	}

	private void realizandoBuscaAvancadaDoTermo(String termo) {
		ObservableList<NotaAvulsa> busca = FXCollections.observableArrayList();

		controller.result.forEach(nota -> {
			if (isTituloTemTermo(nota, termo))
				busca.add(nota);
			else if (isLinkTemTermo(nota, termo))
				busca.add(nota);
			else if (isDescricaoTemTermo(nota,termo))
				busca.add(nota);
		});
		controller.result = busca;
	}

	private boolean isTituloTemTermo(NotaAvulsa notaAvulsa, String termo) {
		return removedora.clean(notaAvulsa.getTitulo().toLowerCase()).contains(termo.toLowerCase());
	}

	private boolean isLinkTemTermo(NotaAvulsa notaAvulsa, String termo) {
		return removedora.clean(notaAvulsa.getLink().toLowerCase()).contains(termo.toLowerCase());
	}

	private boolean isDescricaoTemTermo(NotaAvulsa notaAvulsa, String termo) {
		return removedora.clean(notaAvulsa.getDescricao()).contains(termo);
	}

	public void consultarNotaAvulsaBuscaSimples(String termoBase) {
		termoBase = removedora.clean(termoBase);

		
		consultarNotaAvulsaPorTitulo(termoBase);
		consultarNotaAvulsaPorLink(termoBase);
		consultarNotaAvulsaPorDescricao(termoBase);

	}
	
	
	//Os três métodos abaixo parecem ser facilmente implementados em um único método.
	//Refatorar

	private void consultarNotaAvulsaPorTitulo(String termo) {
		controller.mainApp.getNotaAvulsaData().forEach(nota -> {
			if (removedora.clean(nota.getTitulo().toLowerCase()).contains(termo.toLowerCase())) {
				if (!controller.result.contains(nota))
					controller.result.add(nota);
			}
		});

	}
	
	private void consultarNotaAvulsaPorLink(String termo) {
		controller.mainApp.getNotaAvulsaData().forEach(nota -> {
			if (removedora.clean(nota.getLink().toLowerCase()).contains(termo.toLowerCase())) {
				if (!controller.result.contains(nota))
					controller.result.add(nota);
			}
		});

	}
	
	private void consultarNotaAvulsaPorDescricao(String termo) {
		controller.mainApp.getNotaAvulsaData().forEach(nota -> {
			if (removedora.clean(nota.getDescricao().toLowerCase()).contains(termo.toLowerCase())) {
				if (!controller.result.contains(nota))
					controller.result.add(nota);
			}
		});

	}



}

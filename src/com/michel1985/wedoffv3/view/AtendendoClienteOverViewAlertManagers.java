package com.michel1985.wedoffv3.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AtendendoClienteOverViewAlertManagers {
	
	static void alertaClienteAtualDesconhecido() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Quem estamos atendendo?");
		alert.setHeaderText("Por favor, primeiro registre o cliente");
		alert.setContentText(
				"Informe o CPF, Nome e Notas sobre Cliente e ent�o click no bot�o \"Gravar Cliente\"");
		alert.showAndWait();
	}
	
	static void alertaDataDoAtendimentoNaoInformada() {
		alertarWarning("Data do Atendimento?", "O cliente est� sendo atendido hoje?",
				"Favor informar a data do atendimento.");
	}

	static void alertDataDeSolucaoNaoInformada() {
		alertarWarning("Data de Solu��o?", "Quando este atendimento deve ser conclu�do?",
				"Voc� informou que o atendimento ficou pendente.\nQuando ele deve ser concluido?");
	}

	static void alertaNotasSobreAtendimentoNaoInformada() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Contradi��o...");
		alert.setHeaderText("O que foi feito neste atendimento?");
		alert.setContentText(
				"Se n�o � para registrar dados sobre atendimentos realizados, para que serve esta aplica��o?\nInforme no campo \"Notas\" o que foi feito neste atendimento.");
		alert.showAndWait();
	}
	
	static void alertaDeCPFInvalido() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("");
		alert.setHeaderText("CPF inv�lido!");
		alert.setContentText("Por favor, verifique o CPF digitado e tente novamente");
		alert.showAndWait();
	}	
	
	static void alertaDeNomeInvalido() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Nome inv�lido!");
		alert.setHeaderText("Nome do Cliente inv�lido!");
		alert.setContentText("Por favor, verifique o NOME digitado e tente novamente");
		alert.showAndWait();
	}

	static void alertarWarning(String title, String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

}

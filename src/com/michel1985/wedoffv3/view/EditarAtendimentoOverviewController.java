package com.michel1985.wedoffv3.view;

import java.time.LocalDate;

import com.michel1985.wedoffv3.model.Atendimento;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class EditarAtendimentoOverviewController {

    @FXML
    private Label tituloAtendimentoLabel;

    @FXML
    private TextField nbTextField;

    @FXML
    private DatePicker dataAtendimentoDatePicker;

    @FXML
    private CheckBox isPendenteCheckBox;

    @FXML
    private DatePicker dataSolucaoDatePicker;

    @FXML
    private TextArea notasSobreAtendimentoTextArea;

    @FXML
    private CheckBox isAgendamentoCheckBox;

    @FXML
    private Button salvarButton;

    @FXML
    private Button cancelarButton;
    
    private Stage dialogStage;
	private Atendimento atendimento;
	private boolean okClicked = false;
	
	
	/**
	 * Inicializa a classe controlle. Este método é chamado automaticamente após
	 * o arquivo fxml ter sido carregado.
	 */
	@FXML
	private void initialize() {
	}

	/**
	 * Define o palco deste dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	/**
	 * Define o cliente a ser editada
	 */
	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;

		// Povoando o atendimento nos campos do form
		nbTextField.setText(atendimento.getNb());
		dataAtendimentoDatePicker.setValue(LocalDate.parse(atendimento.getDataAtendimento()));
		isPendenteCheckBox.setSelected(atendimento.getIsPendente());
		if(isPendenteCheckBox.isSelected()) dataSolucaoDatePicker.setValue(LocalDate.parse(atendimento.getDataSolucao()));
		notasSobreAtendimentoTextArea.setText(atendimento.getNotasSobreAtendimento());
		isAgendamentoCheckBox.setSelected(atendimento.getIsAgendamento());		
	}
	
	/**
	 * Retorna true se o ok for clicado
	 */
	public boolean isOkCLicked() {
		return okClicked;
	}
	
	/**
	 * Chamado quando o usuário clica ok
	 */
	@FXML
	private void handleOk() {
		String nb = nbTextField.getText();
		String notas = notasSobreAtendimentoTextArea.getText();
		boolean isPendente = isPendenteCheckBox.isSelected();
		boolean isAgendamento = isAgendamentoCheckBox.isSelected();
		
		// Se o atendimento estiver pendente, será obrigatório informar uma
					// data de solução
					String dataSolucao = "";
					if (isPendente) {
						try {
							if (dataSolucaoDatePicker.getValue() == null
									|| dataSolucaoDatePicker.getValue().toString().equalsIgnoreCase("")) {
								alertarWarning("Data de Solução?", "Quando este atendimento deve ser concluído?",
										"Você informou que o atendimento ficou pendente.\nQuando ele deve ser concluido?");
								return;
							} else {
								dataSolucao = dataSolucaoDatePicker.getValue().toString();
							}

						} catch (Exception e) {
							alertarWarning("Data de Solução?", "Data inválida. Quando este atendimento deve ser concluído?",
									"Você informou que o atendimento ficou pendente.\nQuando ele deve ser concluido?");
							return;
						}

					}

					// A data do atendimento séra sempre obrigatória
					String dataAtendimento = "";
					try {
						if (dataAtendimentoDatePicker.getValue() == null
								|| dataAtendimentoDatePicker.getValue().toString().equalsIgnoreCase("")) {
							alertarWarning("Data do Atendimento?", "O cliente está sendo atendido hoje?",
									"Favor informar a data do atendimento.");
							return;
						} else {
							dataAtendimento = dataAtendimentoDatePicker.getValue().toString();
						}
					} catch (Exception e) {
						return;
					}
		
		okClicked = true;
		
		if(notas=="" || notas == null || notas.length()<2){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Contradição...");
			alert.setHeaderText("O que foi feito neste atendimento?");
			alert.setContentText(
					"Se não é para registrar dados sobre atendimentos realizados, para que serve esta aplicação?\nInforme no campo \"Notas\" o que foi feito neste atendimento.");
			alert.showAndWait();
			return;
		}else{
			atendimento.setDataAtendimento(dataAtendimento);
			atendimento.setNotasSobreAtendimento(notas);
			atendimento.setNb(nb);
			atendimento.setIsAgendamento(isAgendamento);
			atendimento.setIsPendente(isPendente);
			if(isPendente) 	atendimento.setDataSolucao(dataSolucao);
			
			
			dialogStage.close();
		}

	}
	
	/**
     * Chamado quando o usuário clica Cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    //Métudo usado em alguns pontos para facilitar a exibição de alerts
    private void alertarWarning(String title, String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

}

package com.michel1985.wedoffv3.view;

import com.michel1985.wedoffv3.model.NotaAvulsa;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditarNotaAvulsaOverviewController {

	@FXML
	private Label tituloNotaAvulsaLabel;

	@FXML
	private TextField tituloDaNotaTextField;

	@FXML
	private TextField linkTextField;

	@FXML
	private TextArea descricaoDaNotaTextArea;

	@FXML
	private Button salvarButton;

	@FXML
	private Button cancelarButton;

	private Stage dialogStage;
	private NotaAvulsa notaAvulsa;
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
	public void setNotaAvulsa(NotaAvulsa notaAvulsa) {
		this.notaAvulsa = notaAvulsa;

		// Povoando o cliente nos campos do form
		tituloDaNotaTextField.setText(notaAvulsa.getTitulo());
		linkTextField.setText(notaAvulsa.getLink());
		descricaoDaNotaTextArea.setText(notaAvulsa.getDescricao());
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
		String titulo = tituloDaNotaTextField.getText();
		String link = linkTextField.getText();
		String descricao = descricaoDaNotaTextArea.getText();
		okClicked = true;

		notaAvulsa.setTitulo(titulo);
		notaAvulsa.setLink(link);
		notaAvulsa.setDescricacao(descricao);
		dialogStage.close();

	}

	/**
	 * Chamado quando o usuário clica Cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	
	

}

package com.michel1985.wedoffv3.view;

import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.util.ValidaCliente;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditarClienteOverviewController {

	@FXML
	private Label tituloClienteLabel;

	@FXML
	private TextField nomeClienteTextField;

	@FXML
	private TextField cpfClienteTextField;

	@FXML
	private TextArea notasSobreClienteTextArea;

	@FXML
	private Button salvarButton;

	@FXML
	private Button cancelarButton;

	private Stage dialogStage;
	private Cliente cliente;
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
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;

		// Povoando o cliente nos campos do form
		nomeClienteTextField.setText(cliente.getNome());
		cpfClienteTextField.setText(cliente.getCpf());
		notasSobreClienteTextArea.setText(cliente.getNotasSobreCLiente());
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
		String nome = nomeClienteTextField.getText();
		String cpf = cpfClienteTextField.getText();
		String notas = notasSobreClienteTextArea.getText();
		okClicked = true;

		if (ValidaCliente.validaCPF(cpf)) {
			if (ValidaCliente.validaNome(nome)) {
				cliente.setCpf(cpf);
				cliente.setNome(nome);
				cliente.setNotasSobreCLiente(notas);
				dialogStage.close();
			}else{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Nome Inválido");
				alert.setHeaderText("Por favor, corrija o Nome informado");
				alert.setContentText("Nome inválido!");
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("CPF Inválido");
			alert.setHeaderText("Por favor, corrija o CPF informado");
			alert.setContentText("CPF inválido!");
			alert.showAndWait();
		}
	}
	
	/**
     * Chamado quando o usuário clica Cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

}

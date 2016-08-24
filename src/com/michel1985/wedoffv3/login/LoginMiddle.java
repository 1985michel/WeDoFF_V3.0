package com.michel1985.wedoffv3.login;

import java.io.File;

import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.crud.DBFactory;
import com.michel1985.wedoffv3.model.Usuario;
import com.michel1985.wedoffv3.view.LoginOverviewController;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginMiddle {

	private LoginOverviewController controller;

	public LoginMiddle(LoginOverviewController controller) {
		this.controller = controller;
	}

	public void logar(String login, String senha) {

		LoginOverviewController.usuarioAtivo = new Usuario(login, senha);

		try {
			Logadora logadora = new Logadora();
			if (isUserNameInUse(login)) {
				logou(logadora.logarCriptografado(login, senha));

			} else {
				logou(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void cadastrarUsuario(String login, String senha) {
		if (isUserNameInUse(login)) {
			System.out.println("Usuário " + login + " já cadastrado");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erro ao Cadastrar");
			alert.setHeaderText("Usuário já existente");
			alert.setContentText("O nome de usuário escolhido já está em uso.\n"
					+ "Escolha outro nome de usuário ou ( caso o login lhe pertença ) click em 'Logar'");

			alert.showAndWait();
			return;
		}

		System.out.println("Novo Usuário... criando ");

		DBFactory fabrica = new DBFactory();
		try {
			fabrica.criarBancos(new CRUD(new Usuario(login, senha)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		try {
			Logadora logadora = new Logadora();
			if (logadora.cadastrarUsuario(login, senha)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Usuário Cadastrado!");
				alert.setHeaderText("Bem vindo!");
				alert.setContentText("Parabéns! Você nunca mais se esquecerá de um atendimento novamente.");

				alert.showAndWait();

				this.logar(login, senha);
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Ops...");
				alert.setHeaderText("Algo deu errado no seu cadastro...");
				alert.setContentText("Por favor, feche a aplicação e tente novamente com outro login");

				alert.showAndWait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isUserNameInUse(String login) {
		login = login + ".tmp";
		File diretorio = new File("C:/Program Files/wedoffSecurity/hsqldb-2.3.3/hsqldb/db/" + login);
		if (!diretorio.exists()) {
			return false;
		} else {
			return true;
		}
	}

	private void logou(boolean resultado) {
		
		//System.out.println("Logado: " + resultado);
		if (!resultado) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Acesso Negado!");
			alert.setHeaderText("Login e/ou Senha não confirmado");
			alert.setContentText("Por favor, verifique os dados e tente novamente.");

			alert.showAndWait();

		} else if (resultado) {
			controller.mostraGif();
			controller.loginConfirmado();			
		}
	}

}

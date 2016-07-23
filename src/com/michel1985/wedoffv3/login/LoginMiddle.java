package com.michel1985.wedoffv3.login;

import java.io.File;

import com.michel1985.wedoffv3.model.Usuario;
import com.michel1985.wedoffv3.view.LoginOverviewController;

import crud.CRUD;
import crud.DBFactory;
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
			System.out.println("Usu�rio " + login + " j� cadastrado");
			return;
		}

		System.out.println("Novo Usu�rio... criando ");

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
				alert.setTitle("Usu�rio Cadastrado!");
				alert.setHeaderText("Bem vindo!");
				alert.setContentText("Parab�ns! Voc� nunca mais se esquecer� de um atendimento novamente.");

				alert.showAndWait();

				this.logar(login, senha);
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Ops...");
				alert.setHeaderText("Algo deu errado no seu cadastro...");
				alert.setContentText("Por favor, feche a aplica��o e tente novamente com outro login");

				alert.showAndWait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isUserNameInUse(String login) {
		login = login + ".tmp";
		File diretorio = new File("D:/Program Files/wedoffSecurity/hsqldb-2.3.3/hsqldb/db/" + login);
		if (!diretorio.exists()) {
			return false;
		} else {
			return true;
		}
	}

	private void logou(boolean resultado) {
		System.out.println("Logado: " + resultado);
		if (!resultado) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Acesso Negado!");
			alert.setHeaderText("Login ou Senha n�o confirmado");
			alert.setContentText("Por favor, verifique os dados e tente novamente.");

			alert.showAndWait();

		} else if (resultado) {
			controller.loginConfirmado();
		}
	}

}

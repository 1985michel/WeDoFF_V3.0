package com.michel1985.wedoffv3.login;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;

import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.model.Usuario;
import com.michel1985.wedoffv3.seguranca.Seguranca;

public class Logadora {

	@SuppressWarnings("finally")
	public boolean logar(String login, String senha) {

		boolean logou = false;

		ResultSet resultSet = null;
		try {
			resultSet = new CRUD(new Usuario(login, senha))
					.getResultSet("SELECT * FROM logar where login = '" + login + "'");
			while (resultSet.next()) {

				if (resultSet.getString("login").equalsIgnoreCase(login)) {
					if (resultSet.getString("senha").equalsIgnoreCase(senha)) {
						logou = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return logou;
		}

	}

	@SuppressWarnings("finally")
	public boolean logarCriptografado(String login, String senha) {

		boolean logou = false;
		String senhaCriptografada = "";
		try {
			senhaCriptografada = new Seguranca().getSenhaHash(senha);
		} catch (NoSuchAlgorithmException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}

		ResultSet resultSet = null;
		try {
			resultSet = new CRUD(new Usuario(login, senha))
					.getResultSet("SELECT * FROM USUARIOS where login = '" + login + "'");
			while (resultSet.next()) {

				if (resultSet.getString("login").equalsIgnoreCase(login)) {
					if (resultSet.getString("senha").equalsIgnoreCase(senhaCriptografada)) {
						logou = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(resultSet!=null) resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return logou;
		}

	}

	@SuppressWarnings("finally")
	public boolean cadastrarUsuario(String nome, String senha) {
		ResultSet resultSet = null;
		String senhaCriptografada = "";
		// boolean criouLogin = false;
		if (isUsuarioCadastrado(nome, senha)) {
			System.out.println("Usu�rio j� cadastrado");
			return false;
		}
		try {
			senhaCriptografada = new Seguranca().getSenhaHash(senha);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		try {
			resultSet = new CRUD(new Usuario(nome, senha)).getResultSet(
					"INSERT INTO usuarios (login, senha) VALUES ('" + nome + "','" + senhaCriptografada + "')");

			// criouLogin = resultSet.rowInserted();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("N�o conseguiu registrar novo usu�rio.");

			return false;
		} finally {
			try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Cadastrou usuario " + nome);
			return true;
		}
	}

	@SuppressWarnings("finally")
	public boolean isUsuarioCadastrado(String login, String senha) {

		boolean existe = false;

		ResultSet resultSet = null;
		try {
			resultSet = new CRUD(new Usuario(login, senha))
					.getResultSet("SELECT * FROM USUARIOS where login = '" + login + "'");
			while (resultSet.next()) {

				existe = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return existe;
		}
	}

	

}

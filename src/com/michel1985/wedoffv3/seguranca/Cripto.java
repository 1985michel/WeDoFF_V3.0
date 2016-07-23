package com.michel1985.wedoffv3.seguranca;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Classe responsável pela criptogrifia dos dados
 * */

public class Cripto {

	public String criptografa(String texto, String senha) {

		BasicTextEncryptor bte = new BasicTextEncryptor();

		// inserimos uma senha qualquer
		bte.setPassword(senha);
		String textoCriptografado = bte.encrypt(texto);

		return textoCriptografado;

	}

	public String descriptografa(String texto, String senha) {

		BasicTextEncryptor bte = new BasicTextEncryptor();

		// inserimos uma senha qualquer
		bte.setPassword(senha);
		String textoDescriptografado = bte.decrypt(texto);

		return textoDescriptografado;

	}

}

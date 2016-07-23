package com.michel1985.wedoffv3.seguranca;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Classe responsável pela adiministração do hash senha
 * 
 * */
public class Seguranca {

	public String getSenhaHash(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return senhaHashHexString(senha);
	}

	public byte[] hashSenha(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8"));

		// System.out.println(messageDigest);
		return messageDigest;
	}

	public Object senhaHashString(String senha) throws UnsupportedEncodingException, NoSuchAlgorithmException {

		return new String(hashSenha(senha), "UTF-8");
	}

	public String senhaHashHexString(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		byte messageDigest[] = hashSenha(string);
		StringBuilder hexString = new StringBuilder();
		for (byte b : messageDigest) {
			hexString.append(String.format("%02X", 0xFF & b));
		}
		String senhahex = hexString.toString();
		return senhahex;
	}

}

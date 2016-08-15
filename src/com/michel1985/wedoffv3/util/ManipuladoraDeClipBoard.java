package com.michel1985.wedoffv3.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

import com.michel1985.wedoffv3.model.ObjetoSAT;

public class ManipuladoraDeClipBoard {

	public static String copiaClipBoard() throws UnsupportedFlavorException {

		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// odd: the Object param of getContents is not currently used
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents.getTransferData(DataFlavor.stringFlavor);
			} catch (IOException ex) {
				// System.out.println(ex);
				ex.printStackTrace();
			}
		}
		return result;

	}

	public static String getParteQueInteressa() {
		String copiado = "";
		try {
			copiado = copiaClipBoard();
		} catch (UnsupportedFlavorException e) {
			// 
			e.printStackTrace();
		}
		if (copiado.equalsIgnoreCase(""))
			return "vazio";

		String parteQueInteressa = "";

		try {
			parteQueInteressa = copiado.split("Dados do Cidadão")[1];
		} catch (Exception e) {
			// 
		}
		if (parteQueInteressa.equalsIgnoreCase(""))
			return "vazio";

		System.out.println("PARTE QUE INTERESSA:");
		System.out.println(parteQueInteressa);
		return parteQueInteressa;
	}

	public static String getNome() {
		// 
		String pqi = getParteQueInteressa();
		// System.out.println(pqi);
		String posPalavraCidadao = pqi.split("Cidadão\n")[1];
		// FUNCIONA BEM SEM SUPER
		String retorno = posPalavraCidadao.split("Super")[0];

		retorno = posPalavraCidadao.split("CPF")[0];
		if (retorno.contains("Super")) {
			retorno = retorno.substring(0, retorno.indexOf("Super"));
		}
		retorno = retorno.trim();
		return retorno;
	}

	public static String getCPF() {
		// 
		String pqi = getParteQueInteressa();
		// pqi = pqi.split("Cidadï¿½o\n")[1];
		String cpf = pqi.split("CPF\n")[1].split("\nNIT")[0];
		if (cpf.contains("NIT"))
			cpf = "";
		return retiraCaracteresEspeciais(cpf);
	}

	public static String getNIT() {
		String pqi = getParteQueInteressa();
		// pqi = pqi.split("Cidadï¿½o\n")[1];
		String nit = pqi.split("NIT\n")[1].split("\nData de Nascimento")[0];
		if (nit.contains("Data")) {
			nit = nit.substring(0, nit.indexOf("Data"));
		}
		return nit;
	}

	public static String retiraCaracteresEspeciais(String entrada) {

		char[] charIn = entrada.toCharArray();
		char[] caracteresIndesejados = { '.', ',', '-' };
		boolean aprovado = true;
		String saida = "";
		for (int i = 0; i < charIn.length; i++) {
			aprovado = true;
			for (int j = 0; j < caracteresIndesejados.length; j++) {
				if (charIn[i] == caracteresIndesejados[j])
					aprovado = false;
			}
			if (aprovado)
				saida += charIn[i];
		}

		return saida;
	}

	public static ObjetoSAT getSAT() {

		String nome = "";
		String cpf = "";
		String nit = "";

		try {
			nome = getNome();
			cpf = getCPF();
			nit = getNIT();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (isStringNulaOuVazia(nome))
				nome = "";
			if (isStringNulaOuVazia(cpf))
				cpf = "";
			if (isStringNulaOuVazia(nit))
				nit = "";
		}

	return new ObjetoSAT(nome, cpf, nit, isAgendamento());

	}

	private static boolean isStringNulaOuVazia(String nome) {
		return nome == null || nome.equals("");
	}

	public static boolean isAgendamento() {
		return getParteQueInteressa().contains("DER");
	}

}



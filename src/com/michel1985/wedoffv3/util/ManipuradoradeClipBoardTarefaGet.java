package com.michel1985.wedoffv3.util;

import java.awt.datatransfer.UnsupportedFlavorException;

import com.michel1985.wedoffv3.model.ObjetoSAT;
import com.michel1985.wedoffv3.model.ObjetoTarefaGET;

public class ManipuradoradeClipBoardTarefaGet extends ManipuladoraDeClipBoard {

	
	public ManipuradoradeClipBoardTarefaGet() {
		// TODO Auto-generated constructor stub
	}
	
	public static ObjetoTarefaGET getGET() {

		String protocoloGET = "";
		String servico = "";
		String DER = "";
		String nome = "";
		String CPF = "";
		String nit = "";
		String dataNascimento = "";
		String nomeMae = "";

		try {
			protocoloGET = getProtocoloGET();
			servico = getServico();
			DER = getDER();
			CPF = getCPF();
			nome = getNome();
			dataNascimento = getDataNascimento();
			nomeMae = getNomeMae();
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (isStringNulaOuVazia(protocoloGET))
				protocoloGET = "";
			if (isStringNulaOuVazia(servico))
				servico = "";
			if (isStringNulaOuVazia(DER))
				DER = "";
			if (isStringNulaOuVazia(CPF))
				CPF = "";
			if (isStringNulaOuVazia(nome))
				nome = "";
			if (isStringNulaOuVazia(dataNascimento))
				dataNascimento = "";
			if (isStringNulaOuVazia(nomeMae))
				nomeMae = "";
		}

	return new ObjetoTarefaGET(protocoloGET,servico,DER, CPF,nome,dataNascimento,nomeMae);

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
			parteQueInteressa = copiado.split("Tarefa de ")[1];
		} catch (Exception e) {
			System.out.println("o erro esta na linha 65");
		}
		if (parteQueInteressa.equalsIgnoreCase(""))
			return "vazio";

		//System.out.println("PARTE QUE INTERESSA:");
		//System.out.println(parteQueInteressa);
		return parteQueInteressa;
	}
	
	public static String getProtocoloGET() {		
		String pqi = getParteQueInteressa();
		// System.out.println(pqi);
		String prot = pqi.split(" -")[0];
		// FUNCIONA BEM SEM SUPER
		return prot;
	}
	
	public static String getServico() {
		String pqi = getParteQueInteressa();
		// System.out.println(pqi);
		String servico = pqi.split("Serviço")[1];
		String servico2 = servico.split("Unidade")[0].trim();
		//System.out.println("|"+servico2+"|");
		return servico2;	
	}
	
	public static String getDER() {		
		String pqi = getParteQueInteressa();
		// System.out.println(pqi);
		String der = pqi.split("Data de Criação da Tarefa")[1];
		der = der.substring(0, 11);
		der = der.trim();
		//System.out.println("|"+der+"|");
		return der+" <?>";
		
	}
	
	public static String getCPF(){
		String pqi = getParteQueInteressa();
		String cpf = pqi.split("Interessado")[1];
		cpf = cpf.split("Procurador")[0];
		cpf = cpf.split("Ação")[1].trim();
		cpf = cpf.substring(0, 14);
		cpf = retiraCaracteresEspeciais(cpf);
		//System.out.println("|"+cpf+"|");
		return cpf;
		
	}
	
	public static String getNome() {		
		String pqi = getParteQueInteressa();
		String cpf = pqi.split("Interessado")[1];
		cpf = cpf.split("Procurador")[0];
		cpf = cpf.split("Ação")[1].trim();
		cpf = cpf.substring(0, 14);
		
		String nome = pqi.split(cpf)[1];
		nome = nome.split("/")[0].trim();
		nome = nome.split("	")[0].trim();
				
		//System.out.println("|"+nome+"|");
		return nome;
	}
	
	public static String getDataNascimento() {		
		String pqi = getParteQueInteressa();
		String dn = pqi.split(getNome())[1];
		dn = dn.trim().split("	")[0];
				
		//System.out.println("|"+dn+"|");
		return dn;
	}
	
	public static String getNomeMae() {		
		String pqi = getParteQueInteressa();
		String nm = pqi.split(getDataNascimento())[1];
		nm = nm.trim().split("	")[0];
				
		//System.out.println("|"+nm+"|");
		return nm;
	}

}

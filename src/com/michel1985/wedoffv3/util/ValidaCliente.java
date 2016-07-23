package com.michel1985.wedoffv3.util;

import com.michel1985.wedoffv3.model.Cliente;

public class ValidaCliente {
	

	public static boolean validarCliente(Cliente cliente) {
		boolean resultado = true;
		resultado = validaCPF(cliente.getCpf());
		if(resultado) resultado = validaIdCliente(cliente.getIdCliente());
		if(resultado) resultado = validaNome(cliente.getNome());
		if(resultado) resultado = validaNota(cliente.getNotasSobreCLiente());

		return resultado;
	}

	// Validacao de CPF simplista, apenas considera a quantidade de caracteres
	public static boolean validaCPF(String cpf) {
		//if (cpf.length() == 11) {
		//	return true;
		//}

		//return false;
		ValidaCPF vCPF = new ValidaCPF();
		return vCPF.validarCPF(cpf);
	}

	public static boolean validaNome(String nome) {

		if (nome.length() < 3)
			return false;

		if (nome.matches(".*\\d+.*"))
			return false; // se cont�m n�meros

		return true;
	}

	public static boolean validaIdCliente(String id) {

		if (id == null || id == "")
			return false;

		return true;
	}

	public static boolean validaNota(String notas) {

		if (notas == null || notas == "")
			return false;

		return true;
	}

}


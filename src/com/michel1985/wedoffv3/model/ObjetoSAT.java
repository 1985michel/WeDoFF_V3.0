package com.michel1985.wedoffv3.model;

public class ObjetoSAT {

	String nome;
	String cpf;
	String nit;
	boolean isAgendamento;
	
	
	public ObjetoSAT(String nome, String cpf, String nit, boolean isAgendamento) {
		super();
		this.nome = nome;
		this.cpf = cpf;
		this.nit = nit;
		this.isAgendamento = isAgendamento;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getCpf() {
		return cpf;
	}


	public void setCpf(String cpf) {
		this.cpf = cpf;
	}


	public String getNit() {
		return nit;
	}


	public void setNit(String nit) {
		this.nit = nit;
	}


	public boolean isAgendamento() {
		return isAgendamento;
	}


	public void setAgendamento(boolean isAgendamento) {
		this.isAgendamento = isAgendamento;
	}
	
	
	
}

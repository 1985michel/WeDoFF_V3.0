package com.michel1985.wedoffv3.model;

public class ObjetoTarefaGET {

	
	String protocoloGET;
	String servico;
	String DER;
	String nome;
	String cpf;
	String nit;
	String dataNascimento;
	String nomeMae;
	
	
	
	public ObjetoTarefaGET(String protocoloGet, String servico, String DER, String CPF, String nome, String dataNascimento,
			String nomeMae) {
		super();
		this.protocoloGET = protocoloGet;
		this.servico = servico;
		this.DER = DER;
		this.cpf = CPF;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.nomeMae = nomeMae;
	}
	
	public ObjetoTarefaGET(String protocoloGet, String servico, String DER, String nome, String cpf, String nit,
			String dataNascimento, String nomeMae) {
		super();
		this.protocoloGET = protocoloGet;
		this.servico = servico;
		this.DER = DER;
		this.nome = nome;
		this.cpf = cpf;
		this.nit = nit;
		this.dataNascimento = dataNascimento;
		this.nomeMae = nomeMae;
	}
	
	public String getProtocoloGET() {
		return protocoloGET;
	}
	public void setProtocoloGET(String protocoloGET) {
		this.protocoloGET = protocoloGET;
	}
	public String getServico() {
		return servico;
	}
	public void setServico(String servico) {
		this.servico = servico;
	}
	public String getDER() {
		return DER;
	}
	public void setDER(String dER) {
		DER = dER;
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
	public String getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public String getNomeMae() {
		return nomeMae;
	}
	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}
	
	
	
}

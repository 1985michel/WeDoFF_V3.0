package com.michel1985.wedoffv3.model;

public class Usuario {

	private String nome;
	private String senha;
	public String getNome() {
		return nome;
	}
	public String getSenha() {
		return senha;
	}
	public Usuario(String nome, String senha) {
		super();
		this.nome = nome;
		this.senha = senha;
	}
	
	
}
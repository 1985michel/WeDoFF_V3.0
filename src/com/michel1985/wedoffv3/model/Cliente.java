/**
 * 
 */
package com.michel1985.wedoffv3.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Classe modelo de cliente
 * @author michel
 *
 */
public class Cliente {

	private final StringProperty idCliente;
	private final StringProperty nome;
	private final StringProperty cpf;
	private final StringProperty notasSobreCLiente;
	
	public Cliente(String nome, String cpf, String notas){
		this.idCliente = null;
		this.nome = new SimpleStringProperty(nome);
		this.cpf = new SimpleStringProperty(cpf);
		this.notasSobreCLiente = new SimpleStringProperty(notas);
	}
	
	public Cliente(String id, String nome, String cpf, String notas){
		this.idCliente = new SimpleStringProperty(id);
		this.nome = new SimpleStringProperty(nome);
		this.cpf = new SimpleStringProperty(cpf);
		this.notasSobreCLiente = new SimpleStringProperty(notas);
	}
	
	public Cliente(){
		this(null, null, null);
	}
	
	//Para cada atributo crie um get, um set e um get Property
	public String getIdCliente(){
		return this.idCliente.get();
	}
	
	public void setIdCliente(String id){
		this.idCliente.set(id);
	}
	
	public StringProperty idClienteProperty(){
		return this.idCliente;
	}
	
	public String getNome(){
		return this.nome.get();
	}
	
	public void setNome(String nome){
		this.nome.set(nome);
	}
	
	public StringProperty nomeProperty(){
		return this.nome;
	}
	
	public String getCpf(){
		return this.cpf.get();
	}
	public void setCpf(String cpf){
		this.cpf.set(cpf);
	}
	
	public StringProperty cpfProperty(){
		return this.cpf;
	}
	
	public String getNotasSobreCLiente(){
		return this.notasSobreCLiente.get();
	}
	
	public void setNotasSobreCLiente(String nota){
		this.notasSobreCLiente.set(nota);
	}
	
	public StringProperty notasSobreClienteProperty(){
		return this.notasSobreCLiente;
	}
}

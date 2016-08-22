package com.michel1985.wedoffv3.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NotaAvulsa {
	
	StringProperty idNotaAvulsa;
	StringProperty link;
	StringProperty titulo;
	StringProperty descricacao;
	
	
	
	public NotaAvulsa(String idNotaAvulsa, String link, String titulo, String descricacao) {
		super();
		this.idNotaAvulsa = new SimpleStringProperty(idNotaAvulsa);
		this.link = new SimpleStringProperty(link);
		this.titulo = new SimpleStringProperty(titulo);
		this.descricacao = new SimpleStringProperty(descricacao);
		
	} 
	
	public NotaAvulsa(String link, String titulo, String descricacao) {
		super();
		this.link = new SimpleStringProperty(link);
		this.titulo = new SimpleStringProperty(titulo);
		this.descricacao = new SimpleStringProperty(descricacao);
	}
	
	public NotaAvulsa(){};
	
	
	
	
	public String getIdNotaAvulsa (){
		return this.idNotaAvulsa.get();
	}
	public StringProperty idNotaAvulsaProperty() {
		return idNotaAvulsa;
	}
	public void setIdNotaAvulsa(String idNotaAvulsa) {
		this.idNotaAvulsa.set(idNotaAvulsa);
	}
	

	public String getLink(){
		return this.link.get();
	}
	public StringProperty linkProperty() {
		return link;
	}
	public void setLink(String link) {
		this.link.set(link);;
	}
	
	
	public String getTitulo(){
		return this.titulo.get();
	}
	public StringProperty tituloProperty() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo.set(titulo);
	}
	
	
	public String getDescricao(){
		return this.descricacao.get();
	}
	public StringProperty descricacaoProperty() {
		return descricacao;
	}
	public void setDescricacao(String descricacao) {
		this.descricacao.set(descricacao);
	}
	
	
	

}

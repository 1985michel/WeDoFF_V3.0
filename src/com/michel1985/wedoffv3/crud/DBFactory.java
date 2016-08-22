package com.michel1985.wedoffv3.crud;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.michel1985.wedoffv3.exceptions.CRUDException;

public class DBFactory {

	public List<String> comandosDeCriacao = new ArrayList<>();

	public DBFactory() {
		// String criarTabelaUsuarios = "CREATE TABLE USUARIOS (" + "id INTEGER
		// IDENTITY PRIMARY KEY,"
		// + "LOGIN VARCHAR(50)," + "senha VARCHAR(100)," + ");";

		String criarTabelaUsuarios = "CREATE TABLE USUARIOS (" + "id INTEGER IDENTITY PRIMARY KEY,"
				+ "LOGIN VARCHAR(50)," + "senha VARCHAR(100)," + ");";
		this.comandosDeCriacao.add(criarTabelaUsuarios);
		// nomeCliente, cpfCliente, notasSobreCliente

		this.comandosDeCriacao.add(criarTabelaUsuarios);

		
		String criarTabelaClientes = "CREATE TABLE CLIENTES (" + "idcliente INTEGER IDENTITY PRIMARY KEY,"
				+ "nomeCliente VARCHAR(300)," + "cpfCliente VARCHAR(100)," + "notasSobreCliente VARCHAR(50000)," + ");";

		this.comandosDeCriacao.add(criarTabelaClientes);

		String criarTabelaAtendimentos = "CREATE TABLE ATENDIMENTOS (" + "idatendimento INTEGER IDENTITY PRIMARY KEY,"
				+ "idcliente VARCHAR(100)," + "isagendamento BOOLEAN," + "ispendente BOOLEAN," + "nb VARCHAR(100),"
				+ "notassobreatendimento VARCHAR(50000)," + "dataatendimento VARCHAR(10)," + "datasolucao VARCHAR(10),"
				+ ");";
	
		this.comandosDeCriacao.add(criarTabelaAtendimentos);
		
		String criarTabelaNotasAvulsas = "CREATE TABLE NOTASAVULSAS (" + "idNotaAvulsa INTEGER IDENTITY PRIMARY KEY,"
				+ "TITULO VARCHAR(1000)," + "LINK VARCHAR(300)," + "DESCRICAO VARCHAR(50000)," + ");";

		this.comandosDeCriacao.add(criarTabelaNotasAvulsas);
	}
	
	public void criarTabelaNotasAvulsas(CRUD crud) throws ClassNotFoundException, SQLException, CRUDException{
		String criarTabelaNotasAvulsas = "CREATE TABLE NOTASAVULSAS (" + "idNotaAvulsa INTEGER IDENTITY PRIMARY KEY,"
				+ "TITULO VARCHAR(1000)," 
				+ "LINK VARCHAR(300)," + "DESCRICAO VARCHAR(50000)," + ");";
		
		crud.getResultSet(criarTabelaNotasAvulsas);
	}

	public boolean criarBancos(CRUD crud) throws ClassNotFoundException, SQLException, CRUDException {

		for (String comando : comandosDeCriacao) {
			crud.getResultSet(comando);
		}
		return true;
	}
	


}

package com.michel1985.wedoffv3.crud;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.michel1985.wedoffv3.model.Atendimento;
import com.michel1985.wedoffv3.model.Usuario;

import javafx.collections.ObservableList;

/**
 * Classe responsável por repercurtir deleções
 * */

public class CascadeDeDelecao {
	/**
	 * Método invocado ao deletar um cliente
	 * */	
	public void deletarTodosOsAtendimentosDeUmCliente(String idCliente, ObservableList<Atendimento> oLA, Usuario user){
		//System.out.println("No historico de atendimentos chegou a ordem para deletar os atendimentos do cliente"+idCliente);
		deletarTodosOsAtendimentosDeUmClienteDaObservableList(idCliente,oLA);
		deletarTodosOsAtendimentosDeUmClienteDoBancoDeDados(idCliente, user);
	}
	
	private void deletarTodosOsAtendimentosDeUmClienteDaObservableList(String idCliente,ObservableList<Atendimento> oLA){
		List<Atendimento> paraDeletar = new ArrayList<>();
		try {
			oLA.forEach(atd ->{
				if(atd.getIdCliente().equalsIgnoreCase(idCliente)) paraDeletar.add(atd);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Vamos deletar: "+ paraDeletar.size()+ " atendimentos");
		oLA.removeAll(paraDeletar);
	}

	private void deletarTodosOsAtendimentosDeUmClienteDoBancoDeDados(String idCliente, Usuario user) {		
		
		ResultSet resultSet = null;
		try {
			CRUD crud = new CRUD(user);
			resultSet = crud.getResultSet("DELETE FROM ATENDIMENTOS WHERE idcliente = '" + idCliente + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}

package com.michel1985.wedoffv3.crud;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.model.Atendimento;

/**
 * Classe responsável por repercurtir deleções
 * */

public class CascadeDeDelecao {
	/**
	 * Método invocado ao deletar um cliente
	 * */	
	public void deletarTodosOsAtendimentosDeUmCliente(String idCliente, MainApp mainApp){
		//System.out.println("No historico de atendimentos chegou a ordem para deletar os atendimentos do cliente"+idCliente);
		deletarTodosOsAtendimentosDeUmClienteDaObservableList(idCliente,mainApp);
		deletarTodosOsAtendimentosDeUmClienteDoBancoDeDados(idCliente, mainApp);
	}
	
	private void deletarTodosOsAtendimentosDeUmClienteDaObservableList(String idCliente, MainApp mainApp){
		List<Atendimento> paraDeletar = new ArrayList<>();
		try {
			mainApp.getAtendimentoData().forEach(atd ->{
				if(atd.getIdCliente().equalsIgnoreCase(idCliente)) paraDeletar.add(atd);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Vamos deletar: "+ paraDeletar.size()+ " atendimentos");
		mainApp.getAtendimentoData().removeAll(paraDeletar);
	}

	private void deletarTodosOsAtendimentosDeUmClienteDoBancoDeDados(String idCliente, MainApp mainApp) {		
		
		ResultSet resultSet = null;
		try {
			CRUD crud = new CRUD(mainApp.getUsuarioAtivo());
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

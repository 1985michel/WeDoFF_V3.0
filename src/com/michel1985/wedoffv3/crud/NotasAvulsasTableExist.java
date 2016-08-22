package com.michel1985.wedoffv3.crud;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.michel1985.wedoffv3.exceptions.CRUDException;

public class NotasAvulsasTableExist {

	public static boolean isNotasAvulsasTableExist(CRUD crud) throws SQLException, ClassNotFoundException, CRUDException {
		Connection connection = null;
		
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection(crud.address, CRUD.user.getNome(), CRUD.user.getSenha());
			DatabaseMetaData dbm = connection.getMetaData();
			// check if "employee" table is there
			ResultSet tables = dbm.getTables(null, null, "NOTASAVULSAS", null);
			if (tables.next()) {
				System.out.println("A tabela NotasAvulsas já existe");
				try {				
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			} else {
				System.out.println("Criando tabela de Notas Avulsas");
				try {				
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				criarTabela(crud);
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false; 

	}
	
	public static void criarTabela(CRUD crud) throws ClassNotFoundException, SQLException, CRUDException{
		new DBFactory().criarTabelaNotasAvulsas(crud);
	}

}

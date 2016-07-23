package crud;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.michel1985.wedoffv3.exceptions.CRUDException;

public class DBFactory {

	public List<String> comandosDeCriacao = new ArrayList<>();

	public DBFactory() {
		String criarTabelaUsuarios = "CREATE TABLE USUARIOS (" + "id INTEGER IDENTITY PRIMARY KEY,"
				+ "LOGIN VARCHAR(50)," + "senha VARCHAR(100)," + ");";
		this.comandosDeCriacao.add(criarTabelaUsuarios);
		// nomeCliente, cpfCliente, notasSobreCliente
		String criarTabelaClientes = "CREATE TABLE CLIENTES (" + "idcliente INTEGER IDENTITY PRIMARY KEY,"
				+ "nomeCliente VARCHAR(300)," + "cpfCliente VARCHAR(100)," + "notasSobreCliente VARCHAR(5000)," + ");";

		this.comandosDeCriacao.add(criarTabelaClientes);

		String criarTabelaAtendimentos = "CREATE TABLE ATENDIMENTOS (" + "idatendimento INTEGER IDENTITY PRIMARY KEY,"
				+ "idcliente VARCHAR(100)," + "isagendamento BOOLEAN," + "ispendente BOOLEAN," + "nb VARCHAR(100),"
				+ "notassobreatendimento VARCHAR(5000)," + "dataatendimento VARCHAR(10)," + "datasolucao VARCHAR(10),"
				+ ");";
		this.comandosDeCriacao.add(criarTabelaAtendimentos);
	}

	public boolean criarBancos(CRUD crud) throws ClassNotFoundException, SQLException, CRUDException {

		for (String comando : comandosDeCriacao) {
			crud.getResultSet(comando);
		}
		return true;
	}

}

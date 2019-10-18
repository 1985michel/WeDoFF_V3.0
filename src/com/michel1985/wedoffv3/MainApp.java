package com.michel1985.wedoffv3;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import com.michel1985.wedoffv3.crud.CRUD;
import com.michel1985.wedoffv3.crud.NotasAvulsasTableExist;
import com.michel1985.wedoffv3.exceptions.CRUDException;
import com.michel1985.wedoffv3.model.Atendimento;
import com.michel1985.wedoffv3.model.Cliente;
import com.michel1985.wedoffv3.model.NotaAvulsa;
import com.michel1985.wedoffv3.model.Usuario;
import com.michel1985.wedoffv3.seguranca.Cripto;
import com.michel1985.wedoffv3.util.PreferenciasDoUsuario;
import com.michel1985.wedoffv3.view.AtendendoClienteOverviewController;
import com.michel1985.wedoffv3.view.AtendimentoDiarioStatisticsController;
import com.michel1985.wedoffv3.view.AtendimentoDiarioStatisticsControllerMensal;
import com.michel1985.wedoffv3.view.EditarAtendimentoOverviewController;
import com.michel1985.wedoffv3.view.EditarClienteOverviewController;
import com.michel1985.wedoffv3.view.EditarNotaAvulsaOverviewController;
import com.michel1985.wedoffv3.view.HistoricoDeAtendimentosOverviewController;
import com.michel1985.wedoffv3.view.HistoricoDeAtendimentosPendentesOverviewController;
import com.michel1985.wedoffv3.view.HistoricoDeClientesOverviewController;
import com.michel1985.wedoffv3.view.HistoricoDeNotasAvulsasOverviewController;
import com.michel1985.wedoffv3.view.LoginOverviewController;
import com.michel1985.wedoffv3.view.PesquisaIntegradaOverviewController;
import com.michel1985.wedoffv3.view.RootLayoutController;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private AtendendoClienteOverviewController atendendoClienteController;

	private ObservableList<Cliente> clienteData = FXCollections.observableArrayList();
	private ObservableList<Atendimento> atendimentoData = FXCollections.observableArrayList();
	private ObservableList<NotaAvulsa> notaAvulsaData = FXCollections.observableArrayList();

	private Usuario usuarioAtivo;

	public static String selectedCss;// = "modernaDark";

	public MainApp() {

		// Cliente newCli = new Cliente("1","Jose Um","87487813983","um notas");
		// clienteData.add(newCli);
		try {
			//Capiturando o CSS preferencial
			MainApp.selectedCss = PreferenciasDoUsuario.getPersonCss();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("WeDoFF V3.0");

		// Set the application icon.

		this.primaryStage.getIcons()
				.add(new Image(this.getClass().getResourceAsStream("/security-app-shield-icon.png")));

		initRootLayout();

		showLoginOverview();
		// showAtendendoClienteOverview();
	}

	/**
	 * Inicializa o rootLayout(base)
	 */
	public void initRootLayout() {
		try {
			// carrega root fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			// rootLayout.setStyle("view/modernaDark.css");

			// rootLayout.getStylesheets().add("path/modernaDark.css");
			// scene.getStylesheets().add("path/stylesheet.css");

			// Give the controller access to the main app.

			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);

			// Colocando o RootLayout em cena
			Scene scene = new Scene(rootLayout);
			addPersonalStyle(scene);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void setRootPersonalStyle(){
		Scene rootScene = this.rootLayout.getScene();
		addPersonalStyle(rootScene);
	}

	public void addPersonalStyle(Scene scene) {

		if (MainApp.selectedCss == "modenaDark") {
			try {
				//this.rootLayout.getScene().getStylesheets().clear();
			//	setUserAgentStylesheet(null);
			//	this.rootLayout.getScene().getStylesheets()
				//		.add(getClass().getResource("view/modenaDark.css").toExternalForm());
				scene.getStylesheets().clear();
				setUserAgentStylesheet(null);
				scene.getStylesheets().add(getClass().getResource("view/modenaDark.css").toExternalForm());
			} catch (Exception e) {
				System.out.println("Erro ao aplicar css Dark");
			}
		} else if (MainApp.selectedCss == "modenaLight") {
			try {
				//this.rootLayout.getScene().getStylesheets().clear();
				//setUserAgentStylesheet(null);
				//this.rootLayout.getScene().getStylesheets()
				//		.add(getClass().getResource("view/modenaLight.css").toExternalForm());
				scene.getStylesheets().clear();
				setUserAgentStylesheet(null);
				scene.getStylesheets().add(getClass().getResource("view/modenaLight.css").toExternalForm());
			} catch (Exception e) {
				System.out.println("Erro ao aplicar css Light");
			}
		} else if (MainApp.selectedCss == "modenaPink") {
			try {
				//this.rootLayout.getScene().getStylesheets().clear();
				//setUserAgentStylesheet(null);
				//this.rootLayout.getScene().getStylesheets()
				//		.add(getClass().getResource("view/modenaPink.css").toExternalForm());
				 scene.getStylesheets().clear();
				 setUserAgentStylesheet(null);
				 scene.getStylesheets().add(getClass().getResource("view/modenaPink.css").toExternalForm());
			} catch (Exception e) {
				System.out.println("Erro ao aplicar CSS Pink");
			}
		} else if (MainApp.selectedCss == "modenaDarkPink") {
			try {
				//this.rootLayout.getScene().getStylesheets().clear();
				//setUserAgentStylesheet(null);
				//this.rootLayout.getScene().getStylesheets()
				//		.add(getClass().getResource("view/modenaPink.css").toExternalForm());
				 scene.getStylesheets().clear();
				 setUserAgentStylesheet(null);
				 scene.getStylesheets().add(getClass().getResource("view/modenaDark_Pink.css").toExternalForm());
			} catch (Exception e) {
				System.out.println("Erro ao aplicar CSS Pink");
			}
		}
	}

	/**
	 * Colocando o LoginOverview dentro do RootLayout
	 */
	public void showLoginOverview() {
		try {
			// Passo 1 - Carregando FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(LoginOverviewController.location));
			AnchorPane loginOverview = (AnchorPane) loader.load();

			// Colocando dentro do root
			rootLayout.setCenter(loginOverview);

			// Dando acesso para o controller acessar o main
			LoginOverviewController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Colocando o AtendendoCLiente dentro do RootLayout
	 */
	public void showAtendendoClienteOverview() {
		try {
			// Passo 1 - Carregandoo FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/AtendendoClienteOverview.fxml"));
			AnchorPane atendendoClienteOverview = (AnchorPane) loader.load();

			// Colocando dentro do root
			rootLayout.setCenter(atendendoClienteOverview);

			// Dando acesso para o controller acessar a MainApp
			atendendoClienteController = loader.getController();
			atendendoClienteController.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Recupera o controle do atendendo cliente para ser invocado por interações
	// solicitadas pelas telas secundárias
	public AtendendoClienteOverviewController getAtendendoClienteController() {
		return this.atendendoClienteController;
	}

	public void showAboutDialog() {
		try {
			// Load o FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/AboutDialog.fxml"));
			DialogPane page = (DialogPane) loader.load();

			// Criando o dialogStage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Sobre o WeDoFF");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(false);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/edit.png"));
			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			// Show
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retorna o palco principal
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Obterndo a ObservableList de Clientes
	 */
	public ObservableList<Cliente> getClienteData() {
		return this.clienteData;
	}

	/**
	 * Obterndo a ObservableList de Clientes
	 */
	public ObservableList<NotaAvulsa> getNotaAvulsaData() {
		return this.notaAvulsaData;
	}

	/**
	 * Obtendo a ObservableList de Atendimentos
	 */
	public ObservableList<Atendimento> getAtendimentoData() {
		return this.atendimentoData;
	}

	/**
	 * Setando o usuário ativo
	 */
	public void setUsuarioAtivo(Usuario user) {
		this.usuarioAtivo = user;

	}

	/**
	 * O método abaixo carrega todos os clientes para a ObservableList
	 * dataCliente
	 * 
	 * O método é chamado pelo loginController quando o login é efetuado
	 */
	public void carregaHistoricoDeClientes() {
		if (this.usuarioAtivo != null) {
			ResultSet resultSet = null;

			try {
				resultSet = new CRUD(this.usuarioAtivo).getResultSet("SELECT * FROM clientes ORDER BY IDCLIENTE DESC");
				adicionaTodosClientesFromDBNaDataClientes(resultSet);
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

	private void adicionaTodosClientesFromDBNaDataClientes(ResultSet resultSet) throws SQLException {
		ArrayList<Cliente> clientes = new ArrayList<>();

		while (resultSet.next()) {

			clientes.add(
					new Cliente(resultSet.getString("idcliente"), descriptografa(resultSet.getString("nomeCliente")),
							descriptografa(resultSet.getString("cpfCliente")),
							descriptografa(resultSet.getString("notassobrecliente"))));
		}

		clienteData.addAll(FXCollections.observableArrayList(clientes));
	}

	public void carregaHistoricoDeAtendimentos() {
		if (this.usuarioAtivo != null) {
			ResultSet resultSet = null;

			try {
				resultSet = new CRUD(this.usuarioAtivo)
						.getResultSet("SELECT * FROM atendimentos ORDER BY IDCLIENTE DESC");
				adicionaTodosAtendimentosFromDBNaObservableList(resultSet, atendimentoData);
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

	private void adicionaTodosAtendimentosFromDBNaObservableList(ResultSet resultSet, ObservableList<Atendimento> ol)
			throws SQLException {
		ArrayList<Atendimento> atendimentos = new ArrayList<>();
		int count = 0;
		while (resultSet.next()) {
			// public Atendimento(String id, String idCli, boolean agendamento,
			// boolean pendente, String nb, String notas, String data, String
			// datasolu){
			count++;
			atendimentos.add(new Atendimento(resultSet.getString("idatendimento"), resultSet.getString("idcliente"),
					resultSet.getBoolean("isagendamento"), resultSet.getBoolean("isPendente"),
					descriptografa(resultSet.getString("nb")),
					descriptografa(resultSet.getString("notassobreatendimento")),
					resultSet.getString("dataatendimento"), resultSet.getString("datasolucao")));
		}
		System.out.println("Encontramos " + count + " atendimentos no resultSet");
		ol.addAll(FXCollections.observableArrayList(atendimentos));
	}

	private String descriptografa(String texto) {
		Cripto cripto = new Cripto();
		return cripto.descriptografa(texto, this.usuarioAtivo.getSenha());
	}

	public Usuario getUsuarioAtivo() {
		return this.usuarioAtivo;
	}

	/**
	 * Mostra o HistoricoDeClienteOverview
	 */
	public void showHistoricoDeClientesOverview() {
		try {

			// Load o FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/HistoricoDeClientesOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Dá ao controlador acesso ao MainApp
			HistoricoDeClientesOverviewController controller = loader.getController();
			controller.setMainApp(this, this.getClienteData());

			/**
			 * Reordenando a clienteData Utilizando lambda - Comparablea
			 */
			clienteData.sort((o1, o2) -> Integer.parseInt(o2.getIdCliente()) - Integer.parseInt(o1.getIdCliente()));

			// Criando o dialogStage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Histórico de Clientes");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(true);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/edit.png"));
			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			// Dando ao controlador poderes sobre seu próprio dialogStage
			controller.setDialogStage(dialogStage);

			// Show
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Mostra o Cliente selecionado a partir do Atendimento
	 */
	public void showHistoricoDeClientesOverview(String idCLi) {
		try {

			Cliente cliente = getClientePeloId(idCLi);

			// Load o FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/HistoricoDeClientesOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Dá ao controlador acesso ao MainApp
			HistoricoDeClientesOverviewController controller = loader.getController();

			ObservableList<Cliente> obListCliente = FXCollections.observableArrayList();
			obListCliente.add(cliente);
			controller.setMainApp(this, obListCliente);

			/**
			 * Reordenando a clienteData Utilizando lambda - Comparablea
			 */
			// clienteData.sort((o1, o2) -> Integer.parseInt(o2.getIdCliente())
			// - Integer.parseInt(o1.getIdCliente()));

			// Criando o dialogStage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Histórico de Clientes");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(true);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/edit.png"));
			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			// Dando ao controlador poderes sobre seu próprio dialogStage
			controller.setDialogStage(dialogStage);

			// Show
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Cliente getClientePeloId(String idCLi) {
		for (Cliente cli : clienteData) {
			if (cli.getIdCliente().equalsIgnoreCase(idCLi))
				return cli;
		}
		return null;
	}

	/**
	 * Apresenta o dialog para edição do cliente
	 */
	public boolean showEditarClienteOverview(Cliente cliente) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/EditarClienteOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Cria o palco
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editar Cliente");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			// Passa o cliente a ser editado ao controller
			EditarClienteOverviewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCliente(cliente);

			// Apresenta o dialog
			dialogStage.showAndWait();

			return controller.isOkCLicked();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Mostra o HistoricoDeClienteOverview
	 */
	public void showHistoricoDeAtendimentosOverview() {
		try {

			// Load o FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/HistoricoDeAtendimentosOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Dá ao controlador acesso ao MainApp
			HistoricoDeAtendimentosOverviewController controller = loader.getController();
			controller.setMainApp(this, this.getAtendimentoData());

			/**
			 * Reordenando a clienteData Utilizando lambda - Comparablea
			 */
			atendimentoData.sort(
					(o1, o2) -> Integer.parseInt(o2.getIdAtendimento()) - Integer.parseInt(o1.getIdAtendimento()));

			// Criando o dialogStage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Histórico de Atendimentos");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(true);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/edit.png"));
			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			// Dando ao controlador poderes sobre seu próprio dialogStage
			controller.setDialogStage(dialogStage);

			// Show
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Mostra o HistoricoDeClienteOverview
	 */
	public void showHistoricoDeAtendimentosPendentesOverview() {
		try {

			// Load o FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/HistoricoDeAtendimentosPendentesOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Dá ao controlador acesso ao MainApp
			HistoricoDeAtendimentosPendentesOverviewController controller = loader.getController();
			controller.setMainApp(this, this.getAtendimentoData());

			/**
			 * Reordenando a clienteData Utilizando lambda - Comparablea
			 * 
			 * 
			 * 
			 * preciso alterar o sort
			 *
			 * 
			 * 
			 */
			// TODO Auto-generated catch block
			// atendimentoData.sort(
			// (o1, o2) -> Integer.parseInt(o2.getIdAtendimento()) -
			// Integer.parseInt(o1.getIdAtendimento()));

			controller.pendentesList
					.sort((o1, o2) -> comparaDatas(geraData(o1.getDataSolucao()), geraData(o2.getDataSolucao())));

			// Criando o dialogStage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Atendimentos Pendentes");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(true);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/edit.png"));
			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			// Dando ao controlador poderes sobre seu próprio dialogStage
			controller.setDialogStage(dialogStage);

			// Show
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LocalDate geraData(String data) {
		System.out.println("data: " + data);
		if (data != null && data.length() == 10) {

			String[] estru = data.split("-");
			int ano = Integer.parseInt(estru[0]);
			int mes = Integer.parseInt(estru[1]);
			int dia = Integer.parseInt(estru[2]);
			LocalDate date = LocalDate.of(ano, mes, dia);
			return date;
		} else
			return null;

	}

	public int comparaDatas(LocalDate data1, LocalDate data2) {
		if (data1.isEqual(data2))
			return 0;
		else if (data1.isBefore(data2))
			return -1;
		else
			return 1;
	}

	/**
	 * Mostra o HistoricoDeClienteOverview
	 */
	public void showHistoricoDeAtendimentosOverview(ObservableList<Atendimento> atdList) {
		try {

			// Load o FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/HistoricoDeAtendimentosOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Dá ao controlador acesso ao MainApp
			HistoricoDeAtendimentosOverviewController controller = loader.getController();
			controller.setMainApp(this, atdList);

			/**
			 * Reordenando a clienteData Utilizando lambda - Comparablea
			 */
			atendimentoData.sort(
					(o1, o2) -> Integer.parseInt(o2.getIdAtendimento()) - Integer.parseInt(o1.getIdAtendimento()));

			// Criando o dialogStage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Histórico de Atendimentos");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(true);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/edit.png"));
			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			// Dando ao controlador poderes sobre seu próprio dialogStage
			controller.setDialogStage(dialogStage);

			// Show
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Apresenta o dialog para edição do cliente
	 */
	public boolean showEditarAtendimentoOverview(Atendimento atendimento) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/EditarAtendimentoOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Cria o palco
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editar Atendimento");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			// Passa o cliente a ser editado ao controller
			EditarAtendimentoOverviewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setAtendimento(atendimento);

			// Apresenta o dialog
			dialogStage.showAndWait();

			return controller.isOkCLicked();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void showHistoricoDeAtendimentosDoClienteOverview(String idClienteAtual) {
		try {

			// Load o FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/HistoricoDeAtendimentosOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Dá ao controlador acesso ao MainApp
			HistoricoDeAtendimentosOverviewController controller = loader.getController();

			// A linha abaixo é retirada para que eu possa passar uma nova
			// observableList para o exibição
			ObservableList<Atendimento> OLHistoricoDeAtendimentosDoCliente = FXCollections.observableArrayList();

			System.out.println("idClienteAtual fornecido: " + idClienteAtual);
			// Colocando somente os atendimentos do cliente na observable list
			carregaHistoricoDeAtendimentosDoClienteOverview(idClienteAtual, OLHistoricoDeAtendimentosDoCliente);

			// Passando a observable list para o controller
			controller.setObservableList(OLHistoricoDeAtendimentosDoCliente);
			System.out.println(
					"Encontrados " + OLHistoricoDeAtendimentosDoCliente.size() + " atendimentos para esse cliente");

			controller.setMainApp(this, OLHistoricoDeAtendimentosDoCliente);

			/**
			 * Reordenando a clienteData Utilizando lambda - Comparablea
			 */
			OLHistoricoDeAtendimentosDoCliente.sort(
					(o1, o2) -> Integer.parseInt(o2.getIdAtendimento()) - Integer.parseInt(o1.getIdAtendimento()));

			// Criando o dialogStage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Histórico de Atendimentos");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(true);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/edit.png"));
			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			// Dando ao controlador poderes sobre seu próprio dialogStage
			controller.setDialogStage(dialogStage);

			// Show
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void carregaHistoricoDeAtendimentosDoClienteOverview(String idClienteAtual,
			ObservableList<Atendimento> oLA) {
		if (this.usuarioAtivo != null) {
			ResultSet resultSet = null;

			try {
				resultSet = new CRUD(this.usuarioAtivo).getResultSet(
						"SELECT * FROM atendimentos where idcliente='" + idClienteAtual + "' ORDER BY IDCLIENTE DESC");
				adicionaTodosAtendimentosFromDBNaObservableList(resultSet, oLA);// parametro
																				// null
																				// pois
																				// a
																				// O.L.
																				// tem
																				// visibilidade
																				// direta
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

	/**
	 * Mostra o HistoricoDeClienteOverview
	 */
	public void showHistoricoDeNotasAvulsasOverview() {
		try {

			// Load o FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/HistoricoDeNotasAvulsasOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Dá ao controlador acesso ao MainApp
			HistoricoDeNotasAvulsasOverviewController controller = loader.getController();
			controller.setMainApp(this);

			/**
			 * Reordenando a clienteData Utilizando lambda - Comparablea
			 */
			notaAvulsaData
					.sort((o1, o2) -> Integer.parseInt(o2.getIdNotaAvulsa()) - Integer.parseInt(o1.getIdNotaAvulsa()));

			// Criando o dialogStage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Histórico de Notas Avulsas");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(true);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/edit.png"));
			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			// Dando ao controlador poderes sobre seu próprio dialogStage
			controller.setDialogStage(dialogStage);

			// Show
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Object[] showHistoricoDeNotasAvulsasOverviewRetornandoController() {
		HistoricoDeNotasAvulsasOverviewController controller = null;
		Stage dialogStage = null;

		try {

			// Load o FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/HistoricoDeNotasAvulsasOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Dá ao controlador acesso ao MainApp
			controller = loader.getController();
			controller.setMainApp(this);

			/**
			 * Reordenando a clienteData Utilizando lambda - Comparablea
			 */
			notaAvulsaData
					.sort((o1, o2) -> Integer.parseInt(o2.getIdNotaAvulsa()) - Integer.parseInt(o1.getIdNotaAvulsa()));

			// Criando o dialogStage
			dialogStage = new Stage();
			dialogStage.setTitle("Histórico de Notas Avulsas");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(true);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/edit.png"));
			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			// Dando ao controlador poderes sobre seu próprio dialogStage
			controller.setDialogStage(dialogStage);

			// Show
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}

		Object[] obj = { controller, dialogStage };
		return obj;
	}

	/**
	 * Apresenta o dialog para edição do cliente
	 */
	public boolean showEditarNotaAvulsaOverview(NotaAvulsa notaAvulsa) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/EditarNotaAvulsaOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Cria o palco
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editar Nota Avulsa");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			// Passa a nota a ser editado ao controller
			EditarNotaAvulsaOverviewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setNotaAvulsa(notaAvulsa);

			// Apresenta o dialog
			dialogStage.showAndWait();

			return controller.isOkCLicked();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void carregaHistoricoDeNotasAvulstas() {
		if (this.usuarioAtivo != null) {
			ResultSet resultSet = null;

			CRUD crud = new CRUD(this.usuarioAtivo);

			try {
				// ESSA LINHA ABAIXO DEVE SER RETIRADA ASSIM QUE EU TIVER A
				// CERTEZA DE QUE TODOS USUÁRIOS JÁ ATUALIZARAM PARA A NOVA
				// VERSÃO
				if (NotasAvulsasTableExist.isNotasAvulsasTableExist(crud)) {
					try {
						resultSet = crud.getResultSet("SELECT * FROM NOTASAVULSAS ORDER BY IDNOTAAVULSA DESC");
						adicionaTodasNotasAvulsasFromDBNaNotasAvulsasData(resultSet);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							resultSet.close();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else
					return;

			} catch (ClassNotFoundException | SQLException | CRUDException e1) {

				e1.printStackTrace();
			}

		}
	}

	private void adicionaTodasNotasAvulsasFromDBNaNotasAvulsasData(ResultSet resultSet) throws SQLException {
		ArrayList<NotaAvulsa> notas = new ArrayList<>();

		while (resultSet.next()) {

			notas.add(new NotaAvulsa(resultSet.getString("idNotaAvulsa"), descriptografa(resultSet.getString("link")),
					descriptografa(resultSet.getString("titulo")), descriptografa(resultSet.getString("descricao"))));
		}

		notaAvulsaData.addAll(FXCollections.observableArrayList(notas));
	}

	/**
	 * Mostra o HistoricoDeClienteOverview
	 */
	public void showPesquisaIntegradaOverview() {
		try {

			// Load o FXML
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PesquisaIntegradaOverview.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Dá ao controlador acesso ao MainApp
			PesquisaIntegradaOverviewController controller = loader.getController();
			controller.setMainApp(this);

			// Criando o dialogStage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Pesquisa Integrada");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(true);
			// dialogStage.getIcons().add(new
			// Image("file:resources/images/edit.png"));
			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			// Dando ao controlador poderes sobre seu próprio dialogStage
			controller.setDialogStage(dialogStage);

			// Show
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ordenaListaDeAtendimentosPendentes(ObservableList<Atendimento> list) {
		list.sort((o1, o2) -> comparaDatas(geraData(o1.getDataSolucao()), geraData(o2.getDataSolucao())));
	}

	/**
	 * Opens a dialog to show birthday statistics.
	 */
	public void showAtendimentoDiarioStatistics() {
		try {
			// Load the fxml file and create a new stage for the popup.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/AtendimentoDiarioStatistics.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Set the persons into the controller.
			AtendimentoDiarioStatisticsController controller = loader.getController();
			controller.setMainApp(this);
			controller.setAndShowData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Atendimentos: Mês Corrente");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showAtendimentoDiarioStatisticsMensal() {
		try {
			// Load the fxml file and create a new stage for the popup.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/AtendimentoDiarioStatisticsMensal.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Set the persons into the controller.
			AtendimentoDiarioStatisticsControllerMensal controller = loader.getController();
			controller.setMainApp(this);
			controller.setAndShowData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Estatísticas Mensais");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			addPersonalStyle(scene);
			dialogStage.setScene(scene);

			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

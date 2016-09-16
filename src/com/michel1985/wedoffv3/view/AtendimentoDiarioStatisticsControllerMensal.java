package com.michel1985.wedoffv3.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.michel1985.wedoffv3.MainApp;
import com.michel1985.wedoffv3.model.Atendimento;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

/// Quero que o gráfico se parece com esse gráfico: http://docs.oracle.com/javafx/2/charts/css-styles.htm

public class AtendimentoDiarioStatisticsControllerMensal {

	@FXML
	private BarChart<String, Integer> barChart;

	@FXML
	private CategoryAxis xAxis;

	private ObservableList<String> meses = FXCollections.observableArrayList();
	HashMap<String, Integer> datasEValores;
	HashMap<String, Integer> datasEQtdAgendamentos;

	private MainApp mainApp;

	@FXML
	private void initialize() {
	}

	/**
	 * Ligando ao main
	 */
	public void setMainApp(MainApp main) {
		this.mainApp = main;
	}

	// Coletando todas as datas em que houve atendimento
	private ArrayList<String> pegaTodasAsDatasDosAtendimentos() {

		ObservableList<Atendimento> listaAtd = mainApp.getAtendimentoData();
		ArrayList<String> datas = new ArrayList<>();

		for (Atendimento atd : listaAtd) {
			if (atd != null && isAnoCorrente(atd.getDataAtendimento())) {

				int valor = datasEValores.get(estruturaData(atd.getDataAtendimento())[1]);
				valor++;
				datasEValores.replace(estruturaData(atd.getDataAtendimento())[1] + "", valor);

				if (atd.getIsAgendamento()) {
					int qtdAgendamentos = datasEQtdAgendamentos.get(estruturaData(atd.getDataAtendimento())[1]);
					qtdAgendamentos++;
					datasEQtdAgendamentos.replace(estruturaData(atd.getDataAtendimento())[1] + "", qtdAgendamentos);
				}

			}
		}

		return datas;
	}

	private boolean jaTemAData(ArrayList<String> datas, String data) {
		boolean tem = false;
		for (String dt : datas) {
			if (dt.equalsIgnoreCase(data))
				tem = true;
		}

		return tem;
	}

	private boolean jaTemADataAgendamento(HashMap<String, Integer> map, String data) {
		boolean tem = false;
		Set<String> datas = map.keySet();
		for (String dt : datas) {
			if (dt.equalsIgnoreCase(data))
				tem = true;
		}

		return tem;
	}

	public void setAndShowData() {

		// Inicializando as listas
		datasEValores = new HashMap<>();
		datasEQtdAgendamentos = new HashMap<>();

		// Criando o eixo x com os números dos meses
		ArrayList<String> diasArray = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			diasArray.add(i + "");
		}
		meses.addAll(diasArray);
		xAxis.setCategories(meses);

		// Criando as colunas exibidas nos gráficos
		XYChart.Series<String, Integer> series = new XYChart.Series<>();
		series.setName("Atendimentos");
		XYChart.Series<String, Integer> seriesAgendamentos = new XYChart.Series<>();
		seriesAgendamentos.setName("Agendamentos");

		// colocando os Meses como Keys nos HashMaps
		for (String dia : meses) {
			datasEValores.clear();
			datasEValores.put(dia, 0);
			datasEQtdAgendamentos.clear();
			datasEQtdAgendamentos.put(dia, 0);
		}

		// Varrendo os atendimentos, coletando os dados e colocando nos maps
		pegaTodasAsDatasDosAtendimentos();

		Set<String> keys = datasEValores.keySet();

		for (String key : keys) {

			series.getData().add(new XYChart.Data<>(key + "", datasEValores.get(key)));
			if (datasEQtdAgendamentos.containsKey(key))
				seriesAgendamentos.getData()
						.add(new XYChart.Data<>(key + "", datasEQtdAgendamentos.get(key)));

		}

		barChart.getData().add(series);
		barChart.getData().add(seriesAgendamentos);
	}

	// metodo que recebe uma String baseadas em LocalDate e retorna o ano, mes e
	// dia
	public int[] estruturaData(String data) {

		String[] dataStr = data.split("-");
		int ano = Integer.parseInt(dataStr[0]);
		int mes = Integer.parseInt(dataStr[1]);
		int dia = Integer.parseInt(dataStr[2]);

		return new int[] { ano, mes, dia };
	}

	public boolean isAnoCorrente(String data) {

		int anoHoje = 2016;

		if (anoHoje == estruturaData(data)[2])
			return true;
		else
			return false;
	}

}
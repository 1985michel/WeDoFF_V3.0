package com.michel1985.wedoffv3.view;

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

public class AtendimentoDiarioStatisticsController {

	@FXML
	private BarChart<String, Integer> barChart;

	@FXML
	private CategoryAxis xAxis;

	private ObservableList<String> dias = FXCollections.observableArrayList();
	HashMap<String, Integer> datasEValores;

	private MainApp mainApp;

	@FXML
	private void initialize() {
		//String[] diasArray = pegaTodasAsDatasDosAtendimentos();
		//dias.addAll(diasArray);

		//xAxis.setCategories(dias);
	}

	/**
	 * Ligando ao main
	 */
	public void setMainApp(MainApp main) {
		this.mainApp = main;
	}

	// Coletando todas as datas em que houve atendimento
	private ArrayList<String> pegaTodasAsDatasDosAtendimentos() {

		ArrayList<String> datas = new ArrayList<>();
		datasEValores = new HashMap<>();
		
		ObservableList<Atendimento> listaAtd = mainApp.getAtendimentoData();
		System.out.println("QTd de atendimentos: "+listaAtd.size());

		for (Atendimento atd : listaAtd ) {
			if (atd != null) {
				if (jaTemAData(datas, atd.getDataAtendimento())) {
					int valor = datasEValores.get(atd.getDataAtendimento());
					valor++;
					datasEValores.replace(atd.getDataAtendimento(), valor);
				} else {
					datas.add(atd.getDataAtendimento());
					datasEValores.put(atd.getDataAtendimento(), 1);
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

	public void setAndShowData() {
		
		ArrayList<String> diasArray = pegaTodasAsDatasDosAtendimentos();
		dias.addAll(diasArray);

		xAxis.setCategories(dias);
		
		XYChart.Series<String, Integer> series = new XYChart.Series<>();

		Set<String> keys = datasEValores.keySet();

		for (String key : keys) {
			series.getData().add(new XYChart.Data<>(key, datasEValores.get(key)));
		}

		barChart.getData().add(series);
	}

}

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

public class AtendimentoDiarioStatisticsController {

	@FXML
	private BarChart<String, Integer> barChart;
	
	
	@FXML
	private CategoryAxis xAxis;

	private ObservableList<String> dias = FXCollections.observableArrayList();
	HashMap<String, Integer> datasEValores;
	HashMap<String, Integer> datasEQtdAgendamentos;

	private MainApp mainApp;

	@FXML
	private void initialize() {
		// String[] diasArray = pegaTodasAsDatasDosAtendimentos();
		// dias.addAll(diasArray);

		// xAxis.setCategories(dias);
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
		datasEQtdAgendamentos = new HashMap<>();

		ObservableList<Atendimento> listaAtd = mainApp.getAtendimentoData();
		System.out.println("QTd de atendimentos: " + listaAtd.size());

		for (Atendimento atd : listaAtd) {
			if (atd != null && isMesCorrente(atd.getDataAtendimento())) {
				if (jaTemAData(datas, atd.getDataAtendimento())) {
					int valor = datasEValores.get(atd.getDataAtendimento());
					valor++;
					datasEValores.replace(atd.getDataAtendimento(), valor);
					
						
				} else {
					datas.add(atd.getDataAtendimento());
					datasEValores.put(atd.getDataAtendimento(), 1);
					
				}
				
				if(jaTemADataAgendamento(datasEQtdAgendamentos, atd.getDataAtendimento())){
					if(atd.getIsAgendamento()){
						int qtdAgendamentos = datasEQtdAgendamentos.get(atd.getDataAtendimento());
						qtdAgendamentos++;
						datasEQtdAgendamentos.replace(atd.getDataAtendimento(), qtdAgendamentos);
					}
				}else{
					if(atd.getIsAgendamento())
						datasEQtdAgendamentos.put(atd.getDataAtendimento(), 1);
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

		//ArrayList<String> diasArray = pegaTodasAsDatasDosAtendimentos();
		
		pegaTodasAsDatasDosAtendimentos();
		ArrayList<String> diasArray = new ArrayList<>();
		
		for(int i =1;i<=31;i++){
			diasArray.add(i+"");
		}
		
		dias.addAll(diasArray);

		xAxis.setCategories(dias);

		XYChart.Series<String, Integer> series = new XYChart.Series<>();
		
		XYChart.Series<String, Integer> seriesAgendamentos = new XYChart.Series<>();

		Set<String> keys = datasEValores.keySet();

		for (String key : keys) {
			//series.getData().add(new XYChart.Data<>(key, datasEValores.get(key)));
			series.getData().add(new XYChart.Data<>(estruturaData(key)[2]+"", datasEValores.get(key)));
			seriesAgendamentos.getData().add(new XYChart.Data<>(estruturaData(key)[2]+"", datasEQtdAgendamentos.get(key)));

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
	
	public boolean isMesCorrente(String data){
		LocalDate hoje = LocalDate.now();
		int mesHoje = hoje.getMonthValue()+0;
		
		if(mesHoje == estruturaData(data)[1])
			return true;
		else
			return false;
	}

}

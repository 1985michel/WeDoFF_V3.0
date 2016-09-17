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

	public void setMainApp(MainApp main) {
		this.mainApp = main;
	}

	// Criando a legenda do gráfico: os meses
	private void criandoOsMeses() {
		for (int i = 0; i <= 12; i++) {
			meses.add(i + "");
		}
	}
	
	private void colocandoOsMesesComoKeys(){
		datasEValores = new HashMap<String, Integer>();
		datasEQtdAgendamentos = new HashMap<String, Integer>();
		
		for(String mes : meses){
			datasEValores.put(mes,0);
			datasEQtdAgendamentos.put(mes,0);
		}
	}
		
	//Pegando todos os atendimentos e separando-os por mes
	private void categorizaAtendimentosPorMes(){		
		
		for(Atendimento atd: mainApp.getAtendimentoData()){
			String mes = (estruturaData(atd.getDataAtendimento())[1])+"";
			int valor = datasEValores.get(mes);
			valor++;
			datasEValores.replace(mes, valor);
		}
	}
	
	//Pegando todos os agendamentos e separando-os por mês
	private void categorizaAgendaimentosPorMes(){
		for(Atendimento atd: mainApp.getAtendimentoData()){
			if(atd.getIsAgendamento()){
				String mes = (estruturaData(atd.getDataAtendimento())[1])+"";
				int valor = datasEQtdAgendamentos.get(mes);
				valor++;
				datasEQtdAgendamentos.replace(mes, valor);				
			}			
		}
	}
	
	private int[] estruturaData(String data) {

		String[] dataStr = data.split("-");
		int ano = Integer.parseInt(dataStr[0]);
		int mes = Integer.parseInt(dataStr[1]);
		int dia = Integer.parseInt(dataStr[2]);

		return new int[] { ano, mes, dia };
	}
	
	private void doTheStatisticWork(){
		criandoOsMeses();
		colocandoOsMesesComoKeys();
		categorizaAtendimentosPorMes();
		categorizaAgendaimentosPorMes();
	}
	
	public void setAndShowData() {

		doTheStatisticWork();
		
		xAxis.setCategories(meses);

		XYChart.Series<String, Integer> series = new XYChart.Series<>();
		series.setName("Atendimentos");
		
		XYChart.Series<String, Integer> seriesAgendamentos = new XYChart.Series<>();
		seriesAgendamentos.setName("Agendamentos");
		
		barChart.setBarGap(1);

		Set<String> keys = datasEValores.keySet();

		for (String key : keys) {
			
			series.getData().add(new XYChart.Data<>(key, datasEValores.get(key)));
			seriesAgendamentos.getData().add(new XYChart.Data<>(key, datasEQtdAgendamentos.get(key)));

		}
		
	

		barChart.getData().add(series);
		barChart.getData().add(seriesAgendamentos);
	}

}
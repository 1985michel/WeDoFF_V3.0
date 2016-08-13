package com.michel1985.wedoffv3.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EstruturaData {
	
	static DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static String estruturaData(String data) {

		String[] dataStr = data.split("-");
		int ano = Integer.parseInt(dataStr[0]);
		int mes = Integer.parseInt(dataStr[1]);
		int dia = Integer.parseInt(dataStr[2]);
		
		LocalDate dataNoFormato = LocalDate.of(ano, mes, dia);
		
		return myDateFormatter.format(dataNoFormato);

	}

	public static StringProperty estruturaData(StringProperty dataAtendimentoProperty) {
		
		String data = dataAtendimentoProperty.getValue();

		String[] dataStr = data.split("-");
		int ano = Integer.parseInt(dataStr[0]);
		int mes = Integer.parseInt(dataStr[1]);
		int dia = Integer.parseInt(dataStr[2]);
		
		LocalDate dataNoFormato = LocalDate.of(ano, mes, dia);
		
		StringProperty dataFormatadaProperty = new SimpleStringProperty(myDateFormatter.format(dataNoFormato));
		return dataFormatadaProperty;
	}


}

package com.michel1985.wedoffv3.model;


import java.time.format.DateTimeFormatter;

import com.michel1985.wedoffv3.util.EstruturaData;

import javafx.beans.property.*;


public class Atendimento {
	
	
	private StringProperty idAtendimento;
	private StringProperty idCliente;
	private BooleanProperty isAgendamento;
	private BooleanProperty isPendente;
	private StringProperty nb;
	private StringProperty notasSobreAtendimento;
	private StringProperty dataAtendimento;
	private StringProperty dataSolucao;


		
		public Atendimento(String id, String idCli, boolean agendamento,  boolean pendente, String nb, String notas, String data, String datasolu){
			this.idAtendimento = new SimpleStringProperty(id);
			this.idCliente = new SimpleStringProperty(idCli);
			this.nb = new SimpleStringProperty(nb);
			this.notasSobreAtendimento = new SimpleStringProperty(notas);
			this.dataAtendimento = new SimpleStringProperty(data);
			this.dataSolucao = new SimpleStringProperty(datasolu);
			this.isAgendamento = new SimpleBooleanProperty(agendamento);
			this.isPendente = new SimpleBooleanProperty(pendente);
		}


		public String getIdAtendimento() {
			return idAtendimento.getValue();
		}		
		public StringProperty idAtendimentoProperty(){
			return this.idAtendimento;
		}
		public void setIdAtendimento(String idAtendimento) {
			this.idAtendimento.set(idAtendimento);
		}
		
		
		public String getIdCliente() {
			return idCliente.getValue();
		}
		public StringProperty idClienteProperty(){
			return this.idCliente;
		}
		public void setIdCliente(String idCliente) {
			this.idCliente.set(idCliente);
		}


		public boolean getIsAgendamento() {
			return isAgendamento.getValue();
		}
		public BooleanProperty isAgendamentoProperty(){
			return this.isAgendamento;
		}
		public void setIsAgendamento(boolean isAgendamento) {
			this.isAgendamento.set(isAgendamento);
		}


		public boolean getIsPendente() {
			return isPendente.getValue();
		}
		public BooleanProperty isPendenteProperty(){
			return this.isPendente;
		}
		public void setIsPendente(boolean isPendente) {
			this.isPendente.set(isPendente);
		}


		public String getNb() {
			return nb.getValue();
		}
		public StringProperty nbProperty(){
			return this.nb;
		}
		public void setNb(String nb) {
			this.nb.set(nb);
		}


		public String getNotasSobreAtendimento() {
			return notasSobreAtendimento.getValue();
		}
		public StringProperty notasSobreAtendimentoProperty(){
			return this.notasSobreAtendimento;
		}
		public void setNotasSobreAtendimento(String notasSobreAtendimento) {
			this.notasSobreAtendimento.set(notasSobreAtendimento);
		}


		public String getDataAtendimento() {
			return dataAtendimento.getValue();
		}
		public StringProperty dataAtendimentoProperty(){
				return this.dataAtendimento;
		}
		public void setDataAtendimento(String dataAtendimento) {
			this.dataAtendimento.set(dataAtendimento);
		}
		
		
		public String getDataSolucao() {
			return dataSolucao.getValue();
		}
		public StringProperty dataSolucaoProperty(){
			return this.dataSolucao;
		}
		public void setDataSolucao(String dataSolucao) {
			this.dataSolucao.set(dataSolucao);
		}
		
		
		
		
		
		
		
		
		
	

}

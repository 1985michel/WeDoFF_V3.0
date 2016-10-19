import static org.junit.Assert.*;

import org.junit.Test;

import com.michel1985.wedoffv3.model.Atendimento;
import com.michel1985.wedoffv3.view.AtendimentoDiarioStatisticsControllerMensal;

public class TestesEstatistica {

	@Test
	public void test() {
		AtendimentoDiarioStatisticsControllerMensal controller = new AtendimentoDiarioStatisticsControllerMensal();
		Atendimento atd = new Atendimento("", "", true, true, "", "", "18-09-2016", "");
		assertTrue(controller.isAtendimentoNosUltimos12Meses(atd));
	}
	
	@Test
	public void test2() {
		AtendimentoDiarioStatisticsControllerMensal controller = new AtendimentoDiarioStatisticsControllerMensal();
		Atendimento atd = new Atendimento("", "", true, true, "", "", "01-01-2016", "");
		assertTrue(controller.isAtendimentoNosUltimos12Meses(atd));
	}
	
	@Test
	public void test3() {
		AtendimentoDiarioStatisticsControllerMensal controller = new AtendimentoDiarioStatisticsControllerMensal();
		Atendimento atd = new Atendimento("", "", true, true, "", "", "02-05-2016", "");
		assertTrue(controller.isAtendimentoNosUltimos12Meses(atd));
	}
	
	@Test
	public void test4() {
		AtendimentoDiarioStatisticsControllerMensal controller = new AtendimentoDiarioStatisticsControllerMensal();
		Atendimento atd = new Atendimento("", "", true, true, "", "", "16-09-2016", "");
		assertTrue(controller.isAtendimentoNosUltimos12Meses(atd));
	}
	
	@Test
	public void test5() {
		AtendimentoDiarioStatisticsControllerMensal controller = new AtendimentoDiarioStatisticsControllerMensal();
		Atendimento atd = new Atendimento("", "", true, true, "", "", "08-03-1985", "");
		assertFalse(controller.isAtendimentoNosUltimos12Meses(atd));
	}
	
	@Test
	public void test6() {
		AtendimentoDiarioStatisticsControllerMensal controller = new AtendimentoDiarioStatisticsControllerMensal();
		Atendimento atd = new Atendimento("", "", true, true, "", "", "01-01-2015", "");
		assertFalse(controller.isAtendimentoNosUltimos12Meses(atd));
	}
	
	@Test
	public void test7() {
		AtendimentoDiarioStatisticsControllerMensal controller = new AtendimentoDiarioStatisticsControllerMensal();
		Atendimento atd = new Atendimento("", "", true, true, "", "", "30-09-2015", "");
		assertFalse(controller.isAtendimentoNosUltimos12Meses(atd));
	}
	
	@Test
	public void test8() {
		AtendimentoDiarioStatisticsControllerMensal controller = new AtendimentoDiarioStatisticsControllerMensal();
		Atendimento atd = new Atendimento("", "", true, true, "", "", "01-10-2015", "");
		assertTrue(controller.isAtendimentoNosUltimos12Meses(atd));
	}
	
	

}

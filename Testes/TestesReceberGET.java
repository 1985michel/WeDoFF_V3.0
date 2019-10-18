import static org.junit.Assert.*;

import org.junit.Test;

import com.michel1985.wedoffv3.model.ObjetoTarefaGET;
import com.michel1985.wedoffv3.util.ManipuradoradeClipBoardTarefaGet;

public class TestesReceberGET {
	
//	@Test // o outofbounds não está em "parte que interessa"
//	public void testGetParteQueInteressa() {
//		//ObjetoTarefaGET obGET = ManipuradoradeClipBoardTarefaGet.getGET();
//		assertEquals("Protocolo: 211377452", ManipuradoradeClipBoardTarefaGet. getParteQueInteressa());
//	}

	@Test
	public void testGetProtocolo() {
		ObjetoTarefaGET obGET = ManipuradoradeClipBoardTarefaGet.getGET();
		assertEquals("Protocolo: 2011477653", obGET.getProtocoloGET());
		assertEquals("Certidão de Tempo de Contribuição", obGET.getServico());
		assertEquals("17/04/2019 <?>",obGET.getDER());
		assertEquals("88383571704",obGET.getCpf());
		assertEquals("MARCELO PECANHA CUTRIM",obGET.getNome());
		assertEquals("30/09/1970",obGET.getDataNascimento());
		assertEquals("JULITA PECANHA",obGET.getNomeMae());
	}
	


}

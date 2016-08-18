package com.michel1985.wedoffv3.util;

import java.text.Normalizer;

public class RemoveCaracteresEspeciais {
		
	public static String removerAcentos(String str) {
	    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	public String clean(String txt){
		return removerAcentos(txt);
	}
	
}
package com.michel1985.wedoffv3.util;

import java.io.File;
import java.util.prefs.Preferences;

import com.michel1985.wedoffv3.MainApp;

public class PreferenciasDoUsuario {

	/**
	 * Retorna o arquivo de preferências da pessoa, o último arquivo que foi
	 * aberto. As preferências são lidas do registro específico do SO (Sistema
	 * Operacional). Se tais prefêrencias não puderem ser encontradas, ele
	 * retorna null.
	 * 
	 * @return
	 */
	public static String getPersonCss() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String css = prefs.get("css", null);
		if (css != null) {
			return css;
		} else {
			return null;
		}
	}

	/**
	 * Define o caminho do arquivo do arquivo carregado atual. O caminho é
	 * persistido no registro específico do SO (Sistema Operacional).
	 * 
	 * @param file
	 *            O arquivo ou null para remover o caminho
	 */
	public static void setPersonCss(String css) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (css != null) {
			prefs.put("css", css);

			// Update the stage title.
			//primaryStage.setTitle("AddressApp - " + file.getName());
		} else {
			prefs.remove("css");

			// Update the stage title.
			//primaryStage.setTitle("AddressApp");
		}
	}
}

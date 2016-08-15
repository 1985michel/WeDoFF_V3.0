package com.michel1985.wedoffv3.model;

/**
 * ATENÇÃO!!!
 * NÃO ESQUECER DE COLOCAR O IMPORT NO INÍCIO DO FXML   
 * <?import com.michel1985.wedoffv3.view.TextFieldLimited?>
 * 
 * */

import javafx.scene.control.TextField;

public class TextFieldLimited extends TextField {  
    private int maxlength;
    public TextFieldLimited() {
        this.maxlength = 11;
    }
    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }
    @Override
    public void replaceText(int start, int end, String text) {
        // Delete or backspace user input.
        if (text.equals("")) {
            super.replaceText(start, end, text);
        } else if (getText().length() < maxlength) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        // Delete or backspace user input.
        if (text.equals("")) {
            super.replaceSelection(text);
        } else if (getText().length() < maxlength) {
            // Add characters, but don't exceed maxlength.
            if (text.length() > maxlength - getText().length()) {
                text = text.substring(0, maxlength- getText().length());
            }
            super.replaceSelection(text);
        }
    }
}
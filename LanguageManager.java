package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.Scanner;

class LanguageManager extends MText implements ActionListener {
    String[] langs = {"Italiano", "English"};
    public void actionPerformed(ActionEvent e) { 
        String clg;
        if(lang == 0)  clg = new String("English");
        else if(lang == 1) clg = new String("Italiano");
        else clg = new String("none");
        try {
            String s = (String) JOptionPane.showInputDialog(frame, "Choose language (requests reboot): ", "Alert", JOptionPane.PLAIN_MESSAGE, null, langs, clg);
            if (s != null) {
                File file = new File("conf\\language.txt");
                FileWriter fw = new FileWriter(file);
                BufferedWriter br = new BufferedWriter(fw);
                br.write(s);
                br.close();
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error: you should run me as administrator to modify the settings.");
            return;
        }
    }

    public String[] getTranslatedStrings(int value, int lang) {
        switch(value) {
            case 0:
                if (lang == 0) {
                    String[] tmp = {"Close", "New", "Open", "Save", "Save as...", "Exit", "Language", "About", "Tab size", "Automatic newline"};
                    return tmp;
                }

                else if (lang == 1) {
                    String[] tmp = {"Chiudi", "Nuovo", "Apri", "Salva", "Salva con nome...", "Esci", "Lingua", "Info", "Larghezza tabulazione", "A capo automatico"};
                    return tmp;
                }
            case 1:
                if (lang == 0) {
                    String[] tmp = {"2 spaces", "4 spaces", "8 spaces"};
                    return tmp;
                } else if (lang == 1) {
                    String[] tmp = {"2 spazi", "4 spazi", "8 spazi"};
                }
            case 2:
                if (lang == 0) {
                    String[] tmp = {"Yes", "No"};
                    return tmp;
                } else if (lang == 1) {
                    String[] tmp = {"Si", "No"};
                    return tmp;
                }
        }
        String[] tmp = {"none", "none"};
        return tmp;
    }
    public String getTranslatedString(int value, int lang) {
        assert(value >= 0 && value <= 5);
        switch(value) {
            case 0:
                if (lang == 0) return "Preferences";
                else if (lang == 1) return "Preferenze";
                
            case 1: //untitled - senza titolo
                if (lang == 0) return "untitled";
                else if(lang == 1) return "senza titolo";
            case 2: //save file when new
                if (lang == 0) return "Would you save the file?";
                else if (lang == 1) return "Salvare le modifiche al file?";
            case 3:
                if (lang == 0) return "MText, build 270621\nAuthor: Mauro Tramonti";
                else if (lang == 1) return "MText, build 270621\nAutore: Mauro Tramonti";
            case 4:
                if (lang == 0) return "Edit";
                else if (lang == 1) return "Modifica";
            case 5:
                if (lang == 0) return "Help";
                else if (lang == 1) return "Aiuto";

        }
        String tmp = "none";
        return tmp;
    }

}
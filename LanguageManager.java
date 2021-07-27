package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.Scanner;

class LanguageManager extends MText implements ActionListener {
    private int lang;
    private String[] langs = {"Italiano", "English"};
    public void actionPerformed(ActionEvent e) { 
        lang = frame.getLang();
        String clg;
        if(lang == 0) clg = new String("English");
        else if(lang == 1) clg = new String("Italiano");
        else clg = new String("none");
        try {
            String s = (String) JOptionPane.showInputDialog(frame.getFrame(), getTranslatedString(16, lang), getTranslatedString(10, lang), JOptionPane.PLAIN_MESSAGE, null, langs, clg);
            if (s != null) {
                File file = new File(SysConst.getPrePath() + "conf" + SysConst.getSlash() + "language.txt");
                FileWriter fw = new FileWriter(file);
                BufferedWriter br = new BufferedWriter(fw);
                br.write(s);
                br.close();
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(frame.getFrame(), getTranslatedString(15, lang));
            return;
        }
    }

    public static String[] getTranslatedStrings(int value, int lang) {
        switch(value) {
            case 0:
                if (lang == 0) {
                    String[] tmp = {"Close", "New", "Open", "Open folder", "Save", "Save as...", "Exit", "Language", "About MText", "Tab size", "Automatic newline", "App theme", "About Java"};
                    return tmp;
                }

                else if (lang == 1) {
                    String[] tmp = {"Chiudi", "Nuovo", "Apri", "Apri cartella", "Salva", "Salva con nome...", "Esci", "Lingua", "Informazioni su MText", "Larghezza tabulazione", "A capo automatico", "Tema applicazione", "Informazioni su Java"};
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
    public static String getTranslatedString(int value, int lang) {
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
                if (lang == 0) return "MText 1.7.1 (build 260721)\nAuthor: Mauro Tramonti";
                else if (lang == 1) return "MText 1.7.1 (build 260721)\nAutore: Mauro Tramonti";
            case 4:
                if (lang == 0) return "Edit";
                else if (lang == 1) return "Modifica";
            case 5:
                if (lang == 0) return "Help";
                else if (lang == 1) return "Aiuto";
            case 6:
                if (lang == 0) return "There are some files unsaved. Would you like to save them?";
                else if (lang == 1) return "Sono presenti file non salvati. Vuoi salvarli?";
            case 7:
                if (lang == 0) return "Tab size: ";
                else if (lang == 1) return "Larghezza tabulazione: ";
            case 8:
                if (lang == 0) return "Row: ";
                else if (lang == 1) return "Riga: ";
            case 9:
                if (lang == 0) return "Column: ";
                else if (lang == 1) return "Colonna: ";
            case 10:
                if (lang == 0) return "Warning";
                else if (lang == 1) return "Attenzione";
            case 11:
                if (lang == 0) return "You opened too many tabs!";
                else if (lang == 1) return "Hai aperto troppe schede!";
            case 12:
                if (lang == 0) return "An error occurred while reading the file!";
                else if (lang == 1) return "Si è verificato un errore durante l'apertura del file.";
            case 13:
                if (lang == 0) return "An error occurred while saving the file!";
                else if (lang == 1) return "Si è verificato un errore durante il salvataggio del file!";
            case 14:
                if (lang == 0) return "Activate or deactivate automatic newline: ";
                else if (lang == 1) return "Attiva o disattiva il ritorno a capo automatico: ";
            case 15:
                if (lang == 0) return "Error: you should run me as administrator to modify the settings.";
                else if (lang == 1) return "Errore: è necessario eseguire come amministratore per modificare le impostazioni";
            case 16:
                if (lang == 0) return "Choose language (requests reboot): ";
                else if (lang == 1) return "Seleziona lingua (richiede riavvio): ";
            case 17:
                if (lang == 0) return "Choose tabs lenght: ";
                else if (lang == 1) return "Seleziona larghezza tabulazione: ";
            case 18:
                if (lang == 0) return "An error occurred while loading a setting file.";
                else if (lang == 1) return "Si è verificato un errore durante il caricamento di un file di impostazioni.";
            case 19:
                if (lang == 0) return " spaces";
                else if (lang == 1) return " spazi";
            case 20:
                if (lang == 0) return "Explore files: ";
                else if (lang == 1) return "Esplora file: ";
            case 21:
                if (lang == 0) return "Close";
                else if (lang == 1) return "Chiudi";
            case 22:
                if (lang == 0) return "Recent files";
                else if (lang == 1) return "File recenti";
            case 23:
                if (lang == 0) return "No recent files";
                else if (lang == 1) return "Nessun file recente";
            case 24:
                if (lang == 0) return "Restart is needed to apply the setting.";
                else if (lang == 1) return "Riavvio richiesto per applicare le modifiche.";
            case 25:
                if (lang == 0) return "Version: " + System.getProperty("java.vm.version") + "\nInstall path:  " + System.getProperty("java.home") + "\nOperating system: " + System.getProperty("os.name");
                else if (lang == 1) return "Versione: " + System.getProperty("java.vm.version") + "\nPercorso di installazione:  " + System.getProperty("java.home") + "\nSistema operativo: " + System.getProperty("os.name");
        }
        String tmp = "none";
        return tmp;
    }

}

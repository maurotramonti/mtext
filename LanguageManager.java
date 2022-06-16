package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.Scanner;

class LanguageManager extends MText {
    public static final int ITALIAN = 1;
    public static final int ENGLISH = 0;

    private int lang;
    public static final String[] langs = {"Italiano", "English"};
    

    public static String[] getTranslatedStrings(int value, int lang) {
        switch(value) {
            case 0:
                if (true) {
                    String[] tmp = {"Close", "New", "Open", "Open folder", "Save", "Save as...", "Exit", "Undo", "Redo", "Cut", "Copy", "Paste", "Check updates", "About MText", "About Java"};
                    return tmp;
                }            
            case 3:  
                if (true) {              
                    String[] tmp = {getTranslationsFromFile("Close", lang), getTranslationsFromFile("New", lang), getTranslationsFromFile("Open", lang), getTranslationsFromFile("OpenFolder", lang), getTranslationsFromFile("Save", lang), getTranslationsFromFile("SaveAs", lang), getTranslationsFromFile("Exit", lang), getTranslationsFromFile("Undo", lang), getTranslationsFromFile("Redo", lang), getTranslationsFromFile("Cut", lang), getTranslationsFromFile("Copy", lang), getTranslationsFromFile("Paste", lang), getTranslationsFromFile("CheckUpdates", lang), getTranslationsFromFile("InfoAboutMText", lang),  getTranslationsFromFile("AboutJava", lang)};
                    return tmp;
                }
                
            case 2:
                if (true) {
                    String[] tmp = {getTranslationsFromFile("Yes", lang), getTranslationsFromFile("No", lang)};
                    return tmp;
                }
        }
        String[] tmp = {"none", "none"};
        return tmp;
    }
    public static String getTranslationsFromFile(String property) {
        return getTranslationsFromFile(property, getCurrentLang());
    }

    public static String getTranslationsFromFile(String property, int lang) {
        String prefix, contents = "";
        switch (lang) {
            case 0: 
                prefix = SysConst.getPrePath() + File.separator + "langs" + File.separator + "eng" + File.separator;
                break;
            case 1:
                prefix = SysConst.getPrePath() + File.separator + "langs" + File.separator + "ita" + File.separator;
                break;
            default:
                prefix = SysConst.getPrePath() + File.separator + "langs" + File.separator + "eng" + File.separator;
                break;
        }
        try {
            File lf = new File(prefix + property + ".txt");
            Scanner s = new Scanner(lf);
            do {
                contents = contents + s.nextLine() + '\n';
            } while (s.hasNextLine());
            s.close();
        } catch (FileNotFoundException e) {}
        return contents;
    }
    public static String getJavaVersionString(int lang) {
        if (lang == 0) return "Version: " + System.getProperty("java.vm.version") + "\nInstall path:  " + System.getProperty("java.home") + "\nOperating system: " + System.getProperty("os.name");
        else if (lang == 1) return "Versione: " + System.getProperty("java.vm.version") + "\nPercorso di installazione:  " + System.getProperty("java.home") + "\nSistema operativo: " + System.getProperty("os.name");
        return "none";
    }

    public static int getCurrentLang() {
        return frame.getLang();
    }
    

}

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
            String s = (String) JOptionPane.showInputDialog(frame.getFrame(), getTranslationsFromFile("ChooseLanguage", lang), getTranslationsFromFile("Warning", lang), JOptionPane.PLAIN_MESSAGE, null, langs, clg);
            if (s != null) {
                File file = new File(SysConst.getPrePath() + "conf" + File.separator + "language.txt");
                FileWriter fw = new FileWriter(file);
                BufferedWriter br = new BufferedWriter(fw);
                br.write(s);
                br.close();
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(frame.getFrame(), getTranslationsFromFile("PermsError", lang));
            System.out.println("[DEBUG] Error in LanguageManager.java");
            return;
        }
    }

    public static String[] getTranslatedStrings(int value, int lang) {
        switch(value) {
            case 0:
                if (true) {
                    String[] tmp = {"Close", "New", "Open", "Open folder", "Save", "Save as...", "Exit", "Language", "About MText", "Tab size", "Automatic newline", "App theme", "About Java", "Check updates"};
                    return tmp;
                }            
            case 3:  
                if (true) {              
                    String[] tmp = {getTranslationsFromFile("Close", lang), getTranslationsFromFile("New", lang), getTranslationsFromFile("Open", lang), getTranslationsFromFile("OpenFolder", lang), getTranslationsFromFile("Save", lang), getTranslationsFromFile("SaveAs", lang), getTranslationsFromFile("Exit", lang), getTranslationsFromFile("Language", lang), getTranslationsFromFile("InfoAboutMText", lang), getTranslationsFromFile("TabLength", lang), getTranslationsFromFile("AutomaticNewline", lang), getTranslationsFromFile("AppTheme", lang), getTranslationsFromFile("AboutJava", lang), getTranslationsFromFile("CheckUpdates", lang)};
                    return tmp;
                }
                
            case 1:
                if (true) {
                    String[] tmp = {"2 " + getTranslationsFromFile("Spaces", lang), "4 " + getTranslationsFromFile("Spaces", lang), "8 " + getTranslationsFromFile("Spaces", lang)};
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
    public static String getTranslatedString(int value, int lang) {
            System.out.println("Missing translation: number " + value);
            return "none";        
    }
    

}

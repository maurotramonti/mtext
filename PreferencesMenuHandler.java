package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Scanner;
import java.io.*;

class PreferencesMenuHandler extends MText implements ActionListener {
    int lang;
    public void actionPerformed(ActionEvent e) {
        lang = frame.getLang();
        if (e.getActionCommand().equals("Language")) {
           LanguageManager lm = new LanguageManager();
           lm.actionPerformed(null);            
        }
        else if (e.getActionCommand().equals("Tab size")) {
            tabSizeRoutine();
        }
        else if (e.getActionCommand().equals("Automatic newline")) {
            autoNewlineRoutine();
     
        }
        else if (e.getActionCommand().equals("App theme")) {
            themeRoutine();
        }
    }

    public void tabSizeRoutine() {
        String ts = " " + LanguageManager.getTranslationsFromFile("Spaces", lang);

        String[] len = {"2" + ts, "4" + ts, "8" + ts};
        String ctb;
        if(frame.getTabSize() == 2)  ctb = new String(len[0]);
        else if(frame.getTabSize() == 4) ctb = new String(len[1]);
        else if(frame.getTabSize() == 8) ctb = new String (len[2]);
        else ctb = new String("none");
        try {
            String s = (String) JOptionPane.showInputDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("TabSizeSetting", lang), LanguageManager.getTranslationsFromFile("Warning", lang), JOptionPane.PLAIN_MESSAGE, null, len, ctb);
            if (s != null) {
                File file = new File(SysConst.getPrePath() + "conf" + File.separator + "tabsize.txt");
                FileWriter fw = new FileWriter(file);
                BufferedWriter br = new BufferedWriter(fw);
                if (s.contains("2")) br.write("2 spaces");
                else if (s.contains("4")) br.write("4 spaces");
                else if (s.contains("8")) br.write("8 spaces");
                br.close();
            }
        } catch (IOException ex) {
            System.out.println("[DEBUG] Error in TabSizeManager.java");
        }
        frame.loadTabs();
    }

    public void themeRoutine() {
        String[] opt = {"Java Cross-Platform", "System theme"};
            JFrame fr;
            try {
                fr = frame.getFrame();
            } catch (NullPointerException ex) {
                fr = null;
            }
            String s = (String) JOptionPane.showInputDialog(fr, null, "Select theme", JOptionPane.PLAIN_MESSAGE, null, opt, opt[0]);
            if (s != null) {
                try {
                    FileWriter fw = new FileWriter(new File(SysConst.getPrePath() + "conf" + File.separator + "theme.txt"));
                    BufferedWriter br = new BufferedWriter(fw);
                    if (s.equals("Java Cross-Platform")) {                        
                        br.write("0");
                        theme = 0;
                    } else if (s.equals("System theme")) {
                        br.write("1");
                        theme = 1;
                    }
                    br.close();
                    try {
                        JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("RebootNeeded", frame.getLang()));
                    } catch (NullPointerException ex) {};
                } catch (IOException ex) {
                System.out.println("[DEBUG] Error in ThemeManager.java");
                }
            } else theme = 0;
    }

    public void autoNewlineRoutine() {
        String[] opt = LanguageManager.getTranslatedStrings(2, lang);
            String cln;
            if (frame.getLineWrap() == true) cln = opt[0];
            else cln = opt[1];

            try {
                String s = (String) JOptionPane.showInputDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("AutomaticNewlineSetting", lang), LanguageManager.getTranslationsFromFile("Warning", lang), JOptionPane.PLAIN_MESSAGE, null, opt, cln);
                if (s != null) {
                    File file = new File(SysConst.getPrePath() + "conf" + File.separator + "wraplines.txt");
                    FileWriter fw = new FileWriter(file);
                    BufferedWriter br = new BufferedWriter(fw);
                    if (s.equals(opt[0])) br.write("Yes");
                    else if (s.equals(opt[1])) br.write("No");
                    br.close();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("PermsError", lang)); // only on Windows, because of permissions
                System.out.println("[DEBUG] Error in WrapLinesSetting.java");
                return;
            }
            frame.loadWrap();
    }
}
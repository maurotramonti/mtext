package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.Scanner;

class WrapLineSetting extends MText implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        int lang = frame.getLang();
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
     
    return;   
    }
}

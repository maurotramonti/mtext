package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;


class TabSizeManager extends MText implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        int lang = frame.getLang();
        String ts = LanguageManager.getTranslatedString(19, lang);

        String[] len = {"2" + ts, "4" + ts, "8" + ts};
        String ctb;
        if(frame.getTabSize() == 2)  ctb = new String(len[0]);
        else if(frame.getTabSize() == 4) ctb = new String(len[1]);
        else if(frame.getTabSize() == 8) ctb = new String (len[2]);
        else ctb = new String("none");
        try {
            String s = (String) JOptionPane.showInputDialog(frame.getFrame(), LanguageManager.getTranslatedString(17, lang), LanguageManager.getTranslatedString(10, lang), JOptionPane.PLAIN_MESSAGE, null, len, ctb);
            if (s != null) {
                File file = new File(SysConst.getPrePath() + "conf" + SysConst.getSlash() + "tabsize.txt");
                FileWriter fw = new FileWriter(file);
                BufferedWriter br = new BufferedWriter(fw);
                if (s.contains("2")) br.write("2 spaces");
                else if (s.contains("4")) br.write("4 spaces");
                else if (s.contains("8")) br.write("8 spaces");
                br.close();
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslatedString(15, lang)); // only Windows
            return;
        }
        frame.loadTabs();
        return;
        
    }
}

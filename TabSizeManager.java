package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.Scanner;

class TabSizeManager extends MText implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        String ts = lm.getTranslatedString(19, lang);

        String[] len = {"2" + ts, "4" + ts, "8" + ts};
        String ctb;
        if(tabSize == 2)  ctb = new String(len[0]);
        else if(tabSize == 4) ctb = new String(len[1]);
        else if(tabSize == 8) ctb = new String (len[2]);
        else ctb = new String("none");
        try {
            String s = (String) JOptionPane.showInputDialog(frame, lm.getTranslatedString(17, lang), lm.getTranslatedString(10, lang), JOptionPane.PLAIN_MESSAGE, null, len, ctb);
            if (s != null) {
                File file = new File(prepath + "conf" + slash + "tabsize.txt");
                FileWriter fw = new FileWriter(file);
                BufferedWriter br = new BufferedWriter(fw);
                if (s.contains("2")) br.write("2 spaces");
                else if (s.contains("4")) br.write("4 spaces");
                else if (s.contains("8")) br.write("8 spaces");
                br.close();
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, lm.getTranslatedString(15, lang)); // only Windows
            return;
        }
        loadTabs();
        return;
        
    }
}

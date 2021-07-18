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

        String[] len = {"2 spaces", "4 spaces", "8 spaces"};
        String ctb;
        if(tabSize == 2)  ctb = new String("2 spaces");
        else if(tabSize == 4) ctb = new String("4 spaces");
        else if(tabSize == 8) ctb = new String ("8 spaces");
        else ctb = new String("none");
        try {
            String s = (String) JOptionPane.showInputDialog(frame, lm.getTranslatedString(17, lang), lm.getTranslatedString(10, lang), JOptionPane.PLAIN_MESSAGE, null, len, tabSize);
            if (s != null) {
                File file = new File(prepath + "conf" + slash + "tabsize.txt");
                FileWriter fw = new FileWriter(file);
                BufferedWriter br = new BufferedWriter(fw);
                br.write(s);
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

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
        String[] opt = {"Yes", "No"};

        try {
            String s = (String) JOptionPane.showInputDialog(frame, lm.getTranslatedString(14, lang), lm.getTranslatedString(10, lang), JOptionPane.PLAIN_MESSAGE, null, opt, lineWrap);
            if (s != null) {
                File file = new File(prepath + "conf" + slash + "wraplines.txt");
                FileWriter fw = new FileWriter(file);
                BufferedWriter br = new BufferedWriter(fw);
                br.write(s);
                br.close();
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, lm.getTranslatedString(15, lang)); // only on Windows, because of permissions
            return;
        }
        loadWrap();
     
    return;   
    }
}

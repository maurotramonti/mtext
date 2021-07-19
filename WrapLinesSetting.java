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
        String[] opt = lm.getTranslatedStrings(2, lang);
        String cln;
        if (lineWrap == true) cln = opt[0];
        else cln = opt[1];

        try {
            String s = (String) JOptionPane.showInputDialog(frame, lm.getTranslatedString(14, lang), lm.getTranslatedString(10, lang), JOptionPane.PLAIN_MESSAGE, null, opt, cln);
            if (s != null) {
                File file = new File(prepath + "conf" + slash + "wraplines.txt");
                FileWriter fw = new FileWriter(file);
                BufferedWriter br = new BufferedWriter(fw);
                if (s.equals(opt[0])) br.write("Yes");
                else if (s.equals(opt[1])) br.write("No");

                br.close();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, lm.getTranslatedString(15, lang)); // only on Windows, because of permissions
            return;
        }
        loadWrap();
     
    return;   
    }
}

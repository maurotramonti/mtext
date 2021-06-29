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
            String s = (String) JOptionPane.showInputDialog(frame, "Activate or deactivate automatic newline: ", "Alert", JOptionPane.PLAIN_MESSAGE, null, opt, lineWrap);
            if (s != null) {
                File file = new File("conf\\wraplines.txt");
                FileWriter fw = new FileWriter(file);
                BufferedWriter br = new BufferedWriter(fw);
                br.write(s);
                br.close();
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error: you should run me as administrator to modify the settings.");
            return;
        }
        loadWrap();
     
    return;   
    }
}

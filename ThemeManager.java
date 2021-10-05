package mtext;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

class ThemeManager extends MText implements ActionListener {
    public void actionPerformed(ActionEvent e) {
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
                    e.getSource();
                    JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("RebootNeeded", frame.getLang()));
                } catch (NullPointerException ex) {};
        } catch (IOException ex) {}
            } else theme = 0;

        } 
    }


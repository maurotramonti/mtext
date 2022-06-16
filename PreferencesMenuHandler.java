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
        
            String results[] = new String[5];
            try {
                Scanner s = new Scanner(new File(SysConst.getPrePath() + "conf" + File.separator + "language.txt"));  results[0] = s.nextLine(); s.close();
                s = new Scanner(new File (SysConst.getPrePath() + "conf" + File.separator + "wraplines.txt")); results[1] = s.nextLine(); s.close();
                s = new Scanner(new File(SysConst.getPrePath() + "conf" + File.separator + "tabsize.txt")); results[2] = s.nextLine(); s.close();
                s = new Scanner(new File(SysConst.getPrePath() + "conf" + File.separator + "theme.txt")); results[3] = s.nextLine(); s.close();

                results[4] = "false";
                SettingsDialog dialog = new SettingsDialog(frame.getFrame(), results);
                if (results[4].equals("false" )) return;
                BufferedWriter bw = new BufferedWriter(new FileWriter(new File(SysConst.getPrePath() + "conf" + File.separator + "language.txt"))); bw.write(results[0]); bw.close();
                bw = new BufferedWriter(new FileWriter(new File(SysConst.getPrePath() + "conf" + File.separator + "wraplines.txt"))); bw.write(results[1]); bw.close();
                bw = new BufferedWriter(new FileWriter(new File(SysConst.getPrePath() + "conf" + File.separator + "tabsize.txt"))); bw.write(results[2]); bw.close();
                bw = new BufferedWriter(new FileWriter(new File(SysConst.getPrePath() + "conf" + File.separator + "theme.txt"))); bw.write(results[3]); bw.close();
                frame.loadTabs();
                frame.loadWrap();
                
            } catch (IOException ex) {
                ex.printStackTrace();

            }
        
    }   

    
}
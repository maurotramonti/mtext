package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;


class PreferencesMenuHandler extends MText implements ActionListener {
    int lang;
    public void actionPerformed(ActionEvent e) {
        lang = frame.getLang();
        if (e.getActionCommand().equals("Settings")) {  
            SettingsDialog dialog = new SettingsDialog(frame.getFrame(), frame.getLineWrap(), frame.getTabSize(), theme);
            frame.loadTabs();
            frame.loadWrap();
                
        } else if (e.getActionCommand().equals("Appearance")) {
            AppearanceDialog dialog = new AppearanceDialog(frame);
        }
        
    }   

    
}
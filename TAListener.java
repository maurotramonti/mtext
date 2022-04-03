package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

class TAListener extends MText implements DocumentListener {
    static boolean active = true;
    public void changedUpdate(DocumentEvent de) {
        act();
    }    
    public void removeUpdate(DocumentEvent de) {
        act();
    } 
    public void insertUpdate(DocumentEvent de) {
        act();
    }
    public void setActive(boolean b) {
        active = b;
    }

    private void act() {
        if (active) {
            TextFilePanel current = frame.getFileTabs()[frame.getTabPane().getSelectedIndex()];
            if (current.getIfIsModified() == false) {
                current.setModified(true);
                frame.getFrame().setTitle(frame.getFrame().getTitle() + "*");
            }

            current.addToHistory(current.getTextArea().getText());
        }
    }
}
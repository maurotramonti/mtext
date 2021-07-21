package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

class TAListener extends MText implements DocumentListener {
    public void changedUpdate(DocumentEvent de) {
        act();
    }    
    public void removeUpdate(DocumentEvent de) {
        act();
    } 
    public void insertUpdate(DocumentEvent de) {
        act();
    }
    
    private void act() {
        TextFilePanel current = frame.getFileTabs()[frame.getTabPane().getSelectedIndex()];
        if (current.getIfIsModified() == false) {
            current.setModified(true);
            frame.getFrame().setTitle(frame.getFrame().getTitle() + "*");
        }
    }
}
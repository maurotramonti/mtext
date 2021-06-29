package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.Scanner;

class TAListener extends MText implements DocumentListener {
    public void changedUpdate(DocumentEvent de) {
        TextFilePanel current = tabs[tPane.getSelectedIndex()];
        if (current.getIfIsModified() == false) {
            current.setModified(true);
            frame.setTitle(frame.getTitle() + "*");
        }
    }
    
    public void removeUpdate(DocumentEvent de) {
        TextFilePanel current = tabs[tPane.getSelectedIndex()];
        if (current.getIfIsModified() == false) {
            current.setModified(true);
            frame.setTitle(frame.getTitle() + "*");
        }
    } 
    public void insertUpdate(DocumentEvent de) {
        TextFilePanel current = tabs[tPane.getSelectedIndex()];
        if (current.getIfIsModified() == false) {
            current.setModified(true);
            frame.setTitle(frame.getTitle() + "*");
        }
    }
}
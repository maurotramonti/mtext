package mtext;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Scanner;
import javax.swing.filechooser.*;
import java.io.*;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

class EditMenuHandler extends MText implements ActionListener {
    int lang;
    public void actionPerformed(ActionEvent e) {
        lang = frame.getLang();
        if (e.getActionCommand().equals("Copy")) {
            StringSelection sl = new StringSelection(new String(frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getTextArea().getSelectedText())); // copy only selected text
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(sl, null);
        }

        else if (e.getActionCommand().equals("Paste")) {
            frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getTextArea().paste();
        
        }
    }
}
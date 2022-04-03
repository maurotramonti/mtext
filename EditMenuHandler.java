package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

class EditMenuHandler extends MText implements ActionListener {
    int lang;
    JTextArea txtArea;
    public void actionPerformed(ActionEvent e) {
        lang = frame.getLang();
        txtArea = frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getTextArea();
        if (e.getActionCommand().equals("Undo")) {
            int index = frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getHistoryIndex();
            if (index > 1) {
                frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].setHistoryIndex(index - 1);
                frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getTAListener().setActive(false);
                String[] history = frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getHistory();
                txtArea.setText(history[index - 2]);                
                frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getTAListener().setActive(true);
            }

        }

        else if (e.getActionCommand().equals("Redo")) {
            int index = frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getHistoryIndex();
            if (index < 63) {
                frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].setHistoryIndex(index + 1);
                frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getTAListener().setActive(false);
                String[] history = frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getHistory();
                if (history[index + 1] != null) txtArea.setText(history[index + 1]);                
                frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getTAListener().setActive(true);
            } 
        }


        else if (e.getActionCommand().equals("Cut")) {
            StringSelection sl = new StringSelection(new String(txtArea.getSelectedText())); // copy only selected text
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(sl, null);
            txtArea.replaceSelection("");  // replaces the selected text with nothing --> deletes it

        }
        else if (e.getActionCommand().equals("Copy")) {
            StringSelection sl = new StringSelection(new String(txtArea.getSelectedText())); // copy only selected text
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(sl, null);
        }

        else if (e.getActionCommand().equals("Paste")) {
            txtArea.paste();
        
        }
    }
}
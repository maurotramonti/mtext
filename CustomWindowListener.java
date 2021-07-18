package mtext;

import javax.swing.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class CustomWindowListener extends MText implements WindowListener {
    public void windowClosing(WindowEvent e) {
        boolean aFileModified = false;
        for (TextFilePanel tp : tabs) {
            try {
                if (tp.getIfIsModified()) {
                    aFileModified = true;
                    break;
                }
            } catch(NullPointerException ex) {
                break;
            }
        }
        if (aFileModified) {
            int r = JOptionPane.showConfirmDialog(frame, lm.getTranslatedString(6, lang), lm.getTranslatedString(10, lang), JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) return;
            else if (r == JOptionPane.NO_OPTION) System.exit(0);
        }
        System.exit(0);
    }

    public void windowDeactivated(WindowEvent e) {
        return;
    }

    public void windowActivated(WindowEvent e) {
        return;
    }

    public void windowDeiconified(WindowEvent e) {
        return;
    }

    public void windowIconified(WindowEvent e) {
        return;
    }

    public void windowClosed(WindowEvent e) {
        return;
    }

    public void windowOpened(WindowEvent e) {
        return;
    }

}
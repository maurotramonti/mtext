package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;


// Useful personalized widgets for dialogs


class ColorLabel extends JLabel {
    ColorLabel(Color c) {
        super("");
        this.setBackground(c);
        this.setOpaque(true);
    }
}


// Buttons

class CancelButton extends JButton implements ActionListener {
    private JDialog dtc;
    
    CancelButton(JDialog dialogToClose) {
        super(LanguageManager.getTranslationsFromFile("Cancel"));
        dtc = dialogToClose;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dtc.dispose();
    }

}
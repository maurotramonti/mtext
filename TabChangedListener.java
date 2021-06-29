package mtext;

import javax.swing.event.*;

class TabChangedListener extends MText implements ChangeListener {
    public void stateChanged(ChangeEvent e) {
        if (tPane.getSelectedIndex() == -1) System.exit(0);
        frame.setTitle("Mtext - " + tabs[tPane.getSelectedIndex()].getFilePath());
    }
}

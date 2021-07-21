package mtext;

import javax.swing.event.*;

class TabChangedListener extends MText implements ChangeListener {
    public void stateChanged(ChangeEvent e) {
        if (frame.getTabPane().getSelectedIndex() == -1) System.exit(0);
        TextFilePanel[] fileTabs = frame.getFileTabs();
        frame.getFrame().setTitle("Mtext - " + fileTabs[frame.getTabPane().getSelectedIndex()].getFilePath());
    }
}

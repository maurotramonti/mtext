package mtext;

import javax.swing.event.*;

class TabChangedListener extends MText implements ChangeListener {
    public void stateChanged(ChangeEvent e) {
        String w = "";
        if (frame.getTabPane().getSelectedIndex() == -1) System.exit(0);
        TextFilePanel[] fileTabs = frame.getFileTabs();
        if ((fileTabs[frame.getTabPane().getSelectedIndex()].isWritable() == false) && fileTabs[frame.getTabPane().getSelectedIndex()].getFilePath().equals("none") == false) {
            w = new String(LanguageManager.getTranslationsFromFile("ReadOnly", frame.getLang()));
        }
        frame.getFrame().setTitle("Mtext - " + fileTabs[frame.getTabPane().getSelectedIndex()].getFilePath() + w);
    }
}

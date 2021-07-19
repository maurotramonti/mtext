package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

class TextFilePanel extends JScrollPane {
    String filePath;
    boolean isModified = false;
    TextFilePanel(String contents, String filepath, int tabSize) {
        super(new JTextArea(contents));
        filePath = new String(filepath);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        getTextArea().setTabSize(tabSize);
        getTextArea().getDocument().addDocumentListener(new TAListener());
        getTextArea().addCaretListener(new CustomCaretListener());
    }

    public JTextArea getTextArea() {
        JViewport viewport = this.getViewport(); 
        return (JTextArea) viewport.getView();
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String path) {
        filePath = new String(path);
    }

    public boolean getIfIsModified() {
        return isModified;
    }

    public void setModified(boolean b) {
        isModified = b;
    }
}
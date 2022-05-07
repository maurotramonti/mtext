package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

class TextFilePanel extends JScrollPane {
    String filePath;
    boolean isModified = false;
    boolean isWritable;
    String[] history = new String[64];
    int historyIndex = 0;
    TAListener tal = new TAListener();
    TextFilePanel(String contents, String filepath, int tabSize) {
        super(new JTextArea(contents));
        filePath = new String(filepath);
        history[0] = "";
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        getTextArea().setTabSize(tabSize);
        getTextArea().getDocument().addDocumentListener(tal);
        getTextArea().addCaretListener(new CustomCaretListener());
    }

    public JTextArea getTextArea() {
        JViewport viewport = this.getViewport(); 
        return (JTextArea) viewport.getView();
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String[] getHistory() {
        return history;
    }

    public void setHistoryIndex(int index) {
        historyIndex = index;
    }

    public void addToHistory(String contents) {
        
        if (historyIndex == 64) {       //  Deletes the oldest thing in the history and move all one position back
            for (int i = 1; i < 64; i++) {
                history[i - 1] = history[i];
            }
            historyIndex = 63;
        }
        // System.out.println("Chiamato addToHistory all'index " + historyIndex + " con parametro: " + contents);
        history[historyIndex] = contents;
        historyIndex++;
        
    }

    public int getHistoryIndex() {
        return historyIndex;
    }

    public TAListener getTAListener() {
        return tal;
    }

    public void setWritable(boolean writable) {
        isWritable = writable;
    }

    public boolean isWritable() {
        return this.isWritable;
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


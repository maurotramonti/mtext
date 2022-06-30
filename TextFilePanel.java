package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

class TextFilePanel extends JScrollPane {
    private String filePath;
    private boolean isWritable, isModified = false;
    private String[] history = new String[64];
    private int historyIndex = 0;
    private TAListener tal = new TAListener();
    private LineCounter lc;
    private JTextArea tArea;
    private JPanel internalPane = new JPanel(new BorderLayout(2, 0));
    TextFilePanel(String contents, String filepath, int tabSize, boolean lcvisibility, Color bcolor, Color fcolor, Color lcbg, Color lcfg) {
        super();
        tArea = new JTextArea(contents);
        filePath = new String(filepath);
        history[0] = "";
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.getVerticalScrollBar().setUnitIncrement(16);
        tArea.setTabSize(tabSize);
        tArea.setBackground(bcolor); tArea.setForeground(fcolor);
        tArea.getDocument().addDocumentListener(tal);
        tArea.addCaretListener(new LineColumnTracker());
        lc = new LineCounter(getTextArea().getLineCount(), lcbg, lcfg);
        
        internalPane.setBackground(bcolor);
        internalPane.add(lc, BorderLayout.WEST);
        internalPane.add(tArea, BorderLayout.CENTER);

        lc.setVisible(lcvisibility);

        this.setViewportView(internalPane);

    }

    public JTextArea getTextArea() {
        return tArea;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String[] getHistory() {
        return history;
    }

    public JPanel getInternalPanel() {
        return internalPane;
    }

    public LineCounter getLineCounter() {
        return lc;
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


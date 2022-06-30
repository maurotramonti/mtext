package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;


class LineCounter extends JTextArea {
    private int lineCount = 1;

    LineCounter(int lc, Color bg, Color fg) {
        super("");
        this.setEditable(false);
        this.setHighlighter(null);
        this.setColumns(1);
        this.setBackground(bg); this.setForeground(fg);
        setLineCount(lc);
    }

    public void setLineCount(int lc) {
        lineCount = lc;
        this.setText("");
        for (int i = 1; i <= lineCount; i++) {
            this.setText(this.getText() + Integer.toString(i) + "\n");
        }
    }
}
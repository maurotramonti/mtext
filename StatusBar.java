package mtext;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.text.*;

class LineColumnTracker extends MText implements CaretListener {
    public void caretUpdate (CaretEvent e) {
        JTextArea txt = (JTextArea)e.getSource();

        int linenum = 1;
        int columnnum = 1;
        try {
            int caretpos = txt.getCaretPosition();
            linenum = txt.getLineOfOffset(caretpos);
            columnnum = caretpos - txt.getLineStartOffset(linenum);
            frame.getStatusBar().setLC(++linenum, ++columnnum);
        }
        catch(BadLocationException ex) {}

    }
}

class StatusBar extends JLabel {
    static int lang, tabsize, col, row;
    private String tn = new String("ciaoo");
    public StatusBar(JFrame master) {
        super("Loading...", SwingConstants.RIGHT);
        super.setPreferredSize(new Dimension(master.getWidth(), 23));
        col = 1;
        row = 1;
        this.setOpaque(true);
        this.setForeground(Color.black);
    }
    public void setSbTabSize(int tabSize) {
        tabsize = tabSize;
        setStatusMessage();
    }

    public void setSbLanguage(int language) {
        lang = language;
        setStatusMessage();
    }

    public void setLC(int line, int column) {
        row = line;
        col = column;
        setStatusMessage();
    }

    public void setThemeName(String name) {
        tn = name;
        setStatusMessage();
    }

    private void setStatusMessage() {
        String msg = new String("");
        if (lang == 0) msg = new String(LanguageManager.getTranslationsFromFile("ColorTheme", lang) + tn + "        Language: English");
        else if (lang == 1) msg = new String(LanguageManager.getTranslationsFromFile("ColorTheme", lang) + tn + "        Lingua: Italiano");

        msg = new String(msg + "      " + LanguageManager.getTranslationsFromFile("TabSizeSetting", lang) + tabsize + " " +  LanguageManager.getTranslationsFromFile("Spaces", lang));
        msg = new String(msg + "        " + LanguageManager.getTranslationsFromFile("Row", lang) + row + " " + LanguageManager.getTranslationsFromFile("Column", lang) + col);

        setText("   " + msg);
    }
}
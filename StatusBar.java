package mtext;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.text.*;

class CustomCaretListener extends MText implements CaretListener {
    public void caretUpdate (CaretEvent e) {
        JTextArea txt = (JTextArea)e.getSource();

        int linenum = 1;
        int columnnum = 1;
        try {
            int caretpos = txt.getCaretPosition();
            linenum = txt.getLineOfOffset(caretpos);
            columnnum = caretpos - txt.getLineStartOffset(linenum);
            sb.setLC(++linenum, ++columnnum);
        }
        catch(BadLocationException ex) {}

    }
}

class StatusBar extends JLabel {
    static int lang, tabsize, col, row;
    public StatusBar(JFrame master) {
        super("Loading...", SwingConstants.RIGHT);
        super.setPreferredSize(new Dimension(master.getWidth(), 16));
        col = 1;
        row = 1;
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

    public void setStatusMessage() {
        LanguageManager lm = new LanguageManager();
        String msg = new String("");
        if (lang == 0) msg = new String("Language: English");
        else if (lang == 1) msg = new String("Lingua: Italiano");

        msg = new String(msg + "      " + lm.getTranslatedString(7, lang) + tabsize + lm.getTranslatedString(19, lang));
        msg = new String(msg + "        " + lm.getTranslatedString(8, lang) + row + " " + lm.getTranslatedString(9, lang) + col);

        setText(" " + msg);
    }
}
package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.Scanner;

public class MText extends JFrame {    
    static String slash;
    static String prepath;
    static JFrame frame = new JFrame("MText");
    static int lang;
    static String lineWrap;
    static int tabSize;
    static LanguageManager lm = new LanguageManager();
    static TabSizeManager tm = new TabSizeManager();
    static WrapLineSetting ws = new WrapLineSetting();
    static JTabbedPane tPane = new JTabbedPane();
    static TextFilePanel[] tabs = new TextFilePanel[64];
    static StatusBar sb = new StatusBar(frame);
    MText() {}    
    public static void main(String args[]) {
        frame.setSize(800, 600);

        final String system = System.getProperty("os.name");
        try {
            if (system.compareTo("Linux") == 0 && args[0].equals("dev") == false)  {
                slash = "/";
                prepath = "/etc/mtext/";
            }
            else if (system.equals("Linux") && args[0].equals("dev")) {
                slash = "/";
                prepath = "";
            }
            else if (system.contains("Windows")) {
                slash = "\\";
                prepath = "";
            }
        } catch (ArrayIndexOutOfBoundsException ex) {};
        loadLanguage();
        loadTabs();
        loadWrap();
        
        tabs[0] = new TextFilePanel(null, "none", tabSize);

        frame.setTitle("MText - " + lm.getTranslatedString(1, lang));
        
        String[] menuitem_lbls = lm.getTranslatedStrings(0, lang);
        String[] menuitem_acts = lm.getTranslatedStrings(0, 0);
        JMenuItem[] menuitems = new JMenuItem[10];
        KeyStroke[] accelerators = {KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), null, KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK)};
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu(lm.getTranslatedString(0, lang));
        JMenu about = new JMenu(lm.getTranslatedString(5, lang));
        tPane.addTab(lm.getTranslatedString(1, lang), null, tabs[0], null);
        tPane.addChangeListener(new TabChangedListener());

        for (int i = 0; i < 6; i++) {
            menuitems[i] = new JMenuItem(menuitem_lbls[i]);
            file.add(menuitems[i]);
            if (i == 4 || i == 0) file.addSeparator();
            menuitems[i].setActionCommand(menuitem_acts[i]);
            menuitems[i].addActionListener(new MIListener());
            if (i == 4) continue;
            menuitems[i].setAccelerator(accelerators[i]);
        } 

        
        menuitems[6] = new JMenuItem(menuitem_lbls[6]);
        edit.add(menuitems[6]);
        menuitems[6].setAccelerator(accelerators[6]);
        menuitems[6].addActionListener(new LanguageManager());
        
        menuitems[7] = new JMenuItem(menuitem_lbls[7]);
        menuitems[7].setActionCommand(menuitem_acts[7]);
        menuitems[7].addActionListener(new MIListener());

        menuitems[8] = new JMenuItem(menuitem_lbls[8]);
        edit.add(menuitems[8]);
        menuitems[8].setAccelerator(accelerators[7]);
        menuitems[8].addActionListener(new TabSizeManager());

        menuitems[9] = new JMenuItem(menuitem_lbls[9]);
        edit.add(menuitems[9]);
        menuitems[9].addActionListener(new WrapLineSetting());        

        about.add(menuitems[7]);

        menubar.add(file);
        menubar.add(edit);
        menubar.add(about);

        
        frame.setJMenuBar(menubar);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new CustomWindowListener());
        frame.getContentPane().add(tPane);
        frame.getContentPane().add(sb, BorderLayout.SOUTH);
        frame.setVisible(true);
    }   
    static void loadLanguage() {
        try {
            File file = new File(prepath + "conf" + slash + "language.txt");
            Scanner scanner = new Scanner(file);
            String l = new String();
            if (scanner.hasNextLine() == false) {
                lm.actionPerformed(null);
                loadLanguage();
            }
            l = scanner.nextLine();
            if (l.equals("Italiano")) lang = 1;
            else if (l.equals("English")) lang = 0;
            else {
                JOptionPane.showMessageDialog(frame, lm.getTranslatedString(18, lang));
                lm.actionPerformed(null);
                loadLanguage();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, lm.getTranslatedString(18, lang));
            lm.actionPerformed(null);
            loadLanguage();
        }
        sb.setSbLanguage(lang);
    }
    static void loadWrap() {
        try {
            File file = new File(prepath + "conf" + slash + "wraplines.txt");
            Scanner scanner = new Scanner(file);
            String l = new String();
            if (scanner.hasNextLine() == false) {
                ws.actionPerformed(null);
                loadWrap();
            }
            l = scanner.nextLine();
            if (l.equals("Yes")) {
                lineWrap = new String("Yes");
                for(TextFilePanel tp : tabs) {
                    try {
                        tp.getTextArea().setLineWrap(true);
                    } catch (NullPointerException ex) {
                        return;
                    }
                }
            }
            else if (l.equals("No")) {
                lineWrap = new String("No");
                for(TextFilePanel tp : tabs) {
                    try {
                        tp.getTextArea().setLineWrap(false);
                    } catch (NullPointerException ex) {
                        return;
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(frame, lm.getTranslatedString(18, lang));
                ws.actionPerformed(null);
                loadWrap();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, lm.getTranslatedString(18, lang));
            ws.actionPerformed(null);
            loadWrap();
        }
    }
    static void loadTabs() {
        try {
            File file = new File(prepath + "conf" + slash + "tabsize.txt");
            Scanner scanner = new Scanner(file);
            String l = new String();
            if (scanner.hasNextLine() == false) {
                tm.actionPerformed(null);
                loadTabs();
            }
            l = scanner.nextLine();
            if (l.equals("2 spaces")) {
                tabSize = 2;
                sb.setSbTabSize(tabSize);
                for(TextFilePanel tp : tabs) {
                    try {
                        tp.getTextArea().setTabSize(2);
                    } catch (NullPointerException ex) {
                        return;
                    }
                }
            }
            else if (l.equals("4 spaces")) {
                tabSize = 4;
                sb.setSbTabSize(tabSize);
                for(TextFilePanel tp : tabs) {
                    try {
                        tp.getTextArea().setTabSize(4);
                    } catch (NullPointerException ex) {
                        return;
                    }
                }
            }
            else if (l.equals("8 spaces")) {
                tabSize = 8;
                sb.setSbTabSize(tabSize);
                for(TextFilePanel tp : tabs) {
                    try {
                        tp.getTextArea().setTabSize(8);
                    } catch (NullPointerException ex) {
                        return;
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(frame, lm.getTranslatedString(18, lang));
                tm.actionPerformed(null);
                loadTabs();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, lm.getTranslatedString(18, lang));
            tm.actionPerformed(null);
            loadTabs();
        }

    }    
}


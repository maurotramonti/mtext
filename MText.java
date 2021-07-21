package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.Scanner;

class SysConst {
    public static String getSlash() {
        final String system = System.getProperty("os.name");
        if (system.contains("Windows")) return "\\";
        else return "/";
    }
    public static String getPrePath() {
        final String system = System.getProperty("os.name");
        if (system.contains("Windows")) return "";
        else return "/etc/mtext/";
    }
}

class MTextFrame extends JFrame {
    JFrame frame;
    int lang, tabSize;
    boolean lineWrap;
    TextFilePanel[] fileTabs = new TextFilePanel[64];
    JTabbedPane tPane = new JTabbedPane();
    String lastFileOpened = new String("");
    StatusBar sb;
    SideBar sib;
    JMenu recentFiles;
    
    public MTextFrame() {
        super("MText");
        frame = this;
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new CustomWindowListener());
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("/usr/share/icons/mtext.png"));

        sb = new StatusBar(frame);

        loadLanguage();
        loadTabs();
        loadWrap();

        recentFiles = new JMenu(LanguageManager.getTranslatedString(22, lang));
        loadRecentFiles();

        sib = new SideBar(frame, lang);

        fileTabs[0] = new TextFilePanel(null, "none", tabSize);
        tPane.addTab(LanguageManager.getTranslatedString(1, lang), null, fileTabs[0], null);
        tPane.addChangeListener(new TabChangedListener());

        String[] menuItemLbls = LanguageManager.getTranslatedStrings(0, lang);
        String[] menuItemActs = LanguageManager.getTranslatedStrings(0, 0);
        JMenuItem[] menuItems = new JMenuItem[11];
        KeyStroke[] accelerators = {KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK), null, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), null, KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK)};

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu(LanguageManager.getTranslatedString(0, lang));
        JMenu about = new JMenu(LanguageManager.getTranslatedString(5, lang));

        for (int i = 0; i < 7; i++) {
            menuItems[i] = new JMenuItem(menuItemLbls[i]);
            file.add(menuItems[i]);
            if (i == 0) {
                file.addSeparator();
                file.add(recentFiles);
            }
            menuItems[i].setActionCommand(menuItemActs[i]);
            menuItems[i].addActionListener(new MIListener());
            if (i == 5) {
                file.addSeparator();
                continue;
            }
            menuItems[i].setAccelerator(accelerators[i]);
        } 

        menuItems[7] = new JMenuItem(menuItemLbls[7]);
        edit.add(menuItems[7]);
        menuItems[7].setAccelerator(accelerators[7]);
        menuItems[7].addActionListener(new LanguageManager());
        
        menuItems[8] = new JMenuItem(menuItemLbls[8]);
        menuItems[8].setActionCommand(menuItemActs[8]);
        menuItems[8].addActionListener(new MIListener());

        menuItems[9] = new JMenuItem(menuItemLbls[9]);
        edit.add(menuItems[9]);
        menuItems[9].setAccelerator(accelerators[8]);
        menuItems[9].addActionListener(new TabSizeManager());

        menuItems[10] = new JMenuItem(menuItemLbls[10]);
        edit.add(menuItems[10]);
        menuItems[10].addActionListener(new WrapLineSetting());        

        about.add(menuItems[8]);

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(about);

        frame.setJMenuBar(menuBar);

        frame.add(tPane);
        frame.add(sb, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadRecentFiles() {
        String s = new String();
        try {
            File file = new File(SysConst.getPrePath() + "conf" + SysConst.getSlash() + "recentfiles.txt");
            Scanner scanner = new Scanner(file);
            JMenuItem mi;
            if (scanner.hasNextLine() == false) {
                recentFiles.add(new JMenuItem(LanguageManager.getTranslatedString(23, lang)));
                return;
            } else {
                do {
                    mi = new JMenuItem(scanner.nextLine());
                    mi.setActionCommand("Recent file");
                    mi.addActionListener(new MIListener());
                    recentFiles.add(mi);

                } while (scanner.hasNextLine());
                
            }
        } catch (IOException ex) {};
    }

    protected void loadLanguage() {
        LanguageManager lm = new LanguageManager();
        try {
            File file = new File(SysConst.getPrePath() + "conf" + SysConst.getSlash() + "language.txt");
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
                JOptionPane.showMessageDialog(frame, LanguageManager.getTranslatedString(18, lang));
                lm.actionPerformed(null);
                loadLanguage();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslatedString(18, lang));
            lm.actionPerformed(null);
            loadLanguage();
        }
        sb.setSbLanguage(lang);
    }

    protected void loadTabs() {
        TabSizeManager tm = new TabSizeManager();
        try {
            File file = new File(SysConst.getPrePath() + "conf" + SysConst.getSlash() + "tabsize.txt");
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
                for(TextFilePanel tp : fileTabs) {
                    try {
                        tp.getTextArea().setTabSize(2);
                    } catch (NullPointerException ex) {
                        return;
                    }
                }
            } else if (l.equals("4 spaces")) {
                tabSize = 4;
                sb.setSbTabSize(tabSize);
                for(TextFilePanel tp : fileTabs) {
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
                for(TextFilePanel tp : fileTabs) {
                    try {
                        tp.getTextArea().setTabSize(8);
                    } catch (NullPointerException ex) {
                        return;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, LanguageManager.getTranslatedString(18, lang));
                tm.actionPerformed(null);
                loadTabs();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslatedString(18, lang));
            tm.actionPerformed(null);
            loadTabs();
        }
    }

    protected void loadWrap() {
        WrapLineSetting ws = new WrapLineSetting();
        try {
            File file = new File(SysConst.getPrePath() + "conf" + SysConst.getSlash() + "wraplines.txt");
            Scanner scanner = new Scanner(file);
            String l = new String();
            if (scanner.hasNextLine() == false) {
                ws.actionPerformed(null);
                loadWrap();
            }
            l = scanner.nextLine();
            if (l.equals("Yes")) {
                lineWrap = true;
                for(TextFilePanel tp : fileTabs) {
                    try {
                        tp.getTextArea().setLineWrap(true);
                    } catch (NullPointerException ex) {
                        return;
                    }
                }
            } else if (l.equals("No")) {
                lineWrap = false;
                for(TextFilePanel tp : fileTabs) {
                    try {
                        tp.getTextArea().setLineWrap(false);
                    } catch (NullPointerException ex) {
                        return;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, LanguageManager.getTranslatedString(18, lang));
                ws.actionPerformed(null);
                loadWrap();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslatedString(18, lang));
            ws.actionPerformed(null);
            loadWrap();
        }
    }

    public SideBar getSideBar() {
        return sib;
    }

    public StatusBar getStatusBar() {
        return sb;
    }

    public boolean getLineWrap() {
        return lineWrap;
    }

    public int getTabSize() {
        return tabSize;
    }

    public void setLFO(String str) {
        lastFileOpened = new String(str);
    }

    public String getLFO() {
        return lastFileOpened;
    }

    public int getLang() {
        return lang;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JTabbedPane getTabPane() {
        return tPane;
    }

    public TextFilePanel[] getFileTabs() {
        return fileTabs;
    }
}

    

public class MText {
    static MTextFrame frame;
    public static void main(String[] args) {
        frame = new MTextFrame();
    }
}

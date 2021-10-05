package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.Scanner;

class SysConst {
    static final String system = System.getProperty("os.name");
    public static String getPrePath() {
        if (system.contains("Windows")) return System.getenv("LOCALAPPDATA") + "\\mtext\\";
        else return "/etc/mtext/";
    }
    public static String getLogoPath() {
        if (system.contains("Windows")) return "mtext.png";
        else return "/usr/share/icons/mtext.png";
    }
    public static String getJavaLogoPath() {
        if (system.contains("Windows")) return "javalogo.png";
        else return "/usr/share/icons/javalogo.png";
    }
}

class MTextFrame extends JFrame {
    private JFrame frame;
    private int lang, tabSize;
    private boolean lineWrap;
    private TextFilePanel[] fileTabs = new TextFilePanel[64];
    private JTabbedPane tPane = new JTabbedPane();
    private String lastFileOpened = new String("");
    private StatusBar sb;
    private SideBar sib;
    private JMenu recentFiles;
    static MIListener x = new MIListener();
    
    MTextFrame(String[] args) {
        super("MText");
        frame = this;
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new CustomWindowListener());
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(SysConst.getLogoPath()));

        sb = new StatusBar(frame);

        loadLanguage();
        loadTabs();
        loadWrap();

        recentFiles = new JMenu(LanguageManager.getTranslationsFromFile("RecentFiles", lang));
        loadRecentFiles();

        sib = new SideBar(frame, lang);
        if (args.length == 0) {
            fileTabs[0] = new TextFilePanel(null, "none", tabSize);
            tPane.addTab(LanguageManager.getTranslationsFromFile("Untitled", lang), null, fileTabs[0], null);
        } else {
            for (int i = 0; i < args.length; i++) {
                String contents = new String();
                try {
                    File file = new File(args[i]);
                    Scanner scanner = new Scanner(file);
                    while(scanner.hasNextLine()) {
                        contents = contents + scanner.nextLine() + '\n';
                    }
                    getFileTabs()[getTabPane().getSelectedIndex() + 1] = new TextFilePanel(contents, args[i], /*tabSize*/4);
                    getTabPane().addTab(args[i], null, getFileTabs()[getTabPane().getSelectedIndex() + 1], null);
                    getTabPane().setSelectedIndex(getTabPane().getSelectedIndex());  
                    getFileTabs()[getTabPane().getSelectedIndex()].setModified(false);
                    scanner.close();
                    setTitle("MText - " + getFileTabs()[getTabPane().getSelectedIndex()].getFilePath());
                    setLFO(args[i]);
                    BufferedWriter bw = new BufferedWriter(new FileWriter(SysConst.getPrePath() + "conf" + File.separator + "recentfiles.txt", true));
                    bw.write(args[i] + "\n");
                    bw.close();
                }
                catch(IOException ex) {
                    JOptionPane.showMessageDialog(this, LanguageManager.getTranslationsFromFile("ReadingError", lang));
                }
            }                                 
                }

        tPane.addChangeListener(new TabChangedListener());
        

        String[] menuItemLbls = LanguageManager.getTranslatedStrings(3, lang);
        String[] menuItemActs = LanguageManager.getTranslatedStrings(0, 0);
        JMenuItem[] menuItems = new JMenuItem[13];
        KeyStroke[] accelerators = {KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK), null, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), null, KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK)};

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu(LanguageManager.getTranslationsFromFile("Preferences", lang));
        JMenu about = new JMenu(LanguageManager.getTranslationsFromFile("Help", lang));

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

        menuItems[11] = new JMenuItem(menuItemLbls[11]);
        edit.add(menuItems[11]);
        menuItems[11].addActionListener(new ThemeManager());   

        menuItems[12] = new JMenuItem(menuItemLbls[12]);
        menuItems[12].setActionCommand("Java info");
        menuItems[12].addActionListener(new MIListener()); 

        about.add(menuItems[8]);
        about.add(menuItems[12]);

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
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "recentfiles.txt");
            Scanner scanner = new Scanner(file);
            JMenuItem mi;
            if (scanner.hasNextLine() == false) {
                recentFiles.add(new JMenuItem(LanguageManager.getTranslationsFromFile("NoRecentFiles", lang)));
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
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "language.txt");
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
                JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang));
                lm.actionPerformed(null);
                loadLanguage();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang));
            lm.actionPerformed(null);
            loadLanguage();
        }
        sb.setSbLanguage(lang);
    }

    protected void loadTabs() {
        TabSizeManager tm = new TabSizeManager();
        try {
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "tabsize.txt");
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
                JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang));
                tm.actionPerformed(null);
                loadTabs();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang));
            tm.actionPerformed(null);
            loadTabs();
        }
    }

    protected void loadWrap() {
        WrapLineSetting ws = new WrapLineSetting();
        try {
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "wraplines.txt");
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
                JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang));
                ws.actionPerformed(null);
                loadWrap();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang));
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
    static int theme = 0;
    public static void main(String[] args) {
        try {
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "theme.txt");
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine() == false) {
                scanner.close();
                ThemeManager tm = new ThemeManager();
                tm.actionPerformed(null);
                
            } else {
                String s = scanner.nextLine();
                if (s.equals("0")) theme = 0;
                else if (s.equals("1")) theme = 1;
                scanner.close();
            }
        } catch (IOException ex) {}
        try {
            if (theme == 0) UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            else if (theme == 1) UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
       // handle exception
        }
        catch (ClassNotFoundException e) {
       // handle exception
        }
        catch (InstantiationException e) {
       // handle exception
        }
        catch (IllegalAccessException e) {
       // handle exception
        }
        frame = new MTextFrame(args);
    }
}

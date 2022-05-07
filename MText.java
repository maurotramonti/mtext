package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.Scanner;
import java.net.*;

class SysConst {
    static final String system = System.getProperty("os.name");
    public static String getPrePath() {
        if (system.contains("Windows")) return System.getenv("LOCALAPPDATA") +  "\\mtext\\";
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

    private StatusBar sb;
    private SideBar sib;

    private JMenuBar menuBar;
    private JMenu file, edit, preferences, about, recentFiles;
    private String[] menuItemLbls, menuItemActs, actuallyOpenedFiles = new String[64];
    private JMenuItem[] menuItems = new JMenuItem[19];
    private final KeyStroke[] accelerators = {KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK), null, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), null, KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK), null, null, null, null, null};
    
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

        try {
            BufferedReader br = new BufferedReader(new FileReader(SysConst.getPrePath() + "conf" + File.separator + "lastcheck.txt"));
            String lc = br.readLine();
            final long millis = Long.parseLong(lc);
            br.close();
            if ((System.currentTimeMillis() - millis) >= 172800000) { // sono passati almeno 2 giorni
                checkUpdates(true);
            }
        } catch (IOException ex) {}


        sib = new SideBar(frame, lang);
        try {
            File fb = new File(SysConst.getPrePath() + "conf" + File.separator + "fb.txt");
            Scanner s = new Scanner(fb);
            String st = s.nextLine();
            s.close();
            if (st.equals("1")) {
                JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("Changelog", lang), "Changelog", JOptionPane.INFORMATION_MESSAGE);
                BufferedWriter bw = new BufferedWriter(new FileWriter(fb));
                bw.write("0");
                bw.close();
            } 
            
        } catch (IOException ex) {}

        if (args.length == 0) {
            fileTabs[0] = new TextFilePanel(null, "none", tabSize);
            tPane.addTab(LanguageManager.getTranslationsFromFile("Untitled", lang), null, fileTabs[0], null);
        } else {
            for (int i = 0; i < args.length; i++) {
                String contents = new String();
                try {
                    String fullfilename = new String("");
                    if (args[i].startsWith("\'") && args[i].endsWith("\'") == false) { // verifica se l'argomento è solo un nome parziale di file (gli apici non si chiudono)
                        fullfilename = new String(args[i]);
                        do {
                            fullfilename = new String(fullfilename + " " + args[i + 1]);
                            i++;
                            
                        } while (fullfilename.endsWith("\'") == false);
                        fullfilename = new String(fullfilename.replace("\'", "")); // rimuove gli apici dalla path
                    }
                    File file = new File(fullfilename);
                    Scanner scanner = new Scanner(file);
                    while(scanner.hasNextLine()) {
                        contents = contents + scanner.nextLine() + '\n';
                    }
                    getFileTabs()[getTabPane().getSelectedIndex() + 1] = new TextFilePanel(contents, fullfilename, tabSize);
                    getTabPane().addTab(fullfilename, null, getFileTabs()[getTabPane().getSelectedIndex() + 1], null);
                    getTabPane().setSelectedIndex(getTabPane().getSelectedIndex());  
                    getFileTabs()[getTabPane().getSelectedIndex()].setModified(false);
                    File f = new File(fullfilename);
                    getFileTabs()[getTabPane().getSelectedIndex()].setWritable(f.canWrite());
                    scanner.close();
                    setTitle("MText - " + getFileTabs()[getTabPane().getSelectedIndex()].getFilePath());
                    actuallyOpenedFiles[getTabPane().getSelectedIndex()] = fullfilename;
                    
                    BufferedWriter bw = new BufferedWriter(new FileWriter(SysConst.getPrePath() + "conf" + File.separator + "recentfiles.txt", true));
                    bw.write(fullfilename + "\n");
                    bw.close();                 
                    
                }
                catch(IOException ex) {
                    JOptionPane.showMessageDialog(this, LanguageManager.getTranslationsFromFile("ReadingError", lang));
                    fileTabs[0] = new TextFilePanel(null, "none", tabSize);
                    tPane.addTab(LanguageManager.getTranslationsFromFile("Untitled", lang), null, fileTabs[0], null);
                }
            }                                 
                }

        tPane.addChangeListener(new TabChangedListener());      
        

        menuItemLbls = LanguageManager.getTranslatedStrings(3, lang);
        menuItemActs = LanguageManager.getTranslatedStrings(0, 0);
        
        

        menuBar = new JMenuBar();
        file = new JMenu("File");
        edit = new JMenu(LanguageManager.getTranslationsFromFile("Edit", lang));
        preferences = new JMenu(LanguageManager.getTranslationsFromFile("Preferences", lang));
        about = new JMenu(LanguageManager.getTranslationsFromFile("Help", lang));

/*      File menu entries cycle      */

        for (int i = 0; i < 7; i++) {
            menuItems[i] = new JMenuItem(menuItemLbls[i]);
            file.add(menuItems[i]);
            if (i == 0) {
                file.addSeparator();
                file.add(recentFiles);
            }
            menuItems[i].setActionCommand(LanguageManager.getTranslatedStrings(0, 0)[i]);
            menuItems[i].addActionListener(new FileMenuHandler());
            if (i == 5) {
                file.addSeparator();
                continue;
            }
            menuItems[i].setAccelerator(accelerators[i]);
        } 

/*    Edit menu entries cycle    */

        for (int i = 7; i < 12; i++) {
            menuItems[i] = new JMenuItem(menuItemLbls[i]);
            edit.add(menuItems[i]);
            menuItems[i].setActionCommand(menuItemActs[i]);
            menuItems[i].addActionListener(new EditMenuHandler());
            menuItems[i].setAccelerator(accelerators[i]);
            if (i == 8) edit.addSeparator();
        }

/*    Preferences menu entries cycle    */

        for (int i = 12; i < 16; i++) {
            menuItems[i] = new JMenuItem(menuItemLbls[i]);
            preferences.add(menuItems[i]);
            menuItems[i].setActionCommand(menuItemActs[i]);
            menuItems[i].addActionListener(new PreferencesMenuHandler());
            menuItems[i].setAccelerator(accelerators[i]);
        } 

/*    About menu entries cycle    */

        for (int i = 16; i < 19; i++) {
            menuItems[i] = new JMenuItem(menuItemLbls[i]);
            about.add(menuItems[i]);
            menuItems[i].setActionCommand(menuItemActs[i]);
            menuItems[i].addActionListener(new AboutMenuHandler());
            menuItems[i].setAccelerator(accelerators[i]);
        }
        

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(preferences);
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
                    mi.addActionListener(new FileMenuHandler());
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
        PreferencesMenuHandler pmh = new PreferencesMenuHandler();
        try {
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "tabsize.txt");
            Scanner scanner = new Scanner(file);
            String l = new String();
            if (scanner.hasNextLine() == false) {
                pmh.tabSizeRoutine();
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
                pmh.tabSizeRoutine();
                loadTabs();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang));
            pmh.tabSizeRoutine();
            loadTabs();
        }
    }

    protected void loadWrap() {
        PreferencesMenuHandler pmh = new PreferencesMenuHandler();
        try {
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "wraplines.txt");
            Scanner scanner = new Scanner(file);
            String l = new String();
            if (scanner.hasNextLine() == false) {
                pmh.autoNewlineRoutine();
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
                pmh.autoNewlineRoutine();
                loadWrap();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang));
            pmh.autoNewlineRoutine();
            loadWrap();
        }
    }

    public void checkUpdates(boolean showOnlyIfPositive) {
        final int internalVersion = 230;
        final String internalVersionString = new String("230");
        try {
            URL url = new URL("https://raw.githubusercontent.com/maurotramonti/mtext/main/conf/latest.txt");

                InputStream is = url.openStream();
                // Stream to the destionation file
                FileOutputStream fos = new FileOutputStream(SysConst.getPrePath() + "conf" + File.separator + "latest.txt");
		        // Read bytes from URL to the local file
                byte[] buffer = new byte[4096];
                int bytesRead = 0;

                System.out.println("[DEBUG] Downloading latest.txt");
                while ((bytesRead = is.read(buffer)) != -1) {
        	        fos.write(buffer, 0, bytesRead);
                }

                // Close destination stream
                fos.close();
                // Close URL stream
                is.close();
                File file = new File(SysConst.getPrePath() + "conf" + File.separator + "latest.txt");
                Scanner scanner = new Scanner(file);
                String l = new String();
                l = scanner.nextLine();
                int internalVersionRead = Integer.parseInt(l);
                System.out.println(internalVersionRead);
                if (internalVersionRead > internalVersion) {
                    JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("CUTxtY", lang), LanguageManager.getTranslationsFromFile("CUTtl", lang), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    if (showOnlyIfPositive == false) JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("CUTxtN", lang), LanguageManager.getTranslationsFromFile("CUTtl", lang), JOptionPane.INFORMATION_MESSAGE);                    
                }
                scanner.close();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write(internalVersionString);
                bw.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("CheckUpdatesErr", lang), LanguageManager.getTranslationsFromFile("Warning", lang), JOptionPane.ERROR_MESSAGE);
                return;
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

    public int getLang() {
        return lang;
    }

    public JFrame getFrame() {
        return frame;
    }

    public String[] getActuallyOpenedFiles() {
        return actuallyOpenedFiles;
    }

    public void addActualOpenedFile(String path, int index) {
        actuallyOpenedFiles[index] = new String(path);
        System.out.println("[DEBUG] Aggiunto file alla tabella con indice " + index + "e percorso " + path);
    }

    public void removeOpenedFileAt(int index) {
        actuallyOpenedFiles[index] = new String("");
        System.out.println("[DEBUG] Rimosso file dalla tabella all\'indice " + index);
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
                PreferencesMenuHandler pmh = new PreferencesMenuHandler();
                pmh.themeRoutine();
                
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

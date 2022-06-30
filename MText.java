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
        if (system.contains("Windows")) return System.getenv("PROGRAMFILES") + "\\mtext\\mtext.png";
        else return "/usr/share/icons/mtext.png";
    }
    public static String getJavaLogoPath() {
        if (system.contains("Windows")) return System.getenv("PROGRAMFILES") + "\\mtext\\javalogo.png";
        else return "/usr/share/icons/javalogo.png";
    }
}

class MTextFrame extends JFrame implements WindowListener {
    private JFrame frame;

    private String currentThemeName;

    private int lang, tabSize;
    private boolean lineWrap, globalLineCounterVisibility;

    protected Color lcbg, lcfg = Color.black, ftbg, ftfg = Color.black;

    private TextFilePanel[] fileTabs = new TextFilePanel[64];
    private JTabbedPane tPane = new JTabbedPane();

    private StatusBar sb;
    private SideBar sib;

    private JMenuBar menuBar = new JMenuBar();
    private JMenu file, edit, preferences, about, recentFiles;
    private String[] menuItemLbls, menuItemActs, actuallyOpenedFiles = new String[64];
    private JMenuItem[] menuItems = new JMenuItem[15];
    private final KeyStroke[] accelerators = {KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK), null, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), null, KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), null, null, null, null, null};

    
    MTextFrame(String[] args) {
        super("MText");
        frame = this;
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(this);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(SysConst.getLogoPath()));

        sb = new StatusBar(frame);


        loadLanguage();
        loadTabs();
        loadWrap();
        loadAppearance();
        

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
            fileTabs[0] = new TextFilePanel(null, "none", tabSize, true, ftbg, ftfg, lcbg, lcfg);
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
                    getFileTabs()[getTabPane().getSelectedIndex() + 1] = new TextFilePanel(contents, fullfilename, tabSize, true, ftbg, ftfg, lcbg, lcfg);
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
                    fileTabs[0] = new TextFilePanel(null, "none", tabSize, true, ftbg, ftfg, lcbg, lcfg);
                    tPane.addTab(LanguageManager.getTranslationsFromFile("Untitled", lang), null, fileTabs[0], null);
                }
            }                                 
                }
        tPane.addChangeListener(new TabChangedListener());    
        

        menuItemLbls = LanguageManager.getMenuItemsInfo(LanguageManager.MI_LBLS, lang);
        menuItemActs = LanguageManager.getMenuItemsInfo(LanguageManager.MI_ACTS, 0);
        
        
        file = new JMenu("File");
        edit = new JMenu(LanguageManager.getTranslationsFromFile("Edit", lang));
        preferences = new JMenu(LanguageManager.getTranslationsFromFile("Preferences", lang)); 
        JMenuItem settings = new JMenuItem(LanguageManager.getTranslationsFromFile("Settings", lang)); settings.setActionCommand("Settings"); settings.addActionListener(new PreferencesMenuHandler()); preferences.add(settings); settings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.SHIFT_DOWN_MASK));
        JMenuItem appearance = new JMenuItem(LanguageManager.getTranslationsFromFile("Appearance", lang)); appearance.setActionCommand("Appearance"); appearance.addActionListener(new PreferencesMenuHandler()); preferences.add(appearance); appearance.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        
        about = new JMenu(LanguageManager.getTranslationsFromFile("Help", lang));

/*      File menu entries cycle      */

        for (int i = 0; i < 7; i++) {
            menuItems[i] = new JMenuItem(menuItemLbls[i]);
            file.add(menuItems[i]);
            if (i == 0) {
                file.addSeparator();
                file.add(recentFiles);
            }
            menuItems[i].setActionCommand(menuItemActs[i]);
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



/*    About menu entries cycle    */

        for (int i = 12; i < 15; i++) {
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
        try {
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "language.txt");
            Scanner scanner = new Scanner(file);
            String l = new String();
            if (scanner.hasNextLine() == false) {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file)); bw.write("English"); bw.close();
                scanner.close();
                loadLanguage();
            }
            l = scanner.nextLine();
            scanner.close();
            if (l.equals("Italiano")) lang = LanguageManager.ITALIAN;
            else if (l.equals("English")) lang = LanguageManager.ENGLISH;
            else {
                String[] langs = {"English", "Italiano"};
                String s = (String) JOptionPane.showInputDialog(frame, LanguageManager.getTranslationsFromFile("ChooseLanguage", lang), LanguageManager.getTranslationsFromFile("Warning", lang), JOptionPane.PLAIN_MESSAGE, null, langs, 0);
                if (s != null) {

                    FileWriter fw = new FileWriter(file);
                    BufferedWriter br = new BufferedWriter(fw);
                    br.write(s);
                    br.close();
                } else {                    
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                    bw.write("English"); bw.close();                    
                }
                loadLanguage();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang));
            try {
                File file = new File(SysConst.getPrePath() + "conf" + File.separator + "language.txt");
                BufferedWriter bw = new BufferedWriter(new FileWriter(file)); bw.write("English"); bw.close();
                loadLanguage();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        }
        sb.setSbLanguage(lang);
    }

    protected void loadTabs() {
        try {
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "tabsize.txt");
            Scanner scanner = new Scanner(file);
            String l = new String();
            if (scanner.hasNextLine() == false) {
                scanner.close();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write("4");
                bw.close();                
                loadTabs();
            }
            l = scanner.nextLine();
            
            tabSize = Integer.parseInt(l);
            sb.setSbTabSize(tabSize);
            for(TextFilePanel tp : fileTabs) {
                try {
                    tp.getTextArea().setTabSize(tabSize);
                } catch (NullPointerException ex) {
                    return;
                }
            }   
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang));
            try {
                File file = new File(SysConst.getPrePath() + "conf" + File.separator + "tabsize.txt");
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write("4");
                bw.close();  
                loadTabs(); 
            } catch (IOException ex) {
                ex.printStackTrace();
            }
                
            
        }
    }

    protected void loadWrap() {
        try {
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "wraplines.txt");
            Scanner scanner = new Scanner(file);
            String l = new String();
            if (scanner.hasNextLine() == false) {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file)); bw.write("No"); bw.close();
                scanner.close();
                loadWrap();
            }
            l = scanner.nextLine();
            scanner.close();
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
                BufferedWriter bw = new BufferedWriter(new FileWriter(file)); bw.write("No"); bw.close();
                loadWrap();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang));
            try {
                File file = new File(SysConst.getPrePath() + "conf" + File.separator + "wraplines.txt");
                BufferedWriter bw = new BufferedWriter(new FileWriter(file)); bw.write("No"); bw.close();
                loadWrap();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
        }
    }

    public void checkUpdates(boolean showOnlyIfPositive) {
        final int internalVersion = 250;
        final String internalVersionString = new String("250");
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
                File lastcheckfile = new File(SysConst.getPrePath() + "conf" + File.separator + "lastcheck.txt");
                bw = new BufferedWriter(new FileWriter(lastcheckfile));
                bw.write(Long.toString(System.currentTimeMillis()));
                bw.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("CheckUpdatesErr", lang), LanguageManager.getTranslationsFromFile("Warning", lang), JOptionPane.ERROR_MESSAGE);
                return;
            }       
        
    }

    private void loadTheme(String name) {
        File themeFile = new File(SysConst.getPrePath() + "themes" + File.separator + name + ".txt");
        try {
            Scanner scanner = new Scanner(themeFile);
            String s = scanner.nextLine();
            String[] rgb = s.split(";");
            lcbg = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
            for (TextFilePanel fp : fileTabs) {
                try {
                    fp.getLineCounter().setBackground(lcbg);
                } catch (NullPointerException e) {
                    break;
                }
            }
            

            // status bar color

            s = scanner.nextLine();
            rgb = s.split(";");
            sb.setBackground(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
            
            // text background

            s = scanner.nextLine();
            rgb = s.split(";");

            ftbg = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));

            for (TextFilePanel fp : fileTabs) {
                try {
                    fp.getTextArea().setBackground(ftbg);
                    fp.getInternalPanel().setBackground(ftbg);
                } catch (NullPointerException ex) {
                    break;
                }
            }

            
            

            // menubar background

            s = scanner.nextLine();
            rgb = s.split(";");

            menuBar.setBackground(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
            tPane.setBackground(menuBar.getBackground()); tPane.setOpaque(true);
            // veryfing if white text is set

            // line counter

            if (scanner.nextLine().equals("1")) {
                lcfg = Color.white;
            } else lcfg = Color.black;

            for (TextFilePanel fp : fileTabs) {
                try {
                    fp.getLineCounter().setForeground(lcfg);
                } catch (NullPointerException ex) {
                    break;
                }
            }

            // status bar
            if (scanner.nextLine().equals("1")) {
                sb.setForeground(Color.white);
            } else sb.setForeground(Color.black);

            // text color

            if (scanner.nextLine().equals("1")) ftfg = Color.white;
            else ftfg = Color.black;

            for (TextFilePanel fp : fileTabs) {
                try {
                    fp.setForeground(ftfg);
                } catch (NullPointerException ex) {
                    break;
                }
            }
            
            setThemeName(name);
            sb.setThemeName(name);
            scanner.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang), LanguageManager.getTranslationsFromFile("Warning", lang), JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadAppearance() {
        File conffile = new File(SysConst.getPrePath() + "conf" + File.separator + "appearance.txt");
        try {
            Scanner s = new Scanner(conffile);
            String themeName = s.nextLine();

            loadTheme(themeName);

            if (s.nextLine().equals("1")) {
                globalLineCounterVisibility = true;
                for (TextFilePanel fp : fileTabs) {
                    try {
                        fp.getLineCounter().setVisible(true);
                    } catch (NullPointerException ex) {
                        break;
                    }
                }
            } else {
                globalLineCounterVisibility = false;
                for (TextFilePanel fp : fileTabs) {
                    try {
                        fp.getLineCounter().setVisible(false);
                    } catch (NullPointerException ex) {
                        break;
                    }
                }
            }

            if (s.nextLine().equals("1")) sb.setVisible(true);
            else sb.setVisible(false);

            s.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, LanguageManager.getTranslationsFromFile("SettingFileError", lang), LanguageManager.getTranslationsFromFile("Warning", lang), JOptionPane.ERROR_MESSAGE);
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(conffile));
                bw.write("Basic\n1\n1");
                bw.close();
            } catch (IOException ex2) {}
        }
    }

    public void setThemeName(String name) {
        currentThemeName = name;
    }

    public String getThemeName() {
        return currentThemeName;
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
    }

    public void removeOpenedFileAt(int index) {
        actuallyOpenedFiles[index] = new String("");
    }

    public JTabbedPane getTabPane() {
        return tPane;
    }

    public TextFilePanel[] getFileTabs() {
        return fileTabs;
    }

    public boolean getLineCounterVisibility() {
        return globalLineCounterVisibility;
    }

    // Custom window listener

    @Override
    public void windowClosing(WindowEvent e) {
        boolean aFileModified = false;
        for (TextFilePanel tp : fileTabs) {
            try {
                if (tp.getIfIsModified()) {
                    aFileModified = true;
                    break;
                }
            } catch(NullPointerException ex) {
                break;
            }
        }
        if (aFileModified) {
            int r = JOptionPane.showConfirmDialog(frame, LanguageManager.getTranslationsFromFile("SomeFilesUnsaved"), LanguageManager.getTranslationsFromFile("Warning"), JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) return;
            else if (r == JOptionPane.NO_OPTION) System.exit(0);
        }
        System.exit(0);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowOpened(WindowEvent e) {}

}

    

public class MText {
    public static final int SYSTEM_THEME = 0; public static final int CROSS_PLATFORM_THEME = 1;
    
    protected static MTextFrame frame;
    protected static int theme = 0;
    
    public static void main(String[] args) {
        try {
            File file = new File(SysConst.getPrePath() + "conf" + File.separator + "theme.txt");
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine() == false) {
                scanner.close();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file)); bw.write("Cross-Platform"); bw.close();
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());          

                
            } else {
                String s = scanner.nextLine();
                if (s.equals("Cross-Platform")) {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    theme = CROSS_PLATFORM_THEME;
                }
                else if (s.equals("System")) {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    theme = SYSTEM_THEME;
                }
                scanner.close();
            }
        } catch (Exception ex) {}
        frame = new MTextFrame(args);
    }
}

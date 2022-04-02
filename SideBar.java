package mtext;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

class SideBar extends JPanel {
    boolean aDirectoryOpened = false;
    String directoryPath;
    JLabel lbl;
    JButton closeSidebar;
    String[] filesPaths = {"No directory opened"};
    JComboBox filesList = new JComboBox(filesPaths);
    SideBarListener sbl = new SideBarListener();
    SideBar(JFrame master, int lang) {
        super();
        setPreferredSize(new Dimension(190, 600));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        lbl = new JLabel(LanguageManager.getTranslationsFromFile("ExploreFiles", lang));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(lbl);
        filesList.setMaximumSize(new Dimension(183, 22));
        filesList.addItemListener(sbl);
        add(filesList);
        closeSidebar = new JButton(LanguageManager.getTranslationsFromFile("Close", lang));
        closeSidebar.addActionListener(new CloseSidebarListener());
        closeSidebar.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(closeSidebar);
    }

    public boolean isDirectoryOpened() {
        return aDirectoryOpened;
    }

    public String getCurrentDir() {
        return directoryPath;
    }

    public void closeDir() {
        if (aDirectoryOpened) {
            sbl.eventState(false);
            directoryPath = new String("");
            aDirectoryOpened = false;
            filesPaths = null;
            filesList.removeAllItems();
        }
    }

    public void openDir(String path) {
        sbl.eventState(false);
        directoryPath = new String(path);
        aDirectoryOpened = true;
        File dir = new File(path);     
        filesPaths = dir.list(new FNFilter());   
        filesList.removeAllItems();
        for (String str : filesPaths) {
            filesList.addItem(str);
        }
        sbl.eventState(true);
    }
}

class SideBarListener extends FileMenuHandler implements ItemListener {
    static boolean active = false;

    public void itemStateChanged(ItemEvent e) {
        if (active) {
            JComboBox box = (JComboBox)e.getSource();
            readAndInsert(frame.getSideBar().getCurrentDir() + File.separator + box.getSelectedItem());
        }
    }

    public void eventState(boolean b) {
        active = b;
    }
}

class CloseSidebarListener extends MText implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        frame.getSideBar().closeDir();
        frame.getFrame().getContentPane().remove(frame.getSideBar());
    }
}

class FNFilter implements FilenameFilter {
    public boolean accept(File dir, String fileName) {
        File file = new File(dir.getPath() + File.separator + fileName);
        if (file.isFile()) {
            return fileName.endsWith(".txt") || fileName.endsWith(".sh") || fileName.endsWith(".java") || (!fileName.contains(".") && fileName.endsWith(""));
        } else return false;
    }
}
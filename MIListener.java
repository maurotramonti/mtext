package mtext;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Scanner;
import javax.swing.filechooser.*;
import java.io.*;


class MIListener extends MText implements ActionListener {
    int lang;
    public void actionPerformed(ActionEvent e) {   
        lang = frame.getLang();
        if (e.getActionCommand().equals("About MText")) {
            JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("VersionString", lang), "Version info", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(SysConst.getLogoPath()));
        }     
        else if (e.getActionCommand().equals("Recent file")) {
            if (frame.getTabPane().getSelectedIndex() == 63) {
                JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslatedString(11, lang));
                return;
            }
            JMenuItem mi = (JMenuItem)e.getSource();
            readAndInsert(mi.getLabel());
        }
        else if (e.getActionCommand().equals("Close")) {
            if (frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getIfIsModified()) {
                int n = JOptionPane.showConfirmDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("SaveEditsQuestion", lang), LanguageManager.getTranslationsFromFile("Warning", lang), JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    if (frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getFilePath().equals("none")) {
                        JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                        int r = fc.showSaveDialog(frame.getFrame());
                        if (r == JFileChooser.APPROVE_OPTION) {
                            String path = fc.getSelectedFile().getAbsolutePath();
                            saveFile(path);
                        }
                    } else saveFile(frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getFilePath());
                }
            }
            frame.getTabPane().remove(frame.getTabPane().getSelectedIndex());
        }
        else if (e.getActionCommand().equals("New")) {
            if (frame.getTabPane().getSelectedIndex() == 63) {
                JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("TooManyTabs", lang));
                return;
            }
            TextFilePanel[] fileTabs = frame.getFileTabs();
            fileTabs[frame.getTabPane().getSelectedIndex() + 1] = new TextFilePanel(null, "none", 4/*tabSize*/);
            frame.getTabPane().addTab(LanguageManager.getTranslatedString(1, /*lang*/0), null, fileTabs[frame.getTabPane().getSelectedIndex() + 1], null);
            frame.getTabPane().setSelectedIndex(frame.getTabPane().getSelectedIndex() + 1); 
            frame.getFrame().setTitle("MText - " + LanguageManager.getTranslatedString(1, lang)); 
        }
        else if (e.getActionCommand().equals("Open")) {
            if (frame.getTabPane().getSelectedIndex() == 63) {
                JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("TooManyTabs", lang));
                return;
            }
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int r = fc.showOpenDialog(frame.getFrame());
            if (r == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                readAndInsert(path);                
            }
        }
        else if (e.getActionCommand().equals("Open folder")) {
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setAcceptAllFileFilterUsed(false);
            int r = fc.showOpenDialog(frame.getFrame());
            if (r == JFileChooser.APPROVE_OPTION) {
                String currentDir = new String(fc.getSelectedFile().getAbsolutePath());
                frame.getSideBar().openDir(currentDir);
                frame.getFrame().getContentPane().add(frame.getSideBar(), BorderLayout.WEST);
                frame.getFrame().setVisible(true);
            }
        }
        else if (e.getActionCommand().equals("Save")) {            
                if (frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getFilePath().compareTo("none") != 0) {
                    saveFile(frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getFilePath());
                }
                else {
                    JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                    int r = fc.showSaveDialog(frame.getFrame());
                    if (r == JFileChooser.APPROVE_OPTION) {
                        String path = fc.getSelectedFile().getAbsolutePath();
                        frame.getTabPane().setTitleAt(frame.getTabPane().getSelectedIndex(), path);
                        saveFile(path);
                    }
                }                
            }
                  
        
        else if (e.getActionCommand().equals("Save as...")) {
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int r = fc.showSaveDialog(frame.getFrame());
            if (r == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                saveFile(path);
            }
        }
        else if (e.getActionCommand().equals("Exit")) {
            CustomWindowListener cwl = new CustomWindowListener();
            cwl.windowClosing(null);
        }
        else if (e.getActionCommand().equals("Java info")) {
            JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getJavaVersionString(lang), "About Java", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(SysConst.getJavaLogoPath()));
        }
    }  
    public void readAndInsert(String path) {
        String contents = new String();

        if (frame.getLFO().equals(path) == false) {
            int lang = frame.getLang();
            try {
                File file = new File(path);
                Scanner scanner = new Scanner(file);
                while(scanner.hasNextLine()) {
                    contents = contents + scanner.nextLine() + '\n';
                }
                frame.getFileTabs()[frame.getTabPane().getSelectedIndex() + 1] = new TextFilePanel(contents, path, /*tabSize*/4);
                frame.getTabPane().addTab(path, null, frame.getFileTabs()[frame.getTabPane().getSelectedIndex() + 1], null);
                frame.getTabPane().setSelectedIndex(frame.getTabPane().getSelectedIndex() + 1);  
                frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].setModified(false);
                scanner.close();
                frame.getFrame().setTitle("MText - " + frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getFilePath());
                frame.setLFO(path);
                BufferedWriter bw = new BufferedWriter(new FileWriter(SysConst.getPrePath() + "conf" + File.separator + "recentfiles.txt", true));
                bw.write(path + "\n");
                bw.close();
            }
            catch(IOException ex) {
                JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("ReadingError", lang));
            }
        }                  
    }
    public void saveFile(String path) {
        int lang = frame.getLang();
        String contents = new String(frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getTextArea().getText());
        try {
            File file = new File(path);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contents);
            bw.close();
            frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].setFilePath(path);
            frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].setModified(false);
            frame.setTitle("MText - " + frame.getFileTabs()[frame.getTabPane().getSelectedIndex()].getFilePath());
        }
        catch(IOException ex) {
            JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("SavingError", lang));
        }                  
    }

}
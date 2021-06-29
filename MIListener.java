package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.Scanner;

class MIListener extends MText implements ActionListener {
    public void actionPerformed(ActionEvent e) {       
        if (e.getActionCommand().equals("About")) {
            JOptionPane.showMessageDialog(frame, lm.getTranslatedString(3, lang));
        }     
        else if (e.getActionCommand().equals("Close")) {
            if (tabs[tPane.getSelectedIndex()].getIfIsModified()) {
                int n = JOptionPane.showConfirmDialog(frame, "The file selected is not saved. Would you like to save it?", "Warning", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    if (tabs[tPane.getSelectedIndex()].getFilePath().equals("none")) {
                        JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                        int r = fc.showSaveDialog(null);
                        if (r == JFileChooser.APPROVE_OPTION) {
                            String path = fc.getSelectedFile().getAbsolutePath();
                            saveFile(path);
                        }
                    } else saveFile(tabs[tPane.getSelectedIndex()].getFilePath());
                }
            }
            tPane.remove(tPane.getSelectedIndex());
        }
        else if (e.getActionCommand().equals("New")) {
            if (tPane.getSelectedIndex() == 63) {
                JOptionPane.showMessageDialog(frame, "You opened too many tabs!");
                return;
            }
            tabs[tPane.getSelectedIndex() + 1] = new TextFilePanel(null, "none", tabSize);
            tPane.addTab(lm.getTranslatedString(1, lang), null, tabs[tPane.getSelectedIndex() + 1], null);
            tPane.setSelectedIndex(tPane.getSelectedIndex() + 1); 
            frame.setTitle("MText - " + lm.getTranslatedString(1, lang)); 
        }
        else if (e.getActionCommand().equals("Open")) {
            if (tPane.getSelectedIndex() == 63) {
                JOptionPane.showMessageDialog(frame, "You opened too many tabs!");
                return;
            }
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int r = fc.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                readAndInsert(path);                
            }
        }
        else if (e.getActionCommand().equals("Save")) {            
                if (tabs[tPane.getSelectedIndex()].getFilePath().compareTo("none") != 0) {
                    saveFile(tabs[tPane.getSelectedIndex()].getFilePath());
                }
                else {
                    JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                    int r = fc.showSaveDialog(null);
                    if (r == JFileChooser.APPROVE_OPTION) {
                        String path = fc.getSelectedFile().getAbsolutePath();
                        tPane.setTitleAt(tPane.getSelectedIndex(), path);
                        saveFile(path);
                    }
                }                
            }
                  
        
        else if (e.getActionCommand().equals("Save as...")) {
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int r = fc.showSaveDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                saveFile(path);
            }
        }
        else if (e.getActionCommand().equals("Exit")) {
            System.exit(0);
        }
    }  
    public void readAndInsert(String path) {
        String contents = new String();
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                contents = contents + scanner.nextLine() + '\n';
            }
            tabs[tPane.getSelectedIndex() + 1] = new TextFilePanel(contents, path, tabSize);
            tPane.addTab(path, null, tabs[tPane.getSelectedIndex() + 1], null);
            tPane.setSelectedIndex(tPane.getSelectedIndex() + 1);  
            tabs[tPane.getSelectedIndex()].setModified(false);
            scanner.close();
            frame.setTitle("MText - " + tabs[tPane.getSelectedIndex()].getFilePath());
        }
        catch(IOException ex) {
            JOptionPane.showMessageDialog(frame, "An error occurred while reading the file!");
        }                  
    }
    public void saveFile(String path) {
        String contents = new String(tabs[tPane.getSelectedIndex()].getTextArea().getText());
        try {
            File file = new File(path);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contents);
            bw.close();
            tabs[tPane.getSelectedIndex()].setFilePath(path);
            tabs[tPane.getSelectedIndex()].setModified(false);
            frame.setTitle("MText - " + tabs[tPane.getSelectedIndex()].getFilePath());
        }
        catch(IOException ex) {
            JOptionPane.showMessageDialog(frame, "An error occurred while saving the file!");
        }                  
    }
}
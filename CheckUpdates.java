package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.Scanner;
import java.net.*;


class CheckUpdates extends MText implements ActionListener {
    final int internalVersion = 200;
    final String internalVersionString = new String("200");
    public void actionPerformed(ActionEvent e) {
        int lang = frame.getLang();
        try {
            URL url = new URL("https://raw.githubusercontent.com/maurotramonti/mtext/main/latest.txt");

            InputStream is = url.openStream();
            // Stream to the destionation file
            FileOutputStream fos = new FileOutputStream(SysConst.getPrePath() + "conf" + File.separator + "latest.txt");
		    // Read bytes from URL to the local file
            byte[] buffer = new byte[4096];
            int bytesRead = 0;

            System.out.println("Downloading " + "latest.txt");
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
                JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("CUTxtY", lang), LanguageManager.getTranslationsFromFile("CUTtl", lang), JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame.getFrame(), LanguageManager.getTranslationsFromFile("CUTxtN", lang), LanguageManager.getTranslationsFromFile("CUTtl", lang), JOptionPane.INFORMATION_MESSAGE);
            }
            scanner.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(internalVersionString);
            bw.close();
            } catch (Exception ex) {
                return;
            }

    }
} 
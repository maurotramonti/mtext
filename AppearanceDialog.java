package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.io.*;

class AppearanceDialog extends JDialog {
    private JPanel contents, buttons;

    private AppearanceDialog dialog;
    private MTextFrame parent;

    private AppearanceDialog.ConfirmButton cb = new AppearanceDialog.ConfirmButton();
    private CancelButton cab;

    private AppearanceDialog.RemoveThemeButton dtb = new AppearanceDialog.RemoveThemeButton();

    private JComboBox<String> colorThemeChooser;
    private JCheckBox showLineCount, showStatusBar;

    private String[] choices = new String[16];

    AppearanceDialog(MTextFrame parent) {
        super(parent.getFrame(), LanguageManager.getTranslationsFromFile("Appearance"), true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());

        dialog = this;
        this.parent = parent;


        GridBagConstraints gbc = new GridBagConstraints();
        contents = new JPanel(new GridBagLayout()); contents.setBackground(Color.white); 
        buttons = new JPanel(new FlowLayout()); buttons.setBackground(Color.white);

        

        gbc.gridy = 0; gbc.gridx = 0; gbc.anchor = GridBagConstraints.LINE_START; gbc.insets = new Insets(8, 8, 8, 8);
        contents.add(new JLabel(LanguageManager.getTranslationsFromFile("ColorTheme")), gbc);
        
        
        colorThemeChooser = new JComboBox<>(choices); colorThemeChooser.setSelectedItem(parent.getThemeName());
        updateThemeList();

        gbc.gridx = 1; 
        contents.add(colorThemeChooser, gbc);

        gbc.gridy = 0; gbc.gridx = 2; 
        contents.add(dtb, gbc);

        gbc.gridy = 0; gbc.gridx = 3; gbc.anchor = GridBagConstraints.LINE_START;
        contents.add(new AppearanceDialog.AddThemeButton(), gbc);

        showLineCount = new JCheckBox(LanguageManager.getTranslationsFromFile("ShowLineCount")); showLineCount.setContentAreaFilled(false); showLineCount.setHorizontalTextPosition(SwingConstants.LEFT);
        if (parent.getFileTabs()[parent.getTabPane().getSelectedIndex()].getLineCounter().isVisible()) showLineCount.setSelected(true);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; 
        contents.add(showLineCount, gbc);

        showStatusBar = new JCheckBox(LanguageManager.getTranslationsFromFile("ShowStatusBar")); showStatusBar.setContentAreaFilled(false); showStatusBar.setHorizontalTextPosition(SwingConstants.LEFT);
        if (parent.getStatusBar().isVisible()) showStatusBar.setSelected(true);

        gbc.gridy = 3;
        contents.add(showStatusBar, gbc);

        gbc.gridy = 4; gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.CENTER;

        contents.add(new JLabel("<html><div style='text-align: center;'><i>" + LanguageManager.getTranslationsFromFile("AppearanceDialogWarning") + "</i></div></html>"), gbc);

    
        cab = new CancelButton(this);

        buttons.add(cab); buttons.add(cb);

        contents.setBorder(new EmptyBorder(10, 10, 4, 10));
        this.add(contents, BorderLayout.NORTH); this.add(buttons, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);

    }

    public void updateThemeList() {
        File themesDir = new File(SysConst.getPrePath() + "themes");
        String[] fileNames = themesDir.list();
        choices = new String[fileNames.length];
        int i = 0;
        for (String s : fileNames) {
            choices[i] = s.replace(".txt", "");
            i++;
        }
        colorThemeChooser.removeAllItems();

        for (String s : choices) {
            colorThemeChooser.addItem(s);
        }

        if (choices.length > 1) dtb.setEnabled(true);
        else dtb.setEnabled(false);

        colorThemeChooser.setSelectedItem(parent.currentThemeName);
        
    }

    class RemoveThemeButton extends JButton implements ActionListener {
        RemoveThemeButton() {
            super(LanguageManager.getTranslationsFromFile("RemoveTheme"));
            addActionListener(this);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            File themeToDelete = new File(SysConst.getPrePath() + "themes" + File.separator + colorThemeChooser.getSelectedItem() + ".txt");
            themeToDelete.delete();
            updateThemeList();
        }
    }

    class AddThemeButton extends JButton implements ActionListener {
        AddThemeButton() {
            super("+");
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AddThemeDialog d = new AddThemeDialog(dialog);
        }
    }

    class ConfirmButton extends JButton implements ActionListener {

        ConfirmButton() {
            super(LanguageManager.getTranslationsFromFile("Confirm"));
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) { 
            try {
                File conffile = new File(SysConst.getPrePath() + "conf" + File.separator + "appearance.txt");
                BufferedWriter bw = new BufferedWriter(new FileWriter(conffile));
                bw.write((String) colorThemeChooser.getSelectedItem());
        
                if (showLineCount.isSelected()) bw.write("\n1");
                else bw.write("\n0");

                if(showStatusBar.isSelected()) bw.write("\n1");
                else bw.write("\n0");

                bw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        
            dialog.dispose();
            parent.loadAppearance();

        }

    }   

}

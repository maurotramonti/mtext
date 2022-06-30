package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.io.*;

class AddThemeDialog extends JDialog {
    private AppearanceDialog parent;
    private AddThemeDialog dialog;

    private JPanel contents, buttons;

    private ColorLabel[] colorResults = new ColorLabel[4];

    private AddThemeDialog.ConfirmButton cb;
    private CancelButton cab;

    private AddThemeDialog.RgbSpinner[][] rgbSpinners = new AddThemeDialog.RgbSpinner[4][3];
    private JCheckBox[] whiteTextQuestion = new JCheckBox[4];

    private JTextField themeName = new JTextField("Custom1");

    AddThemeDialog(AppearanceDialog parent) {
        super(parent, LanguageManager.getTranslationsFromFile("AddTheme"), true);

        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.parent = parent;
        dialog = this;

        GridBagConstraints gbc = new GridBagConstraints();

        contents = new JPanel(new GridBagLayout()); contents.setBackground(Color.white); contents.setBorder(new EmptyBorder(10, 10, 4, 10));
        buttons = new JPanel(new FlowLayout()); buttons.setBackground(Color.white);

        cab = new CancelButton(this); buttons.add(cab);
        cb = new AddThemeDialog.ConfirmButton(); buttons.add(cb);

        

        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(8, 8, 8, 8);
        contents.add(new JLabel("Rosso"), gbc); 
        
        gbc.gridx = 2; contents.add(new JLabel("Verde"), gbc); 
        
        gbc.gridx = 3; contents.add(new JLabel("Blu"), gbc);

        gbc.gridx = 4; contents.add(new JLabel("Risultato"), gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridy = 1; gbc.gridx = 0; contents.add(new JLabel(LanguageManager.getTranslationsFromFile("LineCounterColor")), gbc);


        for (int i = 0; i < 3; i++) {
            rgbSpinners[0][i] = new RgbSpinner();
            gbc.gridx = (i + 1); 
            contents.add(rgbSpinners[0][i], gbc);
        }

        colorResults[0] = new ColorLabel(new Color((Integer) rgbSpinners[0][0].getValue(), (Integer) rgbSpinners[0][1].getValue(), (Integer) rgbSpinners[0][2].getValue()));
        gbc.gridx++; gbc.ipady = 25; gbc.ipadx = 25; gbc.anchor = GridBagConstraints.CENTER; contents.add(colorResults[0], gbc);

        gbc.ipady = 0; gbc.ipadx = 0; gbc.anchor = GridBagConstraints.LINE_START; gbc.gridy = 2; gbc.gridx = 0;
        contents.add(new JLabel(LanguageManager.getTranslationsFromFile("StatusBarColor")), gbc);


        for (int i = 0; i < 3; i++) {
            rgbSpinners[1][i] = new RgbSpinner();
            gbc.gridx = (i + 1); 
            contents.add(rgbSpinners[1][i], gbc);
        }

        colorResults[1] = new ColorLabel(new Color((Integer) rgbSpinners[1][0].getValue(), (Integer) rgbSpinners[1][1].getValue(), (Integer) rgbSpinners[1][2].getValue()));
        gbc.gridx++; gbc.ipady = 25; gbc.ipadx = 25; gbc.anchor = GridBagConstraints.CENTER; contents.add(colorResults[1], gbc);

        gbc.ipady = 0; gbc.ipadx = 0; gbc.anchor = GridBagConstraints.LINE_START; gbc.gridy = 3; gbc.gridx = 0;
        contents.add(new JLabel(LanguageManager.getTranslationsFromFile("TextBackgroundColor")), gbc);

        for (int i = 0; i < 3; i++) {
            rgbSpinners[2][i] = new RgbSpinner();
            gbc.gridx = (i + 1); 
            contents.add(rgbSpinners[2][i], gbc);
        }

        colorResults[2] = new ColorLabel(new Color((Integer) rgbSpinners[2][0].getValue(), (Integer) rgbSpinners[2][1].getValue(), (Integer) rgbSpinners[2][2].getValue()));
        gbc.gridx++; gbc.ipady = 25; gbc.ipadx = 25; gbc.anchor = GridBagConstraints.CENTER; contents.add(colorResults[2], gbc);

        gbc.ipady = 0; gbc.ipadx = 0; gbc.anchor = GridBagConstraints.LINE_START; gbc.gridy = 4; gbc.gridx = 0;
        contents.add(new JLabel(LanguageManager.getTranslationsFromFile("MenuBarColor")), gbc);

        for (int i = 0; i < 3; i++) {
            rgbSpinners[3][i] = new AddThemeDialog.RgbSpinner();
            gbc.gridx = (i + 1); 
            contents.add(rgbSpinners[3][i], gbc);
        }

        colorResults[3] = new ColorLabel(new Color((Integer) rgbSpinners[3][0].getValue(), (Integer) rgbSpinners[3][1].getValue(), (Integer) rgbSpinners[3][2].getValue()));
        gbc.gridx++; gbc.ipady = 25; gbc.ipadx = 25; gbc.anchor = GridBagConstraints.CENTER; contents.add(colorResults[3], gbc);

        gbc.ipady = 0; gbc.ipadx = 0; gbc.anchor = GridBagConstraints.LINE_START; gbc.gridx = 0; gbc.gridy = 5;
        contents.add(new JLabel(LanguageManager.getTranslationsFromFile("ThemeName")), gbc);

        gbc.gridwidth = 4; gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; 
        contents.add(themeName, gbc);

        gbc.gridwidth = 1; gbc.gridx = 6; gbc.gridy = 1; 
        for (int i = 0; i < 4; i++) {
            whiteTextQuestion[i] = new JCheckBox(LanguageManager.getTranslationsFromFile("WhiteText"));
            whiteTextQuestion[i].setContentAreaFilled(false); whiteTextQuestion[i].setHorizontalTextPosition(SwingConstants.LEFT);
            contents.add(whiteTextQuestion[i], gbc);
            gbc.gridy++;
        }



        this.add(contents, BorderLayout.NORTH); this.add(buttons, BorderLayout.SOUTH);

        this.pack();
        this.setVisible(true);
    }

    public void updateColorResults() {
        Integer rgb[] = new Integer[3];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                rgb[j] = (Integer) rgbSpinners[i][j].getValue();
            }
            colorResults[i].setBackground(new Color(rgb[0], rgb[1], rgb[2]));          
            
        }
    }

    class RgbSpinner extends JSpinner implements ChangeListener {
        RgbSpinner() {
            super(new SpinnerNumberModel(190, 0, 255, 1));
            addChangeListener(this);
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            updateColorResults();
        }
    }
    class ConfirmButton extends JButton implements ActionListener {
        ConfirmButton() {
            super(LanguageManager.getTranslationsFromFile("Confirm"));
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (themeName.getText().trim().length() == 0 || themeName.getText().contains("\\") || themeName.getText().contains("/") || themeName.getText().contains(".") || themeName.getText().contains("\"") || themeName.getText().contains("\'") || themeName.getText().contains(":") || themeName.getText().contains("?") || themeName.getText().contains("<") || themeName.getText().contains(">")) {
                JOptionPane.showMessageDialog(dialog, LanguageManager.getTranslationsFromFile("ThemeNameError"), LanguageManager.getTranslationsFromFile("Warning"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            File themeFile = new File(SysConst.getPrePath() + "themes" + File.separator + themeName.getText().trim() + ".txt");
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(themeFile));
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 3; j++) {
                        bw.write(Integer.toString((Integer) rgbSpinners[i][j].getValue()));
                        if (j != 2) bw.write(';');
                    }
                    bw.write('\n');
                }
                for (JCheckBox cb : whiteTextQuestion) {
                    if (cb.isSelected()) bw.write("1\n");
                    else bw.write("0\n");
                }
                bw.close();
                parent.updateThemeList();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            dialog.dispose();
        }
    }

}


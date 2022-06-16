package mtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;


class SettingsDialog extends JDialog {
    private JPanel contents, buttons;
    private SubmitButton submit;
    private CancelButton cancel;
    int lang = LanguageManager.getCurrentLang();

    private String[] settingsLabels = {new String(LanguageManager.getTranslationsFromFile("Language", lang)), new String(LanguageManager.getTranslationsFromFile("AutomaticNewline")), new String(LanguageManager.getTranslationsFromFile("TabLength")), new String(LanguageManager.getTranslationsFromFile("AppTheme"))};

    private JComboBox langChooser;
    private JRadioButton[] radioButtons = new JRadioButton[4];
    private JSpinner tabLength;



    SettingsDialog(JFrame parent, String[] arrayToPutResults) {
        super(parent, LanguageManager.getTranslationsFromFile("Settings"), true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        buttons = new JPanel(new FlowLayout()); buttons.setBackground(Color.white);
        contents = new JPanel(new GridBagLayout()); contents.setBackground(Color.white);
        GridBagConstraints gbc = new GridBagConstraints();

        

        gbc.gridx = 0; gbc.ipady = 10;  gbc.ipadx = 7;

        for (int i = 0; i < 4; i++) {
            gbc.gridy = i;
            contents.add(new JLabel(settingsLabels[i]), gbc);
        }

        String[] langs = {new String("English"), new String("Italiano")};
        langChooser = new JComboBox(langs);

        if (LanguageManager.getCurrentLang() == LanguageManager.ITALIAN) langChooser.setSelectedIndex(1);
        else langChooser.setSelectedIndex(0); 

        radioButtons[0] = new JRadioButton(LanguageManager.getTranslationsFromFile("Yes")); radioButtons[1] = new JRadioButton("No");    
        ButtonGroup group1 = new ButtonGroup(); group1.add(radioButtons[0]); group1.add(radioButtons[1]);

        if (arrayToPutResults[1].equals("Yes")) radioButtons[0].setSelected(true);
        else radioButtons[1].setSelected(true);

        String[] spacesInterval = {"1", "2", "3", "4", "5", "6", "7", "8"};
        tabLength = new JSpinner(new SpinnerNumberModel(Integer.parseInt(arrayToPutResults[2]), 1, 8, 1));
        
        radioButtons[2] = new JRadioButton(LanguageManager.getTranslationsFromFile("CrossPlatform")); radioButtons[3] = new JRadioButton(LanguageManager.getTranslationsFromFile("System"));
        
        if (arrayToPutResults[3].equals("System")) radioButtons[3].setSelected(true);
        else radioButtons[2].setSelected(true);
        
        ButtonGroup group2 = new ButtonGroup(); group2.add(radioButtons[2]); group2.add(radioButtons[3]);

        for (int i = 0; i < 4; i++) {
            radioButtons[i].setContentAreaFilled(false);
        }

        gbc.ipady = 0; gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        contents.add(langChooser, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contents.add(radioButtons[0], gbc);

        gbc.gridx = 2;
        contents.add(radioButtons[1], gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridy = 2;  gbc.gridx = 1; 
        contents.add(tabLength, gbc);
        
        gbc.gridx = 1;  gbc.anchor = GridBagConstraints.CENTER;
        contents.add(new JLabel("  " + LanguageManager.getTranslationsFromFile("Spaces")), gbc);

        gbc.gridy = 3; gbc.gridx = 1; 
        contents.add(radioButtons[2], gbc);

        gbc.gridx = 2;
        contents.add(radioButtons[3], gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3; gbc.ipady = 12; gbc.anchor = GridBagConstraints.PAGE_END;
        contents.add(new JLabel("<html><i>" + LanguageManager.getTranslationsFromFile("SettingsDialogWarning") + "</i></html>"), gbc);

        submit = new SubmitButton(this, arrayToPutResults, radioButtons, langChooser, tabLength);
        submit.setActionCommand("Submit"); submit.addActionListener(new DialogButtonsHandler());

        cancel = new CancelButton(this);
        cancel.setActionCommand("Cancel"); cancel.addActionListener(new DialogButtonsHandler());

        buttons.add(cancel); buttons.add(submit);



        contents.setBorder(new EmptyBorder(10, 10, 4, 10));
        this.setLayout(new BorderLayout());
        this.add(contents, BorderLayout.NORTH); this.add(buttons, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
    }
}


// Buttons

class CancelButton extends JButton {
    JDialog parentDialog;
    CancelButton(JDialog pd) {
        super("Annulla");
        parentDialog = pd;
    }

}

class SubmitButton extends JButton {
    JDialog parentDialog;
    JRadioButton[] radioButtons;
    JComboBox langChooser;
    JSpinner tabLength;
    String[] passedResultsArray;
    SubmitButton(JDialog pd, String[] pra, JRadioButton[] rb, JComboBox lc, JSpinner tl) {
        super("Fine");
        parentDialog = pd;
        passedResultsArray = pra;
        radioButtons = rb;
        tabLength = tl;
        langChooser = lc;
    }
}

// Event handlers

class DialogButtonsHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Cancel")) {
            CancelButton c = (CancelButton) e.getSource();
            c.parentDialog.dispose();
        } else if (e.getActionCommand().equals("Submit")) {
            SubmitButton s = (SubmitButton) e.getSource();
            String[] pra = s.passedResultsArray;
            JComboBox langChooser = s.langChooser;
            JSpinner tabLength = s.tabLength;
            JRadioButton[] radioButtons = s.radioButtons;

            pra[4] = "true";

            if (langChooser.getSelectedIndex() == LanguageManager.ITALIAN) pra[0] = "Italiano";
            else if (langChooser.getSelectedIndex() == LanguageManager.ENGLISH) pra[0] = "English";

            if (radioButtons[0].isSelected()) pra[1] = "Yes";
            else if (radioButtons[1].isSelected()) pra[1] = "No";

            pra[2] = Integer.toString((Integer) tabLength.getValue());

            if (radioButtons[2].isSelected()) pra[3] = "Cross-Platform";
            else if (radioButtons[3].isSelected()) pra[3] = "System";

 
            

            s.parentDialog.dispose();

        }
    }
}

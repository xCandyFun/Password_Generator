package org.example.passwordgen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PasswordGeneratorGUI extends JFrame {
    private JTextField lengthField, purposeField, emailField, passwordField;
    private JCheckBox upperCaseBox, digitsBox, specialCharBox;
    private JButton generateButton, sendEmailButton;

    public PasswordGeneratorGUI(){
        setTitle("Password Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,300);
        setLayout(new GridLayout(8,2,5,5));

        add(new JLabel("Password Length:"));
        lengthField = new JTextField("25");
        add(lengthField);

        upperCaseBox = new JCheckBox("Include Uppercase Letters");
        add(upperCaseBox);
        add(new JLabel(" ")); // Empty cell

        digitsBox = new JCheckBox("Include Uppercase Letters");
        add(digitsBox);
        add(new JLabel(" "));

        specialCharBox = new JCheckBox("Include Special Characters");
        add(specialCharBox);
        add(new JLabel(" "));

        add(new JLabel("Password Purpose (e.g., Google)"));
        purposeField = new JTextField();
        add(purposeField);

        add(new JLabel("Send to Email:"));
        emailField = new JTextField();
        add(emailField);

        generateButton = new JButton("Generate Password");
        generateButton.addActionListener(this::generatePassword);
        add(generateButton);

        sendEmailButton = new JButton("Send email");
        sendEmailButton.addActionListener(this::sendEmail);
        add(sendEmailButton);

        add(new JLabel("Generated Password:"));
        passwordField = new JTextField();
        passwordField.setEditable(false);
        add(passwordField);

        setVisible(true);
    }

    private void generatePassword(ActionEvent e) {
        int length = Integer.parseInt(lengthField.getText());
        boolean includeUpper = upperCaseBox.isSelected();
        boolean includeDigits = digitsBox.isSelected();
        boolean includeSpecial = specialCharBox.isSelected();

        String password = PasswordGeneratorApp.generatePassword(length, includeUpper, includeDigits, includeSpecial);
        passwordField.setText(password);

        // Copy to Clipboard
        //Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(password), null);
    }

    private void sendEmail(ActionEvent e) {
        String to = emailField.getText();
        String purpose = purposeField.getText();
        String password = passwordField.getText();

        if (to.isEmpty() || purpose.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Fill in all fields and generate a password first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        PasswordGeneratorApp.sendEmail(to, purpose, password);
        JOptionPane.showMessageDialog(this,
                "Email sent successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        emailField.setText("");
        purposeField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PasswordGeneratorGUI::new);
    }
}

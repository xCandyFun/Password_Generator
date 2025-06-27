package org.example.passwordgen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PasswordGeneratorGUI extends JFrame {
    private JTextField lengthField, purposeField, emailField, passwordField;
    private JCheckBox upperCaseBox, digitsBox, specialCharBox;
    private JButton generateButton, sendEmailButton;
    private JLabel strengthlabel;

    public PasswordGeneratorGUI(){
        setTitle("Password Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,400);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5); // padding between
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(new JLabel("Password Length:"), gbc);
        gbc.gridx = 1;
        lengthField = new JTextField("25");
        add(lengthField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        upperCaseBox = new JCheckBox("Include Uppercase Letters");
        add(upperCaseBox, gbc);

        gbc.gridy++;
        digitsBox = new JCheckBox("Include Uppercase Letters");
        add(digitsBox, gbc);

        gbc.gridy++;
        specialCharBox = new JCheckBox("Include Special Characters");
        add(specialCharBox, gbc);

        gbc.gridy++;
        add(new JLabel("Password Purpose (e.g., Google)"), gbc);
        gbc.gridx = 1;
        purposeField = new JTextField();
        add(purposeField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Send to Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField();
        add(emailField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        generateButton = new JButton("Generate Password");
        generateButton.addActionListener(this::generatePassword);
        add(generateButton, gbc);

        gbc.gridx = 1;
        sendEmailButton = new JButton("Send email");
        sendEmailButton.addActionListener(this::sendEmail);
        add(sendEmailButton, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Generated Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JTextField();
        passwordField.setEditable(false);
        add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Password Strength:"), gbc);
        gbc.gridx = 1;
        strengthlabel = new JLabel("");
        strengthlabel.setFont(new Font("Arial", Font.BOLD, 12));
        add(strengthlabel, gbc);

        setVisible(true);
    }

    private void generatePassword(ActionEvent e) {
        try {
            int length = Integer.parseInt(lengthField.getText());
            boolean includeUpper = upperCaseBox.isSelected();
            boolean includeDigits = digitsBox.isSelected();
            boolean includeSpecial = specialCharBox.isSelected();

            String password = PasswordGeneratorApp.generatePassword(length, includeUpper, includeDigits, includeSpecial);
            passwordField.setText(password);

            // Copy to Clipboard
            //Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(password), null);

            String strength = evaluatePasswordStrength(password);
            strengthlabel.setText(strength);
            strengthlabel.setForeground(switch (strength) {
                case "Weak" -> Color.RED;
                case "Medium" -> Color.ORANGE;
                case "Strong" -> new Color(0, 128, 0); //Dark Green
                default -> Color.BLACK;
            });
        }catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(this,
                    "password length must be a number.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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

    private String evaluatePasswordStrength(String password){
        int length = password.length();
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[\\d].*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");

        int score = 0;
        if (length >= 8) score++;
        if (length >= 12) score++;
        if (hasUpper && hasLower) score++;
        if (hasDigit) score++;
        if (hasSpecial) score++;

        return switch (score){
            case 0,1,2 -> "Weak";
            case 3,4 -> "Medium";
            case 5 -> "Strong";
            default -> "Unknown";
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PasswordGeneratorGUI::new);
    }
}

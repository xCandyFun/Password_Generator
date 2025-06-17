package org.example.passwordgen;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.security.SecureRandom;
import java.util.Properties;
import java.util.Scanner;

public class PasswordGeneratorApp {
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_+=<>?";

    static Dotenv dotenv = Dotenv.load();


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Password length: ");
        int length = sc.nextInt();
        sc.nextLine();

        System.out.println("Include uppercase? (y/n): ");
        boolean includeUpper = sc.next().equalsIgnoreCase("y");

        System.out.println("Include digits? (y/n): ");
        boolean includeDigits = sc.next().equalsIgnoreCase("y");
        sc.nextLine();

        System.out.println("Include special characters? (y/n): ");
        boolean includeSpecial = sc.next().equalsIgnoreCase("y");
        sc.nextLine();

        System.out.println("What is this password for? ");
        String purpose = sc.nextLine();

        String password = generatePassword(length, includeUpper, includeDigits, includeSpecial);
        System.out.println("Generated password: " + password);

//        System.out.println("Enter your email address to send the password to: ");
//        String toEmail = sc.nextLine();
        String toEmail = dotenv.get("EMAILADDRESS");
        sendEmail(toEmail, purpose, password);
    }

    public static String generatePassword(int length, boolean upper, boolean digits, boolean special){
        StringBuilder charPool = new StringBuilder(LOWER);
        if (upper) charPool.append(UPPER);
        if (digits) charPool.append(DIGITS);
        if (special) charPool.append(SPECIAL);

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charPool.length());
            password.append(charPool.charAt(index));
        }

        return password.toString();
    }

    public static void sendEmail(String toEmail, String purpose, String password) {
        final String fromEmail = dotenv.get("EMAILADDRESS"); //.env
        final String passwordEmail = dotenv.get("TOEMAIL");

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, passwordEmail);
            }
        });

        try{
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setSubject("Your new password for " + purpose);
            msg.setText("Here is your password for " + purpose + ":\n\n" + password);
            Transport.send(msg);
        }catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email.");
        }
    }
}

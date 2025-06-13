package org.example.passwordgen;

import java.security.SecureRandom;
import java.util.Scanner;

public class PasswordGeneratorApp {
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_+=<>?";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Password length: ");
        int length = sc.nextInt();

        System.out.println("Include uppercase? (y/n): ");
        boolean includeUpper = sc.next().equalsIgnoreCase("y");

        System.out.println("Include digits? (y/n): ");
        boolean includeDigits = sc.next().equalsIgnoreCase("y");

        System.out.println("Include special characters? (y/n): ");
        boolean includeSpecial = sc.next().equalsIgnoreCase("y");

        String password = generatePassword(length, includeUpper, includeDigits, includeSpecial);
        System.out.println("Generated password: " + password);
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
}

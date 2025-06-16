package com.example.re_estate.misc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static String getPasswordError(String password) {
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        } else if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter.";
        } else if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        } else if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one numeric character.";
        } else {
            return "";
        }
    }
}

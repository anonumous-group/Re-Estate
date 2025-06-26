package com.example.re_estate.misc;

public class CardValidator {

    //Format card number
    public static String formatCardNumber(String cardNumber) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cardNumber.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                builder.append(" ");
            }
            builder.append(cardNumber.charAt(i));
        }
        return builder.toString();
    }

    //Format card expiry
    public static String formatCardExpiry(String expiryNumber) {
        StringBuilder builder = new StringBuilder();
        expiryNumber = expiryNumber.replace("/", "");
        for (int i = 0; i < expiryNumber.length(); i++) {
            if (i > 0 && i % 2 == 0) {
                builder.append("/");
            }
            builder.append(expiryNumber.charAt(i));
        }
        return builder.toString();
    }

    //detect the type of card either master card or visa card
    public static String detectCardType(String cardNumber){

        if (cardNumber.startsWith("4")) {
            return "VISA";
        } else if (cardNumber.startsWith("51") || cardNumber.startsWith("52") || cardNumber.startsWith("53")
        || cardNumber.startsWith("54") || cardNumber.startsWith("55")) {
            return "MASTERCARD";
        } else {
            return "CARD NAME";
        }
    }

    //validate the card number
    public static boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return false;
        }

        // 1. Remove any non-digit characters (like spaces or dashes)
        String cleanedCardNumber = cardNumber.replaceAll("\\D", "");

        if (cleanedCardNumber.length() <= 1) { // Luhn algorithm generally applies to numbers with more than 1 digit
            return false;
        }

        int sum = 0;
        boolean alternate = false;
        // 2. Iterate over the digits in reverse order (from right to left).
        for (int i = cleanedCardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cleanedCardNumber.charAt(i));

            if (alternate) {
                // 3. Double every second digit.
                digit *= 2;
                // 4. If doubling a digit results in a two-digit number,
                // subtract 9 from it (or, equivalently, add the two digits together).
                if (digit > 9) {
                    digit = (digit % 10) + 1; // e.g., 12 becomes 1 + 2 = 3; 18 becomes 1 + 8 = 9
                }
            }
            sum += digit;
            alternate = !alternate;
        }

        // 5. If the total sum is divisible by 10, the number is valid.
        return (sum % 10 == 0);
    }
}

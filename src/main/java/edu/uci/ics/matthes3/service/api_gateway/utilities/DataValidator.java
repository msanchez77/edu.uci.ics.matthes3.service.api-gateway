package edu.uci.ics.matthes3.service.api_gateway.utilities;

public class DataValidator {
    public static class Emails {
        public static final int MAX_EMAIL_SIZE = 50;

        // CASE -11
        public static boolean isFormatValid(String email) {
            return email.matches("^[A-z0-9_.\\-]+@[A-z0-9]+\\.[a-z]{2,4}$");
        }

        // CASE -10
        public static boolean isLengthValid(String email) {
            return email.length() <= MAX_EMAIL_SIZE;
//            String[] emailName = email.split("@");
//
//            return (emailName[0].length() <= MAX_EMAIL_SIZE);
        }
    }
}

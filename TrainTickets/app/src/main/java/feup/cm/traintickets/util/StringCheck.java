package feup.cm.traintickets.util;

/**
 * Created by pedro on 26/03/17.
 */

public class StringCheck {

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}

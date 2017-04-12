package feup.cm.traintickets.util;

import android.util.Base64;

import java.security.NoSuchAlgorithmException;

import se.simbio.encryption.Encryption;

public abstract class QREncryption {

    static final String SECRET = "J6r8ehuu5pAc9w9H";
    static final String SALT = "salt";
    static byte[] iv = new byte[]{-89, -19, 17, -83, 86, 106, -31, 30, -5, -111, 61, -75, -84, 95, 120, -53};

    public static Encryption getInstance() throws Exception {
        Encryption encryption = Encryption.getDefault(SECRET, SALT, iv);
        return encryption;
    }
}
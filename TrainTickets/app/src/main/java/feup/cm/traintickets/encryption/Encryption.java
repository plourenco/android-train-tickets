package feup.cm.traintickets.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Encryption {

    private static SecureRandom rnd = new SecureRandom();

    public static String getHash(String message, String salt)
            throws NoSuchAlgorithmException {
        return "$SHA$" + salt + "$" + getSHA256(getSHA256(message) + salt);
    }

    public static String genHash(String message) throws NoSuchAlgorithmException {
        return getHash(message, createSalt(16));
    }

    public static boolean compareHashes(String hash, String message)
            throws NoSuchAlgorithmException {
        String[] line = hash.split("\\$");
        try {
            return hash.equals(getHash(message, line[2]));
        }
        catch(ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private static String getSHA256(String message) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        sha256.reset();
        sha256.update(message.getBytes());
        byte[] digest = sha256.digest();
        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest));
    }

    private static String createSalt(int length) throws NoSuchAlgorithmException {
        byte[] msg = new byte[40];
        rnd.nextBytes(msg);
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        sha1.reset();
        byte[] digest = sha1.digest(msg);
        return String.format("%0" + (digest.length << 1) + "x",
                new BigInteger(1, digest)).substring(0, length);
    }
}

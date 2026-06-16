package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Password hashing utility using SHA-256 + random salt.
 * Provides secure password storage and verification.
 */
public class PasswordHasher {

    private static final int SALT_LENGTH = 16;

    /**
     * Generate a random salt.
     * @return Base64-encoded salt string
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hash a password with the given salt using SHA-256.
     * @param password the plain-text password
     * @param salt the Base64-encoded salt
     * @return the Base64-encoded hash
     */
    public static String hash(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String salted = salt + password;
            byte[] hashBytes = digest.digest(salted.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Verify a password against a stored hash and salt.
     * @param password the plain-text password to verify
     * @param storedHash the stored hash
     * @param storedSalt the stored salt
     * @return true if the password matches
     */
    public static boolean verify(String password, String storedHash, String storedSalt) {
        String computedHash = hash(password, storedSalt);
        return computedHash.equals(storedHash);
    }
}

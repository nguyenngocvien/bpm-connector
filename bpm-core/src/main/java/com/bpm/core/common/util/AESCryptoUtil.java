package com.bpm.core.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESCryptoUtil {
    private static final String SECRET_KEY = "MySuperSecret123"; // 16 bytes key
    private static final String ALGO = "AES";

    public static String encrypt(String value) {
        if (value == null) return null;
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGO);
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new RuntimeException("Encryption error", ex);
        }
    }

    public static String decrypt(String encryptedValue) {
        if (encryptedValue == null) return null;
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGO);
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getDecoder().decode(encryptedValue);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception ex) {
            throw new RuntimeException("Decryption error", ex);
        }
    }
}

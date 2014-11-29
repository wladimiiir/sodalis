package sk.magiksoft.sodalis.core.security;

import sk.magiksoft.sodalis.core.logger.LoggerManager;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

/**
 * @author wladimiiir
 */
public class CryptoUtils {
    private static final byte[] SALT = new byte[]{31, 3, 13, 56, 123, 99, 28, 67};
    private static final int ITERATION = 23;

    public static byte[] makeDigest(String plain, String... additional) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(plain.getBytes());

            for (String add : additional) {
                md.update(add.getBytes());
            }

            return md.digest();
        } catch (NoSuchAlgorithmException ex) {
            LoggerManager.getInstance().error(CryptoUtils.class, ex);
            return new byte[0];
        }
    }

    public static boolean passwordsEqual(String password, byte[] passwordBytes, String... additional) {
        return MessageDigest.isEqual(makeDigest(password, additional), passwordBytes);
    }

    public static String generateUIDString() {
        return UUID.randomUUID().toString();
    }

    public static Cipher getEncryptCipher(String password) {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey secretKey = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new PBEParameterSpec(SALT, ITERATION));

            return cipher;
        } catch (InvalidAlgorithmParameterException ex) {
            LoggerManager.getInstance().error(CryptoUtils.class, ex);
        } catch (InvalidKeyException ex) {
            LoggerManager.getInstance().error(CryptoUtils.class, ex);
        } catch (InvalidKeySpecException ex) {
            LoggerManager.getInstance().error(CryptoUtils.class, ex);
        } catch (NoSuchPaddingException ex) {
            LoggerManager.getInstance().error(CryptoUtils.class, ex);
        } catch (NoSuchAlgorithmException ex) {
            LoggerManager.getInstance().error(CryptoUtils.class, ex);
        }

        return null;
    }

    public static Cipher getDecryptCipher(String password) {
        try {
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey secretKey = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

            cipher.init(Cipher.DECRYPT_MODE, secretKey, new PBEParameterSpec(SALT, ITERATION));

            return cipher;
        } catch (InvalidAlgorithmParameterException ex) {
            LoggerManager.getInstance().error(CryptoUtils.class, ex);
        } catch (InvalidKeyException ex) {
            LoggerManager.getInstance().error(CryptoUtils.class, ex);
        } catch (InvalidKeySpecException ex) {
            LoggerManager.getInstance().error(CryptoUtils.class, ex);
        } catch (NoSuchPaddingException ex) {
            LoggerManager.getInstance().error(CryptoUtils.class, ex);
        } catch (NoSuchAlgorithmException ex) {
            LoggerManager.getInstance().error(CryptoUtils.class, ex);
        }

        return null;
    }
}

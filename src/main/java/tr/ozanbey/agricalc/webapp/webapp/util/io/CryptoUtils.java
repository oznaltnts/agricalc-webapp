package tr.ozanbey.agricalc.webapp.webapp.util.io;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public final class CryptoUtils {

    private static final String ALGORITHM = "";
    private static final String AES = "";
    private static final String SALT = ""; // never change salt

    private CryptoUtils() {
    }

    public static String encrypt(String value) {
        if (value == null) {
            return null;
        }

        Key key = new SecretKeySpec(SALT.getBytes(), AES);
        try {

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.encodeBase64String(cipher.doFinal(value.getBytes()));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }


    public static String decrypt(String value) {
        if (value == null) {
            return null;
        }

        Key key = new SecretKeySpec(SALT.getBytes(), AES);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.decodeBase64(value)));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }


    public static String oneWayHash(String value) {
        if (value == null) {
            return null;
        }
        return Base64.encodeBase64String(DigestUtils.sha256(value + SALT));
    }
}

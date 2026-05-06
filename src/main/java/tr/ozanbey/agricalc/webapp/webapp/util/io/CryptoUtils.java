package tr.ozanbey.agricalc.webapp.webapp.util.io;


import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

public final class CryptoUtils {

    // Modern Standart: AES-GCM (Authenticated Encryption)
    private static final String ALGORITHM = "";
    private static final byte[] KEY = "".getBytes(StandardCharsets.UTF_8);
    // GCM Parametreleri
    private static final int IV_SIZE = 12; // GCM standardı
    private static final int TAG_BIT = 128;
    // hashing için iteration

    private CryptoUtils() {
    }

    /**
     * AES-GCM şifreleme yapar. Her işlemde rastgele IV üretir ve
     * bu IV'yi şifreli mesajın başına ekler.
     */
    public static String encrypt(String value) {
        if (value == null) return null;
        try {
            // 1. Rastgele IV Üretimi (ECB'den en büyük farkı budur)
            byte[] iv = new byte[IV_SIZE];
            new SecureRandom().nextBytes(iv);
//            SecureRandom.getInstanceStrong().nextBytes(iv);
            // 2. Şifreleme Kurulumu
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(KEY, "AES"),
                    new GCMParameterSpec(TAG_BIT, iv));
            // 3. Şifreleme
            byte[] cipherText = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            // 4. IV + CipherText birleştirme (Çözmek için IV gereklidir)
            return Base64.getEncoder().encodeToString(
                    ByteBuffer.allocate(IV_SIZE + cipherText.length).put(iv).put(cipherText).array()
            );
        } catch (Exception e) {
            throw new RuntimeException("Şifreleme hatası", e);
        }
    }

    /**
     * Şifreli metnin başındaki IV'yi okur ve kalan kısmı anahtarla çözer.
     */
    public static String decrypt(String value) {
        if (value == null) return null;
        try {
            byte[] decoded = Base64.getDecoder().decode(value);
            ByteBuffer buffer = ByteBuffer.wrap(decoded);
            // 1. IV'yi ve CipherText'i ayrıştır
            byte[] iv = new byte[IV_SIZE];
            buffer.get(iv); // İlk 12 byte IV
            byte[] cipherText = new byte[buffer.remaining()];
            buffer.get(cipherText);
            // 2. Çözme Kurulumu
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,
                    new SecretKeySpec(KEY, "AES"),
                    new GCMParameterSpec(TAG_BIT, iv));
            // 3. Çözme
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Şifre çözme hatası", e);
        }
    }

    /**
     * Java 21 ve HexFormat kullanarak SHA-256 Hash üretir.
     * Commons-Codec bağımlılığını bitirir.
     */
    public static String oneWayHash(String value) {
        if (value == null) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Tuzlama (Salting) işlemi
            byte[] hash = digest.digest((value + new String(KEY)).getBytes(StandardCharsets.UTF_8));
            // Java 17+ ile gelen modern Hex formatlayıcı
            return HexFormat.of().formatHex(hash); // Java 17+ Modern Hex
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algoritması bulunamadı", e);
        }
    }

}

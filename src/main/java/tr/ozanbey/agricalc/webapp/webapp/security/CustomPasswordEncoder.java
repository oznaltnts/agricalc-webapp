package tr.ozanbey.agricalc.webapp.webapp.security;


import org.springframework.security.crypto.password.PasswordEncoder;
import tr.ozanbey.agricalc.webapp.webapp.util.io.CryptoUtils;

public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return CryptoUtils.oneWayHash(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String hashed = encode(rawPassword);
        return hashed.equals(encodedPassword);
    }

}

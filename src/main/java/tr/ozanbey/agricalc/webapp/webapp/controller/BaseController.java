package tr.ozanbey.agricalc.webapp.webapp.controller;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tr.ozanbey.agricalc.webapp.webapp.security.CurrentUser;
import tr.ozanbey.agricalc.webapp.webapp.util.JSFUtils;
import tr.ozanbey.agricalc.webapp.webapp.util.io.CryptoUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public abstract class BaseController implements Serializable {

    public String encryptParameter(String parameter) throws UnsupportedEncodingException {
        String encryptedValue = CryptoUtils.encrypt(parameter);
        return URLEncoder.encode(encryptedValue, StandardCharsets.UTF_8);
    }

    public String decryptParameter(String parameter) {
        return CryptoUtils.decrypt(parameter);
    }

    public String getLocaleMessage(String key) {
        return JSFUtils.getLocaleMessage(key);
    }

    public static CurrentUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof CurrentUser currentUser) {
            return currentUser;
        }
        return null;
    }

    public static boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
    }

}

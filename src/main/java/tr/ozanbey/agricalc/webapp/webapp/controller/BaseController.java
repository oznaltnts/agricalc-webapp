package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tr.ozanbey.agricalc.webapp.service.service.BaseService;
import tr.ozanbey.agricalc.webapp.webapp.util.JSFUtils;
import tr.ozanbey.agricalc.webapp.webapp.util.io.CryptoUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component("baseController")
@ViewScoped
public class BaseController implements Serializable {

    @Autowired
    private BaseService baseService;

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static String encryptParameter(String parameter) throws UnsupportedEncodingException {
        String encryptedValue = CryptoUtils.encrypt(parameter);
        return URLEncoder.encode(encryptedValue, StandardCharsets.UTF_8);
    }

    public static String decryptParameter(String parameter) {
        return CryptoUtils.decrypt(parameter);
    }

    public static void setHttpRequestParameter(String parameterName, Object value) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        request.getSession().setAttribute(parameterName, value);
    }

    public static Object getHttpRequestParameter(String parameterName) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return request.getSession().getAttribute(parameterName);
    }

    public String getLocaleMessage(String key) {
        return JSFUtils.getLocaleMessage(key);
    }

    public static boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
    }

}

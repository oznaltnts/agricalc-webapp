package tr.ozanbey.agricalc.webapp.webapp.security;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;

@Component
public class CookieHelper implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        handleRememberMeCookie(req, res);
        filterChain.doFilter(req, res);
    }

    public void handleRememberMeCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] allCookies = request.getCookies();
        if (allCookies != null) {
            Cookie rememberMe = Arrays.stream(allCookies).filter(x -> "agricalc-remember-me".equals(x.getName()))
                    .findFirst().orElse(null);

            if (rememberMe != null) {
                rememberMe.setHttpOnly(true);
                rememberMe.setSecure(true);
                rememberMe.setMaxAge(1209600);
                rememberMe.setPath("/");
                response.addCookie(rememberMe);
            }
        }
    }

    public static void cancelCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath(StringUtils.hasLength(request.getContextPath()) ? request.getContextPath() : "/");
        cookie.setMaxAge(0);
        cookie.setSecure(request.isSecure());
        response.addCookie(cookie);
    }
}
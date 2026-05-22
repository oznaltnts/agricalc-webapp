package tr.ozanbey.agricalc.webapp.webapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecuritySuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private LoginController loginController;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        long loginFailureCount = loginController.isAccountBlocked(currentUser.getUser().getId());
        if (loginFailureCount > 2) {
            SecurityContextHolder.clearContext();
            response.sendRedirect("/public/login?error=blocked");
        } else {
            loginController.clearLoginFailures(currentUser.getUser().getId());
            loginController.createLoginSuccess(currentUser.getUser().getId());
            loginController.updateLastLoginInfo(currentUser.getUser());
            loginController.assignUserPreference(currentUser.getUser().getId());
            if (currentUser.getUser().getBeforeLastLogin() == null)
                response.sendRedirect("/secured/profile");
            else
                response.sendRedirect("/secured/plant-asset");
        }
    }

}

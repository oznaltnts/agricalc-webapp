package tr.ozanbey.agricalc.webapp.webapp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.io.IOException;

@Component
public class SecurityFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private LoginController loginController;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        String phone = httpServletRequest.getParameter("loginForm:phone");
        Long userId = loginController.getUserIdByPhoneAndStatus(phone, EnumStatus.ACTIVE);

        if (userId != null) {
            long failureCount = loginController.createLoginFailure(userId);
            if (failureCount > 2) {
                super.setDefaultFailureUrl("/public/login?error=blocked");
            } else
                super.setDefaultFailureUrl("/public/login?&error=" + failureCount);
        } else
            super.setDefaultFailureUrl("/public/login?error=usernotfound");

        super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
    }

}

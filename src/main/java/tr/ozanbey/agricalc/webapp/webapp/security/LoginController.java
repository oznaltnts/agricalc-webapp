package tr.ozanbey.agricalc.webapp.webapp.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.User;
import tr.ozanbey.agricalc.webapp.service.domain.UserPreference;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.service.UserLoginService;
import tr.ozanbey.agricalc.webapp.service.service.UserService;
import tr.ozanbey.agricalc.webapp.webapp.util.context.SpringContextAccessor;
import tr.ozanbey.agricalc.webapp.webapp.view.GuestPreferences;

import java.util.Optional;

@Component
@Getter
@Setter
public class LoginController {

    private boolean rememberMe;

    @Autowired
    private UserService userService;

    @Autowired
    private UserLoginService userLoginService;

    public long isAccountBlocked(Long userId) {
        return userLoginService.isAccountBlocked(userId);
    }

    public void assignUserPreference(Long userId) {
        UserPreference userPreference = userService.getPreferenceByUserId(userId);
        GuestPreferences guestPreferences = SpringContextAccessor.getBean(GuestPreferences.class);
        guestPreferences.setMenuMode(userPreference.getMenuMode());
        guestPreferences.setDarkMode(userPreference.getDarkMode());
        guestPreferences.setComponentTheme(userPreference.getComponentTheme());
        guestPreferences.setTopbarTheme(userPreference.getTopbarTheme());
        guestPreferences.setMenuTheme(userPreference.getMenuTheme());
        guestPreferences.setInputStyle(userPreference.getInputStyle());
        guestPreferences.setLightLogo(userPreference.isLightLogo());
    }

    public void clearLoginFailures(Long userId) {
        userLoginService.clearLoginFailures(userId);
    }

    public void createLoginSuccess(Long userId) {
        userLoginService.createLoginSuccess(userId);
    }

    public long createLoginFailure(Long userId) {
        userLoginService.createLoginFailure(userId);
        return isAccountBlocked(userId);
    }

    public Long getUserIdByPhoneAndStatus(String phone, EnumStatus status) {
        return getUserByPhoneAndStatus(phone, status).orElse(new User()).getId();
    }

    public Optional<User> getUserByPhoneAndStatus(String phone, EnumStatus status) {
        return userService.getUserIdByPhoneAndStatus(phone, status);
    }

}

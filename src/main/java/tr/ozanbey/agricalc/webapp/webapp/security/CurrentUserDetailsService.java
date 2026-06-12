package tr.ozanbey.agricalc.webapp.webapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.User;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

@Service
public class CurrentUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginController loginController;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        String formatted = normalizePhone(phone);

        User user = loginController.getUserByPhoneAndStatus(formatted, EnumStatus.ACTIVE)
                .orElseThrow(() -> new UsernameNotFoundException((String.format("User with phone=%s was not found", formatted))));
        return new CurrentUser(user);
    }

    private String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return phone;
        }
        // Zaten canonical formatta mı?
        if (phone.matches("^\\+90\\d{10}$")) {
            return phone;
        }
        // Tüm non-digit karakterleri kaldır
        String digits = phone.replaceAll("\\D", "");
        // 0534xxxxxxx → 534xxxxxxx
        if (digits.startsWith("0")) {
            digits = digits.substring(1);
        }
        // 90534xxxxxxx → 534xxxxxxx
        if (digits.startsWith("90") && digits.length() == 12) {
            digits = digits.substring(2);
        }
        // Final canonical format
        return "+90" + digits;
    }

}

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

        User user = loginController.getUserByPhoneAndStatus(phone, EnumStatus.ACTIVE)
                .orElseThrow(() -> new UsernameNotFoundException((String.format("User with phone=%s was not found", phone))));
        return new CurrentUser(user);
    }

}

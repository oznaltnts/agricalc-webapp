package tr.ozanbey.agricalc.webapp.webapp.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tr.ozanbey.agricalc.webapp.service.domain.User;

import java.util.stream.Collectors;


@Setter
@Getter
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user) {
        super(user.getPhone(),
                user.getPassword(),
                user.getRoleList()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRole().toString()))
                        .collect(Collectors.toList()));
        this.user = user;
    }

}

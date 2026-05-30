package tr.ozanbey.agricalc.webapp.service.domain;


import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class User extends AbstractStatusEntity {

    @Column(name = "phone", unique = true, nullable = false, length = 25)
    @ToString.Include
    private String phone;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserRole> roleList;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "before_last_login")
    private LocalDateTime beforeLastLogin;

    public User(Long id) {
        super.setId(id);
    }

    public EnumRole getUserRole() {
        return this.getRoleList().iterator().next().getRole();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return phone != null && phone.equals(user.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }

}

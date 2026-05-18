package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_login_failures")
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginFailure extends AbstractStatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "ip_address", nullable = false, length = 25)
    private String ipAddress;

}

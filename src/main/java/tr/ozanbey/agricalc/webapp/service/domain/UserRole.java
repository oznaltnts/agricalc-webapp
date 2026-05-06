package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.converter.EnumRoleConverter;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumRole;

@Getter
@Setter
@Entity
@Table(name = "user_roles")
@NoArgsConstructor
public class UserRole extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Convert(converter = EnumRoleConverter.class)
    @Column(name = "role", nullable = false, columnDefinition = "TINYINT")
    private EnumRole role;

}

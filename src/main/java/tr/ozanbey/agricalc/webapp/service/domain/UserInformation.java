package tr.ozanbey.agricalc.webapp.service.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@Table(name = "user_informations")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class UserInformation extends AbstractEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    private City city;

    @Column(name = "tckn")
    private String tckn;

    @Column(name = "name_surname")
    @ToString.Include
    private String nameSurname;

    @Column(name = "email", unique = true)
    @ToString.Include
    private String email;

    @Column(name = "district")
    @ToString.Include
    private String district;

    @Column(name = "village")
    @ToString.Include
    private String village;

    @Column(name = "neighborhood")
    @ToString.Include
    private String neighborhood;

}

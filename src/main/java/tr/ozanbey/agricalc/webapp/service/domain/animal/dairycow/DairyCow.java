package tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;

@Getter
@Setter
@Entity
@Table(name = "dairy_cows")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class DairyCow extends AbstractStatusEntity {

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

}

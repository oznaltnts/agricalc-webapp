package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "crops")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Crop extends AbstractStatusEntity {

    @Column(name = "group_name", nullable = false)
    @ToString.Include
    private String groupName;

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

}

package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "crop_groups")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CropGroup extends AbstractStatusEntity {

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

    @OneToMany(mappedBy = "cropGroup", fetch = FetchType.LAZY)
    private List<Crop> crop;

}

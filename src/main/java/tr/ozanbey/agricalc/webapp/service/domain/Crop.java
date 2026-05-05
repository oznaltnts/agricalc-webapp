package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "crops")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Crop extends AbstractStatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_group_id", referencedColumnName = "id", nullable = false)
    private CropGroup cropGroup;

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

}

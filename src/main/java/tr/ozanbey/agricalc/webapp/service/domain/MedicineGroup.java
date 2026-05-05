package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumMedicineGroupType;

@Getter
@Setter
@Entity
@Table(name = "medicine_groups")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class MedicineGroup extends AbstractStatusEntity {

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumMedicineGroupType type;

    @Column(name = "group_disease", nullable = false)
    @ToString.Include
    private String groupDisease;

}

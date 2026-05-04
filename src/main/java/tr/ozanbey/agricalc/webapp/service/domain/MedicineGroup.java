package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Group disease cannot be blank")
    @Size(min = 1, max = 255, message = "Group disease must be between 1 and 255 characters")
    @ToString.Include
    private String groupDisease;

}

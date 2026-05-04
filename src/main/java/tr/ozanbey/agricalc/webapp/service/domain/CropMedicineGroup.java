package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "crop_medicine_groups")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CropMedicineGroup extends AbstractStatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id", referencedColumnName = "id", nullable = false)
    private Crop crop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_group_id", referencedColumnName = "id", nullable = false)
    private MedicineGroup medicineGroup;

    @OneToMany(mappedBy = "cropMedicineGroup", fetch = FetchType.LAZY)
    private List<CropMedicineGroupValue> cropMedicineGroupValueList;

}

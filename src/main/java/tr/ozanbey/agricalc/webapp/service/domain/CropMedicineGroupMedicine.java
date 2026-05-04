package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "crop_medicine_group_medicines")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CropMedicineGroupMedicine extends AbstractStatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_medicine_group_id", referencedColumnName = "id", nullable = false)
    private CropMedicineGroup cropMedicineGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", referencedColumnName = "id", nullable = false)
    private Medicine medicine;

    @OneToMany(mappedBy = "cropMedicineGroupMedicine", fetch = FetchType.LAZY)
    private List<CropMedicineGroupMedicineValue> cropMedicineGroupMedicineValueList;

}

package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "crop_medicine_group_medicine_values")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CropMedicineGroupMedicineValue extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_medicine_group_medicine_id", referencedColumnName = "id", nullable = false)
    private CropMedicineGroupMedicine cropMedicineGroupMedicine;

    @Column(name = "amount", nullable = false)
    @ToString.Include
    private BigDecimal amount;

    @Column(name = "water_amount", nullable = false)
    @ToString.Include
    private BigDecimal waterAmount;

}

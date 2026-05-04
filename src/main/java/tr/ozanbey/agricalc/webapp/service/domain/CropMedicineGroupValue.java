package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "crop_medicine_group_values")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CropMedicineGroupValue extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_medicine_group_id", referencedColumnName = "id", nullable = false)
    private CropMedicineGroup cropMedicineGroup;

    @Column(name = "coefficient", nullable = false)
    @NotBlank(message = "Coefficient cannot be blank")
    @ToString.Include
    private BigDecimal coefficient;

    @Column(name = "price", nullable = false)
    @NotBlank(message = "Price cannot be blank")
    @ToString.Include
    private BigDecimal price;

}

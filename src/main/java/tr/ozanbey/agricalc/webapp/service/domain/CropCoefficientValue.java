package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "crop_coefficient_values")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CropCoefficientValue extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_coefficient_id", referencedColumnName = "id", nullable = false)
    private CropCoefficient cropCoefficient;

    @Column(name = "value", nullable = false)
    @ToString.Include
    private String value;

}

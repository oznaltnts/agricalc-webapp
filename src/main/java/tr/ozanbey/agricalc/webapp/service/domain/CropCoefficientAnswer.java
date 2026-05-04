package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "crop_coefficient_answers")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CropCoefficientAnswer extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_coefficient_id", referencedColumnName = "id", nullable = false)
    private CropCoefficient cropCoefficient;

    @Column(name = "value", nullable = false)
    @NotBlank(message = "Value cannot be blank")
    @Size(min = 1, max = 255, message = "Value must be between 1 and 255 characters")
    @ToString.Include
    private String value;
}

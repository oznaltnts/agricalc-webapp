package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumCropCoefficientType;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumCropCoefficientValue;


@Getter
@Setter
@Entity
@Table(name = "coefficients")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Coefficient extends AbstractStatusEntity {

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumCropCoefficientType type;

    @Column(name = "value", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumCropCoefficientValue value;

}

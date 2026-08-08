package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumCoefficientType;

@Getter
@Setter
@Entity
@Table(name = "plantation_coefficients")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class PlantationCoefficient extends AbstractEntity {

    @Column(name = "coef_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumCoefficientType enumCoefficientType;

    @Column(name = "diesel_value")
    @ToString.Include
    private Double dieselValue;

    @Column(name = "labor_value", nullable = false)
    @ToString.Include
    private Double laborValue;

}

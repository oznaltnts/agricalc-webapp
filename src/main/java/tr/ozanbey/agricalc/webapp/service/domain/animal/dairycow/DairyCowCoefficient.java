package tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.converter.animal.dairycow.EnumCowTypeConverter;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.dairycow.EnumCowType;

@Getter
@Setter
@Entity
@Table(name = "dairy_cow_coefficients")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class DairyCowCoefficient extends AbstractEntity {

    @Convert(converter = EnumCowTypeConverter.class)
    @Column(name = "cow_type", nullable = false, columnDefinition = "TINYINT")
    @ToString.Include
    private EnumCowType cowType;

    @Column(name = "value", nullable = false)
    @ToString.Include
    private double value;

}

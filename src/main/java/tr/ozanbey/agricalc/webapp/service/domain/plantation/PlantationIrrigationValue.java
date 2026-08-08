package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumIrrigationType;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "plantation_irrigation_values")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class PlantationIrrigationValue extends AbstractEntity {

    @Column(name = "price_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumIrrigationType irrigationType;

    @Column(name = "price_value")
    @ToString.Include
    private BigDecimal priceValue;

    @Column(name = "labor_value", nullable = false)
    @ToString.Include
    private Double laborValue;

    @Column(name = "diesel_value")
    @ToString.Include
    private Double dieselValue;

}

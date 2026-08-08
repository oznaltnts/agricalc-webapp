package tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.converter.animal.dairycow.EnumCostTypeConverter;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.dairycow.EnumCostType;

@Getter
@Setter
@Entity
@Table(name = "dairy_cow_costs")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class DairyCowCost extends AbstractStatusEntity {

    @Convert(converter = EnumCostTypeConverter.class)
    @Column(name = "cost_type", nullable = false, columnDefinition = "TINYINT")
    @ToString.Include
    private EnumCostType costType;

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

}

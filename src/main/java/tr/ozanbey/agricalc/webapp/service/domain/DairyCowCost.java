package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.converter.EnumCostTypeConverter;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumCostType;

@Getter
@Setter
@Entity
@Table(name = "costs")
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

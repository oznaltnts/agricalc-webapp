package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.converter.plantation.EnumPlantationQuestionConverter;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumAllocationType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "user_plant_parcel_plan_allocations")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class UserPlantParcelPlanAllocation extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_plan_id", referencedColumnName = "id", nullable = false)
    private UserPlantParcelPlan plantParcelPlan;

    @Convert(converter = EnumPlantationQuestionConverter.class)
    @Column(name = "q_type", nullable = false, columnDefinition = "TINYINT")
    @ToString.Include
    private EnumPlantationQuestionType questionType;

    @Column(name = "a_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumAllocationType allocationType;

    @Column(name = "calculated_value", nullable = false)
    @ToString.Include
    private BigDecimal calculatedValue;

}

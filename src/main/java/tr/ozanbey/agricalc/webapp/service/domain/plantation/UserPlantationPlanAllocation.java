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
@Table(name = "user_plantation_plan_allocations")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class UserPlantationPlanAllocation extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_plantation_plan_id", referencedColumnName = "id", nullable = false)
    private UserPlantationPlan userPlantationPlan;

    @Convert(converter = EnumPlantationQuestionConverter.class)
    @Column(name = "q_type", columnDefinition = "TINYINT", nullable = false)
    @ToString.Include
    private EnumPlantationQuestionType questionType;

    @Column(name = "a_type", length = 33, nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumAllocationType allocationType;

    @Column(name = "calculated_value", precision = 15, scale = 3, nullable = false)
    @ToString.Include
    private BigDecimal calculatedValue;

}

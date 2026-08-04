package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;

@Getter
@Setter
@Entity
@Table(name = "user_plant_parcel_plan_answers")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class UserPlantParcelPlanAnswer extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_plan_id", referencedColumnName = "id", nullable = false)
    private UserPlantParcelPlan plantParcelPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_question_id", referencedColumnName = "id", nullable = false)
    private PlantationProductQuestion productQuestion;

    @Column(name = "answer_value")
    @ToString.Include
    private String answerValue;

}

package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;

@Getter
@Setter
@Entity
@Table(name = "user_plantation_plan_answers")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class UserPlantationPlanAnswer extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_plantation_plan_id", referencedColumnName = "id", nullable = false)
    private UserPlantationPlan userPlantationPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_question_id", referencedColumnName = "id", nullable = false)
    private PlantationProductQuestion productQuestion;

    @Column(name = "answer_value")
    @ToString.Include
    private String answerValue;

}

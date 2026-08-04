package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;

@Getter
@Setter
@Entity
@Table(name = "user_plant_parcel_answers")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class UserPlantParcelAnswer extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_parcel_id", referencedColumnName = "id", nullable = false)
    private UserPlantParcel plantParcel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_question_id", referencedColumnName = "id", nullable = false)
    private PlantationProductQuestion productQuestion;

    @Column(name = "answer_value")
    @ToString.Include
    private String answerValue;

}

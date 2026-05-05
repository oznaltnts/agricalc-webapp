package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "city_crop_question_answers")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CityCropQuestionAnswer extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_crop_question_id", referencedColumnName = "id", nullable = false)
    private CityCropQuestion cityCropQuestion;

    @Column(name = "value", nullable = false)
    @ToString.Include
    private String value;
}

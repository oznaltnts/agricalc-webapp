package tr.ozanbey.agricalc.webapp.service.domain.literacy;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;

@Getter
@Setter
@Entity
@Table(name = "user_literacy_answers")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class UserLiteracyAnswer extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_literacy_assessment_id", referencedColumnName = "id", nullable = false)
    private UserLiteracyAssessment userLiteracyAssessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "literacy_question_id", referencedColumnName = "id", nullable = false)
    private LiteracyQuestion literacyQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "literacy_question_option_id", referencedColumnName = "id", nullable = false)
    private LiteracyQuestionOption literacyQuestionOption;

}

package tr.ozanbey.agricalc.webapp.service.domain.literacy;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;

@Getter
@Setter
@Entity
@Table(name = "literacy_question_options")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class LiteracyQuestionOption extends AbstractStatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "literacy_question_id", referencedColumnName = "id", nullable = false)
    private LiteracyQuestion literacyQuestion;

    @Column(name = "option_value", nullable = false)
    @ToString.Include
    private String value;

    @Column(name = "score_10_value", nullable = false)
    @ToString.Include
    private Double score10Value;

    @Column(name = "score_11_value", nullable = false)
    @ToString.Include
    private Double score11Value;


}

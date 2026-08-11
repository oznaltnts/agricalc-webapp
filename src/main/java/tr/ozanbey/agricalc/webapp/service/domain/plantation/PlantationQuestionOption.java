package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;

@Getter
@Setter
@Entity
@Table(name = "plantation_question_options")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class PlantationQuestionOption extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plantation_question_id", referencedColumnName = "id", nullable = false)
    private PlantationQuestion plantationQuestion;

    @Column(name = "value", nullable = false)
    @ToString.Include
    private String value;


}

package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "plantation_questions")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class PlantationQuestion extends AbstractStatusEntity {

    @Column(name = "value", nullable = false)
    @ToString.Include
    private String value;

    @OneToMany(mappedBy = "plantationQuestion", fetch = FetchType.LAZY)
    private List<PlantationQuestionOption> questionOptionList;

    @OneToMany(mappedBy = "plantationQuestion", fetch = FetchType.LAZY)
    private List<PlantationProductQuestion> productQuestionList;

}

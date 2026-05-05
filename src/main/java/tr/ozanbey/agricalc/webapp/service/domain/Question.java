package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumQuestionType;

@Getter
@Setter
@Entity
@Table(name = "questions")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Question extends AbstractStatusEntity {

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumQuestionType type;

    @Column(name = "value", nullable = false)
    @ToString.Include
    private String value;

}

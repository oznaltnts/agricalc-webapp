package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Question text cannot be blank")
    @Size(min = 1, max = 255, message = "Question text must be between 1 and 255 characters")
    @ToString.Include
    private String value;

}

package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Value cannot be blank")
    @Size(min = 1, max = 255, message = "Value must be between 1 and 255 characters")
    @ToString.Include
    private String value;
}

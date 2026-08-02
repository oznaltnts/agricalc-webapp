package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.converter.plantation.EnumPlantationQuestionConverter;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionAnswerType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionRecordType;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "plantation_questions")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class PlantationQuestion extends AbstractStatusEntity {

    @Column(name = "q_value", nullable = false)
    @ToString.Include
    private String value;

    @Convert(converter = EnumPlantationQuestionConverter.class)
    @Column(name = "q_type", nullable = false, columnDefinition = "TINYINT")
    @ToString.Include
    private EnumPlantationQuestionType questionType;

    @Column(name = "a_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumQuestionAnswerType answerType;

    @Column(name = "r_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumQuestionRecordType recordType;

    @Column(name = "is_required", columnDefinition = "TINYINT")
    private Boolean isRequired;

    @OneToMany(mappedBy = "plantationQuestion", fetch = FetchType.LAZY)
    private List<PlantationQuestionOption> questionOptionList;

    @OneToMany(mappedBy = "plantationQuestion", fetch = FetchType.LAZY)
    private List<PlantationProductQuestion> productQuestionList;

}

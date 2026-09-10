package tr.ozanbey.agricalc.webapp.service.domain.literacy;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.converter.literacy.EnumLiteracyQuestionConverter;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;
import tr.ozanbey.agricalc.webapp.service.enumtype.literacy.EnumLiteracyQuestionType;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "literacy_questions")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class LiteracyQuestion extends AbstractStatusEntity {

    @Column(name = "q_value", nullable = false)
    @ToString.Include
    private String value;

    @Convert(converter = EnumLiteracyQuestionConverter.class)
    @Column(name = "q_type", nullable = false, columnDefinition = "TINYINT")
    @ToString.Include
    private EnumLiteracyQuestionType questionType;

    @OneToMany(mappedBy = "literacyQuestion", fetch = FetchType.LAZY)
    private List<LiteracyQuestionOption> questionOptionList;

    @Transient
    private Long selectedAnswerId;

}

package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "plantation_product_questions")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class PlantationProductQuestion extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plantation_product_id", referencedColumnName = "id", nullable = false)
    private PlantationProduct plantationProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plantation_question_id", referencedColumnName = "id", nullable = false)
    private PlantationQuestion plantationQuestion;

    @Column(name = "minimum_value")
    @ToString.Include
    private BigDecimal minimumValue;

    @Column(name = "maximum_value")
    @ToString.Include
    private BigDecimal maximumValue;

    @OneToMany(mappedBy = "productQuestion", fetch = FetchType.LAZY)
    private List<UserPlantPlanAnswer> questionAnswerList;

    @Transient
    private String selectedAnswer;

    @Transient
    private Long selectedAnswerId;

    @Transient
    private List<Long> selectedAnswerIds;

    @Transient
    private Integer integerValue;

    @Transient
    private Double doubleValue;

    @Transient
    private BigDecimal bigDecimalValue;

    @Transient
    private boolean dontAskAgain;

}

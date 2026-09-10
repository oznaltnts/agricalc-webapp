package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "plantation_product_question_diseases")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class PlantationProductQuestionDisease extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_question_id", nullable = false)
    private PlantationProductQuestion productQuestion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "disease_id", nullable = false)
    private PlantationDisease disease;

    @ColumnDefault("0")
    @Column(name = "usage_coefficient", nullable = false)
    @ToString.Include
    private Double usageCoefficient;

    @ColumnDefault("0.000")
    @Column(name = "adhesive_price", nullable = false, precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal adhesivePrice;

    @ColumnDefault("25")
    @Column(name = "water_amount", nullable = false)
    @ToString.Include
    private Double waterAmount;

    @OneToMany(mappedBy = "productQuestionDisease", fetch = FetchType.LAZY)
    private List<PlantationProductQuestionDiseaseMedicine> productDiseaseMedicineList;

}
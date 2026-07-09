package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;

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
    private Double minimumValue;

    @Column(name = "maximum_value")
    @ToString.Include
    private Double maximumValue;

}

package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;

@Getter
@Setter
@Entity
@Table(name = "plantation_product_question_disease_medicines")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class PlantationProductQuestionDiseaseMedicine extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_question_disease_id", nullable = false)
    private PlantationProductQuestionDisease productQuestionDisease;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id", nullable = false)
    private PlantationMedicine medicine;

    @ColumnDefault("0")
    @Column(name = "medicine_dosage", nullable = false)
    @ToString.Include
    private Double medicineDosage;

}
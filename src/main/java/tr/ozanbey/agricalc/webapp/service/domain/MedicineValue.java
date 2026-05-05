package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "medicine_values")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class MedicineValue extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", referencedColumnName = "id", nullable = false)
    private Medicine medicine;

    @Column(name = "packaging_size", nullable = false)
    @ToString.Include
    private BigDecimal packagingSize;

    @Column(name = "price", nullable = false)
    @ToString.Include
    private BigDecimal price;

}

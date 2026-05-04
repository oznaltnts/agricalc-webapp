package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Packaging size cannot be blank")
    @ToString.Include
    private BigDecimal packagingSize;

    @Column(name = "price", nullable = false)
    @NotBlank(message = "Price cannot be blank")
    @ToString.Include
    private BigDecimal price;

}

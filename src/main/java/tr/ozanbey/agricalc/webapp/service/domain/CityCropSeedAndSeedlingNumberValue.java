package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "city_crop_seed_and_seedling_number_values")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CityCropSeedAndSeedlingNumberValue extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_crop_seed_and_seedling_number_id", referencedColumnName = "id", nullable = false)
    private CityCropSeedAndSeedlingNumber seedSeedlingNumber;

    @Column(name = "value", nullable = false)
    @NotBlank(message = "Value cannot be blank")
    @ToString.Include
    private BigDecimal value;

}

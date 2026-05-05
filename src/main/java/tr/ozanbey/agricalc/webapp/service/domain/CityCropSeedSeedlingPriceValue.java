package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "city_crop_seed_and_seedling_price_values")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CityCropSeedSeedlingPriceValue extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_crop_seed_and_seedling_price_id", referencedColumnName = "id", nullable = false)
    private CityCropSeedSeedlingPrice seedSeedlingPrice;

    @Column(name = "value", nullable = false)
    @ToString.Include
    private BigDecimal value;

}

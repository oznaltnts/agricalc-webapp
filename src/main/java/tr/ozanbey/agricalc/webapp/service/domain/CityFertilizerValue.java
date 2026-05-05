package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "city_fertilizer_values")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CityFertilizerValue extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_fertilizer_id", referencedColumnName = "id", nullable = false)
    private CityFertilizer cityFertilizer;

    @Column(name = "price", nullable = false)
    @ToString.Include
    private BigDecimal price;

}

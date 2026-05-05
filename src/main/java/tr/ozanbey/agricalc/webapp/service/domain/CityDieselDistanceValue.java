package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "city_diesel_distance_values")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CityDieselDistanceValue extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_diesel_distance_id", referencedColumnName = "id", nullable = false)
    private CityDieselDistance cityDieselDistance;

    @Column(name = "value", nullable = false)
    @ToString.Include
    private BigDecimal value;

}

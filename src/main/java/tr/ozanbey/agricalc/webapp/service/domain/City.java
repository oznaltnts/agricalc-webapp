package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.converter.LongListConverter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "cities")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class City extends AbstractEntity {

    @Column(name = "code", columnDefinition = "TINYINT", nullable = false)
    @ToString.Include
    private int code;

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

    @Column(name = "neighbors_ids", length = 45)
    @Convert(converter = LongListConverter.class)
    private List<Long> neighborsIds;

    @Column(name = "is_triple", columnDefinition = "TINYINT")
    private Boolean isTriple;

    @Column(name = "diesel_price", precision = 15, scale = 3, nullable = false)
    private BigDecimal dieselPrice;

    @Column(name = "fuel_price", precision = 15, scale = 3, nullable = false)
    private BigDecimal fuelPrice;

    @Column(name = "electricity", precision = 15, scale = 3, nullable = false)
    private BigDecimal electricity;

    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CityDistrict> districtList;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(code, city.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}

package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumDieselDistanceType;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "city_diesel_distances")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CityDieselDistance extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    private City city;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumDieselDistanceType type;

    @OneToMany(mappedBy = "cityDieselDistance", fetch = FetchType.LAZY)
    private List<CityDieselDistanceValue> cityDieselDistanceValueList;

}

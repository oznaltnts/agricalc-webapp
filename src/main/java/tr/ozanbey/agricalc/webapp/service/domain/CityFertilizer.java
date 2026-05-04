package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "city_fertilizers")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CityFertilizer extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fertilizer_id", referencedColumnName = "id", nullable = false)
    private Fertilizer fertilizer;

    @OneToMany(mappedBy = "cityFertilizer", fetch = FetchType.LAZY)
    private List<CityFertilizerValue> cityFertilizerValueList;

}

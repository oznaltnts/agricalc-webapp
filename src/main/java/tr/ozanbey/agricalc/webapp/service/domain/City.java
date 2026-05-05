package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.converter.LongListConverter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cities")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class City extends AbstractEntity {

    @Column(name = "code", nullable = false, length = 25)
    @ToString.Include
    private String code;

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

    @Column(name = "neighbors_ids")
    @Convert(converter = LongListConverter.class)
    private List<Long> neighborsIds;

    public City(Long selectedCityId) {
        super.setId(selectedCityId);
    }
}

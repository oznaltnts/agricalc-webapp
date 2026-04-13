package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Code cannot be blank")
    @Size(min = 1, max = 25, message = "Code must be between 1 and 25 characters")
    @ToString.Include
    private String code;

    @Column(name = "name", nullable = false, length = 25)
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 25, message = "Name must be between 1 and 25 characters")
    @ToString.Include
    private String name;

    @Column(name = "neighbors_ids")
    @Convert(converter = LongListConverter.class)
    private List<Long> neighborsIds;

    public City(Long selectedCityId) {
        super.setId(selectedCityId);
    }
}

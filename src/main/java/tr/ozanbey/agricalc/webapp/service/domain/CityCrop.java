package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "city_crops")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CityCrop extends AbstractStatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id", referencedColumnName = "id", nullable = false)
    private Crop crop;
}

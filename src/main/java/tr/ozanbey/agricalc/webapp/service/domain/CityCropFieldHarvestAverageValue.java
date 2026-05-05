package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "city_crop_field_harvest_average_values")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CityCropFieldHarvestAverageValue extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_crop_id", referencedColumnName = "id", nullable = false)
    private CityCrop cityCrop;

    @Column(name = "collected_crop_count", nullable = false)
    @ToString.Include
    private BigDecimal collectedCropCount;

}

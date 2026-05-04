package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "city_crop_watering_values")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CityCropWateringValue extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_crop_id", referencedColumnName = "id", nullable = false)
    private CityCrop cityCrop;

    @Column(name = "maintenance", nullable = false)
    @NotBlank(message = "maintenance cannot be blank")
    @ToString.Include
    private BigDecimal maintenance;

}

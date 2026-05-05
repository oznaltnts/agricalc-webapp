package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumFertilizerType;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "fertilizers")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Fertilizer extends AbstractEntity {

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumFertilizerType type;

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

    @Column(name = "nitrogen_percent", nullable = false)
    @ToString.Include
    private BigDecimal nitrogenPercent;

    @Column(name = "phosphor_percent", nullable = false)
    @ToString.Include
    private BigDecimal phosphorPercent;

    @Column(name = "potassium_percent", nullable = false)
    @ToString.Include
    private BigDecimal potassiumPercent;

    @Column(name = "old_fertilizer_id", updatable = false)
    @ToString.Include
    private Long oldFertilizerId;

}

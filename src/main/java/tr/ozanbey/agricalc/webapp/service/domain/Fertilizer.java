package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    @ToString.Include
    private String name;

    @Column(name = "nitrogen_percent", nullable = false)
    @NotBlank(message = "Nitrogen percent price cannot be blank")
    @ToString.Include
    private BigDecimal nitrogenPercent;

    @Column(name = "phosphor_percent", nullable = false)
    @NotBlank(message = "Phosphor percent cannot be blank")
    @ToString.Include
    private BigDecimal phosphorPercent;

    @Column(name = "potassium_percent", nullable = false)
    @NotBlank(message = "Potassium percent cannot be blank")
    @ToString.Include
    private BigDecimal potassiumPercent;

    @Column(name = "old_fertilizer_id", updatable = false)
    @ToString.Include
    private Long oldFertilizerId;

}

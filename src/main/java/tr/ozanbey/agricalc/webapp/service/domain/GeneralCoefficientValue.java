package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "general_coefficient_values")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class GeneralCoefficientValue extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_coefficient_id", referencedColumnName = "id", nullable = false)
    private GeneralCoefficient generalCoefficient;

    @Column(name = "value", nullable = false)
    @ToString.Include
    private BigDecimal value;
}

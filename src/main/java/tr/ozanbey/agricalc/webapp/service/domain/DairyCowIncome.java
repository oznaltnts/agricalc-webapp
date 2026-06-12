package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "dairy_cow_incomes")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class DairyCowIncome extends AbstractStatusEntity {

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

    @Column(name = "unit", nullable = false)
    @ToString.Include
    private String unit;

}

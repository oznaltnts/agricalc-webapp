package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "general_coefficients")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class GeneralCoefficient extends AbstractStatusEntity {

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

    @OneToMany(mappedBy = "generalCoefficient", fetch = FetchType.LAZY)
    private List<GeneralCoefficientValue> generalCoefficientValueList;

}

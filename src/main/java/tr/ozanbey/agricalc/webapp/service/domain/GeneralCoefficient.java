package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    @ToString.Include
    private String name;

    @OneToMany(mappedBy = "generalCoefficient", fetch = FetchType.LAZY)
    private List<GeneralCoefficientValue> generalCoefficientValueList;

}

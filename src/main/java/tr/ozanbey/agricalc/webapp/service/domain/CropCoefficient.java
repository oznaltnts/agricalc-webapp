package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "crop_coefficients")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CropCoefficient extends AbstractStatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id", referencedColumnName = "id", nullable = false)
    private Crop crop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coefficient_id", referencedColumnName = "id", nullable = false)
    private Coefficient coefficient;

    @OneToMany(mappedBy = "cropCoefficient", fetch = FetchType.LAZY)
    private List<CropCoefficientAnswer> cropCoefficientAnswerList;

}

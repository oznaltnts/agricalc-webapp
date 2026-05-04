package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "crops")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Crop extends AbstractStatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_group_id", referencedColumnName = "id", nullable = false)
    private CropGroup cropGroup;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    @ToString.Include
    private String name;

}

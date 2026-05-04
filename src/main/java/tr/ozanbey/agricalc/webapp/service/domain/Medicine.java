package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "medicines")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Medicine extends AbstractStatusEntity {

    @Column(name = "brand", nullable = false)
    @NotBlank(message = "Brand cannot be blank")
    @Size(min = 1, max = 255, message = "Brand must be between 1 and 255 characters")
    @ToString.Include
    private String brand;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    @ToString.Include
    private String name;

    @Column(name = "ingredient", nullable = false)
    @NotBlank(message = "Ingredient cannot be blank")
    @Size(min = 1, max = 255, message = "Ingredient must be between 1 and 255 characters")
    @ToString.Include
    private String ingredient;

}

package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    @ToString.Include
    private String brand;

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

    @Column(name = "ingredient", nullable = false)
    @ToString.Include
    private String ingredient;

}

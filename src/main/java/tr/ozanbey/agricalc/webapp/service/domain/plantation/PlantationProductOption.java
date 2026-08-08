package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;

@Getter
@Setter
@Entity
@Table(name = "plantation_product_options")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class PlantationProductOption extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plantation_product_id", referencedColumnName = "id", nullable = false)
    private PlantationProduct plantationProduct;

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;


}

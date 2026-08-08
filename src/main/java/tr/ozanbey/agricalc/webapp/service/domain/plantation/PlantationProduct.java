package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "plantation_products")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class PlantationProduct extends AbstractStatusEntity {

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

    @OneToMany(mappedBy = "plantationProduct", fetch = FetchType.LAZY)
    private List<PlantationProductQuestion> productQuestionList;

    @OneToMany(mappedBy = "plantationProduct", fetch = FetchType.LAZY)
    private List<PlantationProductOption> productOptionList;

}

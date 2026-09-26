package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationType;

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

    @Column(name = "plantation_type", length = 33, nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumPlantationType plantationType;

    @OneToMany(mappedBy = "plantationProduct", fetch = FetchType.LAZY)
    private List<PlantationProductQuestion> productQuestionList;

    @OneToMany(mappedBy = "plantationProduct", fetch = FetchType.LAZY)
    private List<PlantationProductOption> productOptionList;

}

package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumDiseaseType;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "plantation_diseases")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class PlantationDisease extends AbstractStatusEntity {

    @Lob
    @Column(name = "disease_type", nullable = false, length = 50)
    @ColumnDefault("'OTHERS'")
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumDiseaseType type;

    @Column(name = "disease_name", nullable = false)
    @ToString.Include
    private String name;

    @OneToMany(mappedBy = "disease", fetch = FetchType.LAZY)
    private List<PlantationProductQuestionDisease> productQuestionDiseaseList;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlantationDisease disease = (PlantationDisease) o;
        return Objects.equals(getId(), disease.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "crops")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Crop extends AbstractStatusEntity {

    @Column(name = "group_name", nullable = false)
    @ToString.Include
    private String groupName;

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Crop crop = (Crop) o;
        return Objects.equals(groupName, crop.groupName) && Objects.equals(name, crop.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName, name);
    }
}

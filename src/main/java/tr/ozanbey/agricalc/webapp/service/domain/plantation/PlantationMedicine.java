package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumDiseaseType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "plantation_medicines")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class PlantationMedicine extends AbstractStatusEntity {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "disease_type_set", nullable = false, columnDefinition = "json")
    @ToString.Include
    private Set<EnumDiseaseType> typeSet;

    @Column(name = "medicine_brand", nullable = false, length = 50)
    @ToString.Include
    private String brand;

    @Column(name = "commercial_name", nullable = false)
    @ToString.Include
    private String commercialName;

    @Column(name = "active_ingredient", nullable = false)
    private String activeIngredient;

    @ColumnDefault("0.000")
    @Column(name = "medicine_price", nullable = false, precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal price;

    @ColumnDefault("0")
    @Column(name = "unit_packaging", nullable = false)
    @ToString.Include
    private Double unitPackaging;

    @OneToMany(mappedBy = "medicine", fetch = FetchType.LAZY)
    private List<PlantationProductQuestionDiseaseMedicine> productQuestionDiseaseMedicineList;


}
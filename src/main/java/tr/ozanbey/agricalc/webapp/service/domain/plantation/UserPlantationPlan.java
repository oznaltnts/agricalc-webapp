package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_plantation_plans")
@ToString(onlyExplicitlyIncluded = true)
public class UserPlantationPlan extends AbstractStatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_plantation_id", referencedColumnName = "id", nullable = false)
    @SQLRestriction("status IN (0, 1)") // Sadece status=0 veya status=1 olanları filtreler
    private UserPlantation userPlantation;

    @Column(name = "start_date", nullable = false)
    @ToString.Include
    private LocalDate planStartDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_product_id", referencedColumnName = "id", nullable = false)
    private PlantationProduct primaryProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secondary_product_id", referencedColumnName = "id")
    private PlantationProduct secondaryProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_product_id", referencedColumnName = "id")
    private PlantationProduct lastProduct;

    @Column(name = "gross_income", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal grossIncome;

    @Column(name = "total_expense", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal totalExpense;

    @Column(name = "soil_prep_cost", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal soilPrepCost;

    @Column(name = "planting_cost", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal plantingCost;

    @Column(name = "fertilizer_cost", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal fertilizerCost;

    @Column(name = "weed_control_cost", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal weedControlCost;

    @Column(name = "irrigation_cost", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal irrigationCost;

    @Column(name = "cultural_cost", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal culturalCost;

    @Column(name = "protection_cost", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal protectionCost;

    @Column(name = "harvest_cost", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal harvestCost;

    @Column(name = "blend_cost", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal blendCost;

    @Column(name = "drying_cost", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal dryingCost;

    @Column(name = "baling_cost", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal balingCost;

    @Column(name = "transportation_cost", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal transportationCost;

    @OneToMany(mappedBy = "userPlantationPlan", fetch = FetchType.LAZY)
    private List<UserPlantationPlanAnswer> planAnswerList;

    @OneToMany(mappedBy = "userPlantationPlan", fetch = FetchType.LAZY)
    private List<UserPlantationPlanAllocation> planAllocationList;

}

package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_plant_parcel_plans")
@ToString(onlyExplicitlyIncluded = true)
public class UserPlantParcelPlan extends AbstractStatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcel_id", referencedColumnName = "id", nullable = false)
    private UserPlantParcel plantParcel;

    @Column(name = "start_date")
    @ToString.Include
    private LocalDate planStartDate;

    @Column(name = "gross_income")
    @ToString.Include
    private BigDecimal grossIncome;

    @Column(name = "total_expense", nullable = false)
    @ToString.Include
    private BigDecimal totalExpense;

    @Column(name = "soil_prep_cost")
    @ToString.Include
    private BigDecimal soilPrepCost;

    @Column(name = "planting_cost")
    @ToString.Include
    private BigDecimal plantingCost;

    @Column(name = "fertilizer_cost")
    @ToString.Include
    private BigDecimal fertilizerCost;

    @Column(name = "weed_control_cost")
    @ToString.Include
    private BigDecimal weedControlCost;

    @Column(name = "irrigation_cost")
    @ToString.Include
    private BigDecimal irrigationCost;

    @Column(name = "cultural_cost")
    @ToString.Include
    private BigDecimal culturalCost;

    @Column(name = "protection_cost")
    @ToString.Include
    private BigDecimal protectionCost;

    @Column(name = "harvest_cost")
    @ToString.Include
    private BigDecimal harvestCost;

    @Column(name = "blend_cost")
    @ToString.Include
    private BigDecimal blendCost;

    @Column(name = "drying_cost")
    @ToString.Include
    private BigDecimal dryingCost;

    @Column(name = "baling_cost")
    @ToString.Include
    private BigDecimal balingCost;

    @Column(name = "transportation_cost")
    @ToString.Include
    private BigDecimal transportationCost;

    @OneToMany(mappedBy = "plantParcelPlan", fetch = FetchType.LAZY)
    private List<UserPlantParcelPlanAnswer> planAnswerList;

    @OneToMany(mappedBy = "plantParcelPlan", fetch = FetchType.LAZY)
    private List<UserPlantParcelPlanAllocation> planAllocationList;

}

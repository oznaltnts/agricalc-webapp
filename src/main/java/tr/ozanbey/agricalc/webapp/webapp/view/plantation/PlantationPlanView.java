package tr.ozanbey.agricalc.webapp.webapp.view.plantation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlantationPlanView implements Serializable {

    private Long plantationPlanId;
    private EnumStatus selectedStatus = EnumStatus.ACTIVE;
    private EnumPlantationType selectedPlantationType = EnumPlantationType.OPEN_FIELD;
    private boolean anyProcessStarted = false;
    private String processTooltipMessage = "Süreç başladıktan sonra değiştirilemez.";
    private LocalDate planStartDate;
    private Long selectedPrimaryProductId;
    private String selectedPrimaryProductName;
    private Long selectedSecondaryProductId;
    private String selectedSecondaryProductName;
    private Long selectedLastProductId;
    private String selectedLastProductName;
    //hesaplama bilgileri
    private BigDecimal grossIncome;
    private BigDecimal totalExpense;

    public PlantationPlanView(PlantationPlanView selected) {
        this.plantationPlanId = selected.getPlantationPlanId();
        this.selectedStatus = selected.getSelectedStatus();
        this.selectedPlantationType = selected.getSelectedPlantationType();
        this.anyProcessStarted = selected.isAnyProcessStarted();
        this.processTooltipMessage = selected.getProcessTooltipMessage();
        this.planStartDate = selected.getPlanStartDate();
        this.selectedPrimaryProductId = selected.getSelectedPrimaryProductId();
        this.selectedPrimaryProductName = selected.getSelectedPrimaryProductName();
        this.selectedSecondaryProductId = selected.getSelectedSecondaryProductId();
        this.selectedSecondaryProductName = selected.getSelectedSecondaryProductName();
        this.selectedLastProductId = selected.getSelectedLastProductId();
        this.selectedLastProductName = selected.getSelectedLastProductName();
        this.grossIncome = selected.getGrossIncome();
        this.totalExpense = selected.getTotalExpense();
    }
}

package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.xdev.chartjs.model.charts.PolarChart;
import software.xdev.chartjs.model.color.RGBAColor;
import software.xdev.chartjs.model.data.PolarData;
import software.xdev.chartjs.model.dataset.PolarDataset;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlan;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationPlanService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class PlanResultController extends BaseController {

    @Autowired
    private PlantationPlanService plantationPlanService;

    private Long parcelPlanId;
    private UserPlantationPlan plantationPlan;

    private String polarAreaModel;

    @PostConstruct
    public void init() {
    }

    public void setParcelPlanId(Long parcelPlanId) {
        if (Objects.equals(this.parcelPlanId, parcelPlanId)) {
            return;
        }
        this.parcelPlanId = parcelPlanId;
    }

    public void fillExpensePackagingQuestionList() throws IOException {
        if (parcelPlanId == null || !checkPlanIdForUser(parcelPlanId)) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }
        createPolarAreaModel();
    }

    private void createPolarAreaModel() {
        BigDecimal soilExpense = plantationPlan.getSoilExpense();
        BigDecimal plantingCost = plantationPlan.getPlantingCost();
        BigDecimal fertilizerCost = plantationPlan.getFertilizerCost();
        BigDecimal weedControlCost = plantationPlan.getWeedControlCost();
        BigDecimal irrigationCost = plantationPlan.getIrrigationCost();
        BigDecimal culturalCost = plantationPlan.getCulturalCost();
        BigDecimal protectionPost = plantationPlan.getProtectionCost();
        BigDecimal harvestCost = plantationPlan.getHarvestCost();
        BigDecimal blendCost = plantationPlan.getBlendCost();
        BigDecimal dryingCost = plantationPlan.getDryingCost();
        BigDecimal balingCost = plantationPlan.getBalingCost();
        BigDecimal transportationCost = plantationPlan.getTransportationCost();

        polarAreaModel = new PolarChart()
                .setData(new PolarData()
                        .addDataset(new PolarDataset()
                                .setData(soilExpense, plantingCost, fertilizerCost, weedControlCost,
                                        irrigationCost, culturalCost, protectionPost, harvestCost,
                                        blendCost, dryingCost, balingCost, transportationCost)
                                .setLabel("Gider haritası")
                                .addBackgroundColors(RGBAColor.BROWN, RGBAColor.YELLOW, RGBAColor.RED, RGBAColor.BLUE,
                                        RGBAColor.CRIMSON, RGBAColor.KHAKI, RGBAColor.LIGHT_BLUE, RGBAColor.VIOLET,
                                        RGBAColor.DARK_GREEN, RGBAColor.GOLD, RGBAColor.BLACK, RGBAColor.GREEN
                                )
                        )
                        .setLabels("toprak", "ekim", "gübre", "yabani ot",
                                "sulama", "kültürel", "koruma", "hasat",
                                "harman", "kurutma", "balya", "paket"))
                .toJson();
    }

    private boolean checkPlanIdForUser(Long parcelPlanId) {
        Optional<UserPlantationPlan> optionalPlan = plantationPlanService.getPlantPlanByIdAndUserId(parcelPlanId, getCurrentUser().getUser().getId());
        if (optionalPlan.isPresent()) {
            plantationPlan = optionalPlan.get();
            return true;
        }
        return false;
    }

    public void previousSaveExpense() throws IOException {
        plantationPlanService.savePlanAnswers(plantationPlan, EnumPlantationQuestionType.EXPENSE_PACKAGING);
        super.navigationController.redirectToUrl("/secured/plantation/expense-packaging-profile?parcelPlanId=" + parcelPlanId);
    }

}

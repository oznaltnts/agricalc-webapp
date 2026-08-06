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
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelPlan;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantParcelPlanService;
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
    private UserPlantParcelPlanService userPlantParcelPlanService;

    private Long parcelPlanId;
    private UserPlantParcelPlan parcelPlan;

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
        BigDecimal soilPrepCost = parcelPlan.getSoilPrepCost();
        BigDecimal plantingCost = parcelPlan.getPlantingCost();
        BigDecimal fertilizerCost = parcelPlan.getFertilizerCost();
        BigDecimal weedControlCost = parcelPlan.getWeedControlCost();
        BigDecimal irrigationCost = parcelPlan.getIrrigationCost();
        BigDecimal culturalCost = parcelPlan.getCulturalCost();
        BigDecimal protectionPost = parcelPlan.getProtectionCost();
        BigDecimal harvestCost = parcelPlan.getHarvestCost();
        BigDecimal blendCost = parcelPlan.getBlendCost();
        BigDecimal dryingCost = parcelPlan.getDryingCost();
        BigDecimal balingCost = parcelPlan.getBalingCost();
        BigDecimal transportationCost = parcelPlan.getTransportationCost();

        polarAreaModel = new PolarChart()
                .setData(new PolarData()
                        .addDataset(new PolarDataset()
                                .setData(soilPrepCost, plantingCost, fertilizerCost, weedControlCost,
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
        Optional<UserPlantParcelPlan> optionalPlan = userPlantParcelPlanService.getPlantPlanByIdAndUserId(parcelPlanId, getCurrentUser().getUser().getId());
        if (optionalPlan.isPresent()) {
            parcelPlan = optionalPlan.get();
            return true;
        }
        return false;
    }

    public void previousSaveExpense() throws IOException {
        userPlantParcelPlanService.savePlanAnswers(parcelPlan, EnumPlantationQuestionType.EXPENSE_PACKAGING);
        super.navigationController.redirectToUrl("/secured/plantation/expense-packaging-profile?parcelPlanId=" + parcelPlanId);
    }

}

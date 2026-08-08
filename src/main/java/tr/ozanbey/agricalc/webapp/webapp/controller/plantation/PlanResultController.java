package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.xdev.chartjs.model.charts.PieChart;
import software.xdev.chartjs.model.color.RGBAColor;
import software.xdev.chartjs.model.data.PieData;
import software.xdev.chartjs.model.dataset.PieDataset;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantParcelPlanService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
@ViewScoped
@Getter
@Setter
public class PlanResultController extends PlanProfileController {

    @Autowired
    private UserPlantParcelPlanService userPlantParcelPlanService;

    //    private String polarAreaModel;
    private String pieModel;

    private BigDecimal totalExpense = BigDecimal.ZERO;
    private List<ResultShowView> resultShowViewList = new ArrayList<>();

    @PostConstruct
    public void init() {
    }

    public void createResultPageValues() throws IOException {
        if (super.getParcelPlanId() == null || !checkPlanIdForUser(super.getParcelPlanId())) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }
        createPieChartModel();
    }

    private String convertRGBAColorToHex(RGBAColor rgbaColor) {
        if (rgbaColor == null) return "#000000";
        if (rgbaColor.getAlpha() >= 1.0)
            return String.format("#%02X%02X%02X", rgbaColor.getR(), rgbaColor.getG(), rgbaColor.getB());
        int a = (int) Math.round(rgbaColor.getAlpha() * 255);
        return String.format("#%02X%02X%02X%02X", rgbaColor.getR(), rgbaColor.getG(), rgbaColor.getB(), a);
    }

    private void createPieChartModel() {
        resultShowViewList.add(new ResultShowView("toprak", RGBAColor.BROWN, super.getParcelPlan().getSoilPrepCost()));
        resultShowViewList.add(new ResultShowView("ekim", RGBAColor.YELLOW, super.getParcelPlan().getPlantingCost()));
        resultShowViewList.add(new ResultShowView("gübre", RGBAColor.RED, super.getParcelPlan().getFertilizerCost()));
        resultShowViewList.add(new ResultShowView("yabani ot", RGBAColor.BLUE, super.getParcelPlan().getWeedControlCost()));
        resultShowViewList.add(new ResultShowView("sulama", RGBAColor.CRIMSON, super.getParcelPlan().getIrrigationCost()));
        resultShowViewList.add(new ResultShowView("kültürel", RGBAColor.KHAKI, super.getParcelPlan().getCulturalCost()));
        resultShowViewList.add(new ResultShowView("koruma", RGBAColor.LIGHT_BLUE, super.getParcelPlan().getProtectionCost()));
        resultShowViewList.add(new ResultShowView("hasat", RGBAColor.VIOLET, super.getParcelPlan().getHarvestCost()));
        resultShowViewList.add(new ResultShowView("harman", RGBAColor.DEEP_PINK, super.getParcelPlan().getBlendCost()));
        resultShowViewList.add(new ResultShowView("kurutma", RGBAColor.GOLD, super.getParcelPlan().getDryingCost()));
        resultShowViewList.add(new ResultShowView("balya", RGBAColor.AZURE, super.getParcelPlan().getBalingCost()));
        resultShowViewList.add(new ResultShowView("paket", RGBAColor.GREEN, super.getParcelPlan().getTransportationCost()));
        pieModel = new PieChart()
                .setData(new PieData()
                        .addDataset(new PieDataset()
                                .setData(resultShowViewList.stream()
                                        .filter(v -> v.getCostValueCost() != null && v.getCostValueCost().compareTo(BigDecimal.ZERO) > 0)
                                        .map(ResultShowView::getCostValueCost)
                                        .toList().toArray(new BigDecimal[0]))
                                .setLabel("Gider grafiği")
                                .addBackgroundColors(resultShowViewList.stream()
                                        .filter(v -> v.getCostValueCost() != null && v.getCostValueCost().compareTo(BigDecimal.ZERO) > 0)
                                        .map(ResultShowView::getCostValueRGBAColor)
                                        .toList().toArray(new RGBAColor[0]))
                        )
                        .setLabels(resultShowViewList.stream()
                                .filter(v -> v.getCostValueCost() != null && v.getCostValueCost().compareTo(BigDecimal.ZERO) > 0)
                                .map(ResultShowView::getCostValueName)
                                .toList().toArray(new String[0])))
                .toJson();
//        polarAreaModel = new PolarChart()
//                .setData(new PolarData()
//                        .addDataset(new PolarDataset()
//                                .setData(resultShowViewList.stream()
//                                        .filter(v -> v.getCostValueCost() != null)
//                                        .map(ResultShowView::getCostValueCost)
//                                        .toList().toArray(new BigDecimal[0]))
//                                .setLabel("Gider grafiği")
//                                .addBackgroundColors(resultShowViewList.stream()
//                                        .filter(v -> v.getCostValueCost() != null)
//                                        .map(ResultShowView::getCostValueRGBAColor)
//                                        .toList().toArray(new RGBAColor[0])))
//                        .setLabels(resultShowViewList.stream()
//                                .filter(v -> v.getCostValueCost() != null)
//                                .map(ResultShowView::getCostValueName)
//                                .toList().toArray(new String[0])))
//                .toJson();
    }

    public void previousSaveExpense() throws IOException {
        goToPreviousPageFromResult();
    }

    private Double calculateValueRate(BigDecimal costValueCost) {
        return costValueCost != null ? costValueCost.multiply(BigDecimal.valueOf(100)).divide(super.getParcelPlan().getTotalExpense(), 2, RoundingMode.HALF_UP).doubleValue() : 0d;
    }

    @AllArgsConstructor
    @Getter
    public class ResultShowView {
        private String costValueName;
        private RGBAColor costValueRGBAColor;
        private BigDecimal costValueCost;

        public String getCostValueHexColor() {
            return convertRGBAColorToHex(costValueRGBAColor);
        }

        public Double getCostValueRate() {
            return calculateValueRate(costValueCost);
        }
    }
}

package tr.ozanbey.agricalc.webapp.webapp.view.plantation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProduct;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumMonth;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlantationIncomeView implements Serializable {

    private Long selectedProductId;
    private PlantationProduct selectedProduct;
    private Long selectedProductOptionId;
    private Long selectedAnacOptionId;
    private String selectedTechnique;
    private Integer plantingYear;
    private Double rowSpacing;
    private Double colSpacing;
    private Long selectedSellOptionId;
    private Double expectedYield;
    private Integer harvestCount;

    private Double lowQualityYieldRate;
    private Double averageJuiceYield;
    private Double averageSideProductYield;
    private Double averageSideSafranYield;
    private BigDecimal previousAveragePrice;
    private BigDecimal currentExpectedPrice;
    private BigDecimal currentLowQualityOrJuicePrice;
    private BigDecimal currentExpectedSideProductPrice;
    private EnumMonth selectedPlanting;
    private EnumMonth selectedHarvestStart;
    private EnumMonth selectedHarvestEnd;
    private Long selectedSellPeriodId;

}

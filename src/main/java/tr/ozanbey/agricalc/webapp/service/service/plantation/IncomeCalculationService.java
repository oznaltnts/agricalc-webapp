package tr.ozanbey.agricalc.webapp.service.service.plantation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.webapp.view.plantation.PlantationIncomeView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class IncomeCalculationService {

    //25
    private Double treePerDecare(Double rowSpacing, Double colSpacing) {
        return 1000 / (rowSpacing * colSpacing);
    }

    //1250
    private Double mainProductKgYieldAsKgPerTree(Double treePerDecare, Double averageYieldAsKgPerTree) {
        return treePerDecare * averageYieldAsKgPerTree;
    }

    //18750
    private BigDecimal mainProductKgGrossIncomeAsKgPerTree(Double mainProductKgYieldAsKgPerTree, BigDecimal averageExpectedPricePerKg) {
        return BigDecimal.valueOf(mainProductKgYieldAsKgPerTree).multiply(averageExpectedPricePerKg);
    }

    //17250
    private BigDecimal mainProductKgGrossIncomeAsKgPerDecare(Double averageYieldAsKgPerDecare, BigDecimal averageExpectedPricePerKg) {
        return BigDecimal.valueOf(averageYieldAsKgPerDecare).multiply(averageExpectedPricePerKg);
    }

    //4500
    private Double mainProductKgYieldAsUnitPerDecare(Double averageExpectedYieldAsUnitPerDecare, Double lowQualityRate) {
        return averageExpectedYieldAsUnitPerDecare * (100 - lowQualityRate) / 100;
    }

    //67500
    private BigDecimal mainProductKgGrossIncomeAsUnitPerDecare(Double mainProductKgYieldAsUnitPerDecare, BigDecimal averageExpectedPricePerKg) {
        return BigDecimal.valueOf(mainProductKgYieldAsUnitPerDecare).multiply(averageExpectedPricePerKg);
    }

    //400
    private Double mainProductKgYieldPerHarvest(Double averageYieldPerHarvest, Integer harvestCount) {
        return averageYieldPerHarvest * harvestCount.doubleValue();
    }

    //6000
    private BigDecimal mainProductKgGrossIncomePerHarvestAsKgPerDecare(Double mainProductKgYieldPerHarvest, BigDecimal averageExpectedPricePerKg) {
        return BigDecimal.valueOf(mainProductKgYieldPerHarvest).multiply(averageExpectedPricePerKg);
    }

    //0,2
    private Double mainProductKgYieldAsGrPerDecare(Double yieldAsGrPerDecare) {
        return yieldAsGrPerDecare / 1000;
    }

    //3
    private BigDecimal mainProductKgGrossIncomeAsGrPerDecare(Double mainProductKgYieldAsGrPerDecare, BigDecimal averageExpectedPricePerKg) {
        return BigDecimal.valueOf(mainProductKgYieldAsGrPerDecare).multiply(averageExpectedPricePerKg);
    }

    //35000
    private BigDecimal mainProductUnitGrossIncomeAsUnitPerDecare(Double averageExpectedYieldAsUnitPerDecare, BigDecimal averageExpectedPricePerUnit) {
        return BigDecimal.valueOf(averageExpectedYieldAsUnitPerDecare).multiply(averageExpectedPricePerUnit);
    }

    //3080
    private BigDecimal mainProductBundleGrossIncomeAsBundlePerDecare(Double averageExpectedYieldAsBundlePerDecare, BigDecimal averageExpectedPricePerBundle) {
        return BigDecimal.valueOf(averageExpectedYieldAsBundlePerDecare).multiply(averageExpectedPricePerBundle);
    }

    //147583
    private BigDecimal mainProductGrossIncome(BigDecimal averageExpectedPricePerKg,
                                              BigDecimal mainProductKgGrossIncomeAsKgPerTree,
                                              BigDecimal mainProductKgGrossIncomeAsKgPerDecare,
                                              Double averageExpectedYieldAsUnitPerDecare, BigDecimal mainProductKgGrossIncomeAsUnitPerDecare,
                                              BigDecimal mainProductKgGrossIncomePerHarvestAsKgPerDecare,
                                              Double mainProductKgYieldAsGrPerDecare,
                                              BigDecimal averageExpectedPricePerUnit,
                                              Double averageExpectedYieldAsBundlePerDecare, BigDecimal averageExpectedPricePerBundle) {
        return mainProductKgGrossIncomeAsKgPerTree
                .add(mainProductKgGrossIncomeAsKgPerDecare)
                .add(mainProductKgGrossIncomeAsUnitPerDecare)
                .add(mainProductKgGrossIncomePerHarvestAsKgPerDecare)
                .add(mainProductKgGrossIncomeAsGrPerDecare(mainProductKgYieldAsGrPerDecare, averageExpectedPricePerKg))
                .add(mainProductUnitGrossIncomeAsUnitPerDecare(averageExpectedYieldAsUnitPerDecare, averageExpectedPricePerUnit))
                .add(mainProductBundleGrossIncomeAsBundlePerDecare(averageExpectedYieldAsBundlePerDecare, averageExpectedPricePerBundle));
    }

    //1125
    private Double highQualityMainProductKgYieldAsKgPerTree(Double treePerDecare, Double averageYieldAsKgPerTree, Double lowQualityRate) {
        return treePerDecare * averageYieldAsKgPerTree * (100 - lowQualityRate) / 100;
    }

    //125
    private Double lowQualityMainProductKgYieldAsKgPerTree(Double mainProductKgYieldAsKgPerTree, Double lowQualityRate) {
        return mainProductKgYieldAsKgPerTree * lowQualityRate / 100;
    }

    //17125
    private BigDecimal highQualityMainProductKgGrossIncomeAsKgPerTree(Double treePerDecare, Double averageYieldAsKgPerTree, Double lowQualityRate,
                                                                      BigDecimal averageExpectedPricePerKg,
                                                                      Double mainProductKgYieldAsKgPerTree, BigDecimal lowQualityExpectedPrice) {
        return BigDecimal.valueOf(highQualityMainProductKgYieldAsKgPerTree(treePerDecare, averageYieldAsKgPerTree, lowQualityRate)).multiply(averageExpectedPricePerKg)
                .add(BigDecimal.valueOf(lowQualityMainProductKgYieldAsKgPerTree(mainProductKgYieldAsKgPerTree, lowQualityRate)).multiply(lowQualityExpectedPrice));
    }

    //1035
    private Double highQualityMainProductKgYieldAsKgPerDecare(Double averageYieldAsKgPerDecare, Double lowQualityRate) {
        return averageYieldAsKgPerDecare * (100 - lowQualityRate) / 100;
    }

    //115
    private Double lowQualityMainProductKgYieldAsKgPerDecare(Double averageYieldAsKgPerDecare, Double lowQualityRate) {
        return averageYieldAsKgPerDecare * lowQualityRate / 100;
    }

    //15755
    private BigDecimal highQualityMainProductKgGrossIncomeAsKgPerDecare(Double averageYieldAsKgPerDecare, Double lowQualityRate,
                                                                        BigDecimal averageExpectedPricePerKg,
                                                                        BigDecimal lowQualityExpectedPrice) {
        return BigDecimal.valueOf(highQualityMainProductKgYieldAsKgPerDecare(averageYieldAsKgPerDecare, lowQualityRate)).multiply(averageExpectedPricePerKg)
                .add(BigDecimal.valueOf(lowQualityMainProductKgYieldAsKgPerDecare(averageYieldAsKgPerDecare, lowQualityRate)).multiply(lowQualityExpectedPrice));
    }

    //450
    private Double lowQualityMainProductKgYieldAsUnitPerDecare(Double mainProductKgYieldAsUnitPerDecare, Double lowQualityRate) {
        return mainProductKgYieldAsUnitPerDecare * lowQualityRate / 100;
    }

    //68400
    private BigDecimal highQualityMainProductKgGrossIncomeAsUnitPerDecare(Double mainProductKgYieldAsUnitPerDecare, Double lowQualityRate,
                                                                          BigDecimal averageExpectedPricePerKg,
                                                                          BigDecimal lowQualityExpectedPrice) {
        return BigDecimal.valueOf(mainProductKgYieldAsUnitPerDecare).multiply(averageExpectedPricePerKg)
                .add(BigDecimal.valueOf(lowQualityMainProductKgYieldAsUnitPerDecare(mainProductKgYieldAsUnitPerDecare, lowQualityRate)).multiply(lowQualityExpectedPrice));
    }

    //360
    private Double highQualityMainProductKgYieldPerHarvest(Double mainProductKgYieldPerHarvest, Double lowQualityRate) {
        return mainProductKgYieldPerHarvest * (100 - lowQualityRate) / 100;
    }

    //40
    private Double lowQualityMainProductKgYieldPerHarvest(Double mainProductKgYieldPerHarvest, Double lowQualityRate) {
        return mainProductKgYieldPerHarvest * lowQualityRate / 100;
    }

    //5480
    private BigDecimal highQualityMainProductKgGrossIncomePerHarvestAsKgPerDecare(Double mainProductKgYieldPerHarvest, Double lowQualityRate,
                                                                                  BigDecimal averageExpectedPricePerKg,
                                                                                  BigDecimal lowQualityExpectedPrice) {
        return BigDecimal.valueOf(highQualityMainProductKgYieldPerHarvest(mainProductKgYieldPerHarvest, lowQualityRate)).multiply(averageExpectedPricePerKg)
                .add(BigDecimal.valueOf(lowQualityMainProductKgYieldPerHarvest(mainProductKgYieldPerHarvest, lowQualityRate)).multiply(lowQualityExpectedPrice));
    }

    //0,18
    private Double highQualityMainProductKgYieldAsGrPerDecare(Double mainProductKgYieldAsGrPerDecare, Double lowQualityRate) {
        return mainProductKgYieldAsGrPerDecare * (100 - lowQualityRate) / 100;
    }

    //0,02
    private Double lowQualityMainProductKgYieldAsGrPerDecare(Double mainProductKgYieldAsGrPerDecare, Double lowQualityRate) {
        return mainProductKgYieldAsGrPerDecare * lowQualityRate / 100;
    }

    //2,74
    private BigDecimal highQualityMainProductKgGrossIncomeAsGrPerDecare(Double mainProductKgYieldAsGrPerDecare, Double lowQualityRate,
                                                                        BigDecimal averageExpectedPricePerKg,
                                                                        BigDecimal lowQualityExpectedPrice) {
        return BigDecimal.valueOf(highQualityMainProductKgYieldAsGrPerDecare(mainProductKgYieldAsGrPerDecare, lowQualityRate)).multiply(averageExpectedPricePerKg)
                .add(BigDecimal.valueOf(lowQualityMainProductKgYieldAsGrPerDecare(mainProductKgYieldAsGrPerDecare, lowQualityRate)).multiply(lowQualityExpectedPrice));
    }

    //106762,74
    private BigDecimal highQualityMainProductGrossIncome(Double treePerDecare, Double averageYieldAsKgPerTree, Double lowQualityRate,
                                                         BigDecimal averageExpectedPricePerKg,
                                                         Double mainProductKgYieldAsKgPerTree, BigDecimal lowQualityExpectedPrice,
                                                         Double averageYieldAsKgPerDecare,
                                                         Double mainProductKgYieldAsUnitPerDecare,
                                                         Double mainProductKgYieldPerHarvest,
                                                         Double mainProductKgYieldAsGrPerDecare) {
        return highQualityMainProductKgGrossIncomeAsKgPerTree(treePerDecare, averageYieldAsKgPerTree, lowQualityRate, averageExpectedPricePerKg, mainProductKgYieldAsKgPerTree, lowQualityExpectedPrice)
                .add(highQualityMainProductKgGrossIncomeAsKgPerDecare(averageYieldAsKgPerDecare, lowQualityRate, averageExpectedPricePerKg, lowQualityExpectedPrice))
                .add(highQualityMainProductKgGrossIncomeAsUnitPerDecare(mainProductKgYieldAsUnitPerDecare, lowQualityRate, averageExpectedPricePerKg, lowQualityExpectedPrice))
                .add(highQualityMainProductKgGrossIncomePerHarvestAsKgPerDecare(mainProductKgYieldPerHarvest, lowQualityRate, averageExpectedPricePerKg, lowQualityExpectedPrice))
                .add(highQualityMainProductKgGrossIncomeAsGrPerDecare(mainProductKgYieldAsGrPerDecare, lowQualityRate, averageExpectedPricePerKg, lowQualityExpectedPrice));
    }

    //19350
    private BigDecimal lowQualityMainProductKgGrossIncomeAsKgPerTree(BigDecimal mainProductKgGrossIncomeAsKgPerTree, Double averageYieldForJuice, BigDecimal expectedPriceForJuice) {
        return mainProductKgGrossIncomeAsKgPerTree.add(BigDecimal.valueOf(averageYieldForJuice).multiply(expectedPriceForJuice));
    }

    //0,55
    private Double sideProductGrYieldAsGrPerDecare(Double averageSideProductYield) {
        return averageSideProductYield / 100;
    }

    //93,55
    private BigDecimal lowQualityMainProductKgGrossIncomeAsGrPerDecare(Double mainProductKgYieldAsGrPerDecare, BigDecimal averageExpectedPricePerKg, BigDecimal expectedPriceForSideProductPerGr, Double averageSideProductYield) {
        return BigDecimal.valueOf(mainProductKgYieldAsGrPerDecare).multiply(averageExpectedPricePerKg).add(expectedPriceForSideProductPerGr.add(BigDecimal.valueOf(sideProductGrYieldAsGrPerDecare(averageSideProductYield))));
    }

    //19443,55
    private BigDecimal lowQualityMainProductGrossIncome(BigDecimal averageExpectedPricePerKg, BigDecimal mainProductKgGrossIncomeAsKgPerTree, Double averageYieldForJuice, BigDecimal expectedPriceForJuice,
                                                        Double mainProductKgYieldAsGrPerDecare, BigDecimal expectedPriceForSideProductPerGr, Double averageSideProductYield) {
        return lowQualityMainProductKgGrossIncomeAsKgPerTree(mainProductKgGrossIncomeAsKgPerTree, averageYieldForJuice, expectedPriceForJuice)
                .add(lowQualityMainProductKgGrossIncomeAsGrPerDecare(mainProductKgYieldAsGrPerDecare, averageExpectedPricePerKg, expectedPriceForSideProductPerGr, averageSideProductYield));
    }

    //17700
    private BigDecimal sideStrawProductKgGrossIncomeAsKgPerDecare(BigDecimal mainProductKgGrossIncomeAsKgPerDecare, Double averageYieldSideStrawProduct, BigDecimal expectedPriceForSideProductPerKg) {
        return mainProductKgGrossIncomeAsKgPerDecare.add(BigDecimal.valueOf(averageYieldSideStrawProduct).multiply(expectedPriceForSideProductPerKg));
    }

    //67950
    private BigDecimal sideStrawProductKgGrossIncomeAsUnitPerDecare(BigDecimal mainProductKgGrossIncomeAsUnitPerDecare, Double averageYieldSideStrawProduct, BigDecimal expectedPriceForSideProductPerKg) {
        return mainProductKgGrossIncomeAsUnitPerDecare.add(BigDecimal.valueOf(averageYieldSideStrawProduct).multiply(expectedPriceForSideProductPerKg));
    }

    //6450
    private BigDecimal sideStrawProductKgGrossIncomePerHarvestAsKgPerDecare(BigDecimal mainProductKgGrossIncomePerHarvestAsKgPerDecare, Double averageYieldSideStrawProduct, BigDecimal expectedPriceForSideProductPerKg) {
        return mainProductKgGrossIncomePerHarvestAsKgPerDecare.add(BigDecimal.valueOf(averageYieldSideStrawProduct).multiply(expectedPriceForSideProductPerKg));
    }

    //92100
    private BigDecimal sideStrawProductGrossIncome(BigDecimal mainProductKgGrossIncomeAsKgPerDecare, Double averageYieldSideStrawProduct, BigDecimal expectedPriceForSideProductPerKg,
                                                   BigDecimal mainProductKgGrossIncomeAsUnitPerDecare,
                                                   BigDecimal mainProductKgGrossIncomePerHarvestAsKgPerDecare) {
        return sideStrawProductKgGrossIncomeAsKgPerDecare(mainProductKgGrossIncomeAsKgPerDecare, averageYieldSideStrawProduct, expectedPriceForSideProductPerKg)
                .add(sideStrawProductKgGrossIncomeAsUnitPerDecare(mainProductKgGrossIncomeAsUnitPerDecare, averageYieldSideStrawProduct, expectedPriceForSideProductPerKg))
                .add(sideStrawProductKgGrossIncomePerHarvestAsKgPerDecare(mainProductKgGrossIncomePerHarvestAsKgPerDecare, averageYieldSideStrawProduct, expectedPriceForSideProductPerKg));
    }

    //17325
    private BigDecimal sideGrainProductKgGrossIncomeAsKgPerDecare(BigDecimal mainProductKgGrossIncomeAsKgPerDecare, Double averageYieldSideGrainProduct, BigDecimal expectedPriceForSideProductPerKg) {
        return mainProductKgGrossIncomeAsKgPerDecare.add(BigDecimal.valueOf(averageYieldSideGrainProduct).multiply(expectedPriceForSideProductPerKg));
    }

    //67575
    private BigDecimal sideGrainProductKgGrossIncomeAsUnitPerDecare(BigDecimal mainProductKgGrossIncomeAsUnitPerDecare, Double averageYieldSideGrainProduct, BigDecimal expectedPriceForSideProductPerKg) {
        return mainProductKgGrossIncomeAsUnitPerDecare.add(BigDecimal.valueOf(averageYieldSideGrainProduct).multiply(expectedPriceForSideProductPerKg));
    }

    //6075
    private BigDecimal sideGrainProductKgGrossIncomePerHarvestAsKgPerDecare(BigDecimal mainProductKgGrossIncomePerHarvestAsKgPerDecare, Double averageYieldSideGrainProduct, BigDecimal expectedPriceForSideProductPerKg) {
        return mainProductKgGrossIncomePerHarvestAsKgPerDecare.add(BigDecimal.valueOf(averageYieldSideGrainProduct).multiply(expectedPriceForSideProductPerKg));
    }

    //90975
    private BigDecimal sideGrainProductGrossIncome(BigDecimal mainProductKgGrossIncomeAsKgPerDecare, Double averageYieldSideGrainProduct, BigDecimal expectedPriceForSideProductPerKg,
                                                   BigDecimal mainProductKgGrossIncomeAsUnitPerDecare,
                                                   BigDecimal mainProductKgGrossIncomePerHarvestAsKgPerDecare) {
        return sideGrainProductKgGrossIncomeAsKgPerDecare(mainProductKgGrossIncomeAsKgPerDecare, averageYieldSideGrainProduct, expectedPriceForSideProductPerKg)
                .add(sideGrainProductKgGrossIncomeAsUnitPerDecare(mainProductKgGrossIncomeAsUnitPerDecare, averageYieldSideGrainProduct, expectedPriceForSideProductPerKg))
                .add(sideGrainProductKgGrossIncomePerHarvestAsKgPerDecare(mainProductKgGrossIncomePerHarvestAsKgPerDecare, averageYieldSideGrainProduct, expectedPriceForSideProductPerKg));
    }

    //17505
    private BigDecimal sideFeedProductKgGrossIncomeAsKgPerDecare(BigDecimal mainProductKgGrossIncomeAsKgPerDecare, Double averageYieldSideFeedProduct, BigDecimal expectedPriceForSideProductPerKg) {
        return mainProductKgGrossIncomeAsKgPerDecare.add(BigDecimal.valueOf(averageYieldSideFeedProduct).multiply(expectedPriceForSideProductPerKg));
    }

    //67755
    private BigDecimal sideFeedProductKgGrossIncomeAsUnitPerDecare(BigDecimal mainProductKgGrossIncomeAsUnitPerDecare, Double averageYieldSideFeedProduct, BigDecimal expectedPriceForSideProductPerKg) {
        return mainProductKgGrossIncomeAsUnitPerDecare.add(BigDecimal.valueOf(averageYieldSideFeedProduct).multiply(expectedPriceForSideProductPerKg));
    }

    //6255
    private BigDecimal sideFeedProductKgGrossIncomePerHarvestAsKgPerDecare(BigDecimal mainProductKgGrossIncomePerHarvestAsKgPerDecare, Double averageYieldSideFeedProduct, BigDecimal expectedPriceForSideProductPerKg) {
        return mainProductKgGrossIncomePerHarvestAsKgPerDecare.add(BigDecimal.valueOf(averageYieldSideFeedProduct).multiply(expectedPriceForSideProductPerKg));
    }

    //91515
    private BigDecimal sideFeedProductGrossIncome(BigDecimal mainProductKgGrossIncomeAsKgPerDecare, Double averageYieldSideFeedProduct, BigDecimal expectedPriceForSideProductPerKg,
                                                  BigDecimal mainProductKgGrossIncomeAsUnitPerDecare,
                                                  BigDecimal mainProductKgGrossIncomePerHarvestAsKgPerDecare) {
        return sideFeedProductKgGrossIncomeAsKgPerDecare(mainProductKgGrossIncomeAsKgPerDecare, averageYieldSideFeedProduct, expectedPriceForSideProductPerKg)
                .add(sideFeedProductKgGrossIncomeAsUnitPerDecare(mainProductKgGrossIncomeAsUnitPerDecare, averageYieldSideFeedProduct, expectedPriceForSideProductPerKg))
                .add(sideFeedProductKgGrossIncomePerHarvestAsKgPerDecare(mainProductKgGrossIncomePerHarvestAsKgPerDecare, averageYieldSideFeedProduct, expectedPriceForSideProductPerKg));
    }

    //17302,5
    private BigDecimal sideKernelProductKgGrossIncomeAsKgPerDecare(BigDecimal mainProductKgGrossIncomeAsKgPerDecare, Double averageYieldSideKernelProduct, BigDecimal expectedPriceForSideProductPerKg) {
        return mainProductKgGrossIncomeAsKgPerDecare.add(BigDecimal.valueOf(averageYieldSideKernelProduct).multiply(expectedPriceForSideProductPerKg));
    }

    //67552,5
    private BigDecimal sideKernelProductKgGrossIncomeAsUnitPerDecare(BigDecimal mainProductKgGrossIncomeAsUnitPerDecare, Double averageYieldSideKernelProduct, BigDecimal expectedPriceForSideProductPerKg) {
        return mainProductKgGrossIncomeAsUnitPerDecare.add(BigDecimal.valueOf(averageYieldSideKernelProduct).multiply(expectedPriceForSideProductPerKg));
    }

    //6052,5
    private BigDecimal sideKernelProductKgGrossIncomePerHarvestAsKgPerDecare(BigDecimal mainProductKgGrossIncomePerHarvestAsKgPerDecare, Double averageYieldSideKernelProduct, BigDecimal expectedPriceForSideProductPerKg) {
        return mainProductKgGrossIncomePerHarvestAsKgPerDecare.add(BigDecimal.valueOf(averageYieldSideKernelProduct).multiply(expectedPriceForSideProductPerKg));

    }

    //90907,5
    private BigDecimal sideKernelProductGrossIncome(BigDecimal mainProductKgGrossIncomeAsKgPerDecare, Double averageYieldSideKernelProduct, BigDecimal expectedPriceForSideProductPerKg,
                                                    BigDecimal mainProductKgGrossIncomeAsUnitPerDecare,
                                                    BigDecimal mainProductKgGrossIncomePerHarvestAsKgPerDecare) {
        return sideKernelProductKgGrossIncomeAsKgPerDecare(mainProductKgGrossIncomeAsKgPerDecare, averageYieldSideKernelProduct, expectedPriceForSideProductPerKg)
                .add(sideKernelProductKgGrossIncomeAsUnitPerDecare(mainProductKgGrossIncomeAsUnitPerDecare, averageYieldSideKernelProduct, expectedPriceForSideProductPerKg))
                .add(sideKernelProductKgGrossIncomePerHarvestAsKgPerDecare(mainProductKgGrossIncomePerHarvestAsKgPerDecare, averageYieldSideKernelProduct, expectedPriceForSideProductPerKg));
    }

    //639286,79
    private BigDecimal totalGrossIncome(BigDecimal averageExpectedPricePerKg, Double rowSpacing, Double colSpacing, Double averageYieldAsKgPerTree, Double averageYieldAsKgPerDecare, Double averageExpectedYieldAsUnitPerDecare, Double lowQualityRate, Double averageYieldPerHarvest, Integer harvestCount, Double yieldAsGrPerDecare,
                                        BigDecimal averageExpectedPricePerUnit, Double averageExpectedYieldAsBundlePerDecare, BigDecimal averageExpectedPricePerBundle,
                                        BigDecimal lowQualityExpectedPrice,
                                        Double averageYieldForJuice, BigDecimal expectedPriceForJuice, BigDecimal expectedPriceForSideProductPerGr, Double averageSideProductYield,
                                        Double averageYieldSideStrawProduct, BigDecimal expectedPriceForSideProductPerKg,
                                        Double averageYieldSideGrainProduct,
                                        Double averageYieldSideFeedProduct,
                                        Double averageYieldSideKernelProduct) {
        Double treePerDecare;
        if (rowSpacing == 0d || colSpacing == 0d)
            treePerDecare = 0d;
        else
            treePerDecare = treePerDecare(rowSpacing, colSpacing);
        Double mainProductKgYieldAsKgPerTree = mainProductKgYieldAsKgPerTree(treePerDecare, averageYieldAsKgPerTree);
        BigDecimal mainProductKgGrossIncomeAsKgPerTree = mainProductKgGrossIncomeAsKgPerTree(mainProductKgYieldAsKgPerTree, averageExpectedPricePerKg);
        BigDecimal mainProductKgGrossIncomeAsKgPerDecare = mainProductKgGrossIncomeAsKgPerDecare(averageYieldAsKgPerDecare, averageExpectedPricePerKg);
        Double mainProductKgYieldAsUnitPerDecare = mainProductKgYieldAsUnitPerDecare(averageExpectedYieldAsUnitPerDecare, lowQualityRate);
        BigDecimal mainProductKgGrossIncomeAsUnitPerDecare = mainProductKgGrossIncomeAsUnitPerDecare(mainProductKgYieldAsUnitPerDecare, averageExpectedPricePerKg);
        Double mainProductKgYieldPerHarvest = mainProductKgYieldPerHarvest(averageYieldPerHarvest, harvestCount);
        BigDecimal mainProductKgGrossIncomePerHarvestAsKgPerDecare = mainProductKgGrossIncomePerHarvestAsKgPerDecare(mainProductKgYieldPerHarvest, averageExpectedPricePerKg);
        Double mainProductKgYieldAsGrPerDecare = mainProductKgYieldAsGrPerDecare(yieldAsGrPerDecare);

        return mainProductGrossIncome(averageExpectedPricePerKg, mainProductKgGrossIncomeAsKgPerTree, mainProductKgGrossIncomeAsKgPerDecare, averageExpectedYieldAsUnitPerDecare, mainProductKgGrossIncomeAsUnitPerDecare, mainProductKgGrossIncomePerHarvestAsKgPerDecare, mainProductKgYieldAsGrPerDecare, averageExpectedPricePerUnit, averageExpectedYieldAsBundlePerDecare, averageExpectedPricePerBundle)
                .add(highQualityMainProductGrossIncome(treePerDecare, averageYieldAsKgPerTree, lowQualityRate, averageExpectedPricePerKg, mainProductKgYieldAsKgPerTree, lowQualityExpectedPrice, averageYieldAsKgPerDecare, mainProductKgYieldAsUnitPerDecare, mainProductKgYieldPerHarvest, mainProductKgYieldAsGrPerDecare))
                .add(lowQualityMainProductGrossIncome(averageExpectedPricePerKg, mainProductKgGrossIncomeAsKgPerTree, averageYieldForJuice, expectedPriceForJuice, mainProductKgYieldAsGrPerDecare, expectedPriceForSideProductPerGr, averageSideProductYield))
                .add(sideStrawProductGrossIncome(mainProductKgGrossIncomeAsKgPerDecare, averageYieldSideStrawProduct, expectedPriceForSideProductPerKg, mainProductKgGrossIncomeAsUnitPerDecare, mainProductKgGrossIncomePerHarvestAsKgPerDecare))
                .add(sideGrainProductGrossIncome(mainProductKgGrossIncomeAsKgPerDecare, averageYieldSideGrainProduct, expectedPriceForSideProductPerKg, mainProductKgGrossIncomeAsUnitPerDecare, mainProductKgGrossIncomePerHarvestAsKgPerDecare))
                .add(sideFeedProductGrossIncome(mainProductKgGrossIncomeAsKgPerDecare, averageYieldSideFeedProduct, expectedPriceForSideProductPerKg, mainProductKgGrossIncomeAsUnitPerDecare, mainProductKgGrossIncomePerHarvestAsKgPerDecare))
                .add(sideKernelProductGrossIncome(mainProductKgGrossIncomeAsKgPerDecare, averageYieldSideKernelProduct, expectedPriceForSideProductPerKg, mainProductKgGrossIncomeAsUnitPerDecare, mainProductKgGrossIncomePerHarvestAsKgPerDecare));
    }

    public BigDecimal calculateIncome(PlantationIncomeView incomeView) {
        List<PlantationProductQuestion> questionList = incomeView.getSelectedProduct().getProductQuestionList();
        BigDecimal averageExpectedPricePerKg = decimalValueSetter(questionList, 27L, incomeView.getCurrentExpectedPrice());
        Double rowSpacing = doubleValueSetter(questionList, 3L, incomeView.getRowSpacing());
        Double colSpacing = doubleValueSetter(questionList, 4L, incomeView.getColSpacing());
        Double averageYieldAsKgPerTree = doubleValueSetter(questionList, 8L, incomeView.getExpectedYield());
        Double averageYieldAsKgPerDecare = doubleValueSetter(questionList, 9L, incomeView.getExpectedYield());
        Double averageExpectedYieldAsUnitPerDecare = doubleValueSetter(questionList, 12L, incomeView.getExpectedYield());
        Double lowQualityRate = doubleValueSetter(questionList, 15L, incomeView.getLowQualityYieldRate());
        Double averageYieldPerHarvest = doubleValueSetter(questionList, 14L, incomeView.getExpectedYield());
        Integer harvestCount = incomeView.getHarvestCount();
        Double yieldAsGrPerDecare = doubleValueSetter(questionList, 10L, incomeView.getExpectedYield());

        BigDecimal averageExpectedPricePerUnit = decimalValueSetter(questionList, 28L, incomeView.getCurrentExpectedPrice());
        Double averageExpectedYieldAsBundlePerDecare = doubleValueSetter(questionList, 13L, incomeView.getExpectedYield());
        BigDecimal averageExpectedPricePerBundle = decimalValueSetter(questionList, 29L, incomeView.getCurrentExpectedPrice());

        BigDecimal lowQualityExpectedPrice = decimalValueSetter(questionList, 30L, incomeView.getCurrentLowQualityOrJuicePrice());

        Double averageYieldForJuice = doubleValueSetter(questionList, 16L, incomeView.getAverageJuiceYield());
        BigDecimal expectedPriceForJuice = decimalValueSetter(questionList, 31L, incomeView.getCurrentLowQualityOrJuicePrice());
        BigDecimal expectedPriceForSideProductPerGr = decimalValueSetter(questionList, 32L, incomeView.getCurrentExpectedSideProductPrice());
        if (expectedPriceForSideProductPerGr != null)
            expectedPriceForSideProductPerGr = expectedPriceForSideProductPerGr.divide(BigDecimal.valueOf(1000), 10, RoundingMode.HALF_UP);
        Double averageSideProductYield = doubleValueSetter(questionList, 23L, incomeView.getAverageSideSafranYield());

        Double averageYieldSideStrawProduct = doubleValueSetter(questionList, 17L, incomeView.getAverageSideProductYield());
        BigDecimal expectedPriceForSideProductPerKg = decimalValueSetter(questionList, 32L, incomeView.getCurrentExpectedSideProductPrice());

        Double averageYieldSideGrainProduct = doubleValueSetter(questionList, 18L, incomeView.getAverageSideProductYield());

        Double averageYieldSideFeedProduct = doubleValueSetter(questionList, 19L, incomeView.getAverageSideProductYield());

        Double averageYieldSideKernelProduct = doubleValueSetter(questionList, 20L, incomeView.getAverageSideProductYield());

        return totalGrossIncome(averageExpectedPricePerKg, rowSpacing, colSpacing, averageYieldAsKgPerTree, averageYieldAsKgPerDecare, averageExpectedYieldAsUnitPerDecare, lowQualityRate, averageYieldPerHarvest, harvestCount, yieldAsGrPerDecare,
                averageExpectedPricePerUnit, averageExpectedYieldAsBundlePerDecare, averageExpectedPricePerBundle,
                lowQualityExpectedPrice,
                averageYieldForJuice, expectedPriceForJuice, expectedPriceForSideProductPerGr, averageSideProductYield,
                averageYieldSideStrawProduct, expectedPriceForSideProductPerKg,
                averageYieldSideGrainProduct,
                averageYieldSideFeedProduct,
                averageYieldSideKernelProduct);
    }

    private BigDecimal decimalValueSetter(List<PlantationProductQuestion> questionList, Long questionId, BigDecimal incomeValue) {
        Optional<PlantationProductQuestion> optional = questionList.stream()
                .filter(q -> q.getPlantationQuestion().getId().equals(questionId))
                .findAny();
        if (optional.isPresent()) {
            if (optional.get().getMaximumValue() != null && incomeValue.compareTo(BigDecimal.valueOf(optional.get().getMaximumValue())) > 0) {
                return BigDecimal.valueOf(optional.get().getMaximumValue());
            } else if (optional.get().getMinimumValue() != null && BigDecimal.valueOf(optional.get().getMinimumValue()).compareTo(incomeValue) > 0) {
                return BigDecimal.valueOf(optional.get().getMinimumValue());
            } else {
                return incomeValue;
            }
        }
        return BigDecimal.ZERO;
    }

    private Double doubleValueSetter(List<PlantationProductQuestion> questionList, Long questionId, Double incomeValue) {
        Optional<PlantationProductQuestion> optional = questionList.stream()
                .filter(q -> q.getPlantationQuestion().getId().equals(questionId))
                .findAny();
        if (optional.isPresent()) {
            if (optional.get().getMaximumValue() != null && incomeValue > optional.get().getMaximumValue()) {
                return optional.get().getMaximumValue();
            } else if (optional.get().getMinimumValue() != null && optional.get().getMinimumValue() > incomeValue) {
                return optional.get().getMinimumValue();
            } else {
                return incomeValue;
            }
        }
        return null;
    }


}

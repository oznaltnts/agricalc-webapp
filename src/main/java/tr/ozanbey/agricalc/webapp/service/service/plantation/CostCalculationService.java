package tr.ozanbey.agricalc.webapp.service.service.plantation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class CostCalculationService {
    //Gider- Toprak Hazırlığı
    //300
    private BigDecimal soilPrepLumpSumCost(Integer soilPrepLumpSumAmount, BigDecimal soilPrepLumpSumPrice) {
        return BigDecimal.valueOf(soilPrepLumpSumAmount).multiply(soilPrepLumpSumPrice);
    }
    //300
    private BigDecimal soilPrepLaserCostPerDecare(Integer soilPrepLaserOperationCount, Integer soilPrepLumpSumAmount, BigDecimal soilPrepLumpSumPrice) {
        return BigDecimal.valueOf(soilPrepLaserOperationCount).multiply(soilPrepLumpSumCost(soilPrepLumpSumAmount, soilPrepLumpSumPrice));
    }
    //0,6
    private Double soilPrepBlastingEnergyAmount(Integer soilPrepBlastingFrequency, Double soilPrepBlastingDieselRate) {
        return soilPrepBlastingFrequency / soilPrepBlastingDieselRate;
    }
    //0,67
    private Double soilPrepBlastingLaborAmount(Double soilPrepBlastingLaborRate, Integer soilPrepBlastingFrequency) {
        return soilPrepBlastingLaborRate / soilPrepBlastingFrequency;
    }
    //250
    private BigDecimal workingManLaborPrice(BigDecimal maleDailyWage, Double workHoursPerDay) {
        return maleDailyWage.divide(BigDecimal.valueOf(workHoursPerDay), 10, RoundingMode.HALF_UP);
    }
    //211,667
    private BigDecimal soilPrepBlastingCostPerDecare(Integer soilPrepBlastingOperationCount,
                                                     Integer soilPrepBlastingFrequency, Double soilPrepBlastingDieselRate, BigDecimal cityDieselPrice,
                                                     Double soilPrepBlastingLaborRate, BigDecimal soilPrepLaborPrice) {
        return BigDecimal.valueOf(soilPrepBlastingOperationCount).multiply(
                BigDecimal.valueOf(soilPrepBlastingEnergyAmount(soilPrepBlastingFrequency, soilPrepBlastingDieselRate)).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(soilPrepBlastingLaborAmount(soilPrepBlastingLaborRate, soilPrepBlastingFrequency)).multiply(soilPrepLaborPrice)));
    }
    //250
    private BigDecimal soilPrepDeepPlowCostPerDecare(Integer soilPrepDeepPlowOperationCount,
                                                     Double soilPrepDeepPlowDieselRate, BigDecimal cityDieselPrice,
                                                     Double soilPrepDeepPlowLaborRate, BigDecimal soilPrepLaborPrice) {
        return BigDecimal.valueOf(soilPrepDeepPlowOperationCount).multiply(
                BigDecimal.valueOf(soilPrepDeepPlowDieselRate).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(soilPrepDeepPlowLaborRate).multiply(soilPrepLaborPrice)));
    }
    //375
    private BigDecimal soilPrepSecondaryCostPerDecare(Integer soilPrepSecondaryOperationCount,
                                                      Double soilPrepSecondaryDieselRate, BigDecimal cityDieselPrice,
                                                      Double soilPrepSecondaryLaborRate, BigDecimal soilPrepLaborPrice) {
        return BigDecimal.valueOf(soilPrepSecondaryOperationCount).multiply(
                BigDecimal.valueOf(soilPrepSecondaryDieselRate).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(soilPrepSecondaryLaborRate).multiply(soilPrepLaborPrice)));
    }
    //Gider- Toprak Hazırlığı
    //1136,667
    private BigDecimal soilPrepTotalCost(Integer soilPrepLaserOperationCount, Integer soilPrepLumpSumAmount, BigDecimal soilPrepLumpSumPrice,
                                         Integer soilPrepBlastingOperationCount, Integer soilPrepBlastingFrequency, Double soilPrepBlastingDieselRate, BigDecimal cityDieselPrice, Double soilPrepBlastingLaborRate, BigDecimal maleDailyWage, Double workHoursPerDay,
                                         Integer soilPrepDeepPlowOperationCount, Double soilPrepDeepPlowDieselRate, Double soilPrepDeepPlowLaborRate,
                                         Integer soilPrepSecondaryOperationCount, Double soilPrepSecondaryDieselRate, Double soilPrepSecondaryLaborRate) {

        BigDecimal soilPrepLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);

        return soilPrepLaserCostPerDecare(soilPrepLaserOperationCount, soilPrepLumpSumAmount, soilPrepLumpSumPrice)
                .add(soilPrepBlastingCostPerDecare(soilPrepBlastingOperationCount, soilPrepBlastingFrequency, soilPrepBlastingDieselRate, cityDieselPrice, soilPrepBlastingLaborRate, soilPrepLaborPrice))
                .add(soilPrepDeepPlowCostPerDecare(soilPrepDeepPlowOperationCount, soilPrepDeepPlowDieselRate, cityDieselPrice, soilPrepDeepPlowLaborRate, soilPrepLaborPrice))
                .add(soilPrepSecondaryCostPerDecare(soilPrepSecondaryOperationCount, soilPrepSecondaryDieselRate, cityDieselPrice, soilPrepSecondaryLaborRate, soilPrepLaborPrice));
    }


    //Gider- Ekim Dikim
    //0,2
    private Double plantingMaterialAmountGr(Double seedGrPerDecare) {
        return seedGrPerDecare / 1000;
    }
    //10
    private BigDecimal plantingCostPerDecareGr(Double seedGrPerDecare, BigDecimal seedPricePerKg) {
        return BigDecimal.valueOf(plantingMaterialAmountGr(seedGrPerDecare)).multiply(seedPricePerKg);
    }
    //0,9
    private Double plantingMaterialAmountUnit(Double seedUnitPerDecare) {
        return seedUnitPerDecare / 1000;
    }
    //180
    private BigDecimal plantingCostPerDecareUnit(Double seedUnitPerDecare, BigDecimal seedPricePer1000) {
        return BigDecimal.valueOf(plantingMaterialAmountUnit(seedUnitPerDecare)).multiply(seedPricePer1000);
    }
    //20000
    private BigDecimal plantingCostPerDecareBag(Double decarePerBag, BigDecimal seedBagPrice) {
        return BigDecimal.valueOf(decarePerBag).multiply(seedBagPrice);
    }
    //0,35
    private Double plantingYearEnergyAmount(Double seederEnergyPerDecare, Double seedUsageYear) {
        return seederEnergyPerDecare / seedUsageYear;
    }
    //0,05
    private Double plantingYearLaborAmount(Double seederLaborPerDecare, Double seedUsageYear) {
        return seederLaborPerDecare / seedUsageYear;
    }
    //38,8
    private BigDecimal plantingCostSeederPerDecare(Double seedUsageYear, Double seederEnergyPerDecare, BigDecimal cityDieselPrice,
                                                   Double seederLaborPerDecare, BigDecimal maleDailyWage, Double workHoursPerDay) {
        return BigDecimal.valueOf(plantingYearEnergyAmount(seedUsageYear, seederEnergyPerDecare)).multiply(cityDieselPrice)
                .add(BigDecimal.valueOf(plantingYearLaborAmount(seedUsageYear, seederLaborPerDecare)).multiply(workingManLaborPrice(maleDailyWage, workHoursPerDay)));
    }
    //120
    private BigDecimal plantingSeederRentalCostPerDecare(Integer plantingSeederLumpSumAmount, BigDecimal plantingLumpSumPrice) {
        return BigDecimal.valueOf(plantingSeederLumpSumAmount).multiply(plantingLumpSumPrice);
    }
    //2
    private Double plantingHandLaborAmount(Double seedHandHourPerDecare, Double seedUsageYear) {
        return seedHandHourPerDecare / seedUsageYear;
    }
    //500
    private BigDecimal plantingSeedCostPerDecare(Double seedHandHourPerDecare, Double seedUsageYear, BigDecimal maleDailyWage, Double workHoursPerDay) {
        return BigDecimal.valueOf(plantingHandLaborAmount(seedHandHourPerDecare, seedUsageYear)).multiply(workingManLaborPrice(maleDailyWage, workHoursPerDay));
    }
    //280
    private BigDecimal plantingDroneCostPerDecare(Integer plantingDroneLumpSumAmount, BigDecimal plantingDroneLumpSumPrice) {
        return BigDecimal.valueOf(plantingDroneLumpSumAmount).multiply(plantingDroneLumpSumPrice);
    }
    //666,6667
    private Double plantingSeedlingMaterialAmountUnit(Integer plantingSeedlingPerDecare, Double seedlingUsageYear) {
        return plantingSeedlingPerDecare / seedlingUsageYear;
    }
    //1333,3
    private BigDecimal seedlingUnitCostPerDecare(Integer plantingSeedlingPerDecare, Double seedlingUsageYear, BigDecimal seedlingUnitPrice) {
        return BigDecimal.valueOf(plantingSeedlingMaterialAmountUnit(plantingSeedlingPerDecare, seedlingUsageYear)).multiply(seedlingUnitPrice);
    }
    //400
    private Double plantingSteelingMaterialAmountUnit(Integer plantingSteelingPerDecare, Double seedlingUsageYear) {
        return plantingSteelingPerDecare / seedlingUsageYear;
    }
    //600
    private BigDecimal steelingUnitCostPerDecare(Integer plantingSteelingPerDecare, Double seedlingUsageYear, BigDecimal steelingUnitPrice) {
        return BigDecimal.valueOf(plantingSteelingMaterialAmountUnit(plantingSteelingPerDecare, seedlingUsageYear)).multiply(steelingUnitPrice);
    }
    //0,4444
    private Double plantingSeedlingHandLaborAmount(Integer plantingSeedlingPerDecare, Double averagePlantingHandPerPerson, Double seedlingUsageYear) {
        return plantingSeedlingPerDecare / averagePlantingHandPerPerson / seedlingUsageYear;
    }
    //225
    private BigDecimal workingWomanLaborPrice(BigDecimal womanDailyWage, Double workHoursPerDay) {
        return womanDailyWage.divide(BigDecimal.valueOf(workHoursPerDay), 10, RoundingMode.HALF_UP);
    }
    //100
    private BigDecimal plantByHandCostPerDecare(Integer plantingSeedlingPerDecare, Double averagePlantingHandPerPerson, Double seedlingUsageYear,
                                                BigDecimal womanDailyWage, Double workHoursPerDay) {
        return BigDecimal.valueOf(plantingSeedlingHandLaborAmount(plantingSeedlingPerDecare, averagePlantingHandPerPerson, seedlingUsageYear))
                .multiply(workingWomanLaborPrice(womanDailyWage, workHoursPerDay));
    }
    //2,222
    private Double plantingDrillEnergyAmount(Integer plantingSeedlingPerDecare, Double drillPlantingPerHour) {
        return plantingSeedlingPerDecare / drillPlantingPerHour;
    }

    //Gider- Ekim Dikim
    //37758,2
    private BigDecimal plantingTotalCost() {
        return BigDecimal.ZERO;
    }

}

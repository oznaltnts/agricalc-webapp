package tr.ozanbey.agricalc.webapp.service.service.plantation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlanAnswer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CostCommonService {
    /*todo aşağıdaki ValueSetter methodları için?
    soru cevaplanmadıysa 0 mı kabul edilmeli? minimum değer mi alınmalı, 2 durum da farklı sonuç yaratacak
    */

    protected BigDecimal decimalAnswerSetter(List<UserPlantationPlanAnswer> answerList, Long questionId) {
        Optional<UserPlantationPlanAnswer> optional = answerList.stream()
                .filter(a -> a.getProductQuestion().getPlantationQuestion().getId().equals(questionId))
                .findAny();
        if (optional.isPresent()) {
            if (optional.get().getProductQuestion().getMaximumValue() != null && new BigDecimal(optional.get().getAnswerValue()).compareTo(optional.get().getProductQuestion().getMaximumValue()) > 0) {
                return optional.get().getProductQuestion().getMaximumValue();
            } else if (optional.get().getProductQuestion().getMinimumValue() != null && optional.get().getProductQuestion().getMinimumValue().compareTo(new BigDecimal(optional.get().getAnswerValue())) > 0) {
                return optional.get().getProductQuestion().getMinimumValue();
            } else {
                return new BigDecimal(optional.get().getAnswerValue());
            }
        }
        return BigDecimal.ZERO;
    }

    protected Double doubleAnswerSetter(List<UserPlantationPlanAnswer> answerList, Long questionId) {
        Optional<UserPlantationPlanAnswer> optional = answerList.stream()
                .filter(a -> a.getProductQuestion().getPlantationQuestion().getId().equals(questionId))
                .findAny();
        if (optional.isPresent()) {
            if (optional.get().getProductQuestion().getMaximumValue() != null && Double.parseDouble(optional.get().getAnswerValue()) > optional.get().getProductQuestion().getMaximumValue().doubleValue()) {
                return optional.get().getProductQuestion().getMaximumValue().doubleValue();
            } else if (optional.get().getProductQuestion().getMinimumValue() != null && optional.get().getProductQuestion().getMinimumValue().doubleValue() > Double.parseDouble(optional.get().getAnswerValue())) {
                return optional.get().getProductQuestion().getMinimumValue().doubleValue();
            } else {
                return Double.parseDouble(optional.get().getAnswerValue());
            }
        }
        return 0d;
    }

    protected Integer integerAnswerSetter(List<UserPlantationPlanAnswer> answerList, Long questionId) {
        Optional<UserPlantationPlanAnswer> optional = answerList.stream()
                .filter(q -> q.getProductQuestion().getPlantationQuestion().getId().equals(questionId))
                .findAny();
        if (optional.isPresent()) {
            if (optional.get().getProductQuestion().getMaximumValue() != null && optional.get().getProductQuestion().getIntegerValue().doubleValue() > optional.get().getProductQuestion().getMaximumValue().doubleValue()) {
                return optional.get().getProductQuestion().getMaximumValue().intValue();
            } else if (optional.get().getProductQuestion().getMinimumValue() != null && optional.get().getProductQuestion().getMinimumValue().doubleValue() > optional.get().getProductQuestion().getIntegerValue().doubleValue()) {
                return optional.get().getProductQuestion().getMinimumValue().intValue();
            } else {
                return optional.get().getProductQuestion().getIntegerValue();
            }
        }
        return 0;
    }

    protected BigDecimal decimalValueSetter(List<PlantationProductQuestion> questionList, Long questionId) {
        Optional<PlantationProductQuestion> optional = questionList.stream()
                .filter(q -> q.getPlantationQuestion().getId().equals(questionId))
                .findAny();
        if (optional.isPresent() && optional.get().getBigDecimalValue() != null) {
            if (optional.get().getMaximumValue() != null && optional.get().getBigDecimalValue().compareTo(optional.get().getMaximumValue()) > 0) {
                return optional.get().getMaximumValue();
            } else if (optional.get().getMinimumValue() != null && optional.get().getMinimumValue().compareTo(optional.get().getBigDecimalValue()) > 0) {
                return optional.get().getMinimumValue();
            } else {
                return optional.get().getBigDecimalValue();
            }
        }
        return BigDecimal.ZERO;
    }

    protected Double doubleValueSetter(List<PlantationProductQuestion> questionList, Long questionId) {
        Optional<PlantationProductQuestion> optional = questionList.stream()
                .filter(q -> q.getPlantationQuestion().getId().equals(questionId))
                .findAny();
        if (optional.isPresent() && optional.get().getDoubleValue() != null) {
            if (optional.get().getMaximumValue() != null && optional.get().getDoubleValue() > optional.get().getMaximumValue().doubleValue()) {
                return optional.get().getMaximumValue().doubleValue();
            } else if (optional.get().getMinimumValue() != null && optional.get().getMinimumValue().doubleValue() > optional.get().getDoubleValue()) {
                return optional.get().getMinimumValue().doubleValue();
            } else {
                return optional.get().getDoubleValue();
            }
        }
        return 0d;
    }

    protected Integer integerValueSetter(List<PlantationProductQuestion> questionList, Long questionId) {
        Optional<PlantationProductQuestion> optional = questionList.stream()
                .filter(q -> q.getPlantationQuestion().getId().equals(questionId))
                .findAny();
        if (optional.isPresent() && optional.get().getIntegerValue() != null) {
            if (optional.get().getMaximumValue() != null && optional.get().getIntegerValue().doubleValue() > optional.get().getMaximumValue().doubleValue()) {
                return optional.get().getMaximumValue().intValue();
            } else if (optional.get().getMinimumValue() != null && optional.get().getMinimumValue().doubleValue() > optional.get().getIntegerValue().doubleValue()) {
                return optional.get().getMinimumValue().intValue();
            } else {
                return optional.get().getIntegerValue();
            }
        }
        return 0;
    }

    //0,6
    protected Double soilPrepBlastingEnergyAmount(Double soilPrepBlastingFrequency, Double soilPrepBlastingDieselRate) {
        return soilPrepBlastingFrequency / soilPrepBlastingDieselRate;
    }

    //0,67
    protected Double soilPrepBlastingLaborAmount(Double soilPrepBlastingLaborRate, Double soilPrepBlastingFrequency) {
        if (soilPrepBlastingFrequency == 0d) return 0d;
        return soilPrepBlastingLaborRate / soilPrepBlastingFrequency;
    }

    //250
    protected BigDecimal workingManLaborPrice(BigDecimal maleDailyWage, Double workHoursPerDay) {
        return maleDailyWage.divide(BigDecimal.valueOf(workHoursPerDay), 10, RoundingMode.HALF_UP);
    }

    //225
    protected BigDecimal workingWomanLaborPrice(BigDecimal womanDailyWage, Double workHoursPerDay) {
        return womanDailyWage.divide(BigDecimal.valueOf(workHoursPerDay), 10, RoundingMode.HALF_UP);
    }

    //1840
    protected BigDecimal mixedDailyWage(BigDecimal maleDailyWage, Double maleCostRate, BigDecimal femaleDailyWage, Double femaleCostRate) {
        return (maleDailyWage.multiply(BigDecimal.valueOf(maleCostRate))).add(femaleDailyWage.multiply(BigDecimal.valueOf(femaleCostRate)));
    }

    //230
    protected BigDecimal workingMixedLaborPrice(BigDecimal maleDailyWage, Double maleCostRate, BigDecimal femaleDailyWage, Double femaleCostRate, Double workHoursPerDay) {
        return mixedDailyWage(maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate).divide(BigDecimal.valueOf(workHoursPerDay), 10, RoundingMode.HALF_UP);
    }

    //0,35
    protected Double plantingYearEnergyAmount(Double seederEnergyPerDecare, Double seedUsageYear) {
        return seederEnergyPerDecare / seedUsageYear;
    }

    //2,222
    protected Double plantingDrillEnergyAmountPerHour(Double plantingSeedlingPerDecare, Double drillPlantingPerHour) {
        return plantingSeedlingPerDecare / drillPlantingPerHour;
    }

    //0,519
    protected Double plantingDrillDieselAmountPerHour(Double plantingSeedlingPerDecare, Double drillPlantingPerHour, Double dieselAmountPerHour, Integer seedlingUsageYear) {
        return plantingDrillEnergyAmountPerHour(plantingSeedlingPerDecare, drillPlantingPerHour) * dieselAmountPerHour / seedlingUsageYear;
    }

    //0,05
    protected Double plantingYearLaborAmount(Double seederLaborPerDecare, Double seedUsageYear) {
        return seederLaborPerDecare / seedUsageYear;
    }

    //2
    protected Double plantingHandLaborAmount(Double seedHandHourPerDecare, Double seedUsageYear) {
        return seedHandHourPerDecare / seedUsageYear;
    }

    //0,4444
    protected Double plantingSeedlingHandLaborAmount(Double plantingSeedlingPerDecare, Double averagePlantingHandPerPerson, Integer seedlingUsageYear) {
        if (seedlingUsageYear == 0d || averagePlantingHandPerPerson == 0d) return 0d;
        return plantingSeedlingPerDecare / averagePlantingHandPerPerson / seedlingUsageYear;
    }

    //1,47
    protected Double seedToFideLaborAmountPerDecare(Double seedToFideAmountPerDecare, Double seedToFideWorkAmount) {
        return seedToFideAmountPerDecare / seedToFideWorkAmount;
    }

    //36
    protected Double plantingLaborAmountYumru(Double workPowerHour, Double workPowerCount) {
        return workPowerHour + workPowerCount;
    }

    //750
    protected BigDecimal plantingCostPerDecareKg(Double seedKgPerDecare, BigDecimal seedPricePerKg) {
        return BigDecimal.valueOf(seedKgPerDecare).multiply(seedPricePerKg);
    }

    //0,2
    protected Double plantingMaterialAmountGr(Double seedGrPerDecare) {
        return seedGrPerDecare / 1000;
    }

    //10
    protected BigDecimal plantingCostPerDecareGr(Double seedGrPerDecare, BigDecimal seedPricePerKg) {
        return BigDecimal.valueOf(plantingMaterialAmountGr(seedGrPerDecare)).multiply(seedPricePerKg);
    }

    //0,9
    protected Double plantingMaterialAmountUnit(Double seedUnitPerDecare) {
        return seedUnitPerDecare / 1000;
    }

    //180
    protected BigDecimal plantingCostPerDecareUnit(Double seedUnitPerDecare, BigDecimal seedPricePer1000) {
        return BigDecimal.valueOf(plantingMaterialAmountUnit(seedUnitPerDecare)).multiply(seedPricePer1000);
    }

    //20000
    protected BigDecimal plantingCostPerDecareBag(Double decarePerBag, BigDecimal seedBagPrice) {
        return BigDecimal.valueOf(decarePerBag).multiply(seedBagPrice);
    }

    //666,6667
    private Double plantingSeedlingMaterialAmountUnit(Double plantingSeedlingPerDecare, Integer seedlingUsageYear) {
        return plantingSeedlingPerDecare / seedlingUsageYear;
    }

    //1333,3
    protected BigDecimal seedlingUnitCostPerDecare(Double plantingSeedlingPerDecare, Integer seedlingUsageYear, BigDecimal seedlingUnitPrice) {
        if (seedlingUsageYear == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(plantingSeedlingMaterialAmountUnit(plantingSeedlingPerDecare, seedlingUsageYear)).multiply(seedlingUnitPrice);
    }

    //400
    private Double plantingSteelingMaterialAmountUnit(Integer plantingSteelingPerDecare, Integer seedlingUsageYear) {
        return plantingSteelingPerDecare / (double) seedlingUsageYear;
    }

    //600
    protected BigDecimal steelingUnitCostPerDecare(Integer plantingSteelingPerDecare, Integer seedlingUsageYear, BigDecimal steelingUnitPrice) {
        if (seedlingUsageYear == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(plantingSteelingMaterialAmountUnit(plantingSteelingPerDecare, seedlingUsageYear)).multiply(steelingUnitPrice);
    }

    //1260
    protected Double plantingMaterialAmountYumru(Double averageYumruAmount, Double yumruUsageYear) {
        return averageYumruAmount / yumruUsageYear;
    }

    //2,5
    protected Double animalFertilizerDieselAmount(Double animalFertilizerDieselRate, Double animalFertilizerFrequency) {
        if (animalFertilizerFrequency == 0d) return 0d;
        return animalFertilizerDieselRate / animalFertilizerFrequency;
    }

    //3
    protected Double animalFertilizerLaborAmount(Double animalFertilizerLaborRate, Double animalFertilizerFrequency) {
        if (animalFertilizerFrequency == 0d) return 0d;
        return animalFertilizerLaborRate / animalFertilizerFrequency;
    }

    //3
    protected Double animalFertilizerKgAmount(Double animalFertilizerPerDecare, Double animalFertilizerFrequency) {
        if (animalFertilizerFrequency == 0d) return 0d;
        return animalFertilizerPerDecare / animalFertilizerFrequency;
    }

    //18
    protected Double mulchingLaborAmount(Double totalMulchingHour, Double mulchUsageYear) {
        return totalMulchingHour / mulchUsageYear;
    }

    //80
    protected Double mulchingCostAmount(Double mulchAmountPerDecare, Double mulchUsageYear) {
        return mulchAmountPerDecare / mulchUsageYear;
    }

    //6,707189268
    protected Double inputAmountForElectricityPump(Double electricityPumpWorkingHour, Double electricityWaterAmountPerHour, Double electricityWaterPumpHeight,
                                                   Integer constantNumber, Double constantMotorEfficiency, Double pumpEfficiencyRate,
                                                   Double irrigationAreaElectricity) {
        return ((electricityPumpWorkingHour * electricityWaterAmountPerHour * electricityWaterPumpHeight) / (constantNumber * constantMotorEfficiency * pumpEfficiencyRate)) / irrigationAreaElectricity;
    }

    //5,45
    protected Double dieselInputAmountForDieselPump(Double specificConstantRate, Double pumpWorkingHour, Double waterAmountPerHour, Double waterPumpHeight, Double gravity,
                                                    Double pumpMotorEfficiencyRate, Double irrigationArea) {
        if (irrigationArea == 0d) return 0d;
        return ((specificConstantRate * pumpWorkingHour * (waterAmountPerHour / 3600) * waterPumpHeight * gravity) / pumpMotorEfficiencyRate) / irrigationArea;
    }

    //6
    protected Double nettingLaborAmountPerDecare(Double nettingLaborHourPerDecare, Double nettingLifeAmount) {
        if (nettingLifeAmount == 0d) return 0d;
        return nettingLaborHourPerDecare / nettingLifeAmount;
    }

    //325
    protected BigDecimal workingPruneLaborPrice(BigDecimal pruneDailyWage, Double workHoursPerDay) {
        return pruneDailyWage.divide(BigDecimal.valueOf(workHoursPerDay), 10, RoundingMode.HALF_UP);
    }

    //60
    protected Double polesInputAmount(Double polesAmountPerDecare, Double poleLifeCycleRate) {
        if (poleLifeCycleRate == 0d) return 0d;
        return polesAmountPerDecare / poleLifeCycleRate;
    }

    //26,6666667
    protected Double nettingInputAmountPerDecare(Double nettingInputAmount, Double nettingLifeAmount) {
        if (nettingLifeAmount == 0d) return 0d;
        return nettingInputAmount / nettingLifeAmount;
    }

    //3,8
    protected Double harvestAndPackingLaborAmountPerDay(Double averageYieldAsKgPerDecare, Double byHandHarvestAmountPerDay) {
        if (byHandHarvestAmountPerDay == 0d) return 0d;
        return averageYieldAsKgPerDecare / byHandHarvestAmountPerDay;
    }

    //4,2
    protected Double harvestAndCleanLaborAmountPerDay(Double mainProductKgYieldAsUnitPerDecare, Double byMachineHarvestAmountPerDay) {
        if (byMachineHarvestAmountPerDay == 0d) return 0d;
        return mainProductKgYieldAsUnitPerDecare / byMachineHarvestAmountPerDay;
    }

    //2,9
    protected Double shakeAndCrateLaborAmountPerDay(Double averageYieldAsKgPerDecare, Double shakeAndCrateInputAmount) {
        if (shakeAndCrateInputAmount == 0d) return 0d;
        return averageYieldAsKgPerDecare / shakeAndCrateInputAmount;
    }

    //2,3
    protected Double cutAndBindLaborAmountPerDay(Double averageYieldAsKgPerDecare, Double cutAndBindInputAmount) {
        if (cutAndBindInputAmount == 0d) return 0d;
        return averageYieldAsKgPerDecare / cutAndBindInputAmount;
    }

    //2,6
    protected Double cutAndLoadLaborAmountPerDay(Double averageYieldAsKgPerDecare, Double cutAndLoadInputAmount) {
        if (cutAndLoadInputAmount == 0d) return 0d;
        return averageYieldAsKgPerDecare / cutAndLoadInputAmount;
    }

    //1,6
    protected Double harvestAndLoadLaborAmountPerDay(Double averageYieldAsKgPerDecare, Double harvestAndLoadInputAmount) {
        if (harvestAndLoadInputAmount == 0d) return 0d;
        return averageYieldAsKgPerDecare / harvestAndLoadInputAmount;
    }

    //0,6
    protected Double harvestAndBindLaborAmountPerDay(Double averageExpectedYieldAsBundlePerDecare, Double harvestAndBindInputAmount) {
        if (harvestAndBindInputAmount == 0d) return 0d;
        return averageExpectedYieldAsBundlePerDecare / harvestAndBindInputAmount;
    }

    //5,0
    protected Double cutAndBindAndLoadLaborAmountPerDay(Double mainProductKgYieldAsUnitPerDecare, Double cutAndBindAndLoadInputAmount) {
        if (cutAndBindAndLoadInputAmount == 0d) return 0d;
        return mainProductKgYieldAsUnitPerDecare / cutAndBindAndLoadInputAmount;
    }

    //5,7
    protected Double harvestGrLaborAmountPerDay(Double mainProductKgYieldAsGrPerDecare, Double harvestGrInputAmount) {
        if (harvestGrInputAmount == 0d) return 0d;
        return mainProductKgYieldAsGrPerDecare / harvestGrInputAmount;
    }

    //0,75
    protected Double harvestLeafLaborAmountPerDay(Double leafProductKgYieldAsGrPerDecare, Double harvestLeafInputAmount) {
        if (harvestLeafInputAmount == 0d) return 0d;
        return leafProductKgYieldAsGrPerDecare / harvestLeafInputAmount;
    }

    //2,170
    protected Double harvestAndMachineLaborAmountPerDay(Double averageYieldAsKgPerDecare, Double harvestAndMachineInputAmount) {
        if (harvestAndMachineInputAmount == 0d) return 0d;
        return averageYieldAsKgPerDecare / harvestAndMachineInputAmount;
    }

    //3,833
    protected Double machineShakingLaborAmountPerDay(Double averageYieldAsKgPerDecare, Double machineShakingInputAmount) {
        if (machineShakingInputAmount == 0d) return 0d;
        return averageYieldAsKgPerDecare / machineShakingInputAmount;
    }

    //1,15
    protected Double blendTransportDieselAmount(Double transportKmAmount, Double averageYieldAsKgPerDecare, Double tractorLoadCapacity) {
        if (tractorLoadCapacity == 0d) return 0d;
        return transportKmAmount * (averageYieldAsKgPerDecare / tractorLoadCapacity);
    }

    //0,96
    protected Double blendLaborAmountPerDay(Double averageYieldAsKgPerDecare, Double blendAmountPerDay) {
        if (blendAmountPerDay == 0d) return 0d;
        return averageYieldAsKgPerDecare / blendAmountPerDay;
    }

    //0,38
    protected Double cureLaborAmountPerDay(Double averageYieldAsKgPerDecare, Double cureAmountPerDay) {
        if (cureAmountPerDay == 0d) return 0d;
        return averageYieldAsKgPerDecare / cureAmountPerDay;
    }

    //0,2
    protected Double sortLaborAmountPerDay(Double yieldAsGrPerDecare, Double sortAmountPerDay) {
        if (sortAmountPerDay == 0d) return 0d;
        return yieldAsGrPerDecare / sortAmountPerDay;
    }

    //0,46
    protected Double blendThreshingAmount(Double averageYieldAsKgPerDecare, Double blendThreshingAmountPerHour) {
        if (blendThreshingAmountPerHour == 0d) return 0d;
        return averageYieldAsKgPerDecare / blendThreshingAmountPerHour;
    }

    //8,05
    protected Double processSievingWashDryLaborAmount(Double averageYieldAsKgPerDecare, Double processSievingWashDryHourAmountPerTonne) {
        return averageYieldAsKgPerDecare / 1000 * processSievingWashDryHourAmountPerTonne;
    }


    //3,45
    protected Double processSievingWashDryDieselAmount(Double averageYieldAsKgPerDecare, Double processSievingWashDryDieselAmountPerTonne) {
        return averageYieldAsKgPerDecare / 1000 * processSievingWashDryDieselAmountPerTonne;
    }

    //30
    protected Double processSievingWashDryLaborAmountPerBunch(Double averageExpectedYieldAsUnitPerDecare, Double processSievingWashDryLaborHourAmount) {
        return averageExpectedYieldAsUnitPerDecare / 1000 * processSievingWashDryLaborHourAmount;
    }

    //3,08
    protected Double processSievingWashDryLaborAmountSeparation(Double averageExpectedYieldAsBundlePerDecare, Double processSievingWashDryHourAmountPer1000) {
        return averageExpectedYieldAsBundlePerDecare / 1000 * processSievingWashDryHourAmountPer1000;
    }

    //6,9
    protected Double processSievingWashDrySulfurizeLaborAmount(Double averageYieldAsKgPerDecare, Double processSievingWashDrySulfurizeHourPerTonne) {
        return averageYieldAsKgPerDecare / 1000 * processSievingWashDrySulfurizeHourPerTonne;
    }

    //5,75
    protected Double processSievingWashDryDipLaborAmount(Double averageYieldAsKgPerDecare, Double processSievingWashDryDipHourPerTonne) {
        return averageYieldAsKgPerDecare / 1000 * processSievingWashDryDipHourPerTonne;
    }

    //11,5
    protected Double processSievingWashDryStringLaborAmount(Double averageYieldAsKgPerDecare, Double processSievingWashDryStringHourPerTonne) {
        return averageYieldAsKgPerDecare / 1000 * processSievingWashDryStringHourPerTonne;
    }

    //4,17
    protected Double processSievingWashDryProcessLabor(Double averageExpectedYieldAsUnitPerDecare, Double processSievingWashDryProcessPerDay) {
        if (processSievingWashDryProcessPerDay == 0d) return 0d;
        return averageExpectedYieldAsUnitPerDecare / processSievingWashDryProcessPerDay;
    }

    //1,15
    protected Double processSievingWashDryLumpSumAmount(Double averageYieldAsKgPerDecare) {
        return averageYieldAsKgPerDecare / 1000;
    }

    //23
    protected Double materialInputAmount(Double averageYieldAsKgPerDecare, Double materialInputAmountPerTonne) {
        return averageYieldAsKgPerDecare / 1000 * materialInputAmountPerTonne;
    }

    //16,1
    protected Double sortSizeScoreBrineLaborAmount(Double averageYieldAsKgPerDecare, Double sortSizeScoreBrineLaborHour) {
        return averageYieldAsKgPerDecare / 1000 * sortSizeScoreBrineLaborHour;
    }

    //5,75
    protected Double balingMaterialInputAmount(Double averageYieldAsKgPerDecare, Double balingMaterialAmountPerTonne) {
        return averageYieldAsKgPerDecare / 1000 * balingMaterialAmountPerTonne;
    }

    //4,2
    protected Double balingMaterialInputKgPriceAmount(Double averageYieldSideStrawProduct, BigDecimal balingMaterialInputPrice) {
        return averageYieldSideStrawProduct / 1000 * balingMaterialInputPrice.doubleValue();
    }

    //52,3
    protected Double balingAverageInputAmount(Double averageYieldAsKgPerDecare, Double balingAverageWeight) {
        if (balingAverageWeight == 0d) return 0d;
        return averageYieldAsKgPerDecare / balingAverageWeight;
    }

    //13,6
    protected Double balingRentalInputAmount(Double averageYieldSideStrawProduct, Double balingAverageWeight) {
        if (balingAverageWeight == 0d) return 0d;
        return averageYieldSideStrawProduct / balingAverageWeight;
    }

    //1,6445
    protected Double tractorDieselAmount(Double averageYieldAsKgPerDecare, Double tractorCapacity, Double tractorTransportCoefficient, Double transportationDistance) {
        if (tractorCapacity == 0d) return 0d;
        return averageYieldAsKgPerDecare / tractorCapacity * tractorTransportCoefficient * transportationDistance;
    }

    //115
    protected Double transportLumpSumAmount(Double averageYieldAsKgPerDecare, Double weightPerUnit) {
        if (weightPerUnit == 0d) return 0d;
        return averageYieldAsKgPerDecare / weightPerUnit;
    }


}


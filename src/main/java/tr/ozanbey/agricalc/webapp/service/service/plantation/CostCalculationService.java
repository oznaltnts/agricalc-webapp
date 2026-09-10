package tr.ozanbey.agricalc.webapp.service.service.plantation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationCoefficient;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationIrrigationValue;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelPlanAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumCoefficientType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumIrrigationType;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.PlantationCoefficientRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.PlantationIrrigationValueRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.UserPlantParcelPlanAnswerRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class CostCalculationService extends CostCommonService {

    @Autowired
    private PlantationCoefficientRepository coefficientRepository;

    @Autowired
    private PlantationIrrigationValueRepository irrigationValueRepository;

    @Autowired
    private UserPlantParcelPlanAnswerRepository planAnswerRepository;

    //Gider- Toprak Hazırlığı
    //300
    private BigDecimal soilPrepLumpSumCost(Integer soilPrepLumpSumAmount, BigDecimal soilPrepLumpSumPrice) {
        return BigDecimal.valueOf(soilPrepLumpSumAmount).multiply(soilPrepLumpSumPrice);
    }
    //300
    private BigDecimal soilPrepLaserCostPerDecare(Integer soilPrepLaserOperationCount, Integer soilPrepLumpSumAmount, BigDecimal soilPrepLumpSumPrice) {
        return BigDecimal.valueOf(soilPrepLaserOperationCount).multiply(soilPrepLumpSumCost(soilPrepLumpSumAmount, soilPrepLumpSumPrice));
    }
    //211,667
    private BigDecimal soilPrepBlastingCostPerDecare(Integer soilPrepBlastingOperationCount,
                                                     Double soilPrepBlastingFrequency, Double soilPrepBlastingDieselRate, BigDecimal cityDieselPrice,
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
    private BigDecimal soilPrepSecondaryCostPerDecare(Double soilPrepSecondaryOperationCount,
                                                      Double soilPrepSecondaryDieselRate, BigDecimal cityDieselPrice,
                                                      Double soilPrepSecondaryLaborRate, BigDecimal soilPrepLaborPrice) {
        return BigDecimal.valueOf(soilPrepSecondaryOperationCount).multiply(
                BigDecimal.valueOf(soilPrepSecondaryDieselRate).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(soilPrepSecondaryLaborRate).multiply(soilPrepLaborPrice)));
    }
    //Gider- Toprak Hazırlığı
    //1136,667
    private BigDecimal soilPrepTotalCost(Integer soilPrepLaserOperationCount, Integer soilPrepLumpSumAmount, BigDecimal soilPrepLumpSumPrice,
                                         Integer soilPrepBlastingOperationCount, Double soilPrepBlastingFrequency, Double soilPrepBlastingDieselRate, BigDecimal cityDieselPrice, Double soilPrepBlastingLaborRate, BigDecimal maleDailyWage, Double workHoursPerDay,
                                         Integer soilPrepDeepPlowOperationCount, Double soilPrepDeepPlowDieselRate, Double soilPrepDeepPlowLaborRate,
                                         Double soilPrepSecondaryOperationCount, Double soilPrepSecondaryDieselRate, Double soilPrepSecondaryLaborRate) {

        BigDecimal soilPrepLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);

        return soilPrepLaserCostPerDecare(soilPrepLaserOperationCount, soilPrepLumpSumAmount, soilPrepLumpSumPrice)
                .add(soilPrepBlastingCostPerDecare(soilPrepBlastingOperationCount, soilPrepBlastingFrequency, soilPrepBlastingDieselRate, cityDieselPrice, soilPrepBlastingLaborRate, soilPrepLaborPrice))
                .add(soilPrepDeepPlowCostPerDecare(soilPrepDeepPlowOperationCount, soilPrepDeepPlowDieselRate, cityDieselPrice, soilPrepDeepPlowLaborRate, soilPrepLaborPrice))
                .add(soilPrepSecondaryCostPerDecare(soilPrepSecondaryOperationCount, soilPrepSecondaryDieselRate, cityDieselPrice, soilPrepSecondaryLaborRate, soilPrepLaborPrice));
    }

    public BigDecimal calculateSoilPrep(List<PlantationProductQuestion> productQuestionList, Long parcelPlanId, City city) {
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());

        Integer soilPrepLaserOperationCount = 1;
        Integer soilPrepLumpSumAmount = 1;
        BigDecimal soilPrepLumpSumPrice = decimalValueSetter(productQuestionList, 44L);
        Integer soilPrepBlastingOperationCount = 1;
        Double soilPrepBlastingFrequency = doubleValueSetter(productQuestionList, 45L);
        Double soilPrepBlastingDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.SOIL_BLASTING)).findFirst().get().getDieselValue();
        BigDecimal cityDieselPrice = city.getDieselPrice();
        Double soilPrepBlastingLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.SOIL_BLASTING)).findFirst().get().getLaborValue();
        BigDecimal maleDailyWage = decimalValueSetter(productQuestionList, 41L);
        Double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        Integer soilPrepDeepPlowOperationCount = 1;
        Double soilPrepDeepPlowDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.DEEP_PLOW)).findFirst().get().getDieselValue();
        Double soilPrepDeepPlowLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.DEEP_PLOW)).findFirst().get().getLaborValue();
        Double soilPrepSecondaryOperationCount = doubleValueSetter(productQuestionList, 47L);
        Double soilPrepSecondaryDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.SECONDARY_OPERATION)).findFirst().get().getDieselValue();
        Double soilPrepSecondaryLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.SECONDARY_OPERATION)).findFirst().get().getLaborValue();
        return soilPrepTotalCost(soilPrepLaserOperationCount, soilPrepLumpSumAmount, soilPrepLumpSumPrice,
                soilPrepBlastingOperationCount, soilPrepBlastingFrequency, soilPrepBlastingDieselRate, cityDieselPrice, soilPrepBlastingLaborRate, maleDailyWage, workHoursPerDay,
                soilPrepDeepPlowOperationCount, soilPrepDeepPlowDieselRate, soilPrepDeepPlowLaborRate,
                soilPrepSecondaryOperationCount, soilPrepSecondaryDieselRate, soilPrepSecondaryLaborRate);
    }

    //Gider- Ekim Dikim
    //38,8
    private BigDecimal plantingCostSeederPerDecare(Double seedUsageYear, Double seederEnergyPerDecare, BigDecimal cityDieselPrice,
                                                   Double seederLaborPerDecare, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(plantingYearEnergyAmount(seedUsageYear, seederEnergyPerDecare)).multiply(cityDieselPrice)
                .add(BigDecimal.valueOf(plantingYearLaborAmount(seedUsageYear, seederLaborPerDecare)).multiply(workingManLaborPrice));
    }
    //120
    private BigDecimal plantingSeederRentalCostPerDecare(Integer plantingSeederLumpSumAmount, BigDecimal plantingLumpSumPrice) {
        return BigDecimal.valueOf(plantingSeederLumpSumAmount).multiply(plantingLumpSumPrice);
    }
    //500
    private BigDecimal plantingSeedCostPerDecare(Double seedHandHourPerDecare, Double seedUsageYear, BigDecimal workingManLaborPrice) {
        if (seedUsageYear == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(plantingHandLaborAmount(seedHandHourPerDecare, seedUsageYear)).multiply(workingManLaborPrice);
    }
    //280
    private BigDecimal plantingDroneCostPerDecare(Integer plantingDroneLumpSumAmount, BigDecimal plantingDroneLumpSumPrice) {
        return BigDecimal.valueOf(plantingDroneLumpSumAmount).multiply(plantingDroneLumpSumPrice);
    }
    //100
    private BigDecimal plantByHandCostPerDecare(Double plantingSeedlingPerDecare, Double averagePlantingHandPerPerson, Integer seedlingUsageYear,
                                                BigDecimal workingWomanLaborPrice) {
        if (seedlingUsageYear == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(plantingSeedlingHandLaborAmount(plantingSeedlingPerDecare, averagePlantingHandPerPerson, seedlingUsageYear))
                .multiply(workingWomanLaborPrice);
    }

    //166,7
    private BigDecimal plantByDrillCostPerDecare(Double plantingSeedlingPerDecare, Double drillPlantingPerHour, BigDecimal cityDieselPrice) {
        if (drillPlantingPerHour == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(plantingDrillEnergyAmountPerHour(plantingSeedlingPerDecare, drillPlantingPerHour)).multiply(cityDieselPrice);
    }

    //38,9
    private BigDecimal plantByDrillCostPerHour(Double plantingSeedlingPerDecare, Double drillPlantingPerHour, Double dieselAmountPerHour, Integer seedlingUsageYear, BigDecimal cityDieselPrice) {
        if (drillPlantingPerHour == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(plantingDrillDieselAmountPerHour(plantingSeedlingPerDecare, drillPlantingPerHour, dieselAmountPerHour, seedlingUsageYear)).multiply(cityDieselPrice);
    }

    //275
    private BigDecimal rentalDrillCostPerHour(Double drillAmount, BigDecimal rentalDrillCostPerHour) {
        return BigDecimal.valueOf(drillAmount).multiply(rentalDrillCostPerHour);
    }

    //2,222
    private Double seedToFideMaterialAmountUnit(Double seedToFideAmountPerDecare) {
        return seedToFideAmountPerDecare / 1000d;
    }

    //410,7
    private BigDecimal plantingUnitCostPerDecare(Double seedToFideAmountPerDecare, Double seedToFideWorkAmount,
                                                 BigDecimal maleDailyWage, Double maleCostRate, BigDecimal femaleDailyWage, Double femaleCostRate, Double workHoursPerDay,
                                                 BigDecimal seedToFidePrice) {
        if (seedToFideWorkAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(seedToFideLaborAmountPerDecare(seedToFideAmountPerDecare, seedToFideWorkAmount))
                .multiply(workingMixedLaborPrice(maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay))
                .add(BigDecimal.valueOf(seedToFideMaterialAmountUnit(seedToFideAmountPerDecare))
                        .multiply(seedToFidePrice));
    }

    //5910,0
    private BigDecimal plantingYumruCostPerDecare(Double workPowerCount, BigDecimal workingMixedLaborPrice,
                                                  Double averageYumruAmount, Double yumruUsageYear, BigDecimal yumruKgPrice) {
        if (yumruUsageYear == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(workPowerCount).multiply(workingMixedLaborPrice)
                .add(BigDecimal.valueOf(plantingMaterialAmountYumru(averageYumruAmount, yumruUsageYear)).multiply(yumruKgPrice));
    }

    //8100
    private BigDecimal plantingHourCostPerDecare(Double workPowerHour, Double workPowerCount, BigDecimal workingWomanLaborPrice) {
        return BigDecimal.valueOf(plantingLaborAmountYumru(workPowerHour, workPowerCount)).multiply(workingWomanLaborPrice);
    }

    //Gider- Ekim Dikim
    //38508,2
    private BigDecimal plantingTotalCost(Double seedKgPerDecare, BigDecimal seedPricePerKg, Double seedGrPerDecare, Double seedUnitPerDecare, BigDecimal seedPricePer1000, Double decarePerBag, BigDecimal seedBagPrice,
                                         Double seedUsageYear, Double seederEnergyPerDecare, BigDecimal cityDieselPrice, Double seederLaborPerDecare, BigDecimal maleDailyWage, Double workHoursPerDay,
                                         Integer plantingSeederLumpSumAmount, BigDecimal plantingLumpSumPrice,
                                         Double seedHandHourPerDecare,
                                         Integer plantingDroneLumpSumAmount, BigDecimal plantingDroneLumpSumPrice,
                                         Double plantingSeedlingPerDecare, Integer seedlingUsageYear, BigDecimal seedlingUnitPrice,
                                         Integer plantingSteelingPerDecare, BigDecimal steelingUnitPrice,
                                         Double averagePlantingHandPerPerson, BigDecimal femaleDailyWage,
                                         Double drillPlantingPerHour,
                                         Double dieselAmountPerHour,
                                         Double drillAmount, BigDecimal rentalDrillCostPerHour,
                                         Double seedToFideAmountPerDecare, BigDecimal seedToFidePrice, Double seedToFideWorkAmount,
                                         Double maleCostRate, Double femaleCostRate,
                                         Double workPowerCount, Double averageYumruAmount, Double yumruUsageYear, BigDecimal yumruKgPrice,
                                         Double workPowerHour) {
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);
        BigDecimal workingWomanLaborPrice = workingWomanLaborPrice(femaleDailyWage, workHoursPerDay);
        BigDecimal workingMixedLaborPrice = workingMixedLaborPrice(maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay);

        return plantingCostPerDecareKg(seedKgPerDecare, seedPricePerKg)
                .add(plantingCostPerDecareGr(seedGrPerDecare, seedPricePerKg))
                .add(plantingCostPerDecareUnit(seedUnitPerDecare, seedPricePer1000))
                .add(plantingCostPerDecareBag(decarePerBag, seedBagPrice))
                .add(plantingCostSeederPerDecare(seedUsageYear, seederEnergyPerDecare, cityDieselPrice, seederLaborPerDecare, workingManLaborPrice))
                .add(plantingSeederRentalCostPerDecare(plantingSeederLumpSumAmount, plantingLumpSumPrice))
                .add(plantingSeedCostPerDecare(seedHandHourPerDecare, seedUsageYear, workingManLaborPrice))
                .add(plantingDroneCostPerDecare(plantingDroneLumpSumAmount, plantingDroneLumpSumPrice))
                .add(seedlingUnitCostPerDecare(plantingSeedlingPerDecare, seedlingUsageYear, seedlingUnitPrice))
                .add(steelingUnitCostPerDecare(plantingSteelingPerDecare, seedlingUsageYear, steelingUnitPrice))
                .add(plantByHandCostPerDecare(plantingSeedlingPerDecare, averagePlantingHandPerPerson, seedlingUsageYear, workingWomanLaborPrice))
                .add(plantByDrillCostPerDecare(plantingSeedlingPerDecare, drillPlantingPerHour, cityDieselPrice))
                .add(plantByDrillCostPerHour(plantingSeedlingPerDecare, drillPlantingPerHour, dieselAmountPerHour, seedlingUsageYear, cityDieselPrice))
                .add(rentalDrillCostPerHour(drillAmount, rentalDrillCostPerHour))
                .add(plantingUnitCostPerDecare(seedToFideAmountPerDecare, seedToFideWorkAmount, maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay, seedToFidePrice))
                .add(plantingYumruCostPerDecare(workPowerCount, workingMixedLaborPrice, averageYumruAmount, yumruUsageYear, yumruKgPrice))
                .add(plantingHourCostPerDecare(workPowerHour, workPowerCount, workingWomanLaborPrice));
    }

    public BigDecimal calculatePlantingCost(List<PlantationProductQuestion> productQuestionList, Long parcelPlanId, City city) {
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlanId, List.of(41L, 42L));

        Double seedKgPerDecare = doubleValueSetter(productQuestionList, 57L);
        BigDecimal seedPricePerKg = decimalValueSetter(productQuestionList, 61L);
        Double seedGrPerDecare = doubleValueSetter(productQuestionList, 58L);
        Double seedUnitPerDecare = doubleValueSetter(productQuestionList, 59L);
        BigDecimal seedPricePer1000 = decimalValueSetter(productQuestionList, 62L);
        Double decarePerBag = doubleValueSetter(productQuestionList, 60L);
        BigDecimal seedBagPrice = decimalValueSetter(productQuestionList, 63L);
        Double seedUsageYear = doubleValueSetter(productQuestionList, 69L);
        Double seederEnergyPerDecare = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.DRILL_PLANTING)).findFirst().get().getDieselValue();
        BigDecimal cityDieselPrice = city.getDieselPrice();
        Double seederLaborPerDecare = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.DRILL_PLANTING)).findFirst().get().getLaborValue();
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        Double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        Integer plantingSeederLumpSumAmount = 1;
        BigDecimal plantingLumpSumPrice = decimalValueSetter(productQuestionList, 66L);
        Double seedHandHourPerDecare = doubleValueSetter(productQuestionList, 67L);
        Integer plantingDroneLumpSumAmount = 1;
        BigDecimal plantingDroneLumpSumPrice = decimalValueSetter(productQuestionList, 68L);
        Double plantingSeedlingPerDecare = doubleValueSetter(productQuestionList, 71L);
        Integer seedlingUsageYear = integerValueSetter(productQuestionList, 84L);
        BigDecimal seedlingUnitPrice = decimalValueSetter(productQuestionList, 73L);
        Integer plantingSteelingPerDecare = integerValueSetter(productQuestionList, 72L);
        BigDecimal steelingUnitPrice = decimalValueSetter(productQuestionList, 74L);
        Double averagePlantingHandPerPerson = doubleValueSetter(productQuestionList, 76L);
        BigDecimal femaleDailyWage = decimalAnswerSetter(previousAnswerList, 42L);
        Double drillPlantingPerHour = doubleValueSetter(productQuestionList, 78L);
        Double dieselAmountPerHour = doubleValueSetter(productQuestionList, 79L);
        Double drillAmount = 1d;
        BigDecimal rentalDrillCostPerHour = decimalValueSetter(productQuestionList, 80L);
        Double seedToFideAmountPerDecare = doubleValueSetter(productQuestionList, 81L);
        BigDecimal seedToFidePrice = decimalAnswerSetter(previousAnswerList, 82L);
        Double seedToFideWorkAmount = doubleValueSetter(productQuestionList, 83L);
        Double maleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        Double femaleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.FEMALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        Double workPowerCount = doubleValueSetter(productQuestionList, 89L);
        Double averageYumruAmount = doubleValueSetter(productQuestionList, 85L);
        Double yumruUsageYear = doubleValueSetter(productQuestionList, 90L);
        BigDecimal yumruKgPrice = decimalValueSetter(productQuestionList, 87L);
        Double workPowerHour = doubleValueSetter(productQuestionList, 88L);

        return plantingTotalCost(seedKgPerDecare, seedPricePerKg, seedGrPerDecare, seedUnitPerDecare, seedPricePer1000, decarePerBag, seedBagPrice,
                seedUsageYear, seederEnergyPerDecare, cityDieselPrice, seederLaborPerDecare, maleDailyWage, workHoursPerDay,
                plantingSeederLumpSumAmount, plantingLumpSumPrice,
                seedHandHourPerDecare,
                plantingDroneLumpSumAmount, plantingDroneLumpSumPrice,
                plantingSeedlingPerDecare, seedlingUsageYear, seedlingUnitPrice,
                plantingSteelingPerDecare, steelingUnitPrice,
                averagePlantingHandPerPerson, femaleDailyWage,
                drillPlantingPerHour,
                dieselAmountPerHour,
                drillAmount, rentalDrillCostPerHour,
                seedToFideAmountPerDecare, seedToFidePrice, seedToFideWorkAmount,
                maleCostRate, femaleCostRate,
                workPowerCount, averageYumruAmount, yumruUsageYear, yumruKgPrice,
                workPowerHour);
    }

    //Gider - Gübre
    //753,75
    private BigDecimal composeFertilizerCostPerDecare(Double baseFertilizerDieselRate, BigDecimal cityDieselPrice,
                                                      Double baseFertilizerLaborRate, BigDecimal workingManLaborPrice,
                                                      Double composeFertilizerKgAmount, BigDecimal composeFertilizerCostPerKg) {
        if (composeFertilizerKgAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(baseFertilizerDieselRate).multiply(cityDieselPrice)
                .add(BigDecimal.valueOf(baseFertilizerLaborRate).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(composeFertilizerKgAmount).multiply(composeFertilizerCostPerKg));
    }

    //648,75
    private BigDecimal firstNitrogenFertilizerCostPerDecare(Double topFertilizerDieselRate, BigDecimal cityDieselPrice,
                                                            Double topFertilizerLaborRate, BigDecimal workingManLaborPrice,
                                                            Double firstNitrogenFertilizerKgAmount, BigDecimal firstNitrogenFertilizerCostPerKg) {
        if (firstNitrogenFertilizerKgAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(topFertilizerDieselRate).multiply(cityDieselPrice)
                .add(BigDecimal.valueOf(topFertilizerLaborRate).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(firstNitrogenFertilizerKgAmount).multiply(firstNitrogenFertilizerCostPerKg));
    }

    //88,75
    private BigDecimal secondNitrogenFertilizerCostPerDecare(Double topFertilizerDieselRate, BigDecimal cityDieselPrice,
                                                             Double topFertilizerLaborRate, BigDecimal workingManLaborPrice,
                                                             Double secondNitrogenFertilizerKgAmount, BigDecimal secondNitrogenFertilizerCostPerKg) {
        if (secondNitrogenFertilizerKgAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(topFertilizerDieselRate).multiply(cityDieselPrice)
                .add(BigDecimal.valueOf(topFertilizerLaborRate).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(secondNitrogenFertilizerKgAmount).multiply(secondNitrogenFertilizerCostPerKg));
    }

    //108,75
    private BigDecimal potassiumFertilizerCostPerDecare(Double topFertilizerDieselRate, BigDecimal cityDieselPrice,
                                                        Double topFertilizerLaborRate, BigDecimal workingManLaborPrice,
                                                        Double potassiumFertilizerKgAmount, BigDecimal potassiumFertilizerCostPerKg) {
        if (potassiumFertilizerKgAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(topFertilizerDieselRate).multiply(cityDieselPrice)
                .add(BigDecimal.valueOf(topFertilizerLaborRate).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(potassiumFertilizerKgAmount).multiply(potassiumFertilizerCostPerKg));
    }

    //177,75
    private BigDecimal phosphorusFertilizerCostPerDecare(Double topFertilizerDieselRate, BigDecimal cityDieselPrice,
                                                         Double topFertilizerLaborRate, BigDecimal workingManLaborPrice,
                                                         Double phosphorusFertilizerKgAmount, BigDecimal phosphorusFertilizerCostPerKg) {
        if (phosphorusFertilizerKgAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(topFertilizerDieselRate).multiply(cityDieselPrice)
                .add(BigDecimal.valueOf(topFertilizerLaborRate).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(phosphorusFertilizerKgAmount).multiply(phosphorusFertilizerCostPerKg));
    }

    //1490
    private BigDecimal liquidFertilizerCostPerDecare(Integer liquidFertilizer,
                                                     Double liquidFertilizerDieselRate, BigDecimal cityDieselPrice,
                                                     Double liquidFertilizerLaborRate, BigDecimal workingManLaborPrice,
                                                     BigDecimal liquidFertilizerAveragePrice) {
        if (liquidFertilizer == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(liquidFertilizer).multiply(
                BigDecimal.valueOf(liquidFertilizerDieselRate).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(liquidFertilizerLaborRate).multiply(workingManLaborPrice))
                        .add(BigDecimal.ONE.multiply(liquidFertilizerAveragePrice)));
    }

    //12937,5
    private BigDecimal animalFertilizerCostPerDecare(Double animalFertilizerDieselRate, Double animalFertilizerFrequency, BigDecimal cityDieselPrice,
                                                     Double animalFertilizerLaborRate, BigDecimal workingManLaborPrice,
                                                     Double animalFertilizerPerDecare, BigDecimal animalFertilizerPerTon) {
        if (animalFertilizerFrequency == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(animalFertilizerDieselAmount(animalFertilizerDieselRate, animalFertilizerFrequency)).multiply(cityDieselPrice)
                .add(BigDecimal.valueOf(animalFertilizerLaborAmount(animalFertilizerLaborRate, animalFertilizerFrequency)).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(animalFertilizerKgAmount(animalFertilizerPerDecare, animalFertilizerFrequency)).multiply(animalFertilizerPerTon));
    }

    //953,75
    private BigDecimal humicAcidCostPerDecare(Double humicAcidAmountPerDecare,
                                              Double humicAcidDieselRate, BigDecimal cityDieselPrice,
                                              Double humicAcidLaborRate, BigDecimal workingManLaborPrice,
                                              BigDecimal humicAcidAveragePrice) {
        if (humicAcidAmountPerDecare == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(humicAcidAmountPerDecare).multiply(
                BigDecimal.valueOf(humicAcidDieselRate).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(humicAcidLaborRate).multiply(workingManLaborPrice))
                        .add(BigDecimal.ONE.multiply(humicAcidAveragePrice)));

    }

    //400
    private BigDecimal leonarditeCostPerDecare(Double leonarditeDieselRate, BigDecimal cityDieselPrice,
                                               Double leonarditeLaborRate, BigDecimal workingManLaborPrice,
                                               Double leonarditePerDecare, BigDecimal leonarditePerTon) {
        if (leonarditePerDecare == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(leonarditeDieselRate).multiply(cityDieselPrice)
                .add(BigDecimal.valueOf(leonarditeLaborRate).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(leonarditePerDecare).multiply(leonarditePerTon));
    }

    //2800,75
    private BigDecimal wormFertilizerCostPerDecare(Double wormFertilizerDieselRate, BigDecimal cityDieselPrice,
                                                   Double wormFertilizerLaborRate, BigDecimal workingManLaborPrice,
                                                   Double wormFertilizerPerDecare, BigDecimal wormFertilizerPerTon) {
        if (wormFertilizerPerDecare == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(wormFertilizerDieselRate).multiply(cityDieselPrice)
                .add(BigDecimal.valueOf(wormFertilizerLaborRate).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(wormFertilizerPerDecare).multiply(wormFertilizerPerTon));
    }

    //1477,5
    private BigDecimal bioConditionerCostPerDecare(Double bioConditionerDieselRate, BigDecimal cityDieselPrice,
                                                   Double bioConditionerLaborRate, BigDecimal workingManLaborPrice,
                                                   Double bioConditionerPerDecare, BigDecimal bioConditionerPerTon) {
        if (bioConditionerPerDecare == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(bioConditionerDieselRate).multiply(cityDieselPrice)
                .add(BigDecimal.valueOf(bioConditionerLaborRate).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(bioConditionerPerDecare).multiply(bioConditionerPerTon));
    }

    //Gider - Gübre
    //21837,25
    private BigDecimal fertilizerTotalCost(Double baseFertilizerDieselRate, BigDecimal cityDieselPrice, Double baseFertilizerLaborRate, BigDecimal maleDailyWage, Double workHoursPerDay, Double composeFertilizerKgAmount, BigDecimal composeFertilizerCostPerKg,
                                           Double topFertilizerDieselRate, Double topFertilizerLaborRate, Double firstNitrogenFertilizerKgAmount, BigDecimal firstNitrogenFertilizerCostPerKg,
                                           Double secondNitrogenFertilizerKgAmount, BigDecimal secondNitrogenFertilizerCostPerKg,
                                           Double potassiumFertilizerKgAmount, BigDecimal potassiumFertilizerCostPerKg,
                                           Double phosphorusFertilizerKgAmount, BigDecimal phosphorusFertilizerCostPerKg,
                                           Integer liquidFertilizer, Double liquidFertilizerDieselRate, Double liquidFertilizerLaborRate, BigDecimal liquidFertilizerAveragePrice,
                                           Double animalFertilizerDieselRate, Double animalFertilizerFrequency, Double animalFertilizerLaborRate, Double animalFertilizerPerDecare, BigDecimal animalFertilizerPerTon,
                                           Double humicAcidAmountPerDecare, Double humicAcidDieselRate, Double humicAcidLaborRate, BigDecimal humicAcidAveragePrice,
                                           Double leonarditeDieselRate, Double leonarditeLaborRate, Double leonarditePerDecare, BigDecimal leonarditePerTon,
                                           Double wormFertilizerDieselRate, Double wormFertilizerLaborRate, Double wormFertilizerPerDecare, BigDecimal wormFertilizerPerTon,
                                           Double bioConditionerDieselRate, Double bioConditionerLaborRate, Double bioConditionerPerDecare, BigDecimal bioConditionerPerTon) {
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);

        return composeFertilizerCostPerDecare(baseFertilizerDieselRate, cityDieselPrice, baseFertilizerLaborRate, workingManLaborPrice, composeFertilizerKgAmount, composeFertilizerCostPerKg)
                .add(firstNitrogenFertilizerCostPerDecare(topFertilizerDieselRate, cityDieselPrice, topFertilizerLaborRate, workingManLaborPrice, firstNitrogenFertilizerKgAmount, firstNitrogenFertilizerCostPerKg))
                .add(secondNitrogenFertilizerCostPerDecare(topFertilizerDieselRate, cityDieselPrice, topFertilizerLaborRate, workingManLaborPrice, secondNitrogenFertilizerKgAmount, secondNitrogenFertilizerCostPerKg))
                .add(potassiumFertilizerCostPerDecare(topFertilizerDieselRate, cityDieselPrice, topFertilizerLaborRate, workingManLaborPrice, potassiumFertilizerKgAmount, potassiumFertilizerCostPerKg))
                .add(phosphorusFertilizerCostPerDecare(topFertilizerDieselRate, cityDieselPrice, topFertilizerLaborRate, workingManLaborPrice, phosphorusFertilizerKgAmount, phosphorusFertilizerCostPerKg))
                .add(liquidFertilizerCostPerDecare(liquidFertilizer, liquidFertilizerDieselRate, cityDieselPrice, liquidFertilizerLaborRate, workingManLaborPrice, liquidFertilizerAveragePrice))
                .add(animalFertilizerCostPerDecare(animalFertilizerDieselRate, animalFertilizerFrequency, cityDieselPrice, animalFertilizerLaborRate, workingManLaborPrice, animalFertilizerPerDecare, animalFertilizerPerTon))
                .add(humicAcidCostPerDecare(humicAcidAmountPerDecare, humicAcidDieselRate, cityDieselPrice, humicAcidLaborRate, workingManLaborPrice, humicAcidAveragePrice))
                .add(leonarditeCostPerDecare(leonarditeDieselRate, cityDieselPrice, leonarditeLaborRate, workingManLaborPrice, leonarditePerDecare, leonarditePerTon))
                .add(wormFertilizerCostPerDecare(wormFertilizerDieselRate, cityDieselPrice, wormFertilizerLaborRate, workingManLaborPrice, wormFertilizerPerDecare, wormFertilizerPerTon))
                .add(bioConditionerCostPerDecare(bioConditionerDieselRate, cityDieselPrice, bioConditionerLaborRate, workingManLaborPrice, bioConditionerPerDecare, bioConditionerPerTon));
    }

    public BigDecimal calculateFertilizerCost(List<PlantationProductQuestion> productQuestionList, Long parcelPlanId, City city) {
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlanId, List.of(41L));

        Double baseFertilizerDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.BASE_FERTILIZER)).findFirst().get().getDieselValue();
        BigDecimal cityDieselPrice = city.getDieselPrice();
        Double baseFertilizerLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.BASE_FERTILIZER)).findFirst().get().getLaborValue();
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        Double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        Double composeFertilizerKgAmount = doubleValueSetter(productQuestionList, 93L);
        BigDecimal composeFertilizerCostPerKg = decimalValueSetter(productQuestionList, 94L);
        Double topFertilizerDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.TOP_FERTILIZER)).findFirst().get().getDieselValue();
        Double topFertilizerLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.TOP_FERTILIZER)).findFirst().get().getLaborValue();
        Double firstNitrogenFertilizerKgAmount = doubleValueSetter(productQuestionList, 95L);
        BigDecimal firstNitrogenFertilizerCostPerKg = decimalValueSetter(productQuestionList, 96L);
        Double secondNitrogenFertilizerKgAmount = doubleValueSetter(productQuestionList, 97L);
        BigDecimal secondNitrogenFertilizerCostPerKg = decimalValueSetter(productQuestionList, 98L);
        Double potassiumFertilizerKgAmount = doubleValueSetter(productQuestionList, 101L);
        BigDecimal potassiumFertilizerCostPerKg = decimalValueSetter(productQuestionList, 102L);
        Double phosphorusFertilizerKgAmount = doubleValueSetter(productQuestionList, 103L);
        BigDecimal phosphorusFertilizerCostPerKg = decimalValueSetter(productQuestionList, 104L);
        Integer liquidFertilizer = integerValueSetter(productQuestionList, 106L);
        Double liquidFertilizerDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.LIQUID_FERTILIZER)).findFirst().get().getDieselValue();
        Double liquidFertilizerLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.LIQUID_FERTILIZER)).findFirst().get().getLaborValue();
        BigDecimal liquidFertilizerAveragePrice = BigDecimal.valueOf(300);//todo hep ortalama değer mi alınacaK?
        Double animalFertilizerDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.ANIMAL_FERTILIZER)).findFirst().get().getDieselValue();
        Double animalFertilizerFrequency = doubleValueSetter(productQuestionList, 109L);
        Double animalFertilizerLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.ANIMAL_FERTILIZER)).findFirst().get().getLaborValue();
        Double animalFertilizerPerDecare = doubleValueSetter(productQuestionList, 107L);
        BigDecimal animalFertilizerPerTon = decimalValueSetter(productQuestionList, 108L);
        Double humicAcidAmountPerDecare = doubleValueSetter(productQuestionList, 110L);
        Double humicAcidDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.HUMIC_ACID)).findFirst().get().getDieselValue();
        Double humicAcidLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.HUMIC_ACID)).findFirst().get().getLaborValue();
        BigDecimal humicAcidAveragePrice = decimalValueSetter(productQuestionList, 111L);
        Double leonarditeDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.LEONARDITE)).findFirst().get().getDieselValue();
        Double leonarditeLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.LEONARDITE)).findFirst().get().getLaborValue();
        Double leonarditePerDecare = doubleValueSetter(productQuestionList, 112L);
        BigDecimal leonarditePerTon = decimalValueSetter(productQuestionList, 113L);
        Double wormFertilizerDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORM_COMPOST)).findFirst().get().getDieselValue();
        Double wormFertilizerLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORM_COMPOST)).findFirst().get().getLaborValue();
        Double wormFertilizerPerDecare = doubleValueSetter(productQuestionList, 114L);
        BigDecimal wormFertilizerPerTon = decimalValueSetter(productQuestionList, 115L);
        Double bioConditionerDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.BIO_CONDITIONER)).findFirst().get().getDieselValue();
        Double bioConditionerLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.BIO_CONDITIONER)).findFirst().get().getLaborValue();
        Double bioConditionerPerDecare = doubleValueSetter(productQuestionList, 116L);
        BigDecimal bioConditionerPerTon = decimalValueSetter(productQuestionList, 117L);

        return fertilizerTotalCost(baseFertilizerDieselRate, cityDieselPrice, baseFertilizerLaborRate, maleDailyWage, workHoursPerDay, composeFertilizerKgAmount, composeFertilizerCostPerKg,
                topFertilizerDieselRate, topFertilizerLaborRate, firstNitrogenFertilizerKgAmount, firstNitrogenFertilizerCostPerKg,
                secondNitrogenFertilizerKgAmount, secondNitrogenFertilizerCostPerKg,
                potassiumFertilizerKgAmount, potassiumFertilizerCostPerKg,
                phosphorusFertilizerKgAmount, phosphorusFertilizerCostPerKg,
                liquidFertilizer, liquidFertilizerDieselRate, liquidFertilizerLaborRate, liquidFertilizerAveragePrice,
                animalFertilizerDieselRate, animalFertilizerFrequency, animalFertilizerLaborRate, animalFertilizerPerDecare, animalFertilizerPerTon,
                humicAcidAmountPerDecare, humicAcidDieselRate, humicAcidLaborRate, humicAcidAveragePrice,
                leonarditeDieselRate, leonarditeLaborRate, leonarditePerDecare, leonarditePerTon,
                wormFertilizerDieselRate, wormFertilizerLaborRate, wormFertilizerPerDecare, wormFertilizerPerTon,
                bioConditionerDieselRate, bioConditionerLaborRate, bioConditionerPerDecare, bioConditionerPerTon);
    }

    //Gider - Y. Ot kontrolü
    //1350
    private BigDecimal throatFillingByHandCostPerDecare(Double throatFillingLaborAmount, BigDecimal workingWomanLaborPrice) {
        return BigDecimal.valueOf(throatFillingLaborAmount).multiply(workingWomanLaborPrice);
    }

    //187,5
    private BigDecimal throatFillingByMachineCostPerDecare(Double throatFillingEnergyAmount, BigDecimal cityDieselPrice,
                                                           Double throatFillingLaborRate, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(throatFillingEnergyAmount).multiply(cityDieselPrice)
                .add(BigDecimal.valueOf(throatFillingLaborRate).multiply(workingManLaborPrice));
    }

    //5400
    private BigDecimal weedingByHandCostPerDecare(Integer handWeedingCount, Double averageHandLaborAmount, BigDecimal workingWomanLaborPrice) {
        return BigDecimal.valueOf(handWeedingCount).multiply(
                BigDecimal.valueOf(averageHandLaborAmount).multiply(workingWomanLaborPrice));
    }

    //355
    private BigDecimal weedingByTractorCostPerDecare(Integer tractorWeedingCount,
                                                     Double averageTractorLaborAmount, BigDecimal cityDieselPrice,
                                                     Double tractorWeedingLabor, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(tractorWeedingCount).multiply(
                BigDecimal.valueOf(averageTractorLaborAmount).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(tractorWeedingLabor).multiply(workingManLaborPrice)));
    }

    //4200
    private BigDecimal weedingByMachineCostPerDecare(Integer machineWeedingCount,
                                                     Double averageMachineLaborAmount, BigDecimal cityDieselPrice,
                                                     Double machineWeedingLabor, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(machineWeedingCount).multiply(
                BigDecimal.valueOf(averageMachineLaborAmount).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(machineWeedingLabor).multiply(workingManLaborPrice)));
    }

    //188,7301533
    private BigDecimal medicineCostPerDecare(Integer medicineCount,
                                             Double medicineEnergyAmount, BigDecimal cityDieselPrice,
                                             Double medicineLaborAmount, BigDecimal workingManLaborPrice,
                                             BigDecimal productMedicinePrice) {
        return BigDecimal.valueOf(medicineCount).multiply(
                BigDecimal.valueOf(medicineEnergyAmount).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(medicineLaborAmount).multiply(workingManLaborPrice))
                        .add(productMedicinePrice));
    }

    //2300
    private BigDecimal byHandCostPerDecare(Integer handCountFrequency, Double averageLaborHourAmount,
                                           BigDecimal workingMixedLaborPrice) {
        return BigDecimal.valueOf(handCountFrequency).multiply(
                BigDecimal.valueOf(averageLaborHourAmount).multiply(workingMixedLaborPrice));
    }

    //5740
    private BigDecimal mulchingCostPerDecare(Double totalMulchingHour, Double mulchUsageYear,
                                             BigDecimal workingMixedLaborPrice,
                                             Double mulchAmountPerDecare, BigDecimal mulchCostPricePerKg) {
        if (mulchUsageYear == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(mulchingLaborAmount(totalMulchingHour, mulchUsageYear)).multiply(workingMixedLaborPrice)
                .add(BigDecimal.valueOf(mulchingCostAmount(mulchAmountPerDecare, mulchUsageYear)).multiply(mulchCostPricePerKg));
    }

    //2940
    private BigDecimal weedToolCostPerDecare(Integer weedToolCount,
                                             Double averageWeedToolLaborAmount, BigDecimal cityFuelPrice,
                                             Double weedToolLaborDecareAmountPerDay, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(weedToolCount).multiply(
                BigDecimal.valueOf(averageWeedToolLaborAmount).multiply(cityFuelPrice)
                        .add(BigDecimal.valueOf(weedToolLaborDecareAmountPerDay).multiply(workingManLaborPrice)));
    }

    //2000
    private BigDecimal animalPlowCostPerDecare(Double animalPlowPerYear, Double animalPlowAmountDecarePerDay, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(animalPlowPerYear).multiply(
                BigDecimal.valueOf(animalPlowAmountDecarePerDay).multiply(workingManLaborPrice));
    }

    //Gider - Y. Ot kontrolü
    //24661,2302
    private BigDecimal wildGrassTotalCost(Double throatFillingLaborAmount, BigDecimal femaleDailyWage, Double workHoursPerDay,
                                          Double throatFillingEnergyAmount, BigDecimal cityDieselPrice, Double throatFillingLaborRate, BigDecimal maleDailyWage,
                                          Integer handWeedingCount, Double averageHandLaborAmount,
                                          Integer tractorWeedingCount, Double averageTractorLaborAmount, Double tractorWeedingLabor,
                                          Integer machineWeedingCount, Double averageMachineLaborAmount, Double machineWeedingLabor,
                                          Integer medicineCount, Double medicineEnergyAmount, Double medicineLaborAmount, BigDecimal productMedicinePrice,
                                          Integer handCountFrequency, Double averageLaborHourAmount, Double maleCostRate, Double femaleCostRate,
                                          Double totalMulchingHour, Double mulchUsageYear, Double mulchAmountPerDecare, BigDecimal mulchCostPricePerKg,
                                          Integer weedToolCount, Double averageWeedToolLaborAmount, BigDecimal cityFuelPrice, Double weedToolLaborDecareAmountPerDay,
                                          Double animalPlowPerYear, Double animalPlowAmountDecarePerDay) {

        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);
        BigDecimal workingWomanLaborPrice = workingWomanLaborPrice(femaleDailyWage, workHoursPerDay);
        BigDecimal workingMixedLaborPrice = workingMixedLaborPrice(maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay);

        return throatFillingByHandCostPerDecare(throatFillingLaborAmount, workingWomanLaborPrice)
                .add(throatFillingByMachineCostPerDecare(throatFillingEnergyAmount, cityDieselPrice, throatFillingLaborRate, workingManLaborPrice))
                .add(weedingByHandCostPerDecare(handWeedingCount, averageHandLaborAmount, workingWomanLaborPrice))
                .add(weedingByTractorCostPerDecare(tractorWeedingCount, averageTractorLaborAmount, cityDieselPrice, tractorWeedingLabor, workingManLaborPrice))
                .add(weedingByMachineCostPerDecare(machineWeedingCount, averageMachineLaborAmount, cityDieselPrice, machineWeedingLabor, workingManLaborPrice))
                .add(medicineCostPerDecare(medicineCount, medicineEnergyAmount, cityDieselPrice, medicineLaborAmount, workingManLaborPrice, productMedicinePrice))
                .add(byHandCostPerDecare(handCountFrequency, averageLaborHourAmount, workingMixedLaborPrice))
                .add(mulchingCostPerDecare(totalMulchingHour, mulchUsageYear, workingMixedLaborPrice, mulchAmountPerDecare, mulchCostPricePerKg))
                .add(weedToolCostPerDecare(weedToolCount, averageWeedToolLaborAmount, cityFuelPrice, weedToolLaborDecareAmountPerDay, workingManLaborPrice))
                .add(animalPlowCostPerDecare(animalPlowPerYear, animalPlowAmountDecarePerDay, workingManLaborPrice));
    }

    public BigDecimal calculateWildGrassControlCost(List<PlantationProductQuestion> productQuestionList, Long parcelPlanId, City city) {
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlanId, List.of(41L, 42L));

        Double throatFillingLaborAmount = doubleValueSetter(productQuestionList, 119L);
        BigDecimal femaleDailyWage = decimalAnswerSetter(previousAnswerList, 42L);
        Double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        Double throatFillingEnergyAmount = doubleValueSetter(productQuestionList, 120L);
        BigDecimal cityDieselPrice = city.getDieselPrice();
        Double throatFillingLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.THROAT_FILLING)).findFirst().get().getLaborValue();
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        Integer handWeedingCount = integerValueSetter(productQuestionList, 130L);
        Double averageHandLaborAmount = doubleValueSetter(productQuestionList, 129L);
        Integer tractorWeedingCount = integerValueSetter(productQuestionList, 132L);
        Double averageTractorLaborAmount = doubleValueSetter(productQuestionList, 131L);
        Double tractorWeedingLabor = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.TRACTOR_TILLER)).findFirst().get().getLaborValue();
        Integer machineWeedingCount = integerValueSetter(productQuestionList, 135L);
        Double averageMachineLaborAmount = doubleValueSetter(productQuestionList, 133L);
        Double machineWeedingLabor = doubleValueSetter(productQuestionList, 134L);
        Integer medicineCount = integerValueSetter(productQuestionList, 136L);
        Double medicineEnergyAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WEED_CONTROL)).findFirst().get().getDieselValue();
        Double medicineLaborAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WEED_CONTROL)).findFirst().get().getLaborValue();
        BigDecimal productMedicinePrice = BigDecimal.valueOf(8.115076667d);
        Integer handCountFrequency = integerValueSetter(productQuestionList, 138L);
        Double averageLaborHourAmount = doubleValueSetter(productQuestionList, 137L);
        Double maleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        Double femaleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.FEMALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        Double totalMulchingHour = doubleValueSetter(productQuestionList, 141L);
        Double mulchUsageYear = doubleValueSetter(productQuestionList, 142L);
        Double mulchAmountPerDecare = doubleValueSetter(productQuestionList, 139L);
        BigDecimal mulchCostPricePerKg = decimalValueSetter(productQuestionList, 140L);
        Integer weedToolCount = integerValueSetter(productQuestionList, 145L);
        Double averageWeedToolLaborAmount = doubleValueSetter(productQuestionList, 144L);
        BigDecimal cityFuelPrice = city.getFuelPrice();
        Double weedToolLaborDecareAmountPerDay = doubleValueSetter(productQuestionList, 143L);
        Double animalPlowPerYear = doubleValueSetter(productQuestionList, 147L);
        Double animalPlowAmountDecarePerDay = doubleValueSetter(productQuestionList, 146L);

        return wildGrassTotalCost(throatFillingLaborAmount, femaleDailyWage, workHoursPerDay,
                throatFillingEnergyAmount, cityDieselPrice, throatFillingLaborRate, maleDailyWage,
                handWeedingCount, averageHandLaborAmount,
                tractorWeedingCount, averageTractorLaborAmount, tractorWeedingLabor,
                machineWeedingCount, averageMachineLaborAmount, machineWeedingLabor,
                medicineCount, medicineEnergyAmount, medicineLaborAmount, productMedicinePrice,
                handCountFrequency, averageLaborHourAmount, maleCostRate, femaleCostRate,
                totalMulchingHour, mulchUsageYear, mulchAmountPerDecare, mulchCostPricePerKg,
                weedToolCount, averageWeedToolLaborAmount, cityFuelPrice, weedToolLaborDecareAmountPerDay,
                animalPlowPerYear, animalPlowAmountDecarePerDay);
    }

    //Gider - Sulama Hesabı
    //5650
    private BigDecimal irrigationCostPaidPerTonne(Integer irrigationCountPerTonne,
                                                  Double irrigationDieselRateForSelectedIrrigation, BigDecimal cityDieselPrice,
                                                  Double irrigationLaborRateForSelectedIrrigation, BigDecimal workingManLaborPrice,
                                                  Double waterAmountPerDecarePerTonne, BigDecimal waterPricePerTonne, BigDecimal amortizationForSelectedIrrigation) {
        if (irrigationCountPerTonne == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(irrigationCountPerTonne).multiply(
                        BigDecimal.valueOf(irrigationDieselRateForSelectedIrrigation).multiply(cityDieselPrice)
                                .add(BigDecimal.valueOf(irrigationLaborRateForSelectedIrrigation).multiply(workingManLaborPrice))
                                .add(BigDecimal.valueOf(waterAmountPerDecarePerTonne).multiply(waterPricePerTonne)))
                .add(amortizationForSelectedIrrigation);
    }

    //5295
    private BigDecimal irrigationCostPaidPerDecare(Double irrigationCountPerDecare, Double pumpEfficiencyRate, BigDecimal cityDieselPrice,
                                                   Double irrigationLaborRateForSelectedIrrigation, BigDecimal workingManLaborPrice,
                                                   Double waterAmountPerDecare, BigDecimal waterPricePerDecare, BigDecimal amortizationForSelectedIrrigation) {
        if (irrigationCountPerDecare == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(irrigationCountPerDecare).multiply(
                        BigDecimal.valueOf(pumpEfficiencyRate).multiply(cityDieselPrice)
                                .add(BigDecimal.valueOf(irrigationLaborRateForSelectedIrrigation).multiply(workingManLaborPrice))
                                .add(BigDecimal.valueOf(waterAmountPerDecare).multiply(waterPricePerDecare)))
                .add(amortizationForSelectedIrrigation);
    }

    //1200

    //5586,575141
    private BigDecimal irrigationCostForElectricityPump(Double irrigationCountForElectricityPump, Double irrigationLaborRateForSelectedIrrigation, BigDecimal workingManLaborPrice,
                                                        Double electricityPumpWorkingHour, Double electricityWaterAmountPerHour, Double electricityWaterPumpHeight,
                                                        Integer constantNumber, Double constantMotorEfficiency, Double pumpEfficiencyRate,
                                                        Double irrigationAreaElectricity, BigDecimal cityElectricityPrice, BigDecimal amortizationForSelectedIrrigation) {
        if (irrigationCountForElectricityPump == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(irrigationCountForElectricityPump).multiply(
                        BigDecimal.valueOf(irrigationLaborRateForSelectedIrrigation).multiply(workingManLaborPrice)
                                .add(BigDecimal.valueOf(inputAmountForElectricityPump(electricityPumpWorkingHour, electricityWaterAmountPerHour, electricityWaterPumpHeight, constantNumber, constantMotorEfficiency, pumpEfficiencyRate, irrigationAreaElectricity)).multiply(cityElectricityPrice)))
                .add(amortizationForSelectedIrrigation);
    }

    //5835
    private BigDecimal irrigationCostForDieselPump(Double irrigationCountForDieselPump, Double specificConstantRate, Double pumpWorkingHour, Double waterAmountPerHour, Double waterPumpHeight, Double gravity,
                                                   Double pumpMotorEfficiencyRate, Double irrigationArea, BigDecimal cityDieselPrice, Double irrigationLaborRateForSelectedIrrigation, BigDecimal workingManLaborPrice,
                                                   BigDecimal amortizationForSelectedIrrigation) {
        if (pumpWorkingHour == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(irrigationCountForDieselPump).multiply(
                BigDecimal.valueOf(dieselInputAmountForDieselPump(specificConstantRate, pumpWorkingHour, waterAmountPerHour, waterPumpHeight, gravity, pumpMotorEfficiencyRate, irrigationArea)).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(irrigationLaborRateForSelectedIrrigation).multiply(workingManLaborPrice)))
                .add(amortizationForSelectedIrrigation);
    }

    //Gider - Sulama Hesabı
    //23566,57514
    private BigDecimal irrigationTotalCost(Integer irrigationCountPerTonne, Double irrigationDieselRateForSelectedIrrigation, Double irrigationLaborRateForSelectedIrrigation, BigDecimal maleDailyWage, Double workHoursPerDay, Double waterAmountPerDecarePerTonne, BigDecimal waterPricePerTonne, BigDecimal amortizationForSelectedIrrigation,
                                           Double irrigationCountPerDecare, Double pumpEfficiencyRate, BigDecimal cityDieselPrice, Double waterAmountPerDecare, BigDecimal waterPricePerDecare,
                                           BigDecimal waterPricePerDecareAlone,
                                           Double irrigationCountForElectricityPump, Double electricityPumpWorkingHour, Double electricityWaterAmountPerHour, Double electricityWaterPumpHeight, Integer constantNumber, Double constantMotorEfficiency, Double irrigationAreaElectricity, BigDecimal cityElectricityPrice,
                                           Double irrigationCountForDieselPump, Double specificConstantRate, Double pumpWorkingHour, Double waterAmountPerHour, Double waterPumpHeight, Double gravity, Double pumpMotorEfficiencyRate, Double irrigationArea) {
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);

        return irrigationCostPaidPerTonne(irrigationCountPerTonne, irrigationDieselRateForSelectedIrrigation, cityDieselPrice, irrigationLaborRateForSelectedIrrigation, workingManLaborPrice, waterAmountPerDecarePerTonne, waterPricePerTonne, amortizationForSelectedIrrigation)
                .add(irrigationCostPaidPerDecare(irrigationCountPerDecare, pumpEfficiencyRate, cityDieselPrice, irrigationLaborRateForSelectedIrrigation, workingManLaborPrice, waterAmountPerDecare, waterPricePerDecare, amortizationForSelectedIrrigation))
                .add(waterPricePerDecareAlone == null ? BigDecimal.ZERO : waterPricePerDecareAlone)
                .add(irrigationCostForElectricityPump(irrigationCountForElectricityPump, irrigationLaborRateForSelectedIrrigation, workingManLaborPrice, electricityPumpWorkingHour, electricityWaterAmountPerHour, electricityWaterPumpHeight, constantNumber, constantMotorEfficiency, pumpEfficiencyRate, irrigationAreaElectricity, cityElectricityPrice, amortizationForSelectedIrrigation))
                .add(irrigationCostForDieselPump(irrigationCountForDieselPump, specificConstantRate, pumpWorkingHour, waterAmountPerHour, waterPumpHeight, gravity, pumpMotorEfficiencyRate, irrigationArea, cityDieselPrice, irrigationLaborRateForSelectedIrrigation, workingManLaborPrice, amortizationForSelectedIrrigation));
        //TODO irrigationCountForDieselPump değerinde hata var, 4 mü 12 mi? giriş yoksa gelmeli mi? To:İbrahim Bey
    }

    public BigDecimal calculateIrrigationCost(List<PlantationProductQuestion> productQuestionList, Long parcelPlanId, City city) {
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlanId, List.of(41L, 42L));
        List<PlantationIrrigationValue> irrigationValueList = irrigationValueRepository.findByIrrigationTypeIn(EnumIrrigationType.values());

        BigDecimal amortizationForSelectedIrrigation = BigDecimal.ZERO;
        Double irrigationLaborRateForSelectedIrrigation = 0d;
        Double irrigationDieselRateForSelectedIrrigation = 0d;
        List<PlantationProductQuestion> irrigationQuestionList = productQuestionList.stream().filter(q -> List.of(148L, 149L, 150L).contains(q.getPlantationQuestion().getId())).toList();
        if (!irrigationQuestionList.isEmpty()) {
            for (PlantationProductQuestion iq : irrigationQuestionList) {
                if (iq.getSelectedAnswerId() != null) {
                    if (List.of(122L, 128L).contains(iq.getSelectedAnswerId())) {//Yağmur
                        PlantationIrrigationValue irrigationValue = irrigationValueList.stream().filter(cp -> cp.getIrrigationType().equals(EnumIrrigationType.YAGMUR_IRRIGATION_PRICE)).findFirst().get();
                        amortizationForSelectedIrrigation = irrigationValue.getPriceValue();
                        irrigationLaborRateForSelectedIrrigation = irrigationValue.getLaborValue();
                        irrigationDieselRateForSelectedIrrigation = irrigationValue.getDieselValue();
                    } else if (List.of(123L, 130L, 135L).contains(iq.getSelectedAnswerId())) {//Salma
                        PlantationIrrigationValue irrigationValue = irrigationValueList.stream().filter(cp -> cp.getIrrigationType().equals(EnumIrrigationType.SALMA_IRRIGATION_PRICE)).findFirst().get();
                        amortizationForSelectedIrrigation = irrigationValue.getPriceValue();
                        irrigationLaborRateForSelectedIrrigation = irrigationValue.getLaborValue();
                        irrigationDieselRateForSelectedIrrigation = irrigationValue.getDieselValue();
                    } else if (List.of(124L, 131L, 136L).contains(iq.getSelectedAnswerId())) {//Karık
                        PlantationIrrigationValue irrigationValue = irrigationValueList.stream().filter(cp -> cp.getIrrigationType().equals(EnumIrrigationType.KARIK_IRRIGATION_PRICE)).findFirst().get();
                        amortizationForSelectedIrrigation = irrigationValue.getPriceValue();
                        irrigationLaborRateForSelectedIrrigation = irrigationValue.getLaborValue();
                        irrigationDieselRateForSelectedIrrigation = irrigationValue.getDieselValue();
                    } else if (List.of(125L).contains(iq.getSelectedAnswerId())) {//Pivot
                        PlantationIrrigationValue irrigationValue = irrigationValueList.stream().filter(cp -> cp.getIrrigationType().equals(EnumIrrigationType.PIVOT_IRRIGATION_PRICE)).findFirst().get();
                        amortizationForSelectedIrrigation = irrigationValue.getPriceValue();
                        irrigationLaborRateForSelectedIrrigation = irrigationValue.getLaborValue();
                        irrigationDieselRateForSelectedIrrigation = irrigationValue.getDieselValue();
                    } else if (List.of(126L).contains(iq.getSelectedAnswerId())) {//Tamburlu
                        PlantationIrrigationValue irrigationValue = irrigationValueList.stream().filter(cp -> cp.getIrrigationType().equals(EnumIrrigationType.TAMBURLU_IRRIGATION_PRICE)).findFirst().get();
                        amortizationForSelectedIrrigation = irrigationValue.getPriceValue();
                        irrigationLaborRateForSelectedIrrigation = irrigationValue.getLaborValue();
                        irrigationDieselRateForSelectedIrrigation = irrigationValue.getDieselValue();
                    } else if (List.of(129L, 133L).contains(iq.getSelectedAnswerId())) {//Damla
                        PlantationIrrigationValue irrigationValue = irrigationValueList.stream().filter(cp -> cp.getIrrigationType().equals(EnumIrrigationType.DAMLA_IRRIGATION_PRICE)).findFirst().get();
                        amortizationForSelectedIrrigation = irrigationValue.getPriceValue();
                        irrigationLaborRateForSelectedIrrigation = irrigationValue.getLaborValue();
                        irrigationDieselRateForSelectedIrrigation = irrigationValue.getDieselValue();
                    } else if (List.of(134L).contains(iq.getSelectedAnswerId())) {//Mini sprey
                        PlantationIrrigationValue irrigationValue = irrigationValueList.stream().filter(cp -> cp.getIrrigationType().equals(EnumIrrigationType.MINI_SPREY_IRRIGATION_PRICE)).findFirst().get();
                        amortizationForSelectedIrrigation = irrigationValue.getPriceValue();
                        irrigationLaborRateForSelectedIrrigation = irrigationValue.getLaborValue();
                        irrigationDieselRateForSelectedIrrigation = irrigationValue.getDieselValue();
                    }
                }
            }
        }

        Integer irrigationCountPerTonne = integerValueSetter(productQuestionList, 156L);
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        Double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        Double waterAmountPerDecarePerTonne = doubleValueSetter(productQuestionList, 155L);
        BigDecimal waterPricePerTonne = decimalValueSetter(productQuestionList, 154L);
        Double irrigationCountPerDecare = doubleValueSetter(productQuestionList, 159L);
        Double pumpEfficiencyRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.PUMP_EFFICIENCY_CONSTANT)).findFirst().get().getLaborValue();
        BigDecimal cityDieselPrice = city.getDieselPrice();
        Double waterAmountPerDecare = doubleValueSetter(productQuestionList, 158L);
        BigDecimal waterPricePerDecare = decimalValueSetter(productQuestionList, 157L);
        BigDecimal waterPricePerDecareAlone = decimalValueSetter(productQuestionList, 160L);
        Double irrigationCountForElectricityPump = doubleValueSetter(productQuestionList, 168L);
        Double electricityPumpWorkingHour = doubleValueSetter(productQuestionList, 167L);
        Double electricityWaterAmountPerHour = doubleValueSetter(productQuestionList, 164L);
        Double electricityWaterPumpHeight = doubleValueSetter(productQuestionList, 165L);
        Integer constantNumber = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.CONSTANT_NUMBER)).findFirst().get().getLaborValue().intValue();
        Double constantMotorEfficiency = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MOTOR_EFFICIENCY_CONSTANT)).findFirst().get().getLaborValue();
        Double irrigationAreaElectricity = doubleValueSetter(productQuestionList, 166L);
        BigDecimal cityElectricityPrice = city.getElectricity();
        Double irrigationCountForDieselPump = 4d;//Todo
        Double specificConstantRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.IRRIGATION_SPECIFIC_CONSUMPTION_CONSTANT)).findFirst().get().getLaborValue();
        Double pumpWorkingHour = doubleValueSetter(productQuestionList, 171L);
        Double waterAmountPerHour = doubleValueSetter(productQuestionList, 169L);
        Double waterPumpHeight = doubleValueSetter(productQuestionList, 170L);
        Double gravity = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.GRAVITY)).findFirst().get().getLaborValue();
        Double pumpMotorEfficiencyRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.PUMP_MOTOR_EFFICIENCY)).findFirst().get().getLaborValue();
        Double irrigationArea = doubleValueSetter(productQuestionList, 172L);

        return irrigationTotalCost(irrigationCountPerTonne, irrigationDieselRateForSelectedIrrigation, irrigationLaborRateForSelectedIrrigation, maleDailyWage, workHoursPerDay, waterAmountPerDecarePerTonne, waterPricePerTonne, amortizationForSelectedIrrigation,
                irrigationCountPerDecare, pumpEfficiencyRate, cityDieselPrice, waterAmountPerDecare, waterPricePerDecare,
                waterPricePerDecareAlone,
                irrigationCountForElectricityPump, electricityPumpWorkingHour, electricityWaterAmountPerHour, electricityWaterPumpHeight, constantNumber, constantMotorEfficiency, irrigationAreaElectricity, cityElectricityPrice,
                irrigationCountForDieselPump, specificConstantRate, pumpWorkingHour, waterAmountPerHour, waterPumpHeight, gravity, pumpMotorEfficiencyRate, irrigationArea);
    }

    //Gider - Kültürel İşler
    //3900
    private BigDecimal treePruneCost(Double treePruneAmountHour, BigDecimal workingPruneLaborPrice) {
        return BigDecimal.valueOf(treePruneAmountHour).multiply(workingPruneLaborPrice);
    }

    //1950
    private BigDecimal vinePruneCost(Double vinePruneAmountHour, BigDecimal workingPruneLaborPrice) {
        return BigDecimal.valueOf(vinePruneAmountHour).multiply(workingPruneLaborPrice);
    }

    //2600
    private BigDecimal rejuvenationPruneCost(Double rejuvenationPruneAmountHour, BigDecimal workingPruneLaborPrice) {
        return BigDecimal.valueOf(rejuvenationPruneAmountHour).multiply(workingPruneLaborPrice);
    }

    //1300
    private BigDecimal winterPruneCost(Double winterPruneAmountHour, BigDecimal workingPruneLaborPrice) {
        return BigDecimal.valueOf(winterPruneAmountHour).multiply(workingPruneLaborPrice);
    }

    //750
    private BigDecimal summerPruneCost(Double summerPruneAmountHour, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(summerPruneAmountHour).multiply(workingManLaborPrice);
    }

    //1250
    private BigDecimal basalPruneCost(Double basalPruneAmountHour, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(basalPruneAmountHour).multiply(workingManLaborPrice);
    }

    //2300
    private BigDecimal thinningPruneCost(Double thinningPruneAmountHour, BigDecimal workingMixedLaborPrice) {
        return BigDecimal.valueOf(thinningPruneAmountHour).multiply(workingMixedLaborPrice);
    }

    //3140
    private BigDecimal pullPoleCostPerDecare(Double polePullLaborHour, BigDecimal workingMixedLaborPrice,
                                             Double polesAmountPerDecare, Double poleLifeCycleRate, BigDecimal polePricePerUnit) {
        return BigDecimal.valueOf(polePullLaborHour).multiply(workingMixedLaborPrice)
                .add(BigDecimal.valueOf(polesInputAmount(polesAmountPerDecare, poleLifeCycleRate)).multiply(polePricePerUnit));
    }

    //700
    private BigDecimal plantVineCostPerDecare(Double plantVineHourPerDecare, BigDecimal workingManLaborPrice,
                                              Double plantVineInputAmount, BigDecimal vinePricePerUnit) {
        return BigDecimal.valueOf(plantVineHourPerDecare).multiply(workingManLaborPrice)
                .add(BigDecimal.valueOf(plantVineInputAmount).multiply(vinePricePerUnit));
    }

    //3100
    private BigDecimal nettingCostPerDecare(Double nettingLaborHourPerDecare, Double nettingLifeAmount, BigDecimal workingManLaborPrice,
                                            Double nettingInputAmount, BigDecimal nettingInputPrice) {
        if (nettingLifeAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(nettingLaborAmountPerDecare(nettingLaborHourPerDecare, nettingLifeAmount)).multiply(workingManLaborPrice)
                .add(BigDecimal.valueOf(nettingInputAmountPerDecare(nettingInputAmount, nettingLifeAmount)).multiply(nettingInputPrice));
    }

    //500
    private BigDecimal bendingRopeCostPerDecare(Double bendingRopeHourPerDecare, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(bendingRopeHourPerDecare).multiply(workingManLaborPrice);
    }

    //Gider - Kültürel İşler
    //21490
    private BigDecimal culturalWorkTotalCost(Double treePruneAmountHour, BigDecimal pruneDailyWage, Double workHoursPerDay,
                                             Double vinePruneAmountHour,
                                             Double rejuvenationPruneAmountHour,
                                             Double winterPruneAmountHour,
                                             Double summerPruneAmountHour, BigDecimal maleDailyWage,
                                             Double basalPruneAmountHour,
                                             Double thinningPruneAmountHour, Double maleCostRate, BigDecimal femaleDailyWage, Double femaleCostRate,
                                             Double polePullLaborHour, Double polesAmountPerDecare, Double poleLifeCycleRate, BigDecimal polePricePerUnit,
                                             Double plantVineHourPerDecare, Double plantVineInputAmount, BigDecimal vinePricePerUnit,
                                             Double nettingLaborHourPerDecare, Double nettingLifeAmount, Double nettingInputAmount, BigDecimal nettingInputPrice,
                                             Double bendingRopeHourPerDecare) {
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);
        BigDecimal workingPruneLaborPrice = workingPruneLaborPrice(pruneDailyWage, workHoursPerDay);
        BigDecimal workingMixedLaborPrice = workingMixedLaborPrice(maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay);

        return treePruneCost(treePruneAmountHour, workingPruneLaborPrice)
                .add(vinePruneCost(vinePruneAmountHour, workingPruneLaborPrice))
                .add(rejuvenationPruneCost(rejuvenationPruneAmountHour, workingPruneLaborPrice))
                .add(winterPruneCost(winterPruneAmountHour, workingPruneLaborPrice))
                .add(summerPruneCost(summerPruneAmountHour, workingManLaborPrice))
                .add(basalPruneCost(basalPruneAmountHour, workingManLaborPrice))
                .add(thinningPruneCost(thinningPruneAmountHour, workingMixedLaborPrice))
                .add(pullPoleCostPerDecare(polePullLaborHour, workingMixedLaborPrice, polesAmountPerDecare, poleLifeCycleRate, polePricePerUnit))
                .add(plantVineCostPerDecare(plantVineHourPerDecare, workingManLaborPrice, plantVineInputAmount, vinePricePerUnit))
                .add(nettingCostPerDecare(nettingLaborHourPerDecare, nettingLifeAmount, workingManLaborPrice, nettingInputAmount, nettingInputPrice))
                .add(bendingRopeCostPerDecare(bendingRopeHourPerDecare, workingManLaborPrice));
    }

    public BigDecimal calculateCulturalWorkCost(List<PlantationProductQuestion> productQuestionList, Long parcelPlanId, City city) {
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlanId, List.of(41L, 42L, 43L));

        Double treePruneAmountHour = doubleValueSetter(productQuestionList, 175L);
        BigDecimal pruneDailyWage = decimalAnswerSetter(previousAnswerList, 43L);
        Double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        Double vinePruneAmountHour = doubleValueSetter(productQuestionList, 176L);
        Double rejuvenationPruneAmountHour = doubleValueSetter(productQuestionList, 177L);
        Double winterPruneAmountHour = doubleValueSetter(productQuestionList, 178L);
        Double summerPruneAmountHour = doubleValueSetter(productQuestionList, 179L);
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        Double basalPruneAmountHour = doubleValueSetter(productQuestionList, 180L);
        Double thinningPruneAmountHour = doubleValueSetter(productQuestionList, 181L);
        Double maleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        BigDecimal femaleDailyWage = decimalAnswerSetter(previousAnswerList, 42L);
        Double femaleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.FEMALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        Double polePullLaborHour = doubleValueSetter(productQuestionList, 184L);
        Double polesAmountPerDecare = doubleValueSetter(productQuestionList, 182L);
        Double poleLifeCycleRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.POLE_SERVICE_LIFE)).findFirst().get().getLaborValue();
        BigDecimal polePricePerUnit = decimalValueSetter(productQuestionList, 183L);
        Double plantVineHourPerDecare = doubleValueSetter(productQuestionList, 187L);
        Double plantVineInputAmount = doubleValueSetter(productQuestionList, 185L);
        BigDecimal vinePricePerUnit = decimalValueSetter(productQuestionList, 186L);
        Double nettingLaborHourPerDecare = doubleValueSetter(productQuestionList, 191L);
        Double nettingLifeAmount = doubleValueSetter(productQuestionList, 192L);
        Double nettingInputAmount = doubleValueSetter(productQuestionList, 189L);
        BigDecimal nettingInputPrice = decimalValueSetter(productQuestionList, 190L);
        Double bendingRopeHourPerDecare = doubleValueSetter(productQuestionList, 193L);

        return culturalWorkTotalCost(treePruneAmountHour, pruneDailyWage, workHoursPerDay,
                vinePruneAmountHour,
                rejuvenationPruneAmountHour,
                winterPruneAmountHour,
                summerPruneAmountHour, maleDailyWage,
                basalPruneAmountHour,
                thinningPruneAmountHour, maleCostRate, femaleDailyWage, femaleCostRate,
                polePullLaborHour, polesAmountPerDecare, poleLifeCycleRate, polePricePerUnit,
                plantVineHourPerDecare, plantVineInputAmount, vinePricePerUnit,
                nettingLaborHourPerDecare, nettingLifeAmount, nettingInputAmount, nettingInputPrice,
                bendingRopeHourPerDecare);
    }

    //Gider- Bitki Koruma
    //29,44297165
    private BigDecimal fungalDiseasesCostPerDecare(Integer foliarForFungalCount, BigDecimal productFungalMedicinePricePerUnit) {
        return BigDecimal.valueOf(foliarForFungalCount).multiply(productFungalMedicinePricePerUnit);
    }

    //27,41816933
    private BigDecimal insectDiseasesCostPerDecare(Integer medicineForInsectCount, BigDecimal productInsectMedicinePricePerUnit) {
        return BigDecimal.valueOf(medicineForInsectCount).multiply(productInsectMedicinePricePerUnit);
    }

    //14,33626667
    private BigDecimal redSpiderDiseasesCostPerDecare(Integer medicineForRedSpiderCount, BigDecimal productRedSpiderMedicinePricePerUnit) {
        return BigDecimal.valueOf(medicineForRedSpiderCount).multiply(productRedSpiderMedicinePricePerUnit);
    }

    //0
    private BigDecimal bordeauxDiseasesCostPerDecare(Integer medicineForBordeauxCount, BigDecimal productBordeauxMedicinePricePerUnit) {
        return BigDecimal.valueOf(medicineForBordeauxCount).multiply(productBordeauxMedicinePricePerUnit);
    }

    //139
    private BigDecimal hormoneCostPerDecare(Integer medicineForHormoneCount, BigDecimal productHormonePricePerUnit) {
        return BigDecimal.valueOf(medicineForHormoneCount).multiply(productHormonePricePerUnit);
    }

    //0
    private BigDecimal cottonDefoliantCostPerDecare(Integer cottonDefoliantCount, BigDecimal productCottonDefoliantPricePerUnit) {
        return BigDecimal.valueOf(cottonDefoliantCount).multiply(productCottonDefoliantPricePerUnit);
    }

    //945
    private BigDecimal machineMedicineCostPerDecare(Integer machineMedicineCount, Double machineMedicineDieselRate, BigDecimal cityDieselPrice,
                                                    Double machineMedicineLaborRate, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(machineMedicineCount).multiply(
                BigDecimal.valueOf(machineMedicineDieselRate).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(machineMedicineLaborRate).multiply(workingManLaborPrice)));
    }

    //2135
    private BigDecimal backpackMedicineCostPerDecare(Integer backpackMedicineCount, Double backpackMedicineDieselRate, BigDecimal cityDieselPrice,
                                                     Double backpackMedicineLaborRate, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(backpackMedicineCount).multiply(
                BigDecimal.valueOf(backpackMedicineDieselRate).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(backpackMedicineLaborRate).multiply(workingManLaborPrice)));
    }

    //37,5
    private BigDecimal irrigationMedicineCostPerDecare(Integer irrigationMedicineCount, Double irrigationMedicineDieselRate, BigDecimal cityDieselPrice,
                                                       Double irrigationMedicineLaborRate, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(irrigationMedicineCount).multiply(
                BigDecimal.valueOf(irrigationMedicineDieselRate).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(irrigationMedicineLaborRate).multiply(workingManLaborPrice)));
    }

    //800
    private BigDecimal droneMedicineCostPerDecare(Integer droneMedicineCount, BigDecimal droneMedicineRentalPricePerDecare) {
        return BigDecimal.valueOf(droneMedicineCount).multiply(droneMedicineRentalPricePerDecare);
    }

    //Gider- Bitki Koruma
    //4127,697408
    private BigDecimal protectionTotalCost(Integer foliarForFungalCount, BigDecimal productFungalMedicinePricePerUnit,
                                           Integer medicineForInsectCount, BigDecimal productInsectMedicinePricePerUnit,
                                           Integer medicineForRedSpiderCount, BigDecimal productRedSpiderMedicinePricePerUnit,
                                           Integer medicineForBordeauxCount, BigDecimal productBordeauxMedicinePricePerUnit,
                                           Integer medicineForHormoneCount, BigDecimal productHormonePricePerUnit,
                                           Integer cottonDefoliantCount, BigDecimal productCottonDefoliantPricePerUnit,
                                           Integer machineMedicineCount, Double machineMedicineDieselRate, BigDecimal cityDieselPrice, Double machineMedicineLaborRate, BigDecimal maleDailyWage, Double workHoursPerDay,
                                           Integer backpackMedicineCount, Double backpackMedicineDieselRate, Double backpackMedicineLaborRate,
                                           Integer irrigationMedicineCount, Double irrigationMedicineDieselRate, Double irrigationMedicineLaborRate,
                                           Integer droneMedicineCount, BigDecimal droneMedicineRentalPricePerDecare) {
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);

        return fungalDiseasesCostPerDecare(foliarForFungalCount, productFungalMedicinePricePerUnit)
                .add(insectDiseasesCostPerDecare(medicineForInsectCount, productInsectMedicinePricePerUnit))
                .add(redSpiderDiseasesCostPerDecare(medicineForRedSpiderCount, productRedSpiderMedicinePricePerUnit))
                .add(bordeauxDiseasesCostPerDecare(medicineForBordeauxCount, productBordeauxMedicinePricePerUnit))
                .add(hormoneCostPerDecare(medicineForHormoneCount, productHormonePricePerUnit))
                .add(cottonDefoliantCostPerDecare(cottonDefoliantCount, productCottonDefoliantPricePerUnit))
                .add(machineMedicineCostPerDecare(machineMedicineCount, machineMedicineDieselRate, cityDieselPrice, machineMedicineLaborRate, workingManLaborPrice))
                .add(backpackMedicineCostPerDecare(backpackMedicineCount, backpackMedicineDieselRate, cityDieselPrice, backpackMedicineLaborRate, workingManLaborPrice))
                .add(irrigationMedicineCostPerDecare(irrigationMedicineCount, irrigationMedicineDieselRate, cityDieselPrice, irrigationMedicineLaborRate, workingManLaborPrice))
                .add(droneMedicineCostPerDecare(droneMedicineCount, droneMedicineRentalPricePerDecare));
    }

    public BigDecimal calculatePlantProtectionCost(List<PlantationProductQuestion> productQuestionList, Long parcelPlanId, City city) {
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlanId, List.of(41L));

        Integer foliarForFungalCount = integerValueSetter(productQuestionList, 195L);
        BigDecimal productFungalMedicinePricePerUnit = BigDecimal.valueOf(5.88859433);//TODO
        Integer medicineForInsectCount = integerValueSetter(productQuestionList, 197L);
        BigDecimal productInsectMedicinePricePerUnit = BigDecimal.valueOf(3.916881333);//TODO
        Integer medicineForRedSpiderCount = integerValueSetter(productQuestionList, 198L);
        BigDecimal productRedSpiderMedicinePricePerUnit = BigDecimal.valueOf(14.33626667);//TODO
        Integer medicineForBordeauxCount = integerValueSetter(productQuestionList, 199L);
        BigDecimal productBordeauxMedicinePricePerUnit = BigDecimal.ZERO; //TODO
        Integer medicineForHormoneCount = integerValueSetter(productQuestionList, 200L);
        BigDecimal productHormonePricePerUnit = BigDecimal.valueOf(139);//TODO
        Integer cottonDefoliantCount = integerValueSetter(productQuestionList, 201L);
        BigDecimal productCottonDefoliantPricePerUnit = BigDecimal.ZERO;//TODO
        Integer machineMedicineCount = integerValueSetter(productQuestionList, 203L);
        Double machineMedicineDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MACHINE_SPRAYING)).findFirst().get().getDieselValue();
        BigDecimal cityDieselPrice = city.getDieselPrice();
        Double machineMedicineLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MACHINE_SPRAYING)).findFirst().get().getLaborValue();
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        Double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        Integer backpackMedicineCount = integerValueSetter(productQuestionList, 204L);
        Double backpackMedicineDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.BACKPACK_SPRAYING)).findFirst().get().getDieselValue();
        Double backpackMedicineLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.BACKPACK_SPRAYING)).findFirst().get().getLaborValue();
        Integer irrigationMedicineCount = integerValueSetter(productQuestionList, 205L);
        Double irrigationMedicineDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.IRRIGATION_SPRAYING)).findFirst().get().getDieselValue();
        Double irrigationMedicineLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.IRRIGATION_SPRAYING)).findFirst().get().getLaborValue();
        Integer droneMedicineCount = integerValueSetter(productQuestionList, 206L);
        BigDecimal droneMedicineRentalPricePerDecare = decimalValueSetter(productQuestionList, 207L);

        return protectionTotalCost(foliarForFungalCount, productFungalMedicinePricePerUnit,
                medicineForInsectCount, productInsectMedicinePricePerUnit,
                medicineForRedSpiderCount, productRedSpiderMedicinePricePerUnit,
                medicineForBordeauxCount, productBordeauxMedicinePricePerUnit,
                medicineForHormoneCount, productHormonePricePerUnit,
                cottonDefoliantCount, productCottonDefoliantPricePerUnit,
                machineMedicineCount, machineMedicineDieselRate, cityDieselPrice, machineMedicineLaborRate, maleDailyWage, workHoursPerDay,
                backpackMedicineCount, backpackMedicineDieselRate, backpackMedicineLaborRate,
                irrigationMedicineCount, irrigationMedicineDieselRate, irrigationMedicineLaborRate,
                droneMedicineCount, droneMedicineRentalPricePerDecare);
    }

    //Gider- Hasat
    //881,7
    private BigDecimal harvestAndPackingCostPerDay(Integer harvestCount,
                                                   Double averageYieldAsKgPerDecare, Double byHandHarvestAmountPerDay,
                                                   BigDecimal workingMixedLaborPrice) {
        if (byHandHarvestAmountPerDay == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(harvestAndPackingLaborAmountPerDay(averageYieldAsKgPerDecare, byHandHarvestAmountPerDay))
                        .multiply(workingMixedLaborPrice));
    }

    //958,3
    private BigDecimal harvestAndCleanCostPerDay(Integer harvestCount,
                                                 Double mainProductKgYieldAsUnitPerDecare, Double byMachineHarvestAmountPerDay,
                                                 BigDecimal workingMixedLaborPrice) {
        if (byMachineHarvestAmountPerDay == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(harvestAndCleanLaborAmountPerDay(mainProductKgYieldAsUnitPerDecare, byMachineHarvestAmountPerDay))
                        .multiply(workingMixedLaborPrice));
    }

    //661,3
    private BigDecimal shakeAndCrateCostPerDay(Integer harvestCount,
                                               Double averageYieldAsKgPerDecare, Double shakeAndCrateInputAmount,
                                               BigDecimal workingMixedLaborPrice) {
        if (shakeAndCrateInputAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(shakeAndCrateLaborAmountPerDay(averageYieldAsKgPerDecare, shakeAndCrateInputAmount))
                        .multiply(workingMixedLaborPrice));
    }

    //529,0
    private BigDecimal cutAndBindCostPerDay(Integer harvestCount,
                                            Double averageYieldAsKgPerDecare, Double cutAndBindInputAmount,
                                            BigDecimal workingMixedLaborPrice) {
        if (cutAndBindInputAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(cutAndBindLaborAmountPerDay(averageYieldAsKgPerDecare, cutAndBindInputAmount))
                        .multiply(workingMixedLaborPrice));
    }

    //587,8
    private BigDecimal cutAndLoadCostPerDay(Integer harvestCount,
                                            Double averageYieldAsKgPerDecare, Double cutAndLoadInputAmount,
                                            BigDecimal workingMixedLaborPrice) {
        if (cutAndLoadInputAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(cutAndLoadLaborAmountPerDay(averageYieldAsKgPerDecare, cutAndLoadInputAmount))
                        .multiply(workingMixedLaborPrice));
    }

    //377,9
    private BigDecimal harvestAndLoadCostPerDay(Integer harvestCount,
                                                Double averageYieldAsKgPerDecare, Double harvestAndLoadInputAmount,
                                                BigDecimal workingMixedLaborPrice) {
        if (harvestAndLoadInputAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(harvestAndLoadLaborAmountPerDay(averageYieldAsKgPerDecare, harvestAndLoadInputAmount))
                        .multiply(workingMixedLaborPrice));
    }

    //147,6
    private BigDecimal harvestAndBindCostPerDay(Integer harvestCount,
                                                Double averageExpectedYieldAsBundlePerDecare, Double harvestAndBindInputAmount,
                                                BigDecimal workingMixedLaborPrice) {
        if (harvestAndBindInputAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(harvestAndBindLaborAmountPerDay(averageExpectedYieldAsBundlePerDecare, harvestAndBindInputAmount))
                        .multiply(workingMixedLaborPrice));
    }

    //1150,0
    private BigDecimal cutAndBindAndLoadCostPerDay(Integer harvestCount,
                                                   Double mainProductKgYieldAsUnitPerDecare, Double cutAndBindAndLoadInputAmount,
                                                   BigDecimal workingMixedLaborPrice) {
        if (cutAndBindAndLoadInputAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(cutAndBindAndLoadLaborAmountPerDay(mainProductKgYieldAsUnitPerDecare, cutAndBindAndLoadInputAmount))
                        .multiply(workingMixedLaborPrice));
    }

    //1314,3
    private BigDecimal harvestGrCostPerDay(Integer harvestCount,
                                           Double mainProductKgYieldAsGrPerDecare, Double harvestGrInputAmount,
                                           BigDecimal workingMixedLaborPrice) {
        if (harvestGrInputAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(harvestGrLaborAmountPerDay(mainProductKgYieldAsGrPerDecare, harvestGrInputAmount))
                        .multiply(workingMixedLaborPrice));
    }

    //172,5
    private BigDecimal harvestLeafCostPerDay(Integer harvestCount,
                                             Double leafProductKgYieldAsGrPerDecare, Double harvestLeafInputAmount,
                                             BigDecimal workingMixedLaborPrice) {
        if (harvestLeafInputAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(harvestLeafLaborAmountPerDay(leafProductKgYieldAsGrPerDecare, harvestLeafInputAmount))
                        .multiply(workingMixedLaborPrice));
    }

    //380,0
    private BigDecimal harvesterRentalCostPerDecare(Integer harvestCount, BigDecimal harvesterRentalPricePerDecare) {
        return BigDecimal.valueOf(harvestCount).multiply(harvesterRentalPricePerDecare);
    }

    //890,0
    private BigDecimal demolitionRentalCostPerDecare(Integer harvestCount, BigDecimal demolitionRentalPricePerDecare) {
        return BigDecimal.valueOf(harvestCount).multiply(demolitionRentalPricePerDecare);
    }

    //1550,0
    private BigDecimal cottonRentalCostPerDecare(Integer harvestCount, BigDecimal cottonRentalPricePerDecare) {
        return BigDecimal.valueOf(harvestCount).multiply(cottonRentalPricePerDecare);
    }

    //1300,0
    private BigDecimal silageRentalCostPerDecare(Integer harvestCount, BigDecimal silageRentalPricePerDecare) {
        return BigDecimal.valueOf(harvestCount).multiply(silageRentalPricePerDecare);
    }

    //500,0
    private BigDecimal harvestRentalCostPerDecare(Integer harvestCount, BigDecimal harvestRentalPricePerDecare) {
        return BigDecimal.valueOf(harvestCount).multiply(harvestRentalPricePerDecare);
    }

    //450,0
    private BigDecimal seederRentalCostPerDecare(Integer harvestCount, BigDecimal seederRentalPricePerDecare) {
        return BigDecimal.valueOf(harvestCount).multiply(seederRentalPricePerDecare);
    }

    //611,6
    private BigDecimal harvestAndMachineCostPerDecare(Integer harvestCount,
                                                      Double havestAndMachineDieselInputAmount, BigDecimal cityDieselPrice,
                                                      Double averageYieldAsKgPerDecare, Double harvestAndMachineInputAmount, BigDecimal workingMixedLaborPrice) {
        if (harvestAndMachineInputAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(havestAndMachineDieselInputAmount).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(harvestAndMachineLaborAmountPerDay(averageYieldAsKgPerDecare, harvestAndMachineInputAmount)).multiply(workingMixedLaborPrice)));
    }

    //1181,7
    private BigDecimal machineShakingCostPerDecare(Integer harvestCount,
                                                   Double machineShakingDieselInputAmount, BigDecimal cityDieselPrice,
                                                   Double averageYieldAsKgPerDecare, Double machineShakingInputAmount, BigDecimal workingMixedLaborPrice) {
        if (machineShakingInputAmount == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(machineShakingDieselInputAmount).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(machineShakingLaborAmountPerDay(averageYieldAsKgPerDecare, machineShakingInputAmount)).multiply(workingMixedLaborPrice)));
    }

    //2065,0
    private BigDecimal uprootAndHarvestCostPerDecare(Integer harvestCount,
                                                     Double uprootDieselAmountPerDecare, BigDecimal cityDieselPrice,
                                                     Double uprootHarvestLaborAmountPerDecare, BigDecimal workingMixedLaborPrice) {
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(uprootDieselAmountPerDecare).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(uprootHarvestLaborAmountPerDecare).multiply(workingMixedLaborPrice)));
    }

    //1700
    private BigDecimal lumpSumCostPerDecare(Integer harvestCount, BigDecimal lumpSumCostPerTonne) {
        return BigDecimal.valueOf(harvestCount).multiply(lumpSumCostPerTonne);
    }

    //2500
    private BigDecimal lumpSumHarvestCostPerDecare(Integer harvestCount, BigDecimal lumpSumHarvestCostPerTonne) {
        return BigDecimal.valueOf(harvestCount).multiply(lumpSumHarvestCostPerTonne);
    }

    //937,5
    private BigDecimal mowingCostPerDecare(Integer harvestCount,
                                           Double mowingDieselAmountPerDecare, BigDecimal cityDieselPrice,
                                           Double mowingLaborAmountPerDecare, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(mowingDieselAmountPerDecare).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(mowingLaborAmountPerDecare).multiply(workingManLaborPrice)));
    }

    //950,0
    private BigDecimal motorizedCuttingCostPerDecare(Integer harvestCount,
                                                     Double motorizedCuttingDieselAmount, BigDecimal cityDieselPrice,
                                                     Double motorizedCuttingLaborAmount, BigDecimal workingPruneLaborPrice) {
        return BigDecimal.valueOf(harvestCount).multiply(
                BigDecimal.valueOf(motorizedCuttingDieselAmount).multiply(cityDieselPrice)
                        .add(BigDecimal.valueOf(motorizedCuttingLaborAmount).multiply(workingPruneLaborPrice)));
    }

    //Gider- Hasat
    //21796,0
    private BigDecimal harvestTotalCost(Integer harvestCount, Double averageYieldAsKgPerDecare, Double byHandHarvestAmountPerDay, BigDecimal maleDailyWage, Double maleCostRate, BigDecimal femaleDailyWage, Double femaleCostRate, Double workHoursPerDay,
                                        Double byMachineHarvestAmountPerDay, Double mainProductKgYieldAsUnitPerDecare,
                                        Double shakeAndCrateInputAmount,
                                        Double cutAndBindInputAmount,
                                        Double cutAndLoadInputAmount,
                                        Double harvestAndLoadInputAmount,
                                        Double averageExpectedYieldAsBundlePerDecare, Double harvestAndBindInputAmount,
                                        Double cutAndBindAndLoadInputAmount,
                                        Double mainProductKgYieldAsGrPerDecare, Double harvestGrInputAmount,
                                        Double leafProductKgYieldAsGrPerDecare, Double harvestLeafInputAmount,
                                        BigDecimal harvesterRentalPricePerDecare,
                                        BigDecimal demolitionRentalPricePerDecare,
                                        BigDecimal cottonRentalPricePerDecare,
                                        BigDecimal silageRentalPricePerDecare,
                                        BigDecimal harvestRentalPricePerDecare,
                                        BigDecimal seederRentalPricePerDecare,
                                        Double havestAndMachineDieselInputAmount, BigDecimal cityDieselPrice, Double harvestAndMachineInputAmount,
                                        Double machineShakingDieselInputAmount, Double machineShakingInputAmount,
                                        Double uprootDieselAmountPerDecare, Double uprootHarvestLaborAmountPerDecare,
                                        BigDecimal lumpSumCostPerTonne,
                                        BigDecimal lumpSumHarvestCostPerTonne,
                                        Double mowingDieselAmountPerDecare, Double mowingLaborAmountPerDecare,
                                        Double motorizedCuttingDieselAmount, Double motorizedCuttingLaborAmount, BigDecimal pruneDailyWage) {

        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);
        BigDecimal workingPruneLaborPrice = workingPruneLaborPrice(pruneDailyWage, workHoursPerDay);
        BigDecimal workingMixedLaborPrice = workingMixedLaborPrice(maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay);

        return harvestAndPackingCostPerDay(harvestCount, averageYieldAsKgPerDecare, byHandHarvestAmountPerDay, workingMixedLaborPrice)
                .add(harvestAndCleanCostPerDay(harvestCount, mainProductKgYieldAsUnitPerDecare, byMachineHarvestAmountPerDay, workingMixedLaborPrice))
                .add(shakeAndCrateCostPerDay(harvestCount, averageYieldAsKgPerDecare, shakeAndCrateInputAmount, workingMixedLaborPrice))
                .add(cutAndBindCostPerDay(harvestCount, averageYieldAsKgPerDecare, cutAndBindInputAmount, workingMixedLaborPrice))
                .add(cutAndLoadCostPerDay(harvestCount, averageYieldAsKgPerDecare, cutAndLoadInputAmount, workingMixedLaborPrice))
                .add(harvestAndLoadCostPerDay(harvestCount, averageYieldAsKgPerDecare, harvestAndLoadInputAmount, workingMixedLaborPrice))
                .add(harvestAndBindCostPerDay(harvestCount, averageExpectedYieldAsBundlePerDecare, harvestAndBindInputAmount, workingMixedLaborPrice))
                .add(cutAndBindAndLoadCostPerDay(harvestCount, mainProductKgYieldAsUnitPerDecare, cutAndBindAndLoadInputAmount, workingMixedLaborPrice))
                .add(harvestGrCostPerDay(harvestCount, mainProductKgYieldAsGrPerDecare, harvestGrInputAmount, workingMixedLaborPrice))
                .add(harvestLeafCostPerDay(harvestCount, leafProductKgYieldAsGrPerDecare, harvestLeafInputAmount, workingMixedLaborPrice))
                .add(harvesterRentalCostPerDecare(harvestCount, harvesterRentalPricePerDecare))
                .add(demolitionRentalCostPerDecare(harvestCount, demolitionRentalPricePerDecare))
                .add(cottonRentalCostPerDecare(harvestCount, cottonRentalPricePerDecare))
                .add(silageRentalCostPerDecare(harvestCount, silageRentalPricePerDecare))
                .add(harvestRentalCostPerDecare(harvestCount, harvestRentalPricePerDecare))
                .add(seederRentalCostPerDecare(harvestCount, seederRentalPricePerDecare))
                .add(harvestAndMachineCostPerDecare(harvestCount, havestAndMachineDieselInputAmount, cityDieselPrice, averageYieldAsKgPerDecare, harvestAndMachineInputAmount, workingMixedLaborPrice))
                .add(machineShakingCostPerDecare(harvestCount, machineShakingDieselInputAmount, cityDieselPrice, averageYieldAsKgPerDecare, machineShakingInputAmount, workingMixedLaborPrice))
                .add(uprootAndHarvestCostPerDecare(harvestCount, uprootDieselAmountPerDecare, cityDieselPrice, uprootHarvestLaborAmountPerDecare, workingMixedLaborPrice))
                .add(lumpSumCostPerDecare(harvestCount, lumpSumCostPerTonne))
                .add(lumpSumHarvestCostPerDecare(harvestCount, lumpSumHarvestCostPerTonne))
                .add(mowingCostPerDecare(harvestCount, mowingDieselAmountPerDecare, cityDieselPrice, mowingLaborAmountPerDecare, workingManLaborPrice))
                .add(motorizedCuttingCostPerDecare(harvestCount, motorizedCuttingDieselAmount, cityDieselPrice, motorizedCuttingLaborAmount, workingPruneLaborPrice));
    }

    public BigDecimal calculateHarvestCost(List<PlantationProductQuestion> productQuestionList, Long parcelPlanId, City city) {
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlanId, List.of(11L, 12L, 13L, 14L, 15L, 16L, 17L, 24L, 41L, 42L, 43L));

        Integer harvestCount = integerValueSetter(productQuestionList, 209L);
        Double averageYieldAsKgPerDecare = doubleAnswerSetter(previousAnswerList, 12L);
        Double byHandHarvestAmountPerDay = doubleValueSetter(productQuestionList, 219L);
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        Double maleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        BigDecimal femaleDailyWage = decimalAnswerSetter(previousAnswerList, 42L);
        Double femaleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.FEMALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        Double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        Double byMachineHarvestAmountPerDay = doubleValueSetter(productQuestionList, 220L);
        Double mainProductKgYieldAsUnitPerDecare = doubleAnswerSetter(previousAnswerList, 15L);
        Double shakeAndCrateInputAmount = doubleValueSetter(productQuestionList, 221L);
        Double cutAndBindInputAmount = doubleValueSetter(productQuestionList, 222L);
        Double cutAndLoadInputAmount = doubleValueSetter(productQuestionList, 223L);
        Double harvestAndLoadInputAmount = doubleValueSetter(productQuestionList, 224L);
        Double averageExpectedYieldAsBundlePerDecare = doubleAnswerSetter(previousAnswerList, 16L);
        Double harvestAndBindInputAmount = doubleValueSetter(productQuestionList, 225L);
        Double cutAndBindAndLoadInputAmount = doubleValueSetter(productQuestionList, 226L);
        Double mainProductKgYieldAsGrPerDecare = doubleAnswerSetter(previousAnswerList, 13L);
        Double harvestGrInputAmount = doubleValueSetter(productQuestionList, 227L);
        Double leafProductKgYieldAsGrPerDecare = doubleAnswerSetter(previousAnswerList, 24L);
        Double harvestLeafInputAmount = doubleValueSetter(productQuestionList, 228L);
        BigDecimal harvesterRentalPricePerDecare = decimalValueSetter(productQuestionList, 229L);
        BigDecimal demolitionRentalPricePerDecare = decimalValueSetter(productQuestionList, 230L);
        BigDecimal cottonRentalPricePerDecare = decimalValueSetter(productQuestionList, 231L);
        BigDecimal silageRentalPricePerDecare = decimalValueSetter(productQuestionList, 232L);
        BigDecimal harvestRentalPricePerDecare = decimalValueSetter(productQuestionList, 233L);
        BigDecimal seederRentalPricePerDecare = decimalValueSetter(productQuestionList, 234L);
        Double havestAndMachineDieselInputAmount = doubleValueSetter(productQuestionList, 236L);
        BigDecimal cityDieselPrice = city.getDieselPrice();
        Double harvestAndMachineInputAmount = doubleValueSetter(productQuestionList, 235L);
        Double machineShakingDieselInputAmount = doubleValueSetter(productQuestionList, 238L);
        Double machineShakingInputAmount = doubleValueSetter(productQuestionList, 237L);
        Double uprootDieselAmountPerDecare = doubleValueSetter(productQuestionList, 239L);
        Double uprootHarvestLaborAmountPerDecare = doubleValueSetter(productQuestionList, 240L);
        BigDecimal lumpSumCostPerTonne = decimalValueSetter(productQuestionList, 241L);
        BigDecimal lumpSumHarvestCostPerTonne = decimalValueSetter(productQuestionList, 242L);
        Double mowingDieselAmountPerDecare = doubleValueSetter(productQuestionList, 244L);
        Double mowingLaborAmountPerDecare = doubleValueSetter(productQuestionList, 243L);
        Double motorizedCuttingDieselAmount = doubleValueSetter(productQuestionList, 245L);
        Double motorizedCuttingLaborAmount = doubleValueSetter(productQuestionList, 246L);
        BigDecimal pruneDailyWage = decimalAnswerSetter(previousAnswerList, 43L);

        return harvestTotalCost(harvestCount, averageYieldAsKgPerDecare, byHandHarvestAmountPerDay, maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay,
                byMachineHarvestAmountPerDay, mainProductKgYieldAsUnitPerDecare,
                shakeAndCrateInputAmount,
                cutAndBindInputAmount,
                cutAndLoadInputAmount,
                harvestAndLoadInputAmount,
                averageExpectedYieldAsBundlePerDecare, harvestAndBindInputAmount,
                cutAndBindAndLoadInputAmount,
                mainProductKgYieldAsGrPerDecare, harvestGrInputAmount,
                leafProductKgYieldAsGrPerDecare, harvestLeafInputAmount,
                harvesterRentalPricePerDecare,
                demolitionRentalPricePerDecare,
                cottonRentalPricePerDecare,
                silageRentalPricePerDecare,
                harvestRentalPricePerDecare,
                seederRentalPricePerDecare,
                havestAndMachineDieselInputAmount, cityDieselPrice, harvestAndMachineInputAmount,
                machineShakingDieselInputAmount, machineShakingInputAmount,
                uprootDieselAmountPerDecare, uprootHarvestLaborAmountPerDecare,
                lumpSumCostPerTonne,
                lumpSumHarvestCostPerTonne,
                mowingDieselAmountPerDecare, mowingLaborAmountPerDecare,
                motorizedCuttingDieselAmount, motorizedCuttingLaborAmount, pruneDailyWage);
    }

    //Gider- Harman
    //86,3
    private BigDecimal blendTransportCost(Double transportKmAmount, Double averageYieldAsKgPerDecare, Double tractorLoadCapacity, BigDecimal cityDieselPrice) {
        return BigDecimal.valueOf(blendTransportDieselAmount(transportKmAmount, averageYieldAsKgPerDecare, tractorLoadCapacity)).multiply(cityDieselPrice);
    }

    //239,6
    private BigDecimal blendLaborCostPerDay(Double averageYieldAsKgPerDecare, Double blendAmountPerDay, BigDecimal maleDailyWage, Double workHoursPerDay) {
        if (blendAmountPerDay == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(blendLaborAmountPerDay(averageYieldAsKgPerDecare, blendAmountPerDay)).multiply(workingManLaborPrice(maleDailyWage, workHoursPerDay));
    }

    //88,2
    private BigDecimal cureLaborCostPerDay(Double averageYieldAsKgPerDecare, Double cureAmountPerDay,
                                           BigDecimal maleDailyWage, Double maleCostRate, BigDecimal femaleDailyWage, Double femaleCostRate, Double workHoursPerDay) {
        if (cureAmountPerDay == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(cureLaborAmountPerDay(averageYieldAsKgPerDecare, cureAmountPerDay)).multiply(workingMixedLaborPrice(maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay));
    }

    //45,0
    private BigDecimal sortLaborCostPerDay(Double yieldAsGrPerDecare, Double sortAmountPerDay, BigDecimal womanDailyWage, Double workHoursPerDay) {
        if (sortAmountPerDay == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(sortLaborAmountPerDay(yieldAsGrPerDecare, sortAmountPerDay)).multiply(workingWomanLaborPrice(womanDailyWage, workHoursPerDay));
    }

    //600,0
    //368,0
    private BigDecimal blendThreshingCost(Double averageYieldAsKgPerDecare, Double blendThreshingAmountPerHour, BigDecimal threshingCostPerHour) {
        if (blendThreshingAmountPerHour == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(blendThreshingAmount(averageYieldAsKgPerDecare, blendThreshingAmountPerHour)).multiply(threshingCostPerHour);
    }

    //Gider- Harman
    //1427,0
    private BigDecimal blendTotalCost(Double transportKmAmount, Double averageYieldAsKgPerDecare, Double tractorLoadCapacity, BigDecimal cityDieselPrice,
                                      Double blendAmountPerDay, BigDecimal maleDailyWage, Double workHoursPerDay,
                                      Double cureAmountPerDay, Double maleCostRate, BigDecimal femaleDailyWage, Double femaleCostRate,
                                      Double yieldAsGrPerDecare, Double sortAmountPerDay,
                                      BigDecimal blendThreshingCostPerDecare,
                                      Double blendThreshingAmountPerHour, BigDecimal threshingCostPerHour) {
        return blendTransportCost(transportKmAmount, averageYieldAsKgPerDecare, tractorLoadCapacity, cityDieselPrice)
                .add(blendLaborCostPerDay(averageYieldAsKgPerDecare, blendAmountPerDay, maleDailyWage, workHoursPerDay))
                .add(cureLaborCostPerDay(averageYieldAsKgPerDecare, cureAmountPerDay, maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay))
                .add(sortLaborCostPerDay(yieldAsGrPerDecare, sortAmountPerDay, femaleDailyWage, workHoursPerDay))
                .add(blendThreshingCostPerDecare != null ? blendThreshingCostPerDecare : BigDecimal.ZERO)
                .add(blendThreshingCost(averageYieldAsKgPerDecare, blendThreshingAmountPerHour, threshingCostPerHour));
    }

    public BigDecimal calculateBlendCost(List<PlantationProductQuestion> productQuestionList, Long parcelPlanId, City city) {
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlanId, List.of(11L, 12L, 13L, 14L, 15L, 16L, 17L, 41L, 42L, 43L));

        Double transportKmAmount = doubleValueSetter(productQuestionList, 247L);
        Double averageYieldAsKgPerDecare = doubleAnswerSetter(previousAnswerList, 12L);
        Double tractorLoadCapacity = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.TRACTOR_CARRYING_CAPACITY)).findFirst().get().getLaborValue();
        BigDecimal cityDieselPrice = city.getDieselPrice();
        Double blendAmountPerDay = doubleValueSetter(productQuestionList, 249L);
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        Double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        Double cureAmountPerDay = doubleValueSetter(productQuestionList, 250L);
        Double maleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        BigDecimal femaleDailyWage = decimalAnswerSetter(previousAnswerList, 42L);
        Double femaleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.FEMALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        Double yieldAsGrPerDecare = doubleAnswerSetter(previousAnswerList, 13L);
        Double sortAmountPerDay = doubleValueSetter(productQuestionList, 251L);
        BigDecimal blendThreshingCostPerDecare = decimalValueSetter(productQuestionList, 253L);
        Double blendThreshingAmountPerHour = doubleValueSetter(productQuestionList, 254L);
        BigDecimal threshingCostPerHour = decimalValueSetter(productQuestionList, 255L);

        return blendTotalCost(transportKmAmount, averageYieldAsKgPerDecare, tractorLoadCapacity, cityDieselPrice,
                blendAmountPerDay, maleDailyWage, workHoursPerDay,
                cureAmountPerDay, maleCostRate, femaleDailyWage, femaleCostRate,
                yieldAsGrPerDecare, sortAmountPerDay,
                blendThreshingCostPerDecare,
                blendThreshingAmountPerHour, threshingCostPerHour);
    }

    //Gider- İşleme -kurutma
    //1851,5
    private BigDecimal processSievingWashDryCost(Double averageYieldAsKgPerDecare, Double processSievingWashDryHourAmountPerTonne, BigDecimal workingMixedLaborPrice) {
        return BigDecimal.valueOf(processSievingWashDryLaborAmount(averageYieldAsKgPerDecare, processSievingWashDryHourAmountPerTonne)).multiply(workingMixedLaborPrice);
    }

    //258,8
    private BigDecimal processSievingWashDryDieselCost(Double averageYieldAsKgPerDecare, Double processSievingWashDryDieselAmountPerTonne, BigDecimal cityDieselPrice) {
        return BigDecimal.valueOf(processSievingWashDryDieselAmount(averageYieldAsKgPerDecare, processSievingWashDryDieselAmountPerTonne)).multiply(cityDieselPrice);
    }

    //6900
    private BigDecimal processSievingWashDryCostPerBunch(Double averageExpectedYieldAsUnitPerDecare, Double processSievingWashDryLaborHourAmount, BigDecimal workingMixedLaborPrice) {
        return BigDecimal.valueOf(processSievingWashDryLaborAmountPerBunch(averageExpectedYieldAsUnitPerDecare, processSievingWashDryLaborHourAmount)).multiply(workingMixedLaborPrice);
    }

    //708,4
    private BigDecimal processSievingWashDrySeparationCost(Double averageExpectedYieldAsBundlePerDecare, Double processSievingWashDryHourAmountPer1000, BigDecimal workingMixedLaborPrice) {
        return BigDecimal.valueOf(processSievingWashDryLaborAmountSeparation(averageExpectedYieldAsBundlePerDecare, processSievingWashDryHourAmountPer1000)).multiply(workingMixedLaborPrice);
    }

    //1587,0
    private BigDecimal processSievingWashDrySulfurizeCost(Double averageYieldAsKgPerDecare, Double processSievingWashDrySulfurizeHourPerTonne, BigDecimal workingMixedLaborPrice) {
        return BigDecimal.valueOf(processSievingWashDrySulfurizeLaborAmount(averageYieldAsKgPerDecare, processSievingWashDrySulfurizeHourPerTonne)).multiply(workingMixedLaborPrice);
    }

    //1322,5
    private BigDecimal processSievingWashDryDipCost(Double averageYieldAsKgPerDecare, Double processSievingWashDryDipHourPerTonne, BigDecimal workingMixedLaborPrice) {
        return BigDecimal.valueOf(processSievingWashDryDipLaborAmount(averageYieldAsKgPerDecare, processSievingWashDryDipHourPerTonne)).multiply(workingMixedLaborPrice);
    }

    //2645,0
    private BigDecimal processSievingWashDryStringCost(Double averageYieldAsKgPerDecare, Double processSievingWashDryStringHourPerTonne, BigDecimal workingMixedLaborPrice) {
        return BigDecimal.valueOf(processSievingWashDryStringLaborAmount(averageYieldAsKgPerDecare, processSievingWashDryStringHourPerTonne)).multiply(workingMixedLaborPrice);
    }

    //958,3
    private BigDecimal processSievingWashDryProcessCost(Double averageExpectedYieldAsUnitPerDecare, Double processSievingWashDryProcessPerDay, BigDecimal workingMixedLaborPrice) {
        if (processSievingWashDryProcessPerDay == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(processSievingWashDryProcessLabor(averageExpectedYieldAsUnitPerDecare, processSievingWashDryProcessPerDay)).multiply(workingMixedLaborPrice);
    }

    //575
    private BigDecimal processSievingWashDryMachineCost(Double processSievingWashDryLumpSumAmount, BigDecimal processDryMachinePricePerTonne) {
        return BigDecimal.valueOf(processSievingWashDryLumpSumAmount).multiply(processDryMachinePricePerTonne);
    }

    //805
    private BigDecimal processSulfuringMachineCost(Double processSievingWashDryLumpSumAmount, BigDecimal processSulfuringMachinePricePerTonne) {
        return BigDecimal.valueOf(processSievingWashDryLumpSumAmount).multiply(processSulfuringMachinePricePerTonne);
    }

    //92
    private BigDecimal materialCost(Double averageYieldAsKgPerDecare, Double materialInputAmountPerTonne, BigDecimal materialUnitPrice) {
        return BigDecimal.valueOf(materialInputAmount(averageYieldAsKgPerDecare, materialInputAmountPerTonne)).multiply(materialUnitPrice);
    }

    //3703
    private BigDecimal sortSizeScoreBrineCost(Double averageYieldAsKgPerDecare, Double sortSizeScoreBrineLaborHour, BigDecimal workingMixedLaborPrice) {
        return BigDecimal.valueOf(sortSizeScoreBrineLaborAmount(averageYieldAsKgPerDecare, sortSizeScoreBrineLaborHour)).multiply(workingMixedLaborPrice);
    }

    //Gider- İşleme -kurutma
    //21406,5
    private BigDecimal processDryTotalCost(Double averageYieldAsKgPerDecare, Double processSievingWashDryHourAmountPerTonne, BigDecimal maleDailyWage, Double maleCostRate, BigDecimal femaleDailyWage, Double femaleCostRate, Double workHoursPerDay,
                                           Double processSievingWashDryDieselAmountPerTonne, BigDecimal cityDieselPrice,
                                           Double averageExpectedYieldAsUnitPerDecare, Double processSievingWashDryLaborHourAmount,
                                           Double averageExpectedYieldAsBundlePerDecare, Double processSievingWashDryHourAmountPer1000,
                                           Double processSievingWashDrySulfurizeHourPerTonne,
                                           Double processSievingWashDryDipHourPerTonne,
                                           Double processSievingWashDryStringHourPerTonne,
                                           Double processSievingWashDryProcessPerDay,
                                           BigDecimal processDryMachinePricePerTonne,
                                           BigDecimal processSulfuringMachinePricePerTonne,
                                           Double materialInputAmountPerTonne, BigDecimal materialUnitPrice,
                                           Double sortSizeScoreBrineLaborHour) {

        Double processSievingWashDryLumpSumAmount = processSievingWashDryLumpSumAmount(averageYieldAsKgPerDecare);
        BigDecimal workingMixedLaborPrice = workingMixedLaborPrice(maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay);

        return processSievingWashDryCost(averageYieldAsKgPerDecare, processSievingWashDryHourAmountPerTonne, workingMixedLaborPrice)
                .add(processSievingWashDryDieselCost(averageYieldAsKgPerDecare, processSievingWashDryDieselAmountPerTonne, cityDieselPrice))
                .add(processSievingWashDryCostPerBunch(averageExpectedYieldAsUnitPerDecare, processSievingWashDryLaborHourAmount, workingMixedLaborPrice))
                .add(processSievingWashDrySeparationCost(averageExpectedYieldAsBundlePerDecare, processSievingWashDryHourAmountPer1000, workingMixedLaborPrice))
                .add(processSievingWashDrySulfurizeCost(averageYieldAsKgPerDecare, processSievingWashDrySulfurizeHourPerTonne, workingMixedLaborPrice))
                .add(processSievingWashDryDipCost(averageYieldAsKgPerDecare, processSievingWashDryDipHourPerTonne, workingMixedLaborPrice))
                .add(processSievingWashDryStringCost(averageYieldAsKgPerDecare, processSievingWashDryStringHourPerTonne, workingMixedLaborPrice))
                .add(processSievingWashDryProcessCost(averageExpectedYieldAsUnitPerDecare, processSievingWashDryProcessPerDay, workingMixedLaborPrice))
                .add(processSievingWashDryMachineCost(processSievingWashDryLumpSumAmount, processDryMachinePricePerTonne))
                .add(processSulfuringMachineCost(processSievingWashDryLumpSumAmount, processSulfuringMachinePricePerTonne))
                .add(materialCost(averageYieldAsKgPerDecare, materialInputAmountPerTonne, materialUnitPrice))
                .add(sortSizeScoreBrineCost(averageYieldAsKgPerDecare, sortSizeScoreBrineLaborHour, workingMixedLaborPrice));
    }

    public BigDecimal calculateProcessDryCost(List<PlantationProductQuestion> productQuestionList, Long parcelPlanId, City city) {
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlanId, List.of(12L, 15L, 16L, 41L, 42L, 43L));

        Double averageYieldAsKgPerDecare = doubleAnswerSetter(previousAnswerList, 12L);
        Double processSievingWashDryHourAmountPerTonne = doubleValueSetter(productQuestionList, 259L);
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        Double maleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        BigDecimal femaleDailyWage = decimalAnswerSetter(previousAnswerList, 42L);
        Double femaleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.FEMALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        Double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        Double processSievingWashDryDieselAmountPerTonne = doubleValueSetter(productQuestionList, 260L);
        BigDecimal cityDieselPrice = city.getDieselPrice();
        Double averageExpectedYieldAsUnitPerDecare = doubleAnswerSetter(previousAnswerList, 15L);
        Double processSievingWashDryLaborHourAmount = doubleValueSetter(productQuestionList, 261L);
        Double averageExpectedYieldAsBundlePerDecare = doubleAnswerSetter(previousAnswerList, 16L);
        Double processSievingWashDryHourAmountPer1000 = doubleValueSetter(productQuestionList, 262L);
        Double processSievingWashDrySulfurizeHourPerTonne = doubleValueSetter(productQuestionList, 263L);
        Double processSievingWashDryDipHourPerTonne = doubleValueSetter(productQuestionList, 264L);
        Double processSievingWashDryStringHourPerTonne = doubleValueSetter(productQuestionList, 265L);
        Double processSievingWashDryProcessPerDay = doubleValueSetter(productQuestionList, 266L);
        BigDecimal processDryMachinePricePerTonne = decimalValueSetter(productQuestionList, 267L);
        BigDecimal processSulfuringMachinePricePerTonne = decimalValueSetter(productQuestionList, 268L);
        Double materialInputAmountPerTonne = doubleValueSetter(productQuestionList, 270L);
        BigDecimal materialUnitPrice = decimalValueSetter(productQuestionList, 271L);
        Double sortSizeScoreBrineLaborHour = doubleValueSetter(productQuestionList, 272L);

        return processDryTotalCost(averageYieldAsKgPerDecare, processSievingWashDryHourAmountPerTonne, maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay,
                processSievingWashDryDieselAmountPerTonne, cityDieselPrice,
                averageExpectedYieldAsUnitPerDecare, processSievingWashDryLaborHourAmount,
                averageExpectedYieldAsBundlePerDecare, processSievingWashDryHourAmountPer1000,
                processSievingWashDrySulfurizeHourPerTonne,
                processSievingWashDryDipHourPerTonne,
                processSievingWashDryStringHourPerTonne,
                processSievingWashDryProcessPerDay,
                processDryMachinePricePerTonne,
                processSulfuringMachinePricePerTonne,
                materialInputAmountPerTonne, materialUnitPrice,
                sortSizeScoreBrineLaborHour);
    }

    //Gider- Balyalama
    //258
    private BigDecimal balingMaterialCost(Double balingMachineDieselAmountPerDecare, BigDecimal cityDieselPrice,
                                          Double balingMachineHourAmountPerDecare, BigDecimal workingManLaborPrice,
                                          Double averageYieldAsKgPerDecare, Double balingMaterialAmountPerTonne, BigDecimal balingMaterialInputPrice) {
        return BigDecimal.valueOf(balingMachineDieselAmountPerDecare).multiply(cityDieselPrice)
                .add(BigDecimal.valueOf(balingMachineHourAmountPerDecare).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(balingMaterialInputAmount(averageYieldAsKgPerDecare, balingMaterialAmountPerTonne)).multiply(balingMaterialInputPrice));
    }

    //236,3
    private BigDecimal balingSideMaterialCost(Double balingMachineDieselAmountPerDecare, BigDecimal cityDieselPrice,
                                              Double balingMachineHourAmountPerDecare, BigDecimal workingManLaborPrice,
                                              Double averageYieldSideStrawProduct, BigDecimal balingMaterialInputPrice) {
        return BigDecimal.valueOf(balingMachineDieselAmountPerDecare).multiply(cityDieselPrice)
                .add(BigDecimal.valueOf(balingMachineHourAmountPerDecare).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(balingMaterialInputKgPriceAmount(averageYieldSideStrawProduct, balingMaterialInputPrice)).multiply(balingMaterialInputPrice));
    }

    //418,2
    private BigDecimal balingAverageCost(Double averageYieldAsKgPerDecare, Double balingAverageWeight, BigDecimal rentPricePerBaling) {
        if (balingAverageWeight == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(balingAverageInputAmount(averageYieldAsKgPerDecare, balingAverageWeight)).multiply(rentPricePerBaling);
    }

    //109,1
    private BigDecimal balingRentalCostPerBaling(Double averageYieldSideStrawProduct, Double balingAverageWeight, BigDecimal rentPricePerBaling) {
        if (balingAverageWeight == 0d) return BigDecimal.ZERO;
        return BigDecimal.valueOf(balingRentalInputAmount(averageYieldSideStrawProduct, balingAverageWeight)).multiply(rentPricePerBaling);
    }

    //Gider- Balyalama
    //1021,6
    private BigDecimal balingTotalCost(Double balingMachineDieselAmountPerDecare, BigDecimal cityDieselPrice, Double balingMachineHourAmountPerDecare, BigDecimal maleDailyWage, Double workHoursPerDay, Double averageYieldAsKgPerDecare, Double balingMaterialAmountPerTonne, BigDecimal balingMaterialInputPrice,
                                       Double averageYieldSideStrawProduct,
                                       Double balingAverageWeight, BigDecimal rentPricePerBaling) {
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);

        return balingMaterialCost(balingMachineDieselAmountPerDecare, cityDieselPrice, balingMachineHourAmountPerDecare, workingManLaborPrice, averageYieldAsKgPerDecare, balingMaterialAmountPerTonne, balingMaterialInputPrice)
                .add(balingSideMaterialCost(balingMachineDieselAmountPerDecare, cityDieselPrice, balingMachineHourAmountPerDecare, workingManLaborPrice, averageYieldSideStrawProduct, balingMaterialInputPrice))
                .add(balingAverageCost(averageYieldAsKgPerDecare, balingAverageWeight, rentPricePerBaling))
                .add(balingRentalCostPerBaling(averageYieldSideStrawProduct, balingAverageWeight, rentPricePerBaling));
    }

    public BigDecimal calculateBalingCost(List<PlantationProductQuestion> productQuestionList, Long parcelPlanId, City city) {
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlanId, List.of(12L, 20L, 41L, 42L, 43L));

        Double balingMachineDieselAmountPerDecare = doubleValueSetter(productQuestionList, 276L);
        BigDecimal cityDieselPrice = city.getDieselPrice();
        Double balingMachineHourAmountPerDecare = doubleValueSetter(productQuestionList, 277L);
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        Double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        Double averageYieldAsKgPerDecare = doubleAnswerSetter(previousAnswerList, 12L);
        Double balingMaterialAmountPerTonne = doubleValueSetter(productQuestionList, 278L);
        BigDecimal balingMaterialInputPrice = decimalValueSetter(productQuestionList, 279L);
        Double averageYieldSideStrawProduct = doubleAnswerSetter(previousAnswerList, 20L);
        Double balingAverageWeight = doubleValueSetter(productQuestionList, 280L);
        BigDecimal rentPricePerBaling = decimalValueSetter(productQuestionList, 281L);

        return balingTotalCost(balingMachineDieselAmountPerDecare, cityDieselPrice, balingMachineHourAmountPerDecare, maleDailyWage, workHoursPerDay, averageYieldAsKgPerDecare, balingMaterialAmountPerTonne, balingMaterialInputPrice,
                averageYieldSideStrawProduct,
                balingAverageWeight, rentPricePerBaling);
    }

    //Gider- Pazara Nakil Ambalaj
    //123,3
    private BigDecimal tractorTransportCost(Double averageYieldAsKgPerDecare, Double tractorCapacity, Double tractorTransportCoefficient, Double transportationDistance, BigDecimal cityDieselPrice) {
        return BigDecimal.valueOf(tractorDieselAmount(averageYieldAsKgPerDecare, tractorCapacity, tractorTransportCoefficient, transportationDistance)).multiply(cityDieselPrice);
    }

    //287,5
    private BigDecimal packagingCost(Double averageYieldAsKgPerDecare, Double weightPerUnit, BigDecimal packagingPricePerUnit) {
        return BigDecimal.valueOf(transportLumpSumAmount(averageYieldAsKgPerDecare, weightPerUnit)).multiply(packagingPricePerUnit);
    }

    //Gider- Pazara Nakil Ambalaj
    //410,8
    private BigDecimal transportPackagingTotalCost(Double averageYieldAsKgPerDecare, Double tractorCapacity, Double tractorTransportCoefficient, Double transportationDistance, BigDecimal cityDieselPrice,
                                                   Double weightPerUnit, BigDecimal packagingPricePerUnit) {
        return tractorTransportCost(averageYieldAsKgPerDecare, tractorCapacity, tractorTransportCoefficient, transportationDistance, cityDieselPrice)
                .add(packagingCost(averageYieldAsKgPerDecare, weightPerUnit, packagingPricePerUnit));
    }

    public BigDecimal calculateTransportPackagingCost(List<PlantationProductQuestion> productQuestionList, Long parcelPlanId, City city) {
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlanId, List.of(12L, 41L, 42L, 43L));

        Double averageYieldAsKgPerDecare = doubleAnswerSetter(previousAnswerList, 12L);
        Double tractorCapacity = doubleValueSetter(productQuestionList, 284L);
        Double tractorTransportCoefficient = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.CARRYING_CAPACITY)).findFirst().get().getLaborValue();
        Double transportationDistance = doubleValueSetter(productQuestionList, 283L);
        BigDecimal cityDieselPrice = city.getDieselPrice();
        Double weightPerUnit = doubleValueSetter(productQuestionList, 292L);
        BigDecimal packagingPricePerUnit = decimalValueSetter(productQuestionList, 293L);
        return transportPackagingTotalCost(averageYieldAsKgPerDecare, tractorCapacity, tractorTransportCoefficient, transportationDistance, cityDieselPrice,
                weightPerUnit, packagingPricePerUnit);
    }
}

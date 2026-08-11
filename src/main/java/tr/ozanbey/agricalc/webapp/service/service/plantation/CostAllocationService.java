package tr.ozanbey.agricalc.webapp.service.service.plantation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumAllocationType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumCoefficientType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumIrrigationType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.PlantationCoefficientRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.PlantationIrrigationValueRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.UserPlantParcelPlanAllocationRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.UserPlantParcelPlanAnswerRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.plantation.PlanAllocationResultView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CostAllocationService extends CostCommonService {

    @Autowired
    private PlantationCoefficientRepository coefficientRepository;

    @Autowired
    private UserPlantParcelPlanAnswerRepository planAnswerRepository;

    @Autowired
    private UserPlantParcelPlanAllocationRepository planAllocationRepository;

    @Autowired
    private PlantationIrrigationValueRepository irrigationValueRepository;

    public List<PlanAllocationResultView> getAllocationListByPlanId(Long parcelPlanId, EnumAllocationType[] allocationTypes) {
        return planAllocationRepository.findByPlanIdGroupByAllocationType(parcelPlanId, allocationTypes);
    }


    private int decimalValueChecker(List<PlantationProductQuestion> questionList, Long questionId) {
        Optional<PlantationProductQuestion> optional = questionList.stream()
                .filter(q -> q.getPlantationQuestion().getId().equals(questionId))
                .findAny();
        if (optional.isPresent() && optional.get().getBigDecimalValue() != null && optional.get().getBigDecimalValue().compareTo(BigDecimal.ZERO) != 0) {
            return 1;
        }
        return 0;
    }

    private int doubleValueChecker(List<PlantationProductQuestion> questionList, Long questionId) {
        Optional<PlantationProductQuestion> optional = questionList.stream()
                .filter(q -> q.getPlantationQuestion().getId().equals(questionId))
                .findAny();
        if (optional.isPresent() && optional.get().getDoubleValue() != null && optional.get().getDoubleValue() > 0d) {
            return 1;
        }
        return 0;
    }

    private int choiceValueChecker(List<PlantationProductQuestion> questionList, List<Long> questionIdList, List<Long> answerIdList) {
        int returnValue = 0;
        for (Long questionId : questionIdList) {
            Optional<PlantationProductQuestion> optional = questionList.stream()
                    .filter(q -> q.getPlantationQuestion().getId().equals(questionId))
                    .findAny();
            if (optional.isPresent() && optional.get().getSelectedAnswerId() != null && answerIdList.contains(optional.get().getSelectedAnswerId())) {
                return 1;
            }
        }
        return 0;
    }

    //7
    private Integer calculateSoilTransaction(int laserCount, int soilBlastCount,
                                             int deepPlowCount, int secondaryOpCount) {
        return laserCount + soilBlastCount + deepPlowCount + secondaryOpCount;
    }

    //7,2
    private Double calculateSoilDieselAmount(int laserCount, double laserDieselAmount,
                                             int soilBlastCount, double soilBlastDieselAmount,
                                             int deepPlowCount, double deepPlowDieselAmount,
                                             int secondaryOpCount, double secondaryOpDieselAmount) {
        return (laserCount * laserDieselAmount) + (soilBlastCount * soilBlastDieselAmount) + (deepPlowCount * deepPlowDieselAmount) + (secondaryOpCount * secondaryOpDieselAmount);
    }

    //540
    private BigDecimal calculateSoilDieselCost(double soilDieselAmount, BigDecimal cityDieselPrice) {
        return new BigDecimal(soilDieselAmount).multiply(cityDieselPrice);
    }

    //1,19
    private Double calculateSoilLaborAmount(int laserCount, double laserLaborAmount,
                                            int soilBlastCount, double soilBlastLaborAmount,
                                            int deepPlowCount, double deepPlowLaborAmount,
                                            int secondaryOpCount, double secondaryOpLaborAmount) {
        return (laserCount * laserLaborAmount) + (soilBlastCount * soilBlastLaborAmount) + (deepPlowCount * deepPlowLaborAmount) + (secondaryOpCount * secondaryOpLaborAmount);
    }

    //296,667
    private BigDecimal calculateSoilLaborCost(double soilLaborAmount, BigDecimal workingManLaborPrice) {
        return new BigDecimal(soilLaborAmount).multiply(workingManLaborPrice);
    }

    @Transactional
    public void soilPrepAllocation(List<PlantationProductQuestion> productQuestionList, UserPlantParcelPlan parcelPlan, City city) {
        List<UserPlantParcelPlanAllocation> planAllocationList = new ArrayList<>();

        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
//        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlan.getId(), List.of(41L, 42L));

        int laserCount = decimalValueChecker(productQuestionList, 44L);
        int soilBlastCount = doubleValueChecker(productQuestionList, 45L);
        int deepPlowCount = choiceValueChecker(productQuestionList, List.of(46L), List.of(27L));
        int secondaryOpCount = doubleValueSetter(productQuestionList, 47L).intValue();
        Integer transactionCount = calculateSoilTransaction(
                laserCount, soilBlastCount,
                deepPlowCount, secondaryOpCount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_SOIL, EnumAllocationType.TRANSACTION_COUNT, new BigDecimal(transactionCount)));

        double laserDieselAmount = 0d;
        double soilBlastFrequency = doubleValueSetter(productQuestionList, 45L);
        double soilBlastDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.SOIL_BLASTING)).findFirst().get().getDieselValue();
        double soilBlastDieselAmount = soilPrepBlastingEnergyAmount(soilBlastFrequency, soilBlastDieselRate);
        double deepPlowDieselAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.DEEP_PLOW)).findFirst().get().getDieselValue();
        double secondaryOpDieselAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.SECONDARY_OPERATION)).findFirst().get().getDieselValue();
        Double dieselAmount = calculateSoilDieselAmount(
                laserCount, laserDieselAmount,
                soilBlastCount, soilBlastDieselAmount,
                deepPlowCount, deepPlowDieselAmount,
                secondaryOpCount, secondaryOpDieselAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_SOIL, EnumAllocationType.DIESEL_AMOUNT, new BigDecimal(dieselAmount)));

        BigDecimal dieselCost = calculateSoilDieselCost(dieselAmount, city.getDieselPrice());
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_SOIL, EnumAllocationType.DIESEL_COST, dieselCost));

        double laserLaborAmount = 0d;
        double soilBlastLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.SOIL_BLASTING)).findFirst().get().getLaborValue();
        double soilBlastLaborAmount = soilPrepBlastingLaborAmount(soilBlastLaborRate, soilBlastFrequency);
        double deepPlowLaborAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.DEEP_PLOW)).findFirst().get().getLaborValue();
        double secondaryOpLaborAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.SECONDARY_OPERATION)).findFirst().get().getLaborValue();
        Double laborAmount = calculateSoilLaborAmount(
                laserCount, laserLaborAmount,
                soilBlastCount, soilBlastLaborAmount,
                deepPlowCount, deepPlowLaborAmount,
                secondaryOpCount, secondaryOpLaborAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_SOIL, EnumAllocationType.LABOR_AMOUNT, new BigDecimal(laborAmount)));

        BigDecimal maleDailyWage = decimalValueSetter(productQuestionList, 41L);
        double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);
        BigDecimal laborCost = calculateSoilLaborCost(laborAmount, workingManLaborPrice);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_SOIL, EnumAllocationType.LABOR_COST, laborCost));

        BigDecimal laserCost = decimalValueSetter(productQuestionList, 44L);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_SOIL, EnumAllocationType.LUMP_SUM_COST, laserCost));

        saveAllocationList(parcelPlan.getId(), EnumPlantationQuestionType.EXPENSE_SOIL, planAllocationList);
    }

    //10
    private Integer calculatePlantingTransaction(int aSeedThrowMibzer, int aSeedThrowMibzerCost,
                                                 int aSeedThrowEl, int aSeedThrowDroneCost,
                                                 int bSteelingPlantEl, int bSteelingPlantMakine,
                                                 int bSteelingPlantMakineSahipli, int cSeedlingPlant,
                                                 int cYumruPlantAmount, int cYumruPlantTimeAmount) {
        return aSeedThrowMibzer + aSeedThrowMibzerCost + aSeedThrowEl + aSeedThrowDroneCost + bSteelingPlantEl + bSteelingPlantMakine + bSteelingPlantMakineSahipli + cSeedlingPlant + cYumruPlantAmount + cYumruPlantTimeAmount;
    }

    //3,091
    private Double calculatePlantingDieselAmount(double aSeedThrowMibzerDieselAmount, double bSteelingPlantMakineDieselAmount, double bSteelingPlantMakineSahipliDieselAmount) {
        return aSeedThrowMibzerDieselAmount + bSteelingPlantMakineDieselAmount + bSteelingPlantMakineSahipliDieselAmount;
    }

    //231,806
    private BigDecimal calculatePlantingDieselCost(double soilDieselAmount, BigDecimal cityDieselPrice) {
        return new BigDecimal(soilDieselAmount).multiply(cityDieselPrice);
    }

    //50,9389
    private Double calculatePlantingLaborAmount(double aSeedThrowMibzerLaborAmount, double aSeedThrowElLaborAmount,
                                                double bSteelingPlantElLaborAmount, double cSeedlingPlantLaborAmount,
                                                double workPowerCount, double cYumruPlantLaborAmount) {
        return aSeedThrowMibzerLaborAmount + aSeedThrowElLaborAmount + bSteelingPlantElLaborAmount + cSeedlingPlantLaborAmount + workPowerCount + cYumruPlantLaborAmount;
    }

    //11574,72222
    private BigDecimal calculatePlantingLaborCost(double aSeedThrowMibzerLaborAmount, BigDecimal workingManLaborPrice,
                                                  double aSeedThrowElLaborAmount,
                                                  double bSteelingPlantElLaborAmount, BigDecimal workingWomanLaborPrice,
                                                  double cSeedlingPlantLaborAmount, BigDecimal workingMixedLaborPrice,
                                                  double workPowerCount,
                                                  double cYumruPlantLaborAmount) {
        return BigDecimal.valueOf(aSeedThrowMibzerLaborAmount).multiply(workingManLaborPrice)
                .add(BigDecimal.valueOf(aSeedThrowElLaborAmount).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(bSteelingPlantElLaborAmount).multiply(workingWomanLaborPrice))
                .add(BigDecimal.valueOf(cSeedlingPlantLaborAmount).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(workPowerCount).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(cYumruPlantLaborAmount).multiply(workingWomanLaborPrice));
    }

    //675
    private BigDecimal calculatePlantingLumpSumCost(BigDecimal plantingLumpSumPrice, BigDecimal plantingDroneLumpSumPrice, BigDecimal rentalDrillCostPerHour) {
        return plantingLumpSumPrice.add(plantingDroneLumpSumPrice).add(rentalDrillCostPerHour);
    }

    //20190
    private BigDecimal calculateSeedCost(BigDecimal plantingSeedKgCost, BigDecimal plantingSeedGrCost, BigDecimal plantingSeed1000Cost, BigDecimal plantingSeedBagCost) {
        return plantingSeedKgCost.add(plantingSeedGrCost).add(plantingSeed1000Cost).add(plantingSeedBagCost);
    }

    //3150
    private BigDecimal calculateTuberCost(double cSeedlingPlantTuberAmount, BigDecimal yumruKgPrice) {
        return BigDecimal.valueOf(cSeedlingPlantTuberAmount).multiply(yumruKgPrice);
    }

    @Transactional
    public void plantingAllocation(List<PlantationProductQuestion> productQuestionList, UserPlantParcelPlan parcelPlan, City city) {
        List<UserPlantParcelPlanAllocation> planAllocationList = new ArrayList<>();
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlan.getId(), List.of(41L, 42L));

        int aSeedThrowMibzer = choiceValueChecker(productQuestionList, List.of(64L, 65L), List.of(57L, 60L));
        int aSeedThrowMibzerCost = decimalValueChecker(productQuestionList, 66L);
        int aSeedThrowEl = doubleValueChecker(productQuestionList, 67L);
        int aSeedThrowDroneCost = decimalValueChecker(productQuestionList, 68L);
        int bSteelingPlantEl = doubleValueChecker(productQuestionList, 76L);
        int bSteelingPlantMakine = doubleValueChecker(productQuestionList, 78L);
        int bSteelingPlantMakineSahipli = doubleValueChecker(productQuestionList, 79L);
        int cSeedlingPlant = doubleValueChecker(productQuestionList, 81L);
        int cYumruPlantAmount = doubleValueChecker(productQuestionList, 85L);
        int cYumruPlantTimeAmount = doubleValueChecker(productQuestionList, 88L);
        Integer transactionCount = calculatePlantingTransaction(
                aSeedThrowMibzer, aSeedThrowMibzerCost,
                aSeedThrowEl, aSeedThrowDroneCost,
                bSteelingPlantEl, bSteelingPlantMakine,
                bSteelingPlantMakineSahipli, cSeedlingPlant,
                cYumruPlantAmount, cYumruPlantTimeAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PLANTING, EnumAllocationType.TRANSACTION_COUNT, new BigDecimal(transactionCount)));

        double seederEnergyPerDecare = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.DRILL_PLANTING)).findFirst().get().getDieselValue();
        double seedUsageYear = doubleValueSetter(productQuestionList, 69L);
        double aSeedThrowMibzerDieselAmount = plantingYearEnergyAmount(seedUsageYear, seederEnergyPerDecare);
        double plantingSeedlingPerDecare = doubleValueSetter(productQuestionList, 71L);
        double drillPlantingPerHour = doubleValueSetter(productQuestionList, 78L);
        double bSteelingPlantMakineDieselAmount = drillPlantingPerHour == 0d ? 0d : plantingDrillEnergyAmountPerHour(plantingSeedlingPerDecare, drillPlantingPerHour);
        double dieselAmountPerHour = doubleValueSetter(productQuestionList, 79L);
        int seedlingUsageYear = integerValueSetter(productQuestionList, 84L);
        double bSteelingPlantMakineSahipliDieselAmount = drillPlantingPerHour == 0d ? 0d : plantingDrillDieselAmountPerHour(plantingSeedlingPerDecare, drillPlantingPerHour, dieselAmountPerHour, seedlingUsageYear);
        Double dieselAmount = calculatePlantingDieselAmount(
                aSeedThrowMibzerDieselAmount,
                bSteelingPlantMakineDieselAmount,
                bSteelingPlantMakineSahipliDieselAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PLANTING, EnumAllocationType.DIESEL_AMOUNT, new BigDecimal(dieselAmount)));

        BigDecimal dieselCost = calculatePlantingDieselCost(
                dieselAmount,
                city.getDieselPrice());
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PLANTING, EnumAllocationType.DIESEL_COST, dieselCost));

        double seederLaborPerDecare = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.DRILL_PLANTING)).findFirst().get().getLaborValue();
        double aSeedThrowMibzerLaborAmount = plantingYearLaborAmount(seedUsageYear, seederLaborPerDecare);
        double seedHandHourPerDecare = doubleValueSetter(productQuestionList, 67L);
        double aSeedThrowElLaborAmount = seedUsageYear == 0d ? 0d : plantingHandLaborAmount(seedHandHourPerDecare, seedUsageYear);
        double averagePlantingHandPerPerson = doubleValueSetter(productQuestionList, 76L);
        double bSteelingPlantElLaborAmount = plantingSeedlingHandLaborAmount(plantingSeedlingPerDecare, averagePlantingHandPerPerson, seedlingUsageYear);
        double seedToFideAmountPerDecare = doubleValueSetter(productQuestionList, 81L);
        double seedToFideWorkAmount = doubleValueSetter(productQuestionList, 83L);
        double cSeedlingPlantLaborAmount = seedToFideWorkAmount == 0d ? 0d : seedToFideLaborAmountPerDecare(seedToFideAmountPerDecare, seedToFideWorkAmount);
        double workPowerCount = doubleValueSetter(productQuestionList, 89L);
        double workPowerHour = doubleValueSetter(productQuestionList, 88L);
        double cYumruPlantLaborAmount = plantingLaborAmountYumru(workPowerHour, workPowerCount);
        Double laborAmount = calculatePlantingLaborAmount(
                aSeedThrowMibzerLaborAmount,
                aSeedThrowElLaborAmount,
                bSteelingPlantElLaborAmount,
                cSeedlingPlantLaborAmount,
                workPowerCount,
                cYumruPlantLaborAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PLANTING, EnumAllocationType.LABOR_AMOUNT, new BigDecimal(laborAmount)));

        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        BigDecimal femaleDailyWage = decimalAnswerSetter(previousAnswerList, 42L);
        double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);
        BigDecimal workingWomanLaborPrice = workingWomanLaborPrice(femaleDailyWage, workHoursPerDay);
        double maleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        double femaleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.FEMALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        BigDecimal workingMixedLaborPrice = workingMixedLaborPrice(maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay);
        BigDecimal laborCost = calculatePlantingLaborCost(
                aSeedThrowMibzerLaborAmount,
                workingManLaborPrice,
                aSeedThrowElLaborAmount,
                bSteelingPlantElLaborAmount,
                workingWomanLaborPrice,
                cSeedlingPlantLaborAmount,
                workingMixedLaborPrice,
                workPowerCount,
                cYumruPlantLaborAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PLANTING, EnumAllocationType.LABOR_COST, laborCost));

        BigDecimal plantingLumpSumPrice = decimalValueSetter(productQuestionList, 66L);
        BigDecimal plantingDroneLumpSumPrice = decimalValueSetter(productQuestionList, 68L);
        BigDecimal rentalDrillCostPerHour = decimalValueSetter(productQuestionList, 80L);
        BigDecimal lumpSumCost = calculatePlantingLumpSumCost(
                plantingLumpSumPrice,
                plantingDroneLumpSumPrice,
                rentalDrillCostPerHour);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PLANTING, EnumAllocationType.LUMP_SUM_COST, lumpSumCost));

        double seedKgPerDecare = doubleValueSetter(productQuestionList, 57L);
        BigDecimal seedPricePerKg = decimalValueSetter(productQuestionList, 61L);
        BigDecimal plantingSeedKgCost = plantingCostPerDecareKg(seedKgPerDecare, seedPricePerKg);
        double seedGrPerDecare = doubleValueSetter(productQuestionList, 58L);
        BigDecimal plantingSeedGrCost = plantingCostPerDecareGr(seedGrPerDecare, seedPricePerKg);
        double seedUnitPerDecare = doubleValueSetter(productQuestionList, 59L);
        BigDecimal seedPricePer1000 = decimalValueSetter(productQuestionList, 62L);
        BigDecimal plantingSeed1000Cost = plantingCostPerDecareUnit(seedUnitPerDecare, seedPricePer1000);
        double decarePerBag = doubleValueSetter(productQuestionList, 60L);
        BigDecimal seedBagPrice = decimalValueSetter(productQuestionList, 63L);
        BigDecimal plantingSeedBagCost = plantingCostPerDecareBag(decarePerBag, seedBagPrice);
        BigDecimal seedCost = calculateSeedCost(
                plantingSeedKgCost,
                plantingSeedGrCost,
                plantingSeed1000Cost,
                plantingSeedBagCost);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PLANTING, EnumAllocationType.SEED_COST, seedCost));

        BigDecimal seedlingUnitPrice = decimalValueSetter(productQuestionList, 73L);
        BigDecimal seedlingCost = seedlingUnitCostPerDecare(
                plantingSeedlingPerDecare,
                seedlingUsageYear,
                seedlingUnitPrice);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PLANTING, EnumAllocationType.SEEDLING_COST, seedlingCost));

        int plantingSteelingPerDecare = integerValueSetter(productQuestionList, 72L);
        BigDecimal steelingUnitPrice = decimalValueSetter(productQuestionList, 74L);
        BigDecimal cuttingCost = steelingUnitCostPerDecare(
                plantingSteelingPerDecare,
                seedlingUsageYear,
                steelingUnitPrice);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PLANTING, EnumAllocationType.CUTTING_COST, cuttingCost));

        double averageYumruAmount = doubleValueSetter(productQuestionList, 85L);
        double yumruUsageYear = doubleValueSetter(productQuestionList, 90L);
        double cSeedlingPlantTuberAmount = yumruUsageYear == 0d ? 0d : plantingMaterialAmountYumru(averageYumruAmount, yumruUsageYear);
        BigDecimal yumruKgPrice = decimalValueSetter(productQuestionList, 87L);
        BigDecimal tuberCost = calculateTuberCost(
                cSeedlingPlantTuberAmount,
                yumruKgPrice);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PLANTING, EnumAllocationType.TUBER_COST, tuberCost));

        saveAllocationList(parcelPlan.getId(), EnumPlantationQuestionType.EXPENSE_PLANTING, planAllocationList);
    }

    //18
    private Integer calculateFertilizerTransaction(int composeCount, int firstComposeCount, int secondComposeCount,
                                                   int potassiumCount, int phosphorusCount, int liquidCount,
                                                   int animalFertilizerCount, int humicCount, int leonarditeCount,
                                                   int solucanCount, int bioConditionerCount) {
        return composeCount + firstComposeCount + secondComposeCount + potassiumCount + phosphorusCount + liquidCount + animalFertilizerCount + humicCount + leonarditeCount + solucanCount + bioConditionerCount;
    }

    //7,36
    private Double calculateFertilizerDieselAmount(int composeCount, double composeDieselAmount, int firstComposeCount, double firstComposeDieselAmount,
                                                   int secondComposeCount, double secondComposeDieselAmount, int potassiumCount, double potassiumDieselAmount,
                                                   int phosphorusCount, double phosphorusDieselAmount, int liquidCount, double liquidDieselAmount,
                                                   int animalFertilizerCount, double animalFertilizerDieselAmount, int humicCount, double humicDieselAmount,
                                                   int leonarditeCount, double leonarditeDieselAmount, int solucanCount, double solucanDieselAmount,
                                                   int bioConditionerCount, double bioConditionerDieselAmount) {
        return (composeCount * composeDieselAmount) + (firstComposeCount * firstComposeDieselAmount)
                + (secondComposeCount * secondComposeDieselAmount) + (potassiumCount * potassiumDieselAmount)
                + (phosphorusCount * phosphorusDieselAmount) + (liquidCount * liquidDieselAmount)
                + (animalFertilizerCount * animalFertilizerDieselAmount) + (humicCount * humicDieselAmount)
                + (leonarditeCount * leonarditeDieselAmount) + (solucanCount * solucanDieselAmount)
                + (bioConditionerCount * bioConditionerDieselAmount);
    }

    //552
    private BigDecimal calculateFertilizerDieselCost(Double dieselAmount, BigDecimal dieselPrice) {
        return BigDecimal.valueOf(dieselAmount).multiply(dieselPrice);
    }

    //4,1994
    private Double calculateFertilizerLaborAmount(int composeCount, double composeLaborAmount, int firstComposeCount, double firstComposeLaborAmount,
                                                  int secondComposeCount, double secondComposeLaborAmount, int potassiumCount, double potassiumLaborAmount,
                                                  int phosphorusCount, double phosphorusLaborAmount, int liquidCount, double liquidLaborAmount,
                                                  int animalFertilizerCount, double animalFertilizerLaborAmount, int humicCount, double humicLaborAmount,
                                                  int leonarditeCount, double leonarditeLaborAmount, int solucanCount, double solucanLaborAmount,
                                                  int bioConditionerCount, double bioConditionerLaborAmount) {
        return (composeCount * composeLaborAmount) + (firstComposeCount * firstComposeLaborAmount)
                + (secondComposeCount * secondComposeLaborAmount) + (potassiumCount * potassiumLaborAmount)
                + (phosphorusCount * phosphorusLaborAmount) + (liquidCount * liquidLaborAmount)
                + (animalFertilizerCount * animalFertilizerLaborAmount) + (humicCount * humicLaborAmount)
                + (leonarditeCount * leonarditeLaborAmount) + (solucanCount * solucanLaborAmount)
                + (bioConditionerCount * bioConditionerLaborAmount);
    }

    //1049,85
    private BigDecimal calculateFertilizerLaborCost(Double laborAmount, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(laborAmount).multiply(workingManLaborPrice);
    }

    //20179
    private BigDecimal calculateFertilizerCost(int composeCount, double composeAmount, BigDecimal composePrice,
                                               int firstComposeCount, double firstComposeAmount, BigDecimal firstComposePrice,
                                               int secondComposeCount, double secondComposeAmount, BigDecimal secondComposePrice,
                                               int potassiumCount, double potassiumAmount, BigDecimal potassiumPrice,
                                               int phosphorusCount, double phosphorusAmount, BigDecimal phosphorusPrice,
                                               int liquidCount, double liquidAmount, BigDecimal liquidPrice,
                                               int animalFertilizerCount, double animalFertilizerAmount, BigDecimal animalFertilizerPrice,
                                               int humicCount, double humicAmount, BigDecimal humicPrice,
                                               int leonarditeCount, double leonarditeAmount, BigDecimal leonarditePrice,
                                               int solucanCount, double solucanAmount, BigDecimal solucanPrice,
                                               int bioConditionerCount, double bioConditionerAmount, BigDecimal bioConditionerPrice) {
        return BigDecimal.valueOf(composeCount).multiply(BigDecimal.valueOf(composeAmount)).multiply(composePrice)
                .add(BigDecimal.valueOf(firstComposeCount).multiply(BigDecimal.valueOf(firstComposeAmount)).multiply(firstComposePrice))
                .add(BigDecimal.valueOf(secondComposeCount).multiply(BigDecimal.valueOf(secondComposeAmount)).multiply(secondComposePrice))
                .add(BigDecimal.valueOf(potassiumCount).multiply(BigDecimal.valueOf(potassiumAmount)).multiply(potassiumPrice))
                .add(BigDecimal.valueOf(phosphorusCount).multiply(BigDecimal.valueOf(phosphorusAmount)).multiply(phosphorusPrice))
                .add(BigDecimal.valueOf(liquidCount).multiply(BigDecimal.valueOf(liquidAmount)).multiply(liquidPrice))
                .add(BigDecimal.valueOf(animalFertilizerCount).multiply(BigDecimal.valueOf(animalFertilizerAmount)).multiply(animalFertilizerPrice))
                .add(BigDecimal.valueOf(humicCount).multiply(BigDecimal.valueOf(humicAmount)).multiply(humicPrice))
                .add(BigDecimal.valueOf(leonarditeCount).multiply(BigDecimal.valueOf(leonarditeAmount)).multiply(leonarditePrice))
                .add(BigDecimal.valueOf(solucanCount).multiply(BigDecimal.valueOf(solucanAmount)).multiply(solucanPrice))
                .add(BigDecimal.valueOf(bioConditionerCount).multiply(BigDecimal.valueOf(bioConditionerAmount)).multiply(bioConditionerPrice));
    }

    @Transactional
    public void fertilizerAllocation(List<PlantationProductQuestion> productQuestionList, UserPlantParcelPlan parcelPlan, City city) {
        List<UserPlantParcelPlanAllocation> planAllocationList = new ArrayList<>();
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlan.getId(), List.of(41L));

        int composeCount = doubleValueChecker(productQuestionList, 93L);
        int firstComposeCount = doubleValueChecker(productQuestionList, 95L);
        int secondComposeCount = doubleValueChecker(productQuestionList, 97L);
        int potassiumCount = doubleValueChecker(productQuestionList, 101L);
        int phosphorusCount = doubleValueChecker(productQuestionList, 103L);
        int liquidCount = integerValueSetter(productQuestionList, 106L);
        int animalFertilizerCount = doubleValueChecker(productQuestionList, 107L);
        int humicCount = doubleValueSetter(productQuestionList, 110L).intValue();
        int leonarditeCount = doubleValueChecker(productQuestionList, 112L);
        int solucanCount = doubleValueChecker(productQuestionList, 114L);
        int bioConditionerCount = doubleValueChecker(productQuestionList, 116L);
        Integer transactionCount = calculateFertilizerTransaction(
                composeCount, firstComposeCount,
                secondComposeCount, potassiumCount,
                phosphorusCount, liquidCount,
                animalFertilizerCount, humicCount,
                leonarditeCount, solucanCount,
                bioConditionerCount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_FERTILIZER, EnumAllocationType.TRANSACTION_COUNT, new BigDecimal(transactionCount)));


        double composeDieselAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.BASE_FERTILIZER)).findFirst().get().getDieselValue();
        double firstComposeDieselAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.TOP_FERTILIZER)).findFirst().get().getDieselValue();
        double liquidDieselAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.LIQUID_FERTILIZER)).findFirst().get().getDieselValue();
        double animalFertilizerDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.ANIMAL_FERTILIZER)).findFirst().get().getDieselValue();
        double animalFertilizerFrequency = doubleValueSetter(productQuestionList, 109L);
        double animalFertilizerDieselAmount = animalFertilizerDieselAmount(animalFertilizerDieselRate, animalFertilizerFrequency);
        double humicDieselAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.HUMIC_ACID)).findFirst().get().getDieselValue();
        double leonarditeDieselAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.LEONARDITE)).findFirst().get().getDieselValue();
        double solucanDieselAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORM_COMPOST)).findFirst().get().getDieselValue();
        double bioConditionerDieselAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.BIO_CONDITIONER)).findFirst().get().getDieselValue();
        Double dieselAmount = calculateFertilizerDieselAmount(
                composeCount, composeDieselAmount,
                firstComposeCount, firstComposeDieselAmount,
                secondComposeCount, firstComposeDieselAmount,
                potassiumCount, firstComposeDieselAmount,
                phosphorusCount, firstComposeDieselAmount,
                liquidCount, liquidDieselAmount,
                animalFertilizerCount, animalFertilizerDieselAmount,
                humicCount, humicDieselAmount,
                leonarditeCount, leonarditeDieselAmount,
                solucanCount, solucanDieselAmount,
                bioConditionerCount, bioConditionerDieselAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_FERTILIZER, EnumAllocationType.DIESEL_AMOUNT, new BigDecimal(dieselAmount)));

        BigDecimal dieselCost = calculateFertilizerDieselCost(dieselAmount, city.getDieselPrice());
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_FERTILIZER, EnumAllocationType.DIESEL_COST, dieselCost));

        double composeLaborAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.BASE_FERTILIZER)).findFirst().get().getLaborValue();
        double firstComposeLaborAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.TOP_FERTILIZER)).findFirst().get().getLaborValue();
        double liquidLaborAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.LIQUID_FERTILIZER)).findFirst().get().getLaborValue();
        double animalFertilizerLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.ANIMAL_FERTILIZER)).findFirst().get().getLaborValue();
        double animalFertilizerLaborAmount = animalFertilizerLaborAmount(animalFertilizerLaborRate, animalFertilizerFrequency);
        double humicLaborAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.HUMIC_ACID)).findFirst().get().getLaborValue();
        double leonarditeLaborAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.LEONARDITE)).findFirst().get().getLaborValue();
        double solucanLaborAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORM_COMPOST)).findFirst().get().getLaborValue();
        double bioConditionerLaborAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.BIO_CONDITIONER)).findFirst().get().getLaborValue();
        Double laborAmount = calculateFertilizerLaborAmount(
                composeCount, composeLaborAmount,
                firstComposeCount, firstComposeLaborAmount,
                secondComposeCount, firstComposeLaborAmount,
                potassiumCount, firstComposeLaborAmount,
                phosphorusCount, firstComposeLaborAmount,
                liquidCount, liquidLaborAmount,
                animalFertilizerCount, animalFertilizerLaborAmount,
                humicCount, humicLaborAmount,
                leonarditeCount, leonarditeLaborAmount,
                solucanCount, solucanLaborAmount,
                bioConditionerCount, bioConditionerLaborAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_FERTILIZER, EnumAllocationType.LABOR_AMOUNT, new BigDecimal(laborAmount)));

        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);
        BigDecimal laborCost = calculateFertilizerLaborCost(laborAmount, workingManLaborPrice);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_FERTILIZER, EnumAllocationType.LABOR_COST, laborCost));


        double composeAmount = doubleValueSetter(productQuestionList, 93L);
        BigDecimal composePrice = decimalValueSetter(productQuestionList, 94L);
        double firstComposeAmount = doubleValueSetter(productQuestionList, 95L);
        BigDecimal firstComposePrice = decimalValueSetter(productQuestionList, 96L);
        double secondComposeAmount = doubleValueSetter(productQuestionList, 97L);
        BigDecimal secondComposePrice = decimalValueSetter(productQuestionList, 98L);
        double potassiumAmount = doubleValueSetter(productQuestionList, 101L);
        BigDecimal potassiumPrice = decimalValueSetter(productQuestionList, 102L);
        double phosphorusAmount = doubleValueSetter(productQuestionList, 103L);
        BigDecimal phosphorusPrice = decimalValueSetter(productQuestionList, 104L);
        double liquidAmount = 1d;
        BigDecimal liquidPrice = BigDecimal.valueOf(300);//todo hep ortalama değer mi alınacaK?
        Double animalFertilizerPerDecare = doubleValueSetter(productQuestionList, 107L);
        double animalFertilizerAmount = animalFertilizerKgAmount(animalFertilizerPerDecare, animalFertilizerFrequency);
        BigDecimal animalFertilizerPrice = decimalValueSetter(productQuestionList, 108L);
        double humicAmount = 1d;
        BigDecimal humicPrice = decimalValueSetter(productQuestionList, 111L);
        double leonarditeAmount = doubleValueSetter(productQuestionList, 112L);
        BigDecimal leonarditePrice = decimalValueSetter(productQuestionList, 113L);
        double solucanAmount = doubleValueSetter(productQuestionList, 114L);
        BigDecimal solucanPrice = decimalValueSetter(productQuestionList, 115L);
        double bioConditionerAmount = doubleValueSetter(productQuestionList, 116L);
        BigDecimal bioConditionerPrice = decimalValueSetter(productQuestionList, 117L);
        BigDecimal fertilizerCost = calculateFertilizerCost(
                composeCount, composeAmount, composePrice,
                firstComposeCount, firstComposeAmount, firstComposePrice,
                secondComposeCount, secondComposeAmount, secondComposePrice,
                potassiumCount, potassiumAmount, potassiumPrice,
                phosphorusCount, phosphorusAmount, phosphorusPrice,
                liquidCount, liquidAmount, liquidPrice,
                animalFertilizerCount, animalFertilizerAmount, animalFertilizerPrice,
                humicCount, humicAmount, humicPrice,
                leonarditeCount, leonarditeAmount, leonarditePrice,
                solucanCount, solucanAmount, solucanPrice,
                bioConditionerCount, bioConditionerAmount, bioConditionerPrice);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_FERTILIZER, EnumAllocationType.FERTILIZER_COST, fertilizerCost));

        saveAllocationList(parcelPlan.getId(), EnumPlantationQuestionType.EXPENSE_FERTILIZER, planAllocationList);
    }

    //19
    private Integer calculateWildGrassTransaction(int fillFurrowCount, int fillFurrowAmountCount,
                                                  int handWeedingCount, int tractorWeedingCount,
                                                  int machineWeedingCount, int medicineCount,
                                                  int handCountFrequency, int mulchCostPricePerKg,
                                                  int weedToolCount, int animalPlowPerYear) {
        return fillFurrowCount + fillFurrowAmountCount + handWeedingCount + tractorWeedingCount + machineWeedingCount + medicineCount + handCountFrequency + mulchCostPricePerKg + weedToolCount + animalPlowPerYear;
    }

    //39,9
    private Double calculateWildGrassDieselAmount(int fillFurrowCount, double fillFurrowDieselAmount,
                                                  int fillFurrowAmountCount, double fillFurrowAmountDieselAmount,
                                                  int handWeedingCount, double handWeedingDieselAmount,
                                                  int tractorWeedingCount, double tractorWeedingDieselAmount,
                                                  int machineWeedingCount, double machineWeedingDieselAmount,
                                                  int medicineCount, double medicineDieselAmount,
                                                  int handCountFrequency, double handCountDieselAmount,
                                                  int mulchCostPricePerKg, double mulchDieselAmount,
                                                  int weedToolCount, double weedToolDieselAmount,
                                                  int animalPlowPerYear, double animalPlowDieselAmount) {
        return (fillFurrowCount * fillFurrowDieselAmount)
                + (fillFurrowAmountCount * fillFurrowAmountDieselAmount)
                + (handWeedingCount * handWeedingDieselAmount)
                + (tractorWeedingCount * tractorWeedingDieselAmount)
                + (machineWeedingCount * machineWeedingDieselAmount)
                + (medicineCount * medicineDieselAmount)
                + (handCountFrequency * handCountDieselAmount)
                + (mulchCostPricePerKg * mulchDieselAmount)
                + (weedToolCount * weedToolDieselAmount)
                + (animalPlowPerYear * animalPlowDieselAmount);
    }

    //3082,5
    private BigDecimal calculateWildGrassDieselCost(int fillFurrowCount, double fillFurrowDieselAmount, BigDecimal dieselPrice,
                                                    int fillFurrowAmountCount, double fillFurrowAmountDieselAmount,
                                                    int handWeedingCount, double handWeedingDieselAmount,
                                                    int tractorWeedingCount, double tractorWeedingDieselAmount,
                                                    int machineWeedingCount, double machineWeedingDieselAmount,
                                                    int medicineCount, double medicineDieselAmount,
                                                    int handCountFrequency, double handCountDieselAmount,
                                                    int mulchCostPricePerKg, double mulchDieselAmount,
                                                    int weedToolCount, double weedToolDieselAmount, BigDecimal fuelPrice,
                                                    int animalPlowPerYear, double animalPlowDieselAmount) {
        return BigDecimal.valueOf(fillFurrowCount * fillFurrowDieselAmount).multiply(BigDecimal.ZERO)
                .add(BigDecimal.valueOf(fillFurrowAmountCount * fillFurrowAmountDieselAmount).multiply(dieselPrice))
                .add(BigDecimal.valueOf(handWeedingCount * handWeedingDieselAmount).multiply(BigDecimal.ZERO))
                .add(BigDecimal.valueOf(tractorWeedingCount * tractorWeedingDieselAmount).multiply(dieselPrice))
                .add(BigDecimal.valueOf(machineWeedingCount * machineWeedingDieselAmount).multiply(dieselPrice))
                .add(BigDecimal.valueOf(medicineCount * medicineDieselAmount).multiply(dieselPrice))
                .add(BigDecimal.valueOf(handCountFrequency * handCountDieselAmount).multiply(BigDecimal.ZERO))
                .add(BigDecimal.valueOf(mulchCostPricePerKg * mulchDieselAmount).multiply(BigDecimal.ZERO))
                .add(BigDecimal.valueOf(weedToolCount * weedToolDieselAmount).multiply(fuelPrice))
                .add(BigDecimal.valueOf(animalPlowPerYear * animalPlowDieselAmount).multiply(BigDecimal.ZERO));
    }

    //170,09
    private Double calculateWildGrassLaborAmount(int fillFurrowCount, double fillFurrowLaborAmount,
                                                 int fillFurrowAmountCount, double fillFurrowAmountLaborAmount,
                                                 int handWeedingCount, double handWeedingLaborAmount,
                                                 int tractorWeedingCount, double tractorWeedingLaborAmount,
                                                 int machineWeedingCount, double machineWeedingLaborAmount,
                                                 int medicineCount, double medicineLaborAmount,
                                                 int handCountFrequency, double handCountLaborAmount,
                                                 int mulchCostPricePerKg, double mulchLaborAmount,
                                                 int weedToolCount, double weedToolLaborAmount,
                                                 int animalPlowPerYear, double animalPlowLaborAmount) {
        return (fillFurrowCount * fillFurrowLaborAmount)
                + (fillFurrowAmountCount * fillFurrowAmountLaborAmount)
                + (handWeedingCount * handWeedingLaborAmount)
                + (tractorWeedingCount * tractorWeedingLaborAmount)
                + (machineWeedingCount * machineWeedingLaborAmount)
                + (medicineCount * medicineLaborAmount)
                + (handCountFrequency * handCountLaborAmount)
                + (mulchCostPricePerKg * mulchLaborAmount)
                + (weedToolCount * weedToolLaborAmount)
                + (animalPlowPerYear * animalPlowLaborAmount);
    }

    //19962,5
    private BigDecimal calculateWildGrassLaborCost(int fillFurrowCount, double fillFurrowLaborAmount, BigDecimal workingWomanLaborPrice,
                                                   int fillFurrowAmountCount, double fillFurrowAmountLaborAmount, BigDecimal workingManLaborPrice,
                                                   int handWeedingCount, double handWeedingLaborAmount,
                                                   int tractorWeedingCount, double tractorWeedingLaborAmount,
                                                   int machineWeedingCount, double machineWeedingLaborAmount,
                                                   int medicineCount, double medicineLaborAmount,
                                                   int handCountFrequency, double handCountLaborAmount, BigDecimal workingMixedLaborPrice,
                                                   int mulchCostPricePerKg, double mulchLaborAmount,
                                                   int weedToolCount, double weedToolLaborAmount,
                                                   int animalPlowPerYear, double animalPlowLaborAmount) {
        return BigDecimal.valueOf(fillFurrowCount * fillFurrowLaborAmount).multiply(workingWomanLaborPrice)
                .add(BigDecimal.valueOf(fillFurrowAmountCount * fillFurrowAmountLaborAmount).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(handWeedingCount * handWeedingLaborAmount).multiply(workingWomanLaborPrice))
                .add(BigDecimal.valueOf(tractorWeedingCount * tractorWeedingLaborAmount).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(machineWeedingCount * machineWeedingLaborAmount).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(medicineCount * medicineLaborAmount).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(handCountFrequency * handCountLaborAmount).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(mulchCostPricePerKg * mulchLaborAmount).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(weedToolCount * weedToolLaborAmount).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(animalPlowPerYear * animalPlowLaborAmount).multiply(workingManLaborPrice));
    }

    //16,23015333
    private BigDecimal calculateWildGrassHerbicideCost(int medicineCount, BigDecimal productMedicinePrice) {
        return BigDecimal.valueOf(medicineCount).multiply(productMedicinePrice);
    }

    //1600
    private BigDecimal calculateWildGrassMulchCost(double mulchInputAmount, BigDecimal mulchInputCost) {
        return BigDecimal.valueOf(mulchInputAmount).multiply(mulchInputCost);
    }

    @Transactional
    public void wildGrassAllocation(List<PlantationProductQuestion> productQuestionList, UserPlantParcelPlan parcelPlan, City city) {
        List<UserPlantParcelPlanAllocation> planAllocationList = new ArrayList<>();
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlan.getId(), List.of(41L, 42L));

        int fillFurrowCount = doubleValueChecker(productQuestionList, 119L);
        int fillFurrowAmountCount = decimalValueChecker(productQuestionList, 120L);
        int handWeedingCount = integerValueSetter(productQuestionList, 130L);
        int tractorWeedingCount = integerValueSetter(productQuestionList, 132L);
        int machineWeedingCount = integerValueSetter(productQuestionList, 135L);
        int medicineCount = integerValueSetter(productQuestionList, 136L);
        int handCountFrequency = integerValueSetter(productQuestionList, 138L);
        int mulchCostPricePerKg = decimalValueChecker(productQuestionList, 140L);
        int weedToolCount = integerValueSetter(productQuestionList, 145L);
        int animalPlowPerYear = doubleValueSetter(productQuestionList, 147L).intValue();
        Integer transactionCount = calculateWildGrassTransaction(
                fillFurrowCount, fillFurrowAmountCount,
                handWeedingCount, tractorWeedingCount,
                machineWeedingCount, medicineCount,
                handCountFrequency, mulchCostPricePerKg,
                weedToolCount, animalPlowPerYear);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_WEED, EnumAllocationType.TRANSACTION_COUNT, new BigDecimal(transactionCount)));

        double fillFurrowDieselAmount = 0d;
        double fillFurrowAmountDieselAmount = doubleValueSetter(productQuestionList, 120L);
        double handWeedingDieselAmount = 0d;
        double tractorWeedingDieselAmount = doubleValueSetter(productQuestionList, 131L);
        double machineWeedingDieselAmount = doubleValueSetter(productQuestionList, 133L);
        double medicineDieselAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WEED_CONTROL)).findFirst().get().getDieselValue();
        double handCountDieselAmount = 0d;
        double mulchDieselAmount = 0d;
        double weedToolDieselAmount = doubleValueSetter(productQuestionList, 144L);
        double animalPlowDieselAmount = 0d;
        Double dieselAmount = calculateWildGrassDieselAmount(
                fillFurrowCount, fillFurrowDieselAmount,
                fillFurrowAmountCount, fillFurrowAmountDieselAmount,
                handWeedingCount, handWeedingDieselAmount,
                tractorWeedingCount, tractorWeedingDieselAmount,
                machineWeedingCount, machineWeedingDieselAmount,
                medicineCount, medicineDieselAmount,
                handCountFrequency, handCountDieselAmount,
                mulchCostPricePerKg, mulchDieselAmount,
                weedToolCount, weedToolDieselAmount,
                animalPlowPerYear, animalPlowDieselAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_WEED, EnumAllocationType.DIESEL_AMOUNT, new BigDecimal(dieselAmount)));

        BigDecimal dieselCost = calculateWildGrassDieselCost(
                fillFurrowCount, fillFurrowDieselAmount, city.getDieselPrice(),
                fillFurrowAmountCount, fillFurrowAmountDieselAmount,
                handWeedingCount, handWeedingDieselAmount,
                tractorWeedingCount, tractorWeedingDieselAmount,
                machineWeedingCount, machineWeedingDieselAmount,
                medicineCount, medicineDieselAmount,
                handCountFrequency, handCountDieselAmount,
                mulchCostPricePerKg, mulchDieselAmount,
                weedToolCount, weedToolDieselAmount, city.getFuelPrice(),
                animalPlowPerYear, animalPlowDieselAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_WEED, EnumAllocationType.DIESEL_COST, dieselCost));

        double fillFurrowLaborAmount = doubleValueSetter(productQuestionList, 119L);
        double fillFurrowAmountLaborAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.THROAT_FILLING)).findFirst().get().getLaborValue();
        double handWeedingLaborAmount = doubleValueSetter(productQuestionList, 129L);
        double tractorWeedingLaborAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.TRACTOR_TILLER)).findFirst().get().getLaborValue();
        double machineWeedingLaborAmount = doubleValueSetter(productQuestionList, 134L);
        double medicineLaborAmount = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WEED_CONTROL)).findFirst().get().getLaborValue();
        double handCountLaborAmount = doubleValueSetter(productQuestionList, 137L);
        double totalMulchingHour = doubleValueSetter(productQuestionList, 141L);
        double mulchUsageYear = doubleValueSetter(productQuestionList, 142L);
        double mulchLaborAmount = mulchUsageYear == 0d ? 0d : mulchingLaborAmount(totalMulchingHour, mulchUsageYear);
        double weedToolLaborAmount = doubleValueSetter(productQuestionList, 143L);
        double animalPlowLaborAmount = doubleValueSetter(productQuestionList, 146L);
        Double laborAmount = calculateWildGrassLaborAmount(
                fillFurrowCount, fillFurrowLaborAmount,
                fillFurrowAmountCount, fillFurrowAmountLaborAmount,
                handWeedingCount, handWeedingLaborAmount,
                tractorWeedingCount, tractorWeedingLaborAmount,
                machineWeedingCount, machineWeedingLaborAmount,
                medicineCount, medicineLaborAmount,
                handCountFrequency, handCountLaborAmount,
                mulchCostPricePerKg, mulchLaborAmount,
                weedToolCount, weedToolLaborAmount,
                animalPlowPerYear, animalPlowLaborAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_WEED, EnumAllocationType.LABOR_AMOUNT, new BigDecimal(laborAmount)));

        double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);
        BigDecimal femaleDailyWage = decimalAnswerSetter(previousAnswerList, 42L);
        BigDecimal workingWomanLaborPrice = workingWomanLaborPrice(femaleDailyWage, workHoursPerDay);
        double maleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        double femaleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.FEMALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        BigDecimal workingMixedLaborPrice = workingMixedLaborPrice(maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay);
        BigDecimal laborCost = calculateWildGrassLaborCost(
                fillFurrowCount, fillFurrowLaborAmount, workingWomanLaborPrice,
                fillFurrowAmountCount, fillFurrowAmountLaborAmount, workingManLaborPrice,
                handWeedingCount, handWeedingLaborAmount,
                tractorWeedingCount, tractorWeedingLaborAmount,
                machineWeedingCount, machineWeedingLaborAmount,
                medicineCount, medicineLaborAmount,
                handCountFrequency, handCountLaborAmount, workingMixedLaborPrice,
                mulchCostPricePerKg, mulchLaborAmount,
                weedToolCount, weedToolLaborAmount,
                animalPlowPerYear, animalPlowLaborAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_WEED, EnumAllocationType.LABOR_COST, laborCost));

        BigDecimal productMedicinePrice = BigDecimal.valueOf(8.115076667d);
        BigDecimal herbicideCost = calculateWildGrassHerbicideCost(medicineCount, productMedicinePrice);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_WEED, EnumAllocationType.HERBICIDE_COST, herbicideCost));


        double mulchAmountPerDecare = doubleValueSetter(productQuestionList, 139L);
        double mulchInputAmount = mulchUsageYear == 0d ? 0d : mulchingCostAmount(mulchAmountPerDecare, mulchUsageYear);
        BigDecimal mulchInputCost = decimalValueSetter(productQuestionList, 140L);
        BigDecimal mulchCost = calculateWildGrassMulchCost(mulchInputAmount, mulchInputCost);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_WEED, EnumAllocationType.MULCH_COST, mulchCost));

        saveAllocationList(parcelPlan.getId(), EnumPlantationQuestionType.EXPENSE_WEED, planAllocationList);
    }

    //40
    private Double calculateIrrigationTransaction(int pressuredCount,
                                                  double cazibeCount,
                                                  double electricityCount,
                                                  double irrigationArea) {
        return pressuredCount + cazibeCount + electricityCount + irrigationArea;
    }

    //24,4
    private Double calculateIrrigationDieselAmount(int pressuredCount, double irrigationDieselRateForSelectedIrrigation,
                                                   double cazibeCount, double pumpEfficiencyRate,
                                                   double electricityCount, double v,
                                                   double irrigationArea, double dieselInputAmountForDieselPump) {
        return (pressuredCount * irrigationDieselRateForSelectedIrrigation)
                + (cazibeCount * pumpEfficiencyRate)
                + (electricityCount * v)
                + (irrigationArea * dieselInputAmountForDieselPump);
    }

    //1830
    private BigDecimal calculateIrrigationDieselCost(Double dieselAmount, BigDecimal dieselPrice) {
        return BigDecimal.valueOf(dieselAmount).multiply(dieselPrice);
    }

    //20,8
    private Double calculateIrrigationLaborAmount(int pressuredCount, double irrigationLaborRateForSelectedIrrigation,
                                                  double cazibeCount,
                                                  double electricityCount, double v,
                                                  double irrigationArea) {
        return (pressuredCount * irrigationLaborRateForSelectedIrrigation)
                + (cazibeCount * irrigationLaborRateForSelectedIrrigation)
                + (electricityCount * v)
                + (irrigationArea * irrigationLaborRateForSelectedIrrigation);
    }

    //5200
    private BigDecimal calculateIrrigationLaborCost(Double laborAmount, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(laborAmount).multiply(workingManLaborPrice);
    }

    //2336,575141
    private BigDecimal calculateIrrigationWaterCost(int pressuredCount, double waterAmountPerDecarePerTonne, BigDecimal waterPricePerTonne,
                                                    double cazibeCount, double waterAmountPerDecare, BigDecimal waterPricePerDecare,
                                                    double electricityCount, double inputAmountForElectricityPump, BigDecimal electricity) {
        return BigDecimal.valueOf(pressuredCount * waterAmountPerDecarePerTonne).multiply(waterPricePerTonne)
                .add(BigDecimal.valueOf(cazibeCount * waterAmountPerDecare).multiply(waterPricePerDecare))
                .add(BigDecimal.valueOf(electricityCount * inputAmountForElectricityPump).multiply(electricity));
    }

    @Transactional
    public void irrigationAllocation(List<PlantationProductQuestion> productQuestionList, UserPlantParcelPlan parcelPlan, City city) {
        List<UserPlantParcelPlanAllocation> planAllocationList = new ArrayList<>();
        List<PlantationIrrigationValue> irrigationValueList = irrigationValueRepository.findByIrrigationTypeIn(EnumIrrigationType.values());
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlan.getId(), List.of(41L));


        int pressuredCount = integerValueSetter(productQuestionList, 156L);
        double cazibeCount = doubleValueSetter(productQuestionList, 159L);
        double electricityCount = doubleValueSetter(productQuestionList, 168L);
        double irrigationArea = doubleValueSetter(productQuestionList, 172L);
        Double transactionCount = calculateIrrigationTransaction(
                pressuredCount,
                cazibeCount,
                electricityCount,
                irrigationArea);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_IRRIGATION, EnumAllocationType.TRANSACTION_COUNT, new BigDecimal(transactionCount)));

        BigDecimal amortizationForSelectedIrrigation = BigDecimal.ZERO;
        double irrigationLaborRateForSelectedIrrigation = 0d;
        double irrigationDieselRateForSelectedIrrigation = 0d;
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
        double pumpEfficiencyRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.PUMP_EFFICIENCY_CONSTANT)).findFirst().get().getLaborValue();
        double specificConstantRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.IRRIGATION_SPECIFIC_CONSUMPTION_CONSTANT)).findFirst().get().getLaborValue();
        double pumpWorkingHour = doubleValueSetter(productQuestionList, 171L);
        double waterAmountPerHour = doubleValueSetter(productQuestionList, 169L);
        double waterPumpHeight = doubleValueSetter(productQuestionList, 170L);
        double gravity = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.GRAVITY)).findFirst().get().getLaborValue();
        double pumpMotorEfficiencyRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.PUMP_MOTOR_EFFICIENCY)).findFirst().get().getLaborValue();
        double dieselInputAmountForDieselPump = dieselInputAmountForDieselPump(specificConstantRate, pumpWorkingHour, waterAmountPerHour, waterPumpHeight, gravity, pumpMotorEfficiencyRate, irrigationArea);
        Double dieselAmount = calculateIrrigationDieselAmount(
                pressuredCount, irrigationDieselRateForSelectedIrrigation,
                cazibeCount, pumpEfficiencyRate,
                electricityCount, 0d,
                irrigationArea, dieselInputAmountForDieselPump);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_IRRIGATION, EnumAllocationType.DIESEL_AMOUNT, new BigDecimal(dieselAmount)));

        BigDecimal dieselCost = calculateIrrigationDieselCost(dieselAmount, city.getDieselPrice());
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_IRRIGATION, EnumAllocationType.DIESEL_COST, dieselCost));

        Double laborAmount = calculateIrrigationLaborAmount(
                pressuredCount, irrigationLaborRateForSelectedIrrigation,
                cazibeCount,
                electricityCount, 0.15d,
                irrigationArea);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_IRRIGATION, EnumAllocationType.LABOR_AMOUNT, new BigDecimal(laborAmount)));

        double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);
        BigDecimal laborCost = calculateIrrigationLaborCost(laborAmount, workingManLaborPrice);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_IRRIGATION, EnumAllocationType.LABOR_COST, laborCost));

        double waterAmountPerDecarePerTonne = doubleValueSetter(productQuestionList, 155L);
        BigDecimal waterPricePerTonne = decimalValueSetter(productQuestionList, 154L);
        double waterAmountPerDecare = doubleValueSetter(productQuestionList, 158L);
        BigDecimal waterPricePerDecare = decimalValueSetter(productQuestionList, 157L);
        double electricityPumpWorkingHour = doubleValueSetter(productQuestionList, 167L);
        double electricityWaterAmountPerHour = doubleValueSetter(productQuestionList, 164L);
        double electricityWaterPumpHeight = doubleValueSetter(productQuestionList, 165L);
        Integer constantNumber = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.CONSTANT_NUMBER)).findFirst().get().getLaborValue().intValue();
        double constantMotorEfficiency = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MOTOR_EFFICIENCY_CONSTANT)).findFirst().get().getLaborValue();
        double irrigationAreaElectricity = doubleValueSetter(productQuestionList, 166L);
        double inputAmountForElectricityPump = irrigationAreaElectricity == 0d ? 0d : inputAmountForElectricityPump(electricityPumpWorkingHour, electricityWaterAmountPerHour, electricityWaterPumpHeight, constantNumber, constantMotorEfficiency, pumpEfficiencyRate, irrigationAreaElectricity);
        BigDecimal waterCost = calculateIrrigationWaterCost(
                pressuredCount, waterAmountPerDecarePerTonne, waterPricePerTonne,
                cazibeCount, waterAmountPerDecare, waterPricePerDecare,
                electricityCount, inputAmountForElectricityPump, city.getElectricity());
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_IRRIGATION, EnumAllocationType.WATER_COST, waterCost));

        BigDecimal amortizationCost = amortizationForSelectedIrrigation;
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_IRRIGATION, EnumAllocationType.AMORTIZATION_AMOUNT, amortizationCost));

        saveAllocationList(parcelPlan.getId(), EnumPlantationQuestionType.EXPENSE_IRRIGATION, planAllocationList);
    }

    //11
    private Integer calculateCulturalTransaction(int treeCuttingCount, int vineyardCuttingCount, int youngCuttingCount, int winterCuttingCount, int summerCuttingCount, int basalShootCount, int fruitThinningCount, int pullPoleCount, int plantVineCount, int netLifespanCount, int bendingTyingCount) {
        return treeCuttingCount + vineyardCuttingCount + youngCuttingCount + winterCuttingCount + summerCuttingCount + basalShootCount + fruitThinningCount + pullPoleCount + plantVineCount + netLifespanCount + bendingTyingCount;
    }

    //71
    private Double calculateCulturalLaborAmount(double treePruneAmountHour, double vinePruneAmountHour, double rejuvenationPruneAmountHour, double winterPruneAmountHour, double summerPruneAmountHour, double basalPruneAmountHour, double thinningPruneAmountHour, double polePullLaborHour, double plantVineHourPerDecare, double nettingLaborAmountPerDecare, double bendingRopeHourPerDecare) {
        return treePruneAmountHour + vinePruneAmountHour + rejuvenationPruneAmountHour + winterPruneAmountHour + summerPruneAmountHour + basalPruneAmountHour + thinningPruneAmountHour + polePullLaborHour + plantVineHourPerDecare + nettingLaborAmountPerDecare + bendingRopeHourPerDecare;
    }

    //19540
    private BigDecimal calculateCulturalLaborCost(double treePruneAmountHour, BigDecimal workingPruneLaborPrice,
                                                  double vinePruneAmountHour,
                                                  double rejuvenationPruneAmountHour,
                                                  double winterPruneAmountHour,
                                                  double summerPruneAmountHour, BigDecimal workingManLaborPrice,
                                                  double basalPruneAmountHour,
                                                  double thinningPruneAmountHour, BigDecimal workingMixedLaborPrice,
                                                  double polePullLaborHour,
                                                  double plantVineHourPerDecare,
                                                  double nettingLaborAmountPerDecare,
                                                  double bendingRopeHourPerDecare) {
        return BigDecimal.valueOf(treePruneAmountHour).multiply(workingPruneLaborPrice)
                .add(BigDecimal.valueOf(vinePruneAmountHour).multiply(workingPruneLaborPrice))
                .add(BigDecimal.valueOf(rejuvenationPruneAmountHour).multiply(workingPruneLaborPrice))
                .add(BigDecimal.valueOf(winterPruneAmountHour).multiply(workingPruneLaborPrice))
                .add(BigDecimal.valueOf(summerPruneAmountHour).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(basalPruneAmountHour).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(thinningPruneAmountHour).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(polePullLaborHour).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(plantVineHourPerDecare).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(nettingLaborAmountPerDecare).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(bendingRopeHourPerDecare).multiply(workingManLaborPrice));
    }

    //150
    private BigDecimal calculateCulturalPoleCost(double polesInputAmount, BigDecimal polePricePerUnit) {
        return BigDecimal.valueOf(polesInputAmount).multiply(polePricePerUnit);
    }

    //200
    private BigDecimal calculateCulturalStringCost(double plantVineInputAmount, BigDecimal vinePricePerUnit) {
        return BigDecimal.valueOf(plantVineInputAmount).multiply(vinePricePerUnit);
    }

    //1600
    private BigDecimal calculateCulturalNetCoverCost(double nettingInputAmountPerDecare, BigDecimal nettingInputPrice) {
        return BigDecimal.valueOf(nettingInputAmountPerDecare).multiply(nettingInputPrice);
    }

    @Transactional
    public void culturalAllocation(List<PlantationProductQuestion> productQuestionList, UserPlantParcelPlan parcelPlan, City city) {
        List<UserPlantParcelPlanAllocation> planAllocationList = new ArrayList<>();
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlan.getId(), List.of(41L, 42L, 43L));

        int treeCuttingCount = doubleValueChecker(productQuestionList, 175L);
        int vineyardCuttingCount = doubleValueChecker(productQuestionList, 176L);
        int youngCuttingCount = doubleValueChecker(productQuestionList, 177L);
        int winterCuttingCount = doubleValueChecker(productQuestionList, 178L);
        int summerCuttingCount = doubleValueChecker(productQuestionList, 179L);
        int basalShootCount = doubleValueChecker(productQuestionList, 180L);
        int fruitThinningCount = doubleValueChecker(productQuestionList, 181L);
        int pullPoleCount = doubleValueChecker(productQuestionList, 184L);
        int plantVineCount = doubleValueChecker(productQuestionList, 187L);
        int netLifespanCount = doubleValueChecker(productQuestionList, 192L);
        int bendingTyingCount = doubleValueChecker(productQuestionList, 193L);
        Integer transactionCount = calculateCulturalTransaction(
                treeCuttingCount, vineyardCuttingCount, youngCuttingCount,
                winterCuttingCount, summerCuttingCount, basalShootCount,
                fruitThinningCount, pullPoleCount, plantVineCount,
                netLifespanCount, bendingTyingCount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_CULTURAL, EnumAllocationType.TRANSACTION_COUNT, new BigDecimal(transactionCount)));


        double treePruneAmountHour = doubleValueSetter(productQuestionList, 175L);
        double vinePruneAmountHour = doubleValueSetter(productQuestionList, 176L);
        double rejuvenationPruneAmountHour = doubleValueSetter(productQuestionList, 177L);
        double winterPruneAmountHour = doubleValueSetter(productQuestionList, 178L);
        double summerPruneAmountHour = doubleValueSetter(productQuestionList, 179L);
        double basalPruneAmountHour = doubleValueSetter(productQuestionList, 180L);
        double thinningPruneAmountHour = doubleValueSetter(productQuestionList, 181L);
        double polePullLaborHour = doubleValueSetter(productQuestionList, 184L);
        double plantVineHourPerDecare = doubleValueSetter(productQuestionList, 187L);
        double nettingLaborHourPerDecare = doubleValueSetter(productQuestionList, 191L);
        double nettingLifeAmount = doubleValueSetter(productQuestionList, 192L);
        double nettingLaborAmountPerDecare = nettingLifeAmount == 0d ? 0d : nettingLaborAmountPerDecare(nettingLaborHourPerDecare, nettingLifeAmount);
        double bendingRopeHourPerDecare = doubleValueSetter(productQuestionList, 193L);
        Double laborAmount = calculateCulturalLaborAmount(
                treePruneAmountHour, vinePruneAmountHour, rejuvenationPruneAmountHour,
                winterPruneAmountHour, summerPruneAmountHour, basalPruneAmountHour,
                thinningPruneAmountHour, polePullLaborHour, plantVineHourPerDecare,
                nettingLaborAmountPerDecare, bendingRopeHourPerDecare);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_CULTURAL, EnumAllocationType.LABOR_AMOUNT, new BigDecimal(laborAmount)));

        double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        double maleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        double femaleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.FEMALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);
        BigDecimal femaleDailyWage = decimalAnswerSetter(previousAnswerList, 42L);
        BigDecimal workingMixedLaborPrice = workingMixedLaborPrice(maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay);
        BigDecimal pruneDailyWage = decimalAnswerSetter(previousAnswerList, 43L);
        BigDecimal workingPruneLaborPrice = workingPruneLaborPrice(pruneDailyWage, workHoursPerDay);
        BigDecimal laborCost = calculateCulturalLaborCost(
                treePruneAmountHour, workingPruneLaborPrice,
                vinePruneAmountHour,
                rejuvenationPruneAmountHour,
                winterPruneAmountHour,
                summerPruneAmountHour, workingManLaborPrice,
                basalPruneAmountHour,
                thinningPruneAmountHour, workingMixedLaborPrice,
                polePullLaborHour,
                plantVineHourPerDecare,
                nettingLaborAmountPerDecare,
                bendingRopeHourPerDecare);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_CULTURAL, EnumAllocationType.LABOR_COST, laborCost));

        double polesAmountPerDecare = doubleValueSetter(productQuestionList, 182L);
        double poleLifeCycleRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.POLE_SERVICE_LIFE)).findFirst().get().getLaborValue();
        double polesInputAmount = polesInputAmount(polesAmountPerDecare, poleLifeCycleRate);
        BigDecimal polePricePerUnit = decimalValueSetter(productQuestionList, 183L);
        BigDecimal poleCost = calculateCulturalPoleCost(polesInputAmount, polePricePerUnit);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_CULTURAL, EnumAllocationType.POLE_COST, poleCost));

        double plantVineInputAmount = doubleValueSetter(productQuestionList, 185L);
        BigDecimal vinePricePerUnit = decimalValueSetter(productQuestionList, 186L);
        BigDecimal stringCost = calculateCulturalStringCost(plantVineInputAmount, vinePricePerUnit);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_CULTURAL, EnumAllocationType.STRING_COST, stringCost));

        double nettingInputAmount = doubleValueSetter(productQuestionList, 189L);
        double nettingInputAmountPerDecare = nettingLifeAmount == 0d ? 0d : nettingInputAmountPerDecare(nettingInputAmount, nettingLifeAmount);
        BigDecimal nettingInputPrice = decimalValueSetter(productQuestionList, 190L);
        BigDecimal netCoverCost = calculateCulturalNetCoverCost(nettingInputAmountPerDecare, nettingInputPrice);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_CULTURAL, EnumAllocationType.NET_COVER_COST, netCoverCost));

        saveAllocationList(parcelPlan.getId(), EnumPlantationQuestionType.EXPENSE_CULTURAL, planAllocationList);
    }

    //19
    private Integer calculateProtectionTransactionCount(int foliarForFungalCount, int medicineForInsectCount, int medicineForRedSpiderCount, int medicineForBordeauxCount, int medicineForHormoneCount, int cottonDefoliantCount) {
        return foliarForFungalCount + medicineForInsectCount + medicineForRedSpiderCount + medicineForBordeauxCount + medicineForHormoneCount + cottonDefoliantCount;
    }

    //8,4
    private Double calculateProtectionDieselAmount(int machineMedicineCount, double machineMedicineDieselRate,
                                                   int backpackMedicineCount, double backpackMedicineDieselRate,
                                                   int irrigationMedicineCount, double irrigationMedicineDieselRate) {
        return (machineMedicineCount * machineMedicineDieselRate)
                + (backpackMedicineCount * backpackMedicineDieselRate)
                + (irrigationMedicineCount * irrigationMedicineDieselRate);
    }

    //630
    private BigDecimal calculateProtectionDieselCost(Double dieselAmount, BigDecimal dieselPrice) {
        return BigDecimal.valueOf(dieselAmount).multiply(dieselPrice);
    }

    //9,95
    private Double calculateProtectionLaborAmount(int machineMedicineCount, Double machineMedicineLaborRate,
                                                  int backpackMedicineCount, Double backpackMedicineLaborRate,
                                                  int irrigationMedicineCount, Double irrigationMedicineLaborRate) {
        return (machineMedicineCount * machineMedicineLaborRate)
                + (backpackMedicineCount * backpackMedicineLaborRate)
                + (irrigationMedicineCount * irrigationMedicineLaborRate);
    }

    //2487,5
    private BigDecimal calculateProtectionLaborCost(Double laborAmount, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(laborAmount).multiply(workingManLaborPrice);
    }

    //210,1974077
    private BigDecimal calculateProtectionMedicineCost(int foliarForFungalCount, BigDecimal productFungalMedicinePricePerUnit,
                                                       int medicineForInsectCount, BigDecimal productInsectMedicinePricePerUnit,
                                                       int medicineForRedSpiderCount, BigDecimal productRedSpiderMedicinePricePerUnit,
                                                       int medicineForBordeauxCount, BigDecimal productBordeauxMedicinePricePerUnit,
                                                       int medicineForHormoneCount, BigDecimal productHormonePricePerUnit,
                                                       int cottonDefoliantCount, BigDecimal productCottonDefoliantPricePerUnit) {
        return BigDecimal.valueOf(foliarForFungalCount).multiply(productFungalMedicinePricePerUnit)
                .add(BigDecimal.valueOf(medicineForInsectCount).multiply(productInsectMedicinePricePerUnit))
                .add(BigDecimal.valueOf(medicineForRedSpiderCount).multiply(productRedSpiderMedicinePricePerUnit))
                .add(BigDecimal.valueOf(medicineForBordeauxCount).multiply(productBordeauxMedicinePricePerUnit))
                .add(BigDecimal.valueOf(medicineForHormoneCount).multiply(productHormonePricePerUnit))
                .add(BigDecimal.valueOf(cottonDefoliantCount).multiply(productCottonDefoliantPricePerUnit));
    }

    //800
    private BigDecimal calculateProtectionLumpSumCost(Integer droneMedicineCount, BigDecimal droneMedicineRentalPricePerDecare) {
        return BigDecimal.valueOf(droneMedicineCount).multiply(droneMedicineRentalPricePerDecare);
    }

    @Transactional
    public void protectionAllocation(List<PlantationProductQuestion> productQuestionList, UserPlantParcelPlan parcelPlan, City city) {
        List<UserPlantParcelPlanAllocation> planAllocationList = new ArrayList<>();
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlan.getId(), List.of(41L));

        int foliarForFungalCount = integerValueSetter(productQuestionList, 195L);
        int medicineForInsectCount = integerValueSetter(productQuestionList, 197L);
        int medicineForRedSpiderCount = integerValueSetter(productQuestionList, 198L);
        int medicineForBordeauxCount = integerValueSetter(productQuestionList, 199L);
        int medicineForHormoneCount = integerValueSetter(productQuestionList, 200L);
        int cottonDefoliantCount = integerValueSetter(productQuestionList, 201L);
        Integer transactionCount = calculateProtectionTransactionCount(
                foliarForFungalCount,
                medicineForInsectCount,
                medicineForRedSpiderCount,
                medicineForBordeauxCount,
                medicineForHormoneCount,
                cottonDefoliantCount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PROTECTION, EnumAllocationType.TRANSACTION_COUNT, new BigDecimal(transactionCount)));

        int machineMedicineCount = integerValueSetter(productQuestionList, 203L);
        double machineMedicineDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MACHINE_SPRAYING)).findFirst().get().getDieselValue();
        int backpackMedicineCount = integerValueSetter(productQuestionList, 204L);
        double backpackMedicineDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.BACKPACK_SPRAYING)).findFirst().get().getDieselValue();
        int irrigationMedicineCount = integerValueSetter(productQuestionList, 205L);
        double irrigationMedicineDieselRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.IRRIGATION_SPRAYING)).findFirst().get().getDieselValue();
        Double dieselAmount = calculateProtectionDieselAmount(
                machineMedicineCount, machineMedicineDieselRate,
                backpackMedicineCount, backpackMedicineDieselRate,
                irrigationMedicineCount, irrigationMedicineDieselRate);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PROTECTION, EnumAllocationType.DIESEL_AMOUNT, new BigDecimal(dieselAmount)));

        BigDecimal dieselCost = calculateProtectionDieselCost(dieselAmount, city.getDieselPrice());
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PROTECTION, EnumAllocationType.DIESEL_COST, dieselCost));

        double machineMedicineLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MACHINE_SPRAYING)).findFirst().get().getLaborValue();
        double backpackMedicineLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.BACKPACK_SPRAYING)).findFirst().get().getLaborValue();
        double irrigationMedicineLaborRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.IRRIGATION_SPRAYING)).findFirst().get().getLaborValue();
        Double laborAmount = calculateProtectionLaborAmount(
                machineMedicineCount, machineMedicineLaborRate,
                backpackMedicineCount, backpackMedicineLaborRate,
                irrigationMedicineCount, irrigationMedicineLaborRate);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PROTECTION, EnumAllocationType.LABOR_AMOUNT, new BigDecimal(laborAmount)));

        double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);
        BigDecimal laborCost = calculateProtectionLaborCost(laborAmount, workingManLaborPrice);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PROTECTION, EnumAllocationType.LABOR_COST, laborCost));

        BigDecimal productFungalMedicinePricePerUnit = BigDecimal.valueOf(5.88859433);//TODO
        BigDecimal productInsectMedicinePricePerUnit = BigDecimal.valueOf(3.916881333);//TODO
        BigDecimal productRedSpiderMedicinePricePerUnit = BigDecimal.valueOf(14.33626667);//TODO
        BigDecimal productBordeauxMedicinePricePerUnit = BigDecimal.ZERO; //TODO
        BigDecimal productHormonePricePerUnit = BigDecimal.valueOf(139);//TODO
        BigDecimal productCottonDefoliantPricePerUnit = BigDecimal.ZERO;//TODO
        BigDecimal medicineCost = calculateProtectionMedicineCost(
                foliarForFungalCount, productFungalMedicinePricePerUnit,
                medicineForInsectCount, productInsectMedicinePricePerUnit,
                medicineForRedSpiderCount, productRedSpiderMedicinePricePerUnit,
                medicineForBordeauxCount, productBordeauxMedicinePricePerUnit,
                medicineForHormoneCount, productHormonePricePerUnit,
                cottonDefoliantCount, productCottonDefoliantPricePerUnit);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PROTECTION, EnumAllocationType.MEDICINE_COST, medicineCost));

        Integer droneMedicineCount = integerValueSetter(productQuestionList, 206L);
        BigDecimal droneMedicineRentalPricePerDecare = decimalValueSetter(productQuestionList, 207L);
        BigDecimal lumpSumCost = calculateProtectionLumpSumCost(droneMedicineCount, droneMedicineRentalPricePerDecare);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PROTECTION, EnumAllocationType.LUMP_SUM_COST, lumpSumCost));

        saveAllocationList(parcelPlan.getId(), EnumPlantationQuestionType.EXPENSE_PROTECTION, planAllocationList);
    }

    //22
    private Integer calculateHarvestTransaction(int byHandHarvestAmountPerDay, int byMachineHarvestAmountPerDay, int shakeAndCrateInputAmount, int cutAndBindInputAmount, int cutAndLoadInputAmount, int harvestAndLoadInputAmount, int harvestAndBindInputAmount, int cutAndBindAndLoadInputAmount, int harvestGrInputAmount, int harvestLeafInputAmount, int harvesterRentalPricePerDecare, int demolitionRentalPricePerDecare, int cottonRentalPricePerDecare, int silageRentalPricePerDecare, int harvestRentalPricePerDecare, int seederRentalPricePerDecare, int havestAndMachineDieselInputAmount, int machineShakingDieselInputAmount, int uprootHarvestLaborAmountPerDecare, int lumpSumCostPerTonne, int lumpSumHarvestCostPerTonne, int mowingDieselAmountPerDecare, int motorizedCuttingLaborAmount) {
        return byHandHarvestAmountPerDay + byMachineHarvestAmountPerDay + shakeAndCrateInputAmount + cutAndBindInputAmount + cutAndLoadInputAmount + harvestAndLoadInputAmount + harvestAndBindInputAmount + cutAndBindAndLoadInputAmount + harvestGrInputAmount + harvestLeafInputAmount + harvesterRentalPricePerDecare + demolitionRentalPricePerDecare + cottonRentalPricePerDecare + silageRentalPricePerDecare + harvestRentalPricePerDecare + seederRentalPricePerDecare + havestAndMachineDieselInputAmount + machineShakingDieselInputAmount + uprootHarvestLaborAmountPerDecare + lumpSumCostPerTonne + lumpSumHarvestCostPerTonne + mowingDieselAmountPerDecare + motorizedCuttingLaborAmount;
    }

    //15
    private Double calculateHarvestDieselAmount(double havestAndMachineDieselInputAmount, double machineShakingDieselInputAmount, double uprootDieselAmountPerDecare, double mowingDieselAmountPerDecare, double motorizedCuttingDieselAmount) {
        return havestAndMachineDieselInputAmount + machineShakingDieselInputAmount + uprootDieselAmountPerDecare + mowingDieselAmountPerDecare + motorizedCuttingDieselAmount;
    }

    //1125
    private BigDecimal calculateHarvestDieselCost(Double dieselAmount, BigDecimal dieselPrice) {
        return BigDecimal.valueOf(dieselAmount).multiply(dieselPrice);
    }

    //48,483
    private Double calculateHarvestLaborAmount(double harvestAndPackingLaborAmountPerDay, double harvestAndCleanLaborAmountPerDay, double shakeAndCrateLaborAmountPerDay, double cutAndBindLaborAmountPerDay, double cutAndLoadLaborAmountPerDay, double harvestAndLoadLaborAmountPerDay, double harvestAndBindLaborAmountPerDay, double cutAndBindAndLoadLaborAmountPerDay, double harvestGrLaborAmountPerDay, double harvestLeafLaborAmountPerDay, double harvestAndMachineLaborAmountPerDay, double machineShakingLaborAmountPerDay, double uprootHarvestLaborAmountPerDecare, double mowingLaborAmountPerDecare, double motorizedCuttingLaborAmount) {
        return harvestAndPackingLaborAmountPerDay + harvestAndCleanLaborAmountPerDay + shakeAndCrateLaborAmountPerDay + cutAndBindLaborAmountPerDay + cutAndLoadLaborAmountPerDay + harvestAndLoadLaborAmountPerDay + harvestAndBindLaborAmountPerDay + cutAndBindAndLoadLaborAmountPerDay + harvestGrLaborAmountPerDay + harvestLeafLaborAmountPerDay + harvestAndMachineLaborAmountPerDay + machineShakingLaborAmountPerDay + uprootHarvestLaborAmountPerDecare + mowingLaborAmountPerDecare + motorizedCuttingLaborAmount;
    }

    //11400,97724
    private BigDecimal calculateHarvestLaborCost(double harvestAndPackingLaborAmountPerDay, BigDecimal workingMixedLaborPrice,
                                                 double harvestAndCleanLaborAmountPerDay,
                                                 double shakeAndCrateLaborAmountPerDay,
                                                 double cutAndBindLaborAmountPerDay,
                                                 double cutAndLoadLaborAmountPerDay,
                                                 double harvestAndLoadLaborAmountPerDay,
                                                 double harvestAndBindLaborAmountPerDay,
                                                 double cutAndBindAndLoadLaborAmountPerDay,
                                                 double harvestGrLaborAmountPerDay,
                                                 double harvestLeafLaborAmountPerDay,
                                                 double harvestAndMachineLaborAmountPerDay,
                                                 double machineShakingLaborAmountPerDay,
                                                 double uprootHarvestLaborAmountPerDecare,
                                                 double mowingLaborAmountPerDecare, BigDecimal workingManLaborPrice,
                                                 double motorizedCuttingLaborAmount, BigDecimal pruneDailyWage) {
        return BigDecimal.valueOf(harvestAndPackingLaborAmountPerDay).multiply(workingMixedLaborPrice)
                .add(BigDecimal.valueOf(harvestAndCleanLaborAmountPerDay).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(shakeAndCrateLaborAmountPerDay).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(cutAndBindLaborAmountPerDay).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(cutAndLoadLaborAmountPerDay).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(harvestAndLoadLaborAmountPerDay).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(harvestAndBindLaborAmountPerDay).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(cutAndBindAndLoadLaborAmountPerDay).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(harvestGrLaborAmountPerDay).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(harvestLeafLaborAmountPerDay).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(harvestAndMachineLaborAmountPerDay).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(machineShakingLaborAmountPerDay).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(uprootHarvestLaborAmountPerDecare).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(mowingLaborAmountPerDecare).multiply(workingManLaborPrice))
                .add(BigDecimal.valueOf(motorizedCuttingLaborAmount).multiply(pruneDailyWage));
    }

    //9270
    private BigDecimal calculateHarvestLumpSumCost(BigDecimal harvesterRentalPricePerDecare,
                                                   BigDecimal demolitionRentalPricePerDecare,
                                                   BigDecimal cottonRentalPricePerDecare,
                                                   BigDecimal silageRentalPricePerDecare,
                                                   BigDecimal harvestRentalPricePerDecare,
                                                   BigDecimal seederRentalPricePerDecare,
                                                   BigDecimal lumpSumCostPerTonne,
                                                   BigDecimal lumpSumHarvestCostPerTonne) {
        return harvesterRentalPricePerDecare
                .add(demolitionRentalPricePerDecare)
                .add(cottonRentalPricePerDecare)
                .add(silageRentalPricePerDecare)
                .add(harvestRentalPricePerDecare)
                .add(seederRentalPricePerDecare)
                .add(lumpSumCostPerTonne)
                .add(lumpSumHarvestCostPerTonne);
    }

    @Transactional
    public void harvestAllocation(List<PlantationProductQuestion> productQuestionList, UserPlantParcelPlan parcelPlan, City city) {
        List<UserPlantParcelPlanAllocation> planAllocationList = new ArrayList<>();
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlan.getId(), List.of(12L, 13L, 15L, 16L, 24L, 41L, 42L, 43L));
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());

        Integer harvestCount = integerValueSetter(productQuestionList, 209L);
        int byHandHarvestAmountPerDayCount = doubleValueChecker(productQuestionList, 219L);
        int byMachineHarvestAmountPerDayCount = doubleValueChecker(productQuestionList, 220L);
        int shakeAndCrateInputAmountCount = doubleValueChecker(productQuestionList, 221L) * harvestCount;
        int cutAndBindInputAmountCount = doubleValueChecker(productQuestionList, 222L) * harvestCount;
        int cutAndLoadInputAmountCount = doubleValueChecker(productQuestionList, 223L) * harvestCount;
        int harvestAndLoadInputAmountCount = doubleValueChecker(productQuestionList, 224L) * harvestCount;
        int harvestAndBindInputAmountCount = doubleValueChecker(productQuestionList, 225L) * harvestCount;
        int cutAndBindAndLoadInputAmountCount = doubleValueChecker(productQuestionList, 226L) * harvestCount;
        int harvestGrInputAmountCount = doubleValueChecker(productQuestionList, 227L) * harvestCount;
        int harvestLeafInputAmountCount = doubleValueChecker(productQuestionList, 228L) * harvestCount;
        int harvesterRentalPricePerDecareCount = decimalValueChecker(productQuestionList, 229L) * harvestCount;
        int demolitionRentalPricePerDecareCount = decimalValueChecker(productQuestionList, 230L) * harvestCount;
        int cottonRentalPricePerDecareCount = decimalValueChecker(productQuestionList, 231L) * harvestCount;
        int silageRentalPricePerDecareCount = decimalValueChecker(productQuestionList, 232L) * harvestCount;
        int harvestRentalPricePerDecareCount = decimalValueChecker(productQuestionList, 233L) * harvestCount;
        int seederRentalPricePerDecareCount = decimalValueChecker(productQuestionList, 234L) * harvestCount;
        int havestAndMachineDieselInputAmountCount = doubleValueChecker(productQuestionList, 236L) * harvestCount;
        int machineShakingDieselInputAmountCount = doubleValueChecker(productQuestionList, 238L) * harvestCount;
        int uprootHarvestLaborAmountPerDecareCount = doubleValueChecker(productQuestionList, 240L) * harvestCount;
        int lumpSumCostPerTonneCount = decimalValueChecker(productQuestionList, 241L) * harvestCount;
        int lumpSumHarvestCostPerTonneCount = decimalValueChecker(productQuestionList, 242L) * harvestCount;
        int mowingDieselAmountPerDecareCount = doubleValueChecker(productQuestionList, 244L) * harvestCount;
        int motorizedCuttingLaborAmountCount = doubleValueChecker(productQuestionList, 246L) * harvestCount;
        Integer transactionCount = calculateHarvestTransaction(
                byHandHarvestAmountPerDayCount,
                byMachineHarvestAmountPerDayCount,
                shakeAndCrateInputAmountCount,
                cutAndBindInputAmountCount,
                cutAndLoadInputAmountCount,
                harvestAndLoadInputAmountCount,
                harvestAndBindInputAmountCount,
                cutAndBindAndLoadInputAmountCount,
                harvestGrInputAmountCount,
                harvestLeafInputAmountCount,
                harvesterRentalPricePerDecareCount,
                demolitionRentalPricePerDecareCount,
                cottonRentalPricePerDecareCount,
                silageRentalPricePerDecareCount,
                harvestRentalPricePerDecareCount,
                seederRentalPricePerDecareCount,
                havestAndMachineDieselInputAmountCount,
                machineShakingDieselInputAmountCount,
                uprootHarvestLaborAmountPerDecareCount,
                lumpSumCostPerTonneCount,
                lumpSumHarvestCostPerTonneCount,
                mowingDieselAmountPerDecareCount,
                motorizedCuttingLaborAmountCount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_HARVEST, EnumAllocationType.TRANSACTION_COUNT, new BigDecimal(transactionCount)));

        double havestAndMachineDieselInputAmount = doubleValueSetter(productQuestionList, 236L);
        double machineShakingDieselInputAmount = doubleValueSetter(productQuestionList, 238L);
        double uprootDieselAmountPerDecare = doubleValueSetter(productQuestionList, 239L);
        double mowingDieselAmountPerDecare = doubleValueSetter(productQuestionList, 244L);
        double motorizedCuttingDieselAmount = doubleValueSetter(productQuestionList, 245L);
        Double dieselAmount = calculateHarvestDieselAmount(
                havestAndMachineDieselInputAmount,
                machineShakingDieselInputAmount,
                uprootDieselAmountPerDecare,
                mowingDieselAmountPerDecare,
                motorizedCuttingDieselAmount
        );
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_HARVEST, EnumAllocationType.DIESEL_AMOUNT, new BigDecimal(dieselAmount)));

        BigDecimal dieselCost = calculateHarvestDieselCost(dieselAmount, city.getDieselPrice());
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_HARVEST, EnumAllocationType.DIESEL_COST, dieselCost));

        double averageYieldAsKgPerDecare = doubleAnswerSetter(previousAnswerList, 12L);
        double byHandHarvestAmountPerDay = doubleValueSetter(productQuestionList, 219L);
        double harvestAndPackingLaborAmountPerDay = harvestAndPackingLaborAmountPerDay(averageYieldAsKgPerDecare, byHandHarvestAmountPerDay);
        double mainProductKgYieldAsUnitPerDecare = doubleAnswerSetter(previousAnswerList, 15L);
        double byMachineHarvestAmountPerDay = doubleValueSetter(productQuestionList, 220L);
        double harvestAndCleanLaborAmountPerDay = harvestAndCleanLaborAmountPerDay(mainProductKgYieldAsUnitPerDecare, byMachineHarvestAmountPerDay);
        double shakeAndCrateInputAmount = doubleValueSetter(productQuestionList, 221L);
        double shakeAndCrateLaborAmountPerDay = shakeAndCrateLaborAmountPerDay(averageYieldAsKgPerDecare, shakeAndCrateInputAmount);
        double cutAndBindInputAmount = doubleValueSetter(productQuestionList, 222L);
        double cutAndBindLaborAmountPerDay = cutAndBindLaborAmountPerDay(averageYieldAsKgPerDecare, cutAndBindInputAmount);
        double cutAndLoadInputAmount = doubleValueSetter(productQuestionList, 223L);
        double cutAndLoadLaborAmountPerDay = cutAndLoadLaborAmountPerDay(averageYieldAsKgPerDecare, cutAndLoadInputAmount);
        double harvestAndLoadInputAmount = doubleValueSetter(productQuestionList, 224L);
        double harvestAndLoadLaborAmountPerDay = harvestAndLoadLaborAmountPerDay(averageYieldAsKgPerDecare, harvestAndLoadInputAmount);
        double averageExpectedYieldAsBundlePerDecare = doubleAnswerSetter(previousAnswerList, 16L);
        double harvestAndBindInputAmount = doubleValueSetter(productQuestionList, 225L);
        double harvestAndBindLaborAmountPerDay = harvestAndBindLaborAmountPerDay(averageExpectedYieldAsBundlePerDecare, harvestAndBindInputAmount);
        double cutAndBindAndLoadInputAmount = doubleValueSetter(productQuestionList, 226L);
        double cutAndBindAndLoadLaborAmountPerDay = cutAndBindAndLoadLaborAmountPerDay(mainProductKgYieldAsUnitPerDecare, cutAndBindAndLoadInputAmount);
        double mainProductKgYieldAsGrPerDecare = doubleAnswerSetter(previousAnswerList, 13L);
        double harvestGrInputAmount = doubleValueSetter(productQuestionList, 227L);
        double harvestGrLaborAmountPerDay = harvestGrLaborAmountPerDay(mainProductKgYieldAsGrPerDecare, harvestGrInputAmount);
        double leafProductKgYieldAsGrPerDecare = doubleAnswerSetter(previousAnswerList, 24L);
        double harvestLeafInputAmount = doubleValueSetter(productQuestionList, 228L);
        double harvestLeafLaborAmountPerDay = harvestLeafLaborAmountPerDay(leafProductKgYieldAsGrPerDecare, harvestLeafInputAmount);
        double harvestAndMachineInputAmount = doubleValueSetter(productQuestionList, 235L);
        double harvestAndMachineLaborAmountPerDay = harvestAndMachineLaborAmountPerDay(averageYieldAsKgPerDecare, harvestAndMachineInputAmount);
        double machineShakingInputAmount = doubleValueSetter(productQuestionList, 237L);
        double machineShakingLaborAmountPerDay = machineShakingLaborAmountPerDay(averageYieldAsKgPerDecare, machineShakingInputAmount);
        double uprootHarvestLaborAmountPerDecare = doubleValueSetter(productQuestionList, 240L);
        double mowingLaborAmountPerDecare = doubleValueSetter(productQuestionList, 243L);
        double motorizedCuttingLaborAmount = doubleValueSetter(productQuestionList, 246L);
        Double laborAmount = calculateHarvestLaborAmount(
                harvestAndPackingLaborAmountPerDay, harvestAndCleanLaborAmountPerDay, shakeAndCrateLaborAmountPerDay,
                cutAndBindLaborAmountPerDay, cutAndLoadLaborAmountPerDay, harvestAndLoadLaborAmountPerDay,
                harvestAndBindLaborAmountPerDay, cutAndBindAndLoadLaborAmountPerDay, harvestGrLaborAmountPerDay,
                harvestLeafLaborAmountPerDay, harvestAndMachineLaborAmountPerDay, machineShakingLaborAmountPerDay,
                uprootHarvestLaborAmountPerDecare, mowingLaborAmountPerDecare, motorizedCuttingLaborAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_HARVEST, EnumAllocationType.LABOR_AMOUNT, new BigDecimal(laborAmount)));

        double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        double maleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        double femaleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.FEMALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        BigDecimal femaleDailyWage = decimalAnswerSetter(previousAnswerList, 42L);
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);
        BigDecimal workingMixedLaborPrice = workingMixedLaborPrice(maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay);
        BigDecimal pruneDailyWage = decimalAnswerSetter(previousAnswerList, 43L);
        BigDecimal laborCost = calculateHarvestLaborCost(
                harvestAndPackingLaborAmountPerDay, workingMixedLaborPrice,
                harvestAndCleanLaborAmountPerDay,
                shakeAndCrateLaborAmountPerDay,
                cutAndBindLaborAmountPerDay,
                cutAndLoadLaborAmountPerDay,
                harvestAndLoadLaborAmountPerDay,
                harvestAndBindLaborAmountPerDay,
                cutAndBindAndLoadLaborAmountPerDay,
                harvestGrLaborAmountPerDay,
                harvestLeafLaborAmountPerDay,
                harvestAndMachineLaborAmountPerDay,
                machineShakingLaborAmountPerDay,
                uprootHarvestLaborAmountPerDecare,
                mowingLaborAmountPerDecare, workingManLaborPrice,
                motorizedCuttingLaborAmount, pruneDailyWage
        );
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_HARVEST, EnumAllocationType.LABOR_COST, laborCost));

        BigDecimal harvesterRentalPricePerDecare = decimalValueSetter(productQuestionList, 229L);
        BigDecimal demolitionRentalPricePerDecare = decimalValueSetter(productQuestionList, 230L);
        BigDecimal cottonRentalPricePerDecare = decimalValueSetter(productQuestionList, 231L);
        BigDecimal silageRentalPricePerDecare = decimalValueSetter(productQuestionList, 232L);
        BigDecimal harvestRentalPricePerDecare = decimalValueSetter(productQuestionList, 233L);
        BigDecimal seederRentalPricePerDecare = decimalValueSetter(productQuestionList, 234L);
        BigDecimal lumpSumCostPerTonne = decimalValueSetter(productQuestionList, 241L);
        BigDecimal lumpSumHarvestCostPerTonne = decimalValueSetter(productQuestionList, 242L);
        BigDecimal lumpSumCost = calculateHarvestLumpSumCost(
                harvesterRentalPricePerDecare,
                demolitionRentalPricePerDecare,
                cottonRentalPricePerDecare,
                silageRentalPricePerDecare,
                harvestRentalPricePerDecare,
                seederRentalPricePerDecare,
                lumpSumCostPerTonne,
                lumpSumHarvestCostPerTonne);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_HARVEST, EnumAllocationType.LUMP_SUM_COST, lumpSumCost));

        saveAllocationList(parcelPlan.getId(), EnumPlantationQuestionType.EXPENSE_HARVEST, planAllocationList);
    }

    //6
    private Integer calculateBlendTransaction(int transportKmAmountCount, int blendAmountPerDayCount, int cureAmountPerDayCount, int sortAmountPerDayCount, int blendThreshingCostPerDecareCount, int threshingCostPerHourCount) {
        return transportKmAmountCount + blendAmountPerDayCount + cureAmountPerDayCount + sortAmountPerDayCount + blendThreshingCostPerDecareCount + threshingCostPerHourCount;
    }

    //86,25
    private BigDecimal calculateBlendDieselCost(Double dieselAmount, BigDecimal dieselPrice) {
        return BigDecimal.valueOf(dieselAmount).multiply(dieselPrice);
    }

    //1,54
    private Double calculateBlendLaborAmount(double blendLaborAmountPerDay, double cureLaborAmountPerDay, double sortLaborAmountPerDay) {
        return blendLaborAmountPerDay + cureLaborAmountPerDay + sortLaborAmountPerDay;
    }

    //372,75
    private BigDecimal calculateBlendLaborCost(double blendLaborAmountPerDay, BigDecimal workingManLaborPrice,
                                               double cureLaborAmountPerDay, BigDecimal workingMixedLaborPrice,
                                               double sortLaborAmountPerDay, BigDecimal workingWomanLaborPrice) {
        return BigDecimal.valueOf(blendLaborAmountPerDay).multiply(workingManLaborPrice)
                .add(BigDecimal.valueOf(cureLaborAmountPerDay).multiply(workingMixedLaborPrice))
                .add(BigDecimal.valueOf(sortLaborAmountPerDay).multiply(workingWomanLaborPrice));
    }

    //968
    private BigDecimal calculateBlendLumpSumCost(double blendThreshingLumpSumAmount, BigDecimal blendThreshingCostPerDecare,
                                                 double blendThreshingAmount, BigDecimal threshingCostPerHour) {
        return BigDecimal.valueOf(blendThreshingLumpSumAmount).multiply(blendThreshingCostPerDecare)
                .add(BigDecimal.valueOf(blendThreshingAmount).multiply(threshingCostPerHour));
    }

    @Transactional
    public void blendAllocation(List<PlantationProductQuestion> productQuestionList, UserPlantParcelPlan parcelPlan, City city) {
        List<UserPlantParcelPlanAllocation> planAllocationList = new ArrayList<>();
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlan.getId(), List.of(12L, 13L, 41L, 42L));
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());

        int transportKmAmountCount = doubleValueChecker(productQuestionList, 247L);
        int blendAmountPerDayCount = doubleValueChecker(productQuestionList, 249L);
        int cureAmountPerDayCount = doubleValueChecker(productQuestionList, 250L);
        int sortAmountPerDayCount = doubleValueChecker(productQuestionList, 251L);
        int blendThreshingCostPerDecareCount = decimalValueChecker(productQuestionList, 253L);
        int threshingCostPerHourCount = decimalValueChecker(productQuestionList, 255L);
        Integer transactionCount = calculateBlendTransaction(
                transportKmAmountCount,
                blendAmountPerDayCount,
                cureAmountPerDayCount,
                sortAmountPerDayCount,
                blendThreshingCostPerDecareCount,
                threshingCostPerHourCount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_BLEND, EnumAllocationType.TRANSACTION_COUNT, new BigDecimal(transactionCount)));

        double transportKmAmount = doubleValueSetter(productQuestionList, 247L);
        double averageYieldAsKgPerDecare = doubleAnswerSetter(previousAnswerList, 12L);
        double tractorLoadCapacity = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.TRACTOR_CARRYING_CAPACITY)).findFirst().get().getLaborValue();
        Double dieselAmount = blendTransportDieselAmount(transportKmAmount, averageYieldAsKgPerDecare, tractorLoadCapacity);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_BLEND, EnumAllocationType.DIESEL_AMOUNT, new BigDecimal(dieselAmount)));

        BigDecimal dieselCost = calculateBlendDieselCost(dieselAmount, city.getDieselPrice());
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_BLEND, EnumAllocationType.DIESEL_COST, dieselCost));

        double blendAmountPerDay = doubleValueSetter(productQuestionList, 249L);
        double blendLaborAmountPerDay = blendLaborAmountPerDay(averageYieldAsKgPerDecare, blendAmountPerDay);
        double cureAmountPerDay = doubleValueSetter(productQuestionList, 250L);
        double cureLaborAmountPerDay = cureLaborAmountPerDay(averageYieldAsKgPerDecare, cureAmountPerDay);
        double yieldAsGrPerDecare = doubleAnswerSetter(previousAnswerList, 13L);
        double sortAmountPerDay = doubleValueSetter(productQuestionList, 251L);
        double sortLaborAmountPerDay = sortLaborAmountPerDay(yieldAsGrPerDecare, sortAmountPerDay);
        Double laborAmount = calculateBlendLaborAmount(blendLaborAmountPerDay, cureLaborAmountPerDay, sortLaborAmountPerDay);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_BLEND, EnumAllocationType.LABOR_AMOUNT, new BigDecimal(laborAmount)));


        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        BigDecimal femaleDailyWage = decimalAnswerSetter(previousAnswerList, 42L);
        double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        double maleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        double femaleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.FEMALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);
        BigDecimal workingWomanLaborPrice = workingWomanLaborPrice(femaleDailyWage, workHoursPerDay);
        BigDecimal workingMixedLaborPrice = workingMixedLaborPrice(maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay);
        BigDecimal laborCost = calculateBlendLaborCost(
                blendLaborAmountPerDay, workingManLaborPrice,
                cureLaborAmountPerDay, workingMixedLaborPrice,
                sortLaborAmountPerDay, workingWomanLaborPrice);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_BLEND, EnumAllocationType.LABOR_COST, laborCost));

        double blendThreshingLumpSumAmount = 1;
        BigDecimal blendThreshingCostPerDecare = decimalValueSetter(productQuestionList, 253L);
        double blendThreshingAmountPerHour = doubleValueSetter(productQuestionList, 254L);
        double blendThreshingAmount = blendThreshingAmount(averageYieldAsKgPerDecare, blendThreshingAmountPerHour);
        BigDecimal threshingCostPerHour = decimalValueSetter(productQuestionList, 255L);
        BigDecimal lumpSumCost = calculateBlendLumpSumCost(
                blendThreshingLumpSumAmount, blendThreshingCostPerDecare,
                blendThreshingAmount, threshingCostPerHour);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_BLEND, EnumAllocationType.LUMP_SUM_COST, lumpSumCost));

        saveAllocationList(parcelPlan.getId(), EnumPlantationQuestionType.EXPENSE_BLEND, planAllocationList);
    }


    //12
    private Integer calculateDryingTransaction(int processSievingWashDryHourAmountPerTonneCount, int processSievingWashDryDieselAmountPerTonneCount, int processSievingWashDryLaborHourAmountCount, int processSievingWashDryHourAmountPer1000Count, int processSievingWashDrySulfurizeHourPerTonneCount, int processSievingWashDryDipHourPerTonneCount, int processSievingWashDryStringHourPerTonneCount, int processSievingWashDryProcessPerDayCount, int processDryMachinePricePerTonneCount, int processSulfuringMachinePricePerTonneCount, int materialUnitPriceCount, int sortSizeScoreBrineLaborHourCount) {
        return processSievingWashDryHourAmountPerTonneCount + processSievingWashDryDieselAmountPerTonneCount + processSievingWashDryLaborHourAmountCount + processSievingWashDryHourAmountPer1000Count + processSievingWashDrySulfurizeHourPerTonneCount + processSievingWashDryDipHourPerTonneCount + processSievingWashDryStringHourPerTonneCount + processSievingWashDryProcessPerDayCount + processDryMachinePricePerTonneCount + processSulfuringMachinePricePerTonneCount + materialUnitPriceCount + sortSizeScoreBrineLaborHourCount;
    }

    //258,75
    private BigDecimal calculateDryingDieselCost(Double dieselAmount, BigDecimal dieselPrice) {
        return BigDecimal.valueOf(dieselAmount).multiply(dieselPrice);
    }

    //85,55
    private Double calculateDryingLaborAmount(double processSievingWashDryLaborAmount, double processSievingWashDryLaborAmountPerBunch, double processSievingWashDryLaborAmountSeparation, double processSievingWashDrySulfurizeLaborAmount, double processSievingWashDryDipLaborAmount, double processSievingWashDryStringLaborAmount, double processSievingWashDryProcessLabor, double sortSizeScoreBrineLaborAmount) {
        return processSievingWashDryLaborAmount + processSievingWashDryLaborAmountPerBunch + processSievingWashDryLaborAmountSeparation + processSievingWashDrySulfurizeLaborAmount + processSievingWashDryDipLaborAmount + processSievingWashDryStringLaborAmount + processSievingWashDryProcessLabor + sortSizeScoreBrineLaborAmount;
    }

    //19675,73333
    private BigDecimal calculateDryingLaborCost(Double laborAmount, BigDecimal workingMixedLaborPrice) {
        return BigDecimal.valueOf(laborAmount).multiply(workingMixedLaborPrice);
    }

    //92
    private BigDecimal calculateDryingMaterialCost(double materialInputAmount, BigDecimal materialUnitPrice) {
        return BigDecimal.valueOf(materialInputAmount).multiply(materialUnitPrice);
    }

    //1380
    private BigDecimal calculateDryingLumpSumCost(double processSievingWashDryLumpSumAmount, BigDecimal processDryMachinePricePerTonne,
                                                  BigDecimal processSulfuringMachinePricePerTonne) {
        return BigDecimal.valueOf(processSievingWashDryLumpSumAmount).multiply(processDryMachinePricePerTonne)
                .add(BigDecimal.valueOf(processSievingWashDryLumpSumAmount).multiply(processSulfuringMachinePricePerTonne));
    }

    @Transactional
    public void dryingAllocation(List<PlantationProductQuestion> productQuestionList, UserPlantParcelPlan parcelPlan, City city) {
        List<UserPlantParcelPlanAllocation> planAllocationList = new ArrayList<>();
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlan.getId(), List.of(12L, 15L, 16L, 41L, 42L));
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());

        int processSievingWashDryHourAmountPerTonneCount = doubleValueChecker(productQuestionList, 259L);
        int processSievingWashDryDieselAmountPerTonneCount = doubleValueChecker(productQuestionList, 260L);
        int processSievingWashDryLaborHourAmountCount = doubleValueChecker(productQuestionList, 261L);
        int processSievingWashDryHourAmountPer1000Count = doubleValueChecker(productQuestionList, 262L);
        int processSievingWashDrySulfurizeHourPerTonneCount = doubleValueChecker(productQuestionList, 263L);
        int processSievingWashDryDipHourPerTonneCount = doubleValueChecker(productQuestionList, 264L);
        int processSievingWashDryStringHourPerTonneCount = doubleValueChecker(productQuestionList, 265L);
        int processSievingWashDryProcessPerDayCount = doubleValueChecker(productQuestionList, 266L);
        int processDryMachinePricePerTonneCount = decimalValueChecker(productQuestionList, 267L);
        int processSulfuringMachinePricePerTonneCount = decimalValueChecker(productQuestionList, 268L);
        int materialUnitPriceCount = decimalValueChecker(productQuestionList, 271L);
        int sortSizeScoreBrineLaborHourCount = doubleValueChecker(productQuestionList, 272L);
        Integer transactionCount = calculateDryingTransaction(
                processSievingWashDryHourAmountPerTonneCount,
                processSievingWashDryDieselAmountPerTonneCount,
                processSievingWashDryLaborHourAmountCount,
                processSievingWashDryHourAmountPer1000Count,
                processSievingWashDrySulfurizeHourPerTonneCount,
                processSievingWashDryDipHourPerTonneCount,
                processSievingWashDryStringHourPerTonneCount,
                processSievingWashDryProcessPerDayCount,
                processDryMachinePricePerTonneCount,
                processSulfuringMachinePricePerTonneCount,
                materialUnitPriceCount,
                sortSizeScoreBrineLaborHourCount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_DRYING, EnumAllocationType.TRANSACTION_COUNT, new BigDecimal(transactionCount)));

        double averageYieldAsKgPerDecare = doubleAnswerSetter(previousAnswerList, 12L);
        double processSievingWashDryDieselAmountPerTonne = doubleValueSetter(productQuestionList, 260L);
        Double dieselAmount = processSievingWashDryDieselAmount(averageYieldAsKgPerDecare, processSievingWashDryDieselAmountPerTonne);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_DRYING, EnumAllocationType.DIESEL_AMOUNT, new BigDecimal(dieselAmount)));

        BigDecimal dieselCost = calculateDryingDieselCost(dieselAmount, city.getDieselPrice());
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_DRYING, EnumAllocationType.DIESEL_COST, dieselCost));

        double processSievingWashDryHourAmountPerTonne = doubleValueSetter(productQuestionList, 259L);
        double processSievingWashDryLaborAmount = processSievingWashDryLaborAmount(averageYieldAsKgPerDecare, processSievingWashDryHourAmountPerTonne);
        double averageExpectedYieldAsUnitPerDecare = doubleAnswerSetter(previousAnswerList, 15L);
        double processSievingWashDryLaborHourAmount = doubleValueSetter(productQuestionList, 261L);
        double processSievingWashDryLaborAmountPerBunch = processSievingWashDryLaborAmountPerBunch(averageExpectedYieldAsUnitPerDecare, processSievingWashDryLaborHourAmount);
        double averageExpectedYieldAsBundlePerDecare = doubleAnswerSetter(previousAnswerList, 16L);
        double processSievingWashDryHourAmountPer1000 = doubleValueSetter(productQuestionList, 262L);
        double processSievingWashDryLaborAmountSeparation = processSievingWashDryLaborAmountSeparation(averageExpectedYieldAsBundlePerDecare, processSievingWashDryHourAmountPer1000);
        double processSievingWashDrySulfurizeHourPerTonne = doubleValueSetter(productQuestionList, 263L);
        double processSievingWashDrySulfurizeLaborAmount = processSievingWashDrySulfurizeLaborAmount(averageYieldAsKgPerDecare, processSievingWashDrySulfurizeHourPerTonne);
        double processSievingWashDryDipHourPerTonne = doubleValueSetter(productQuestionList, 264L);
        double processSievingWashDryDipLaborAmount = processSievingWashDryDipLaborAmount(averageYieldAsKgPerDecare, processSievingWashDryDipHourPerTonne);
        double processSievingWashDryStringHourPerTonne = doubleValueSetter(productQuestionList, 265L);
        double processSievingWashDryStringLaborAmount = processSievingWashDryStringLaborAmount(averageYieldAsKgPerDecare, processSievingWashDryStringHourPerTonne);
        double processSievingWashDryProcessPerDay = doubleValueSetter(productQuestionList, 266L);
        double processSievingWashDryProcessLabor = processSievingWashDryProcessLabor(averageExpectedYieldAsUnitPerDecare, processSievingWashDryProcessPerDay);
        double sortSizeScoreBrineLaborHour = doubleValueSetter(productQuestionList, 272L);
        double sortSizeScoreBrineLaborAmount = sortSizeScoreBrineLaborAmount(averageYieldAsKgPerDecare, sortSizeScoreBrineLaborHour);
        Double laborAmount = calculateDryingLaborAmount(
                processSievingWashDryLaborAmount,
                processSievingWashDryLaborAmountPerBunch,
                processSievingWashDryLaborAmountSeparation,
                processSievingWashDrySulfurizeLaborAmount,
                processSievingWashDryDipLaborAmount,
                processSievingWashDryStringLaborAmount,
                processSievingWashDryProcessLabor,
                sortSizeScoreBrineLaborAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_DRYING, EnumAllocationType.LABOR_AMOUNT, new BigDecimal(laborAmount)));

        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        BigDecimal femaleDailyWage = decimalAnswerSetter(previousAnswerList, 42L);
        double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        double maleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.MALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        double femaleCostRate = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.FEMALE_WORKER_RATIO)).findFirst().get().getLaborValue();
        BigDecimal workingMixedLaborPrice = workingMixedLaborPrice(maleDailyWage, maleCostRate, femaleDailyWage, femaleCostRate, workHoursPerDay);
        BigDecimal laborCost = calculateDryingLaborCost(laborAmount, workingMixedLaborPrice);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_DRYING, EnumAllocationType.LABOR_COST, laborCost));

        double materialInputAmountPerTonne = doubleValueSetter(productQuestionList, 270L);
        double materialInputAmount = materialInputAmount(averageYieldAsKgPerDecare, materialInputAmountPerTonne);
        BigDecimal materialUnitPrice = decimalValueSetter(productQuestionList, 271L);
        BigDecimal materialCost = calculateDryingMaterialCost(materialInputAmount, materialUnitPrice);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_DRYING, EnumAllocationType.MATERIAL_COST, materialCost));

        double processSievingWashDryLumpSumAmount = processSievingWashDryLumpSumAmount(averageYieldAsKgPerDecare);
        BigDecimal processDryMachinePricePerTonne = decimalValueSetter(productQuestionList, 267L);
        BigDecimal processSulfuringMachinePricePerTonne = decimalValueSetter(productQuestionList, 268L);
        BigDecimal lumpSumCost = calculateDryingLumpSumCost(processSievingWashDryLumpSumAmount, processDryMachinePricePerTonne, processSulfuringMachinePricePerTonne);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_DRYING, EnumAllocationType.LUMP_SUM_COST, lumpSumCost));

        saveAllocationList(parcelPlan.getId(), EnumPlantationQuestionType.EXPENSE_DRYING, planAllocationList);
    }

    //1,4
    private Double calculateBalingDieselAmount(double balingMachineDieselAmountPerDecare) {
        return balingMachineDieselAmountPerDecare + balingMachineDieselAmountPerDecare;
    }

    //105
    private BigDecimal calculateBalingDieselCost(Double dieselAmount, BigDecimal dieselPrice) {
        return BigDecimal.valueOf(dieselAmount).multiply(dieselPrice);
    }

    //1
    private Double calculateBalingLaborAmount(double balingMachineHourAmountPerDecare) {
        return balingMachineHourAmountPerDecare + balingMachineHourAmountPerDecare;
    }

    //250
    private BigDecimal calculateBalingLaborCost(Double laborAmount, BigDecimal workingManLaborPrice) {
        return BigDecimal.valueOf(laborAmount).multiply(workingManLaborPrice);
    }

    //139,3
    private BigDecimal calculateBalingMaterialCost(double balingMaterialInputAmount, BigDecimal balingMaterialInputPrice,
                                                   double balingMaterialInputKgPriceAmount) {
        return BigDecimal.valueOf(balingMaterialInputAmount).multiply(balingMaterialInputPrice)
                .add(BigDecimal.valueOf(balingMaterialInputKgPriceAmount).multiply(balingMaterialInputPrice));
    }

    //527,2727273
    private BigDecimal calculateBalingLumpSumCost(double balingAverageInputAmount, BigDecimal rentPricePerBaling,
                                                  double balingRentalInputAmount) {
        return BigDecimal.valueOf(balingAverageInputAmount).multiply(rentPricePerBaling)
                .add(BigDecimal.valueOf(balingRentalInputAmount).multiply(rentPricePerBaling));
    }

    @Transactional
    public void balingAllocation(List<PlantationProductQuestion> productQuestionList, UserPlantParcelPlan parcelPlan, City city) {
        List<UserPlantParcelPlanAllocation> planAllocationList = new ArrayList<>();
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlan.getId(), List.of(12L, 20L, 41L));
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());

        Integer transactionCount = 4;//TODO
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_BALING, EnumAllocationType.TRANSACTION_COUNT, new BigDecimal(transactionCount)));

        double balingMachineDieselAmountPerDecare = doubleValueSetter(productQuestionList, 276L);
        Double dieselAmount = calculateBalingDieselAmount(balingMachineDieselAmountPerDecare);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_BALING, EnumAllocationType.DIESEL_AMOUNT, new BigDecimal(dieselAmount)));

        BigDecimal dieselCost = calculateBalingDieselCost(dieselAmount, city.getDieselPrice());
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_BALING, EnumAllocationType.DIESEL_COST, dieselCost));

        double balingMachineHourAmountPerDecare = doubleValueSetter(productQuestionList, 277L);
        Double laborAmount = calculateBalingLaborAmount(balingMachineHourAmountPerDecare);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_BALING, EnumAllocationType.LABOR_AMOUNT, new BigDecimal(laborAmount)));

        BigDecimal maleDailyWage = decimalAnswerSetter(previousAnswerList, 41L);
        double workHoursPerDay = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.WORKING_HOURS)).findFirst().get().getLaborValue();
        BigDecimal workingManLaborPrice = workingManLaborPrice(maleDailyWage, workHoursPerDay);
        BigDecimal laborCost = calculateBalingLaborCost(laborAmount, workingManLaborPrice);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_BALING, EnumAllocationType.LABOR_COST, laborCost));

        double averageYieldAsKgPerDecare = doubleAnswerSetter(previousAnswerList, 12L);
        double balingMaterialAmountPerTonne = doubleValueSetter(productQuestionList, 278L);
        double balingMaterialInputAmount = balingMaterialInputAmount(averageYieldAsKgPerDecare, balingMaterialAmountPerTonne);
        double averageYieldSideStrawProduct = doubleAnswerSetter(previousAnswerList, 20L);
        BigDecimal balingMaterialInputPrice = decimalValueSetter(productQuestionList, 279L);
        double balingMaterialInputKgPriceAmount = balingMaterialInputKgPriceAmount(averageYieldSideStrawProduct, balingMaterialInputPrice);
        BigDecimal materialCost = calculateBalingMaterialCost(balingMaterialInputAmount, balingMaterialInputPrice, balingMaterialInputKgPriceAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_BALING, EnumAllocationType.MATERIAL_COST, materialCost));

        double balingAverageWeight = doubleValueSetter(productQuestionList, 280L);
        double balingAverageInputAmount = balingAverageInputAmount(averageYieldAsKgPerDecare, balingAverageWeight);
        BigDecimal rentPricePerBaling = decimalValueSetter(productQuestionList, 281L);
        double balingRentalInputAmount = balingRentalInputAmount(averageYieldSideStrawProduct, balingAverageWeight);
        BigDecimal lumpSumCost = calculateBalingLumpSumCost(balingAverageInputAmount, rentPricePerBaling, balingRentalInputAmount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_BALING, EnumAllocationType.LUMP_SUM_COST, lumpSumCost));

        saveAllocationList(parcelPlan.getId(), EnumPlantationQuestionType.EXPENSE_BALING, planAllocationList);
    }

    //2
    private Integer calculatePackagingTransaction(int transportationDistance, int weightPerUnit) {
        return transportationDistance + weightPerUnit;
    }

    //123,3375
    private BigDecimal calculatePackagingDieselCost(Double dieselAmount, BigDecimal dieselPrice) {
        return BigDecimal.valueOf(dieselAmount).multiply(dieselPrice);
    }

    //287,5
    private BigDecimal calculatePackagingMaterialCost(double transportLumpSumAmount, BigDecimal packagingPricePerUnit) {
        return BigDecimal.valueOf(transportLumpSumAmount).multiply(packagingPricePerUnit);
    }

    @Transactional
    public void packagingAllocation(List<PlantationProductQuestion> productQuestionList, UserPlantParcelPlan parcelPlan, City city) {
        List<UserPlantParcelPlanAllocation> planAllocationList = new ArrayList<>();
        List<UserPlantParcelPlanAnswer> previousAnswerList = planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(parcelPlan.getId(), List.of(12L));
        List<PlantationCoefficient> coefficientList = coefficientRepository.findByEnumCoefficientTypeIn(EnumCoefficientType.values());

        int transportationDistanceCount = doubleValueChecker(productQuestionList, 283L);
        int weightPerUnitCount = doubleValueChecker(productQuestionList, 292L);
        Integer transactionCount = calculatePackagingTransaction(transportationDistanceCount, weightPerUnitCount);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PACKAGING, EnumAllocationType.TRANSACTION_COUNT, new BigDecimal(transactionCount)));

        double averageYieldAsKgPerDecare = doubleAnswerSetter(previousAnswerList, 12L);
        double tractorCapacity = doubleValueSetter(productQuestionList, 284L);
        double tractorTransportCoefficient = coefficientList.stream().filter(c -> c.getEnumCoefficientType().equals(EnumCoefficientType.CARRYING_CAPACITY)).findFirst().get().getLaborValue();
        double transportationDistance = doubleValueSetter(productQuestionList, 283L);
        Double dieselAmount = tractorDieselAmount(averageYieldAsKgPerDecare, tractorCapacity, tractorTransportCoefficient, transportationDistance);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PACKAGING, EnumAllocationType.DIESEL_AMOUNT, new BigDecimal(dieselAmount)));

        BigDecimal dieselCost = calculatePackagingDieselCost(dieselAmount, city.getDieselPrice());
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PACKAGING, EnumAllocationType.DIESEL_COST, dieselCost));

        double weightPerUnit = doubleValueSetter(productQuestionList, 292L);
        double transportLumpSumAmount = transportLumpSumAmount(averageYieldAsKgPerDecare, weightPerUnit);
        BigDecimal packagingPricePerUnit = decimalValueSetter(productQuestionList, 293L);
        BigDecimal materialCost = calculatePackagingMaterialCost(transportLumpSumAmount, packagingPricePerUnit);
        planAllocationList.add(new UserPlantParcelPlanAllocation(parcelPlan, EnumPlantationQuestionType.EXPENSE_PACKAGING, EnumAllocationType.MATERIAL_COST, materialCost));

        saveAllocationList(parcelPlan.getId(), EnumPlantationQuestionType.EXPENSE_PACKAGING, planAllocationList);
    }


    private void saveAllocationList(Long planId, EnumPlantationQuestionType questionType, List<UserPlantParcelPlanAllocation> planAllocationList) {
        planAllocationRepository.deleteByPlantParcelPlan_IdAndQuestionType(planId, questionType);
        planAllocationRepository.saveAll(planAllocationList);
    }
}















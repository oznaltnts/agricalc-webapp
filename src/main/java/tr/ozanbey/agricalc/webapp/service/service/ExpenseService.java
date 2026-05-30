package tr.ozanbey.agricalc.webapp.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropWateringValue;
import tr.ozanbey.agricalc.webapp.service.domain.GeneralCoefficientValue;
import tr.ozanbey.agricalc.webapp.service.dto.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ExpenseService extends BaseService {

    private final GeneralCoefficientService generalCoefficientService;

    public ExpenseService(GeneralCoefficientService generalCoefficientService
    ) {
        this.generalCoefficientService = generalCoefficientService;
    }

    public BigDecimal calculateExpense(List<QuestionWithFirstValue> questionDTOList,
                                       List<CropCoefficientWithFirstValue> cropCoefficientList,
                                       List<DieselDistanceWithFirstValue> dieselDistanceWithValueList,
                                       List<SeedSeedlingWithFirstValue> seedSeedlingNumberWithValueList,
                                       List<SeedSeedlingWithFirstValue> seedSeedlingPriceWithValueList,
                                       List<CityFertilizerWithFirstValue> cityFertilizerWithValueList,
                                       double fieldAverageValue,
                                       Optional<CityCropWateringValue> wateringValueOptional) {
        BigDecimal totalExpense = BigDecimal.ZERO;
        //Sıra arası sürüm sayısı/ yakıt gideri
        BigDecimal orderCountDec = getAsBigDecimalByQuestionId(questionDTOList, 26L);
        //Ekipman sahiplik durumu nedir?
        BigDecimal orderMachineLtDec = getAsBigDecimalByQuestionIdAndValueAndType(questionDTOList, 21L,
                cropCoefficientList, EnumCropCoefficientType.NEEDED_DIESEL, EnumCropCoefficientValue.NEEDED_DIESEL_DEEP);
        // Çalışma saati
        GeneralCoefficientValue workingHour = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Çalışma saati");
        // Amortisman katsayısı
        GeneralCoefficientValue amortization = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Traktör makine amortisman katsayısı");
        //Erkek işçi yevmiyesi ne kadardır?
        BigDecimal orderMachineTLDec = getAsBigDecimalByQuestionIdAndDieselDistancesAndGeneralCoefficients(questionDTOList, 40L,
                dieselDistanceWithValueList, EnumDieselDistanceType.DIESEL_PRICE, EnumDieselDistanceType.PLOWING_FIELD,
                workingHour.getValue(), amortization.getValue());
        //Ekipman sahiplik durumu nedir?
        BigDecimal orderMaterialTLDec = getAsBigDecimalByEquipmentQuestionIdAndQuestionId(questionDTOList, 21L, 22L);
        BigDecimal orderTotal = (orderCountDec.multiply(orderMachineLtDec).multiply(orderMachineTLDec)).add(orderCountDec.multiply(orderMaterialTLDec)).setScale(3, RoundingMode.HALF_UP);
        //Derin sürüm sayısı/ ort. yakıt gideri
        BigDecimal deepCountDec = getAsBigDecimalByQuestionId(questionDTOList, 28L);
        BigDecimal deepMachineLtDec = orderMachineLtDec;
        BigDecimal deepMachineTLDec = orderMachineTLDec;
        //Ekipman sahiplik durumu nedir?
        BigDecimal deepMaterialTLDec = getAsBigDecimalByEquipmentQuestionIdAndQuestionId(questionDTOList, 21L, 24L);
        BigDecimal deepTotal = (deepCountDec.multiply(deepMachineLtDec).multiply(deepMachineTLDec)).add(deepCountDec.multiply(deepMaterialTLDec)).setScale(3, RoundingMode.HALF_UP);
        //Sıra üzeri sürüm sayısı/ yakıt gideri
        BigDecimal overOrderCountDec = getAsBigDecimalByQuestionId(questionDTOList, 27L);
        BigDecimal overOrderMachineLtDec = getAsBigDecimalByQuestionIdAndValueAndType(questionDTOList, 21L,
                cropCoefficientList, EnumCropCoefficientType.NEEDED_DIESEL, EnumCropCoefficientValue.NEEDED_DIESEL_SECOND);
        BigDecimal overOrderMachineTLDec = deepMachineTLDec;
        BigDecimal overOrderMaterialTLDec = getAsBigDecimalByEquipmentQuestionIdAndQuestionId(questionDTOList, 21L, 23L);
        BigDecimal overOrderTotal = (overOrderCountDec.multiply(overOrderMachineLtDec).multiply(overOrderMachineTLDec)).add(overOrderCountDec.multiply(overOrderMaterialTLDec)).setScale(3, RoundingMode.HALF_UP);
        //İkincil işlem sayısı/ ort. yakıt gideri
        BigDecimal secondaryCountDec = getAsBigDecimalByQuestionId(questionDTOList, 29L);
        BigDecimal secondaryMachineLtDec = getAsBigDecimalByQuestionIdAndValueAndType(questionDTOList, 21L,
                cropCoefficientList, EnumCropCoefficientType.NEEDED_DIESEL, EnumCropCoefficientValue.NEEDED_DIESEL_SECOND);
        BigDecimal secondaryMachineTLDec = overOrderMachineTLDec;
        BigDecimal secondaryMaterialTLDec = getAsBigDecimalByEquipmentQuestionIdAndQuestionId(questionDTOList, 21L, 25L);
        BigDecimal secondaryTotal = (secondaryCountDec.multiply(secondaryMachineLtDec).multiply(secondaryMachineTLDec)).add(secondaryCountDec.multiply(secondaryMaterialTLDec)).setScale(3, RoundingMode.HALF_UP);
        //Ekim ne ile yapılmaktadır?
        BigDecimal plantingManHour = getAsBigDecimalByQuestionIdAndValueAndTypesEl(questionDTOList, 37L,
                cropCoefficientList, EnumCropCoefficientType.HOW_MUCH_SEED, EnumCropCoefficientValue.HOW_MUCH_SEEDLING_HAND, EnumCropCoefficientValue.HOW_MUCH_SEEDLING_HAND_AND_MACHINE_HAND);
        //Karışık işçi yevmiyesi ne kadardır?
        BigDecimal plantingManTL = getAsBigDecimalByQuestionIdAndWorkingValue(questionDTOList, 43L, workingHour.getValue());
        //Ekim ne ile yapılmaktadır?
        BigDecimal plantingMachineLt = getAsBigDecimalByQuestionIdAndValueAndTypesMibzer(questionDTOList, 37L,
                cropCoefficientList, EnumCropCoefficientType.HOW_MUCH_SEED, EnumCropCoefficientValue.HOW_MUCH_SEEDLING_MACHINE, EnumCropCoefficientValue.HOW_MUCH_SEEDLING_HAND_AND_MACHINE_MACHINE);
        BigDecimal plantingMachineTL = secondaryMachineTLDec;
        BigDecimal plantingMaterialKg = BigDecimal.ONE;
        //Ekim sahiplik durumu nedir?
        BigDecimal plantingMaterialTL = getAsBigDecimalByEquipmentQuestionIdAndQuestionId(questionDTOList, 38L, 39L);
        BigDecimal plantingTotal = (plantingManHour.multiply(plantingManTL)).add(plantingMachineLt.multiply(plantingMachineTL)).add(plantingMaterialKg.multiply(plantingMaterialTL)).setScale(3, RoundingMode.HALF_UP);
        // Dikim işlem insan gücü
        //Dikim ne ile yapılmaktadır?
        BigDecimal sewingManHour = getAsBigDecimalByQuestionIdAndValueAndTypesEl2(questionDTOList, 34L,
                seedSeedlingNumberWithValueList,
                cropCoefficientList, EnumCropCoefficientType.HOW_MUCH_SEEDLING, EnumCropCoefficientValue.HOW_MUCH_SEEDLING_HAND, EnumCropCoefficientValue.HOW_MUCH_SEEDLING_HAND_AND_MACHINE_HAND,
                workingHour.getValue());
        //Kadın işçi yevmiyesi ne kadardır?
        BigDecimal sewingManTL = getAsBigDecimalByQuestionIdAndWorkingValue(questionDTOList, 41L, workingHour.getValue());
        // Dikim işlem insan gücü
        //Dikim ne ile yapılmaktadır?
        BigDecimal sewingMachineLt = getAsBigDecimalByQuestionIdAndValueAndTypesMibzer(questionDTOList, 34L,
                seedSeedlingNumberWithValueList,
                cropCoefficientList, EnumCropCoefficientType.HOW_MUCH_SEEDLING, EnumCropCoefficientValue.HOW_MUCH_SEEDLING_HAND, EnumCropCoefficientValue.HOW_MUCH_SEEDLING_HAND_AND_MACHINE_HAND,
                workingHour.getValue());
        BigDecimal sewingMachineTL = plantingMachineTL;
        BigDecimal sewingMaterialKg = BigDecimal.ONE;
        //Dikim sahiplik durumu nedir?
        BigDecimal sewingMaterialTL = getAsBigDecimalByEquipmentQuestionIdAndQuestionId(questionDTOList, 35L, 36L);
        BigDecimal sewingTotal = (sewingManHour.multiply(sewingManTL)).add(sewingMachineLt.multiply(sewingMachineTL)).add(sewingMaterialKg.multiply(sewingMaterialTL)).setScale(3, RoundingMode.HALF_UP);
        // Kanal insan gücü
        //Ekim ne ile yapılmaktadır?
        BigDecimal channelManHour = getAsBigDecimalByQuestionIdAndValueAndTypesVahsi(questionDTOList, 46L,
                cropCoefficientList, EnumCropCoefficientType.CANAL, EnumCropCoefficientValue.OTHER_LABOR);
        //Erkek işçi yevmiyesi ne kadardır?
        BigDecimal channelManTL = getAsBigDecimalByQuestionIdAndWorkingValue(questionDTOList, 40L, workingHour.getValue());
        //KANAL
        //Ekim ne ile yapılmaktadır?
        BigDecimal channelMachineLt = getAsBigDecimalByQuestionIdAndValueAndTypesVahsi(questionDTOList, 46L,
                cropCoefficientList, EnumCropCoefficientType.CANAL, EnumCropCoefficientValue.OTHER_DIESEL);
        BigDecimal channelMachineTL = sewingMachineTL;
        BigDecimal channelTotal = (channelManHour.multiply(channelManTL)).add(channelMachineLt.multiply(channelMachineTL)).setScale(3, RoundingMode.HALF_UP);
        // Fide insan gücü
        //Ekim ne ile yapılmaktadır?
        BigDecimal seedManHour = getAsBigDecimalByQuestionIdAndValueAndTypesKendi(questionDTOList, 32L,
                cropCoefficientList, EnumCropCoefficientType.SEEDLING_PREPARATION, EnumCropCoefficientValue.OTHER_LABOR);
        //Kadın işçi yevmiyesi ne kadardır?
        BigDecimal seedManTL = getAsBigDecimalByQuestionIdAndWorkingValue(questionDTOList, 41L, workingHour.getValue());
        //Fide
        //Ekim ne ile yapılmaktadır?
        BigDecimal seedMachineLt = getAsBigDecimalByQuestionIdAndValueAndTypesKendi(questionDTOList, 32L,
                cropCoefficientList, EnumCropCoefficientType.SEEDLING_PREPARATION, EnumCropCoefficientValue.OTHER_DIESEL);
        BigDecimal seedMachineTL = channelMachineTL;
        BigDecimal seedTotal = (seedManHour.multiply(seedManTL)).add(seedMachineLt.multiply(seedMachineTL)).setScale(3, RoundingMode.HALF_UP);
        BigDecimal cropCountDec = BigDecimal.valueOf(6);
        //Ürün İşlem sayısı
        BigDecimal cropMahineLtDec = getAsBigDecimalByDieselDistancesAndGeneralCoefficients(dieselDistanceWithValueList, EnumDieselDistanceType.PARCEL_DISTANCE, EnumDieselDistanceType.TRACTOR_COEFFICIENT_EMPTY);
        BigDecimal cropMachineTLDec = seedMachineTL;
        BigDecimal cropTotal = cropCountDec.multiply(cropMahineLtDec).multiply(cropMachineTLDec).setScale(3, RoundingMode.HALF_UP);
        BigDecimal landTotalAmount = orderTotal.add(deepTotal).add(overOrderTotal).add(secondaryTotal).add(plantingTotal).add(sewingTotal).add(channelTotal).add(seedTotal).add(cropTotal);
        //Sulama sayısı
        BigDecimal manHourDec = getAsBigDecimalBySulamaQuestionIdsAndCropType(questionDTOList, 46L, 45L, cropCoefficientList, EnumCropCoefficientType.WATERING);
        //Erkek işçi yevmiyesi ne kadardır?
        BigDecimal manTLDec = channelManTL;
        BigDecimal machineLtDec = getAsBigDecimalByMakineSulamaQuestionIdsAndCropType(questionDTOList, 46L, 45L, 47L,
                cropCoefficientList, EnumCropCoefficientType.WATERING);
        BigDecimal machineTLDec = getAsBigDecimalByDieselDistances(dieselDistanceWithValueList, EnumDieselDistanceType.DIESEL_PRICE);
        BigDecimal materialTLDec = getAsBigDecimalBySulamaQuestionIdAndCountQuestionId(questionDTOList, 46L, 45L);
        BigDecimal wateringTotal = (manHourDec.multiply(manTLDec)).add(machineLtDec.multiply(machineTLDec)).add(materialTLDec).setScale(3, RoundingMode.HALF_UP);
        BigDecimal manHourFertilizerDec = getAsBigDecimalByQuestionListAndCoefficientList(questionDTOList, cropCoefficientList);
        BigDecimal manTLFertilizerDec = manTLDec;
        BigDecimal machineLtFertilizerDec = getAsBigDecimalByQuestionListAndCoefficientListMachine(questionDTOList, cropCoefficientList);
        BigDecimal machineTLFertilizerDec = machineTLDec;
        BigDecimal fertilizerTotal = (manHourFertilizerDec.multiply(manTLFertilizerDec)).add(machineLtFertilizerDec.multiply(machineTLFertilizerDec)).setScale(3, RoundingMode.HALF_UP);
        BigDecimal manHourAgrDec = getAsBigDecimalByQuestionListAndCoefficientListPest(questionDTOList, cropCoefficientList);
        BigDecimal manTLAgrDec = manTLFertilizerDec;
        BigDecimal machineLtAgrDec = getAsBigDecimalByQuestionListAndCoefficientListPestMachine(questionDTOList, cropCoefficientList);
        BigDecimal machineTLAgrDec = machineTLFertilizerDec;
        BigDecimal agriculturalTotal = (manHourAgrDec.multiply(manTLAgrDec)).add(machineLtAgrDec.multiply(machineTLAgrDec)).setScale(3, RoundingMode.HALF_UP);
        //Yabani ot konrolü
        BigDecimal manHourWeedingDec = getAsBigDecimalByQuestionIdsAndCoefficientCapalama(questionDTOList, 48L, 50L, 51L,
                cropCoefficientList, EnumCropCoefficientValue.WEEDING_HAND_HUMAN, EnumCropCoefficientValue.WEEDING_MACHINE_HUMAN);
        BigDecimal manTLWeedingDec = getAsBigDecimalByQuestionIdAndWorkingValue(questionDTOList, 41L, workingHour.getValue());
        BigDecimal machineLtWeedingDec = getAsBigDecimalByQuestionIdsAndCoefficientCapalama(questionDTOList, 48L, 50L, 51L,
                cropCoefficientList, EnumCropCoefficientValue.WEEDING_HAND_MACHINE, EnumCropCoefficientValue.WEEDING_MACHINE_MACHINE);
        BigDecimal machineTLWeedingDec = machineTLAgrDec;
        BigDecimal weedingTotal = (manHourWeedingDec.multiply(manTLWeedingDec)).add(machineLtWeedingDec.multiply(machineTLWeedingDec)).setScale(3, RoundingMode.HALF_UP);
        //Budama yapılmakta mıdır
        BigDecimal manHourCuttingDec = getAsBigDecimalByQIdAndCoefficientTypeAndValue(questionDTOList, 52L,
                cropCoefficientList, EnumCropCoefficientType.DURATION, EnumCropCoefficientValue.DURATION_PRUNING_HUMAN);
        //Budama işçi yevmiyesi ne kadardır?
        BigDecimal manTLCuttingDec = getAsBigDecimalByQuestionIdAndWorkingValue(questionDTOList, 42L, workingHour.getValue());
        //Budama yapılmakta mıdır
        BigDecimal machineLtCuttingDec = getAsBigDecimalByQIdAndCoefficientTypeAndValue(questionDTOList, 52L,
                cropCoefficientList, EnumCropCoefficientType.DURATION, EnumCropCoefficientValue.DURATION_PRUNING_MACHINE);
        BigDecimal machineTLCuttingDec = machineTLWeedingDec;
        BigDecimal cuttingTotal = (manHourCuttingDec.multiply(manTLCuttingDec)).add(machineLtCuttingDec.multiply(machineTLCuttingDec)).setScale(3, RoundingMode.HALF_UP);
        //Meyve seyreltmesi yapılmakta mıdır
        //Bir kişinin 1 dekar meyve seyreltme süresi – insan
        BigDecimal manHourFruitDec = getAsBigDecimalByQIdAndCoefficientTypeAndValue(questionDTOList, 53L,
                cropCoefficientList, EnumCropCoefficientType.DURATION, EnumCropCoefficientValue.DURATION_THINNING_HUMAN);
        BigDecimal manTLFruitDec = getAsBigDecimalByQuestionIdAndWorkingValue(questionDTOList, 41L, workingHour.getValue());
        BigDecimal fruitTotal = manHourFruitDec.multiply(manTLFruitDec).setScale(3, RoundingMode.HALF_UP);
        //Yaprak seyreltmesi yapılmakta mıdır
        //Bir kişinin yazlık yaprak alma, uç alma veya yazlık budama süresi – insan
        BigDecimal manHourLeafDec = getAsBigDecimalByQIdAndCoefficientTypeAndValue(questionDTOList, 54L,
                cropCoefficientList, EnumCropCoefficientType.DURATION, EnumCropCoefficientValue.DURATION_SUMMER_PRUNING_HUMAN);
        BigDecimal manTLLeafDec = manTLFruitDec;
        BigDecimal leafTotal = (manHourLeafDec.multiply(manTLLeafDec)).setScale(3, RoundingMode.HALF_UP);
        //Diğer bakım işlemleri  yapılmakta mıdır
        BigDecimal manHourOtherDec = getAsBigDecimalByQIdAndCoefficientTypeAndValue(questionDTOList, 55L,
                cropCoefficientList, EnumCropCoefficientType.OTHER_COEFFICIENT, EnumCropCoefficientValue.OTHER_LABOR);
        BigDecimal manTLOtherDec = getAsBigDecimalByQuestionIdAndWorkingValue(questionDTOList, 40L, workingHour.getValue());
        //Diğer bakım işlemleri  yapılmakta mıdır
        BigDecimal machineLtOtherDec = getAsBigDecimalByQIdAndCoefficientTypeAndValue(questionDTOList, 55L,
                cropCoefficientList, EnumCropCoefficientType.OTHER_COEFFICIENT, EnumCropCoefficientValue.OTHER_DIESEL);
        BigDecimal machineTLOtherDec = machineTLCuttingDec;
        //Diğer bakım işlemleri  yapılmakta mıdır
        BigDecimal materialKgOtherDec = getAsBigDecimalByQIdAndCoefficientTypeAndValue(questionDTOList, 55L,
                cropCoefficientList, EnumCropCoefficientType.OTHER_COEFFICIENT, EnumCropCoefficientValue.OTHER_MATERIAL);
        BigDecimal materialTLOtherDec = getAsBigDecimalByQuestionId(questionDTOList, 57L);
        BigDecimal otherTotal = (manHourOtherDec.multiply(manTLOtherDec)).add(machineLtOtherDec.multiply(machineTLOtherDec)).add(materialKgOtherDec.multiply(materialTLOtherDec)).setScale(3, RoundingMode.HALF_UP);
        //işçilik
        BigDecimal manHourMulchDec = getAsBigDecimalByQIDAndTypeAndValueMalclama(questionDTOList, 48L, cropCoefficientList, EnumCropCoefficientType.MULCH, EnumCropCoefficientValue.OTHER_LABOR);
        //Erkek işçi yevmiyesi ne kadardır?
        BigDecimal manTLMulchDec = getAsBigDecimalByQuestionIdAndWorkingValue(questionDTOList, 40L, workingHour.getValue());
        BigDecimal machineLtMulchDec = getAsBigDecimalByQIDAndTypeAndValueMalclama(questionDTOList, 48L, cropCoefficientList, EnumCropCoefficientType.MULCH, EnumCropCoefficientValue.OTHER_DIESEL);
        BigDecimal machineTLMulchDec = machineTLOtherDec;
        BigDecimal materialKgMulchDec = getAsBigDecimalByQIDAndTypeAndValueMalclama(questionDTOList, 48L, cropCoefficientList, EnumCropCoefficientType.MULCH, EnumCropCoefficientValue.MULCH_AMOUNT);
        BigDecimal materialTLMulchDec = getAsBigDecimalByQuestionId(questionDTOList, 49L);
        BigDecimal mulchTotal = (manHourMulchDec.multiply(manTLMulchDec)).add(machineLtMulchDec.multiply(machineTLMulchDec)).add(materialKgMulchDec.multiply(materialTLMulchDec)).setScale(3, RoundingMode.HALF_UP);
        BigDecimal cropOpCount = getAsBigDecimalByQuestionIds(questionDTOList, 45L, 50L, 51L, 52L, 53L, 54L, 56L, 48L);
        BigDecimal machineLtCropDec = cropMahineLtDec;
        BigDecimal machineTLCropDec = machineTLMulchDec;
        BigDecimal cropTotalMaintenance = cropOpCount.multiply(machineLtCropDec).multiply(machineTLCropDec).setScale(3, RoundingMode.HALF_UP);
        BigDecimal maintenanceTotalAmount = wateringTotal.add(fertilizerTotal).add(agriculturalTotal).add(weedingTotal).add(cuttingTotal).add(fruitTotal).add(leafTotal).add(otherTotal).add(mulchTotal).add(cropTotalMaintenance);
        //Hasat şekli
        BigDecimal manHourHarvest = getAsBigDecimalByQIDsAndTypeValueAndAverageValue(questionDTOList, 59L, 1L,
                cropCoefficientList, EnumCropCoefficientType.HOW_MUCH_COLLECT, EnumCropCoefficientValue.HOW_MUCH_SEEDLING_HAND_AND_MACHINE_HAND, fieldAverageValue);
        //Hasadı yapan insan
        BigDecimal manTLHarvest = getAsBigDecimalByGenderIdAndWorkingHour(questionDTOList, 58L, 41L, 40L, 43L, workingHour.getValue());
        BigDecimal machineLtHarvest = getAsBigDecimalByQIdsAndTypeValues(questionDTOList, 59L, 44L,
                cropCoefficientList, EnumCropCoefficientType.HOW_MUCH_COLLECT, EnumCropCoefficientValue.HOW_MUCH_SEEDLING_MACHINE, EnumCropCoefficientValue.HOW_MUCH_SEEDLING_HAND_AND_MACHINE_MACHINE);
        BigDecimal machineTLHarvest = getAsBigDecimalByDieselDistances(dieselDistanceWithValueList, EnumDieselDistanceType.DIESEL_PRICE);
        BigDecimal materialKgHarvest = getAsBigDecimalByQIDs(questionDTOList, 59L, 1L, 78L);
        BigDecimal materialL_TLHarvest = getAsBigDecimalByQIDsAndGoturu(questionDTOList, 59L, 60L, 44L, 61L);
        BigDecimal harvestTotal = (manHourHarvest.multiply(manTLHarvest)).add(machineLtHarvest.multiply(machineTLHarvest)).add(materialKgHarvest.multiply(materialL_TLHarvest)).setScale(3, RoundingMode.HALF_UP);
        //Hasat yerine nakil yapılıyor mu
        BigDecimal machineLtHarvestTransfer = getAsBigDecimalByQIDsAndDieselTypesAndCoefficientTypeValue(questionDTOList, 62L, 1L,
                dieselDistanceWithValueList, EnumDieselDistanceType.PARCEL_DISTANCE, EnumDieselDistanceType.TRACTOR_COEFFICIENT_FULL,
                cropCoefficientList, EnumCropCoefficientType.OTHER_CONSTANTS, EnumCropCoefficientValue.OTHER_CONSTANTS_TRACTOR_TRAILER);
        BigDecimal machineTLHarvestTransfer = machineTLHarvest;
        BigDecimal harvestTransferTotal = machineLtHarvestTransfer.multiply(machineTLHarvestTransfer).setScale(3, RoundingMode.HALF_UP);
        //Harman şekli
        BigDecimal manHourBlend = getAsBigDecimalByCoefficientTypeValues(questionDTOList, 66L,
                cropCoefficientList, EnumCropCoefficientType.BLEND, EnumCropCoefficientValue.BLEND_HAND, EnumCropCoefficientValue.HOW_MUCH_SEEDLING_HAND_AND_MACHINE_HAND);
        //Kadın yevmiyesi
        BigDecimal manTLBlend = getAsBigDecimalByWorkingHourValue(questionDTOList, 40L, workingHour.getValue());
        //Harman şekli
        BigDecimal machineLtBlend = getAsBigDecimalByCoefficientTypeValues(questionDTOList, 66L,
                cropCoefficientList, EnumCropCoefficientType.BLEND, EnumCropCoefficientValue.BLEND_MACHINE, EnumCropCoefficientValue.HOW_MUCH_SEEDLING_HAND_AND_MACHINE_MACHINE);
        BigDecimal machineTLBlend = machineTLHarvestTransfer;
        BigDecimal materialKgBlend = BigDecimal.ONE;
        //harman şekli
        BigDecimal materialTLBlend = getAsBigDecimalByGoturuQuestionId(questionDTOList, 66L, 67L);
        //Hasat sayısı
        BigDecimal answerResultBlend = getAsBigDecimalByQuestionId(questionDTOList, 44L);
        BigDecimal blendTotal = ((manHourBlend.multiply(manTLBlend)).add(machineLtBlend.multiply(machineTLBlend)).add(materialKgBlend.multiply(materialTLBlend))).multiply(answerResultBlend).setScale(3, RoundingMode.HALF_UP);
        //Patoz şekli
        BigDecimal manHourHaymaker = getAsBigDecimalByQIDAndTypeValue(questionDTOList, 68L, cropCoefficientList, EnumCropCoefficientType.HAYMAKER, EnumCropCoefficientValue.OTHER_LABOR);
        BigDecimal manTLHaymaker = manTLBlend;
        //Patoz şekli
        BigDecimal machineLtHaymaker = getAsBigDecimalByQIDAndTypeValue(questionDTOList, 68L, cropCoefficientList, EnumCropCoefficientType.HAYMAKER, EnumCropCoefficientValue.BLEND_MACHINE);
        BigDecimal machineTLHaymaker = machineTLBlend;
        //Patoz şekli
        BigDecimal materialKgHaymaker = getAsBigDecimalByQIdsAndTypeValue(questionDTOList, 68L, 1L, cropCoefficientList, EnumCropCoefficientType.HAYMAKER, EnumCropCoefficientValue.HAYMAKER_MACHINE_CAPACITY);
        //Patoz şekli
        BigDecimal materialTLHaymaker = getAsBigDecimalByQIDsAndMakine(questionDTOList, 68L, 69L, 70L);
        BigDecimal haymakerTotal = (manHourHaymaker.multiply(manTLHaymaker)).add(machineLtHaymaker.multiply(machineTLHaymaker)).add(materialKgHaymaker.multiply(materialTLHaymaker)).setScale(3, RoundingMode.HALF_UP);
        //İşleme kurutma
        BigDecimal manHourDrying = getAsBigDecimalByQIdAndTypeValue(questionDTOList, 63L, 1L,
                cropCoefficientList, EnumCropCoefficientType.OTHER_CONSTANTS, EnumCropCoefficientValue.OTHER_CONSTANTS_ONE_PERSON_HANDLE);
        //Karışık yevmiyesi
        BigDecimal manTLDrying = getAsBigDecimalByWorkingHourValue(questionDTOList, 43L, workingHour.getValue());
        //İşlemde malzeme var mı
        BigDecimal materialKgDrying = getAsBigDecimalByQIdAndTypeValue(questionDTOList, 64L, 1L,
                cropCoefficientList, EnumCropCoefficientType.OTHER_CONSTANTS, EnumCropCoefficientValue.OTHER_CONSTANTS_HANDLE_MATERIAL);
        //İşlemde malzeme var mı
        BigDecimal materialTLDrying = getAsBigDecimalByQIDsAndEvet(questionDTOList, 64L, 65L);
        BigDecimal dryingTotal = (manHourDrying.multiply(manTLDrying)).add(materialKgDrying.multiply(materialTLDrying)).setScale(3, RoundingMode.HALF_UP);
        //Ambalaj kullanıyormu
        //Bir kişi ne kadar ürün ambalajlar
        BigDecimal manHourPacking = getAsBigDecimalByQIdAndTypeValue(questionDTOList, 75L, 1L,
                cropCoefficientList, EnumCropCoefficientType.OTHER_CONSTANTS, EnumCropCoefficientValue.OTHER_CONSTANTS_ONE_PERSON_PACKAGING);
        //Karışık yevmiyesi
        BigDecimal manTLPacking = getAsBigDecimalByWorkingHourValue(questionDTOList, 41L, workingHour.getValue());
        //Ambalajlama bedelini kim karşılıyor
        BigDecimal materialKgPacking = getAsBigDecimalByQIDsAndUretici(questionDTOList, 76L, 1L, 78L);
        //Ambalaj kasa fiyatı
        BigDecimal materialTLPacking = getAsBigDecimalByQuestionId(questionDTOList, 79L);
        BigDecimal packingTotal = (manHourPacking.multiply(manTLPacking)).add(materialKgPacking.multiply(materialTLPacking)).setScale(3, RoundingMode.HALF_UP);
        //Balya makinesi sahiplik
        BigDecimal manHourBaling = getAsBigDecimalByQIDsAndBale(questionDTOList, 72L, 1L, cropCoefficientList, EnumCropCoefficientType.BALE, EnumCropCoefficientValue.BALE_CAPACITY);
        BigDecimal manTLBaling = manTLHaymaker;
        //Balya makinesi sahiplik
        BigDecimal machineLtBaling = getAsBigDecimalByQuestionIdAndValueAndType(questionDTOList, 72L, cropCoefficientList, EnumCropCoefficientType.BALE, EnumCropCoefficientValue.OTHER_DIESEL);
        BigDecimal machineTLBaling = machineTLHaymaker;
        // Balyalama yapılıyormu?
        BigDecimal materialKgBaling = getAsBigDecimalByQIDsAndVerim14(questionDTOList, 71L, 72L, 140L, 1L, 4L,
                cropCoefficientList, EnumCropCoefficientType.BALE, EnumCropCoefficientValue.BALE_CAPACITY);
        BigDecimal materialTLBaling = getAsBigDecimalByEquipmentQuestionIdAndQuestionId(questionDTOList, 72L, 73L);
        BigDecimal balingTotal = (manHourBaling.multiply(manTLBaling)).add(machineLtBaling.multiply(machineTLBaling)).add(materialKgBaling.multiply(materialTLBaling)).setScale(3, RoundingMode.HALF_UP);
        //Ürün pazara naklediliyormu
        BigDecimal machineLtMarketTransfer = getAsBigDecimalByQIdAndTypeValueNakil(questionDTOList, 74L, 1L,
                cropCoefficientList, EnumCropCoefficientType.OTHER_CONSTANTS, EnumCropCoefficientValue.OTHER_CONSTANTS_VEHICLE_CAPACITY);// Araç taşıma kapasitesi
        BigDecimal machineTLMarketTransfer = getAsBigDecimalByDieselEnumTypes(dieselDistanceWithValueList, EnumDieselDistanceType.HALL_DISTANCE,
                EnumDieselDistanceType.TRANSPORTATION_COEFFICIENT, EnumDieselDistanceType.DIESEL_PRICE);
        BigDecimal marketTransferTotal = (machineLtMarketTransfer.multiply(machineTLMarketTransfer)).setScale(3, RoundingMode.HALF_UP);
        BigDecimal cropCount = getAsBigDecimalByTotalAndEvet(questionDTOList, 44L, 71L);
        BigDecimal machineLtCropCount = getAsBigDecimalByDieselDistancesAndGeneralCoefficients(dieselDistanceWithValueList, EnumDieselDistanceType.PARCEL_DISTANCE, EnumDieselDistanceType.TRACTOR_COEFFICIENT_EMPTY);
        BigDecimal machineTLCropCount = machineTLBaling;
        BigDecimal cropCountTotal = cropCount.multiply(machineLtCropCount).multiply(machineTLCropCount).setScale(3, RoundingMode.HALF_UP);
        BigDecimal harvestTotalAmount = harvestTotal.add(harvestTransferTotal).add(blendTotal).add(haymakerTotal).add(dryingTotal).add(packingTotal).add(balingTotal).add(marketTransferTotal).add(cropCountTotal);
        BigDecimal inputTotalAmount = getTotalInputValueAsBigDecimal(questionDTOList, seedSeedlingNumberWithValueList, seedSeedlingPriceWithValueList);
        BigDecimal fertilizerTotalAmount = getFertilizerTotalValueAsBigDecimal(questionDTOList, cityFertilizerWithValueList);
        BigDecimal medicineTotalAmount = getMedicineTotalValueAsBigDecimal(questionDTOList);
        BigDecimal waterPriceTotalAmount = getWaterPriceTotalValueAsBigDecimal(wateringValueOptional);
        totalExpense = landTotalAmount.add(maintenanceTotalAmount).add(harvestTotalAmount).add(inputTotalAmount).add(fertilizerTotalAmount).add(medicineTotalAmount).add(waterPriceTotalAmount);
        return totalExpense.setScale(3, RoundingMode.HALF_UP);
    }

    //getOperationCount
    private BigDecimal getAsBigDecimalByQuestionId(List<QuestionWithFirstValue> questionDTOList, Long questionId) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        return dtoOptional.map(questionWithFirstAnswer -> new BigDecimal(questionWithFirstAnswer.getValue())).orElse(BigDecimal.ZERO);
    }

    //getMachinePowerLt
    private BigDecimal getAsBigDecimalByQuestionIdAndValueAndType(List<QuestionWithFirstValue> questionDTOList, Long questionId,
                                                                  List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if ("Sahipli".equals(dtoOptional.get().getValue())) {
                return getAsBigDecimalByValueAndType(cropCoefficientList, type, value);
            }
        }
        return BigDecimal.ZERO;
    }

    //getMachinePowerTL
    private BigDecimal getAsBigDecimalByQuestionIdAndDieselDistancesAndGeneralCoefficients(List<QuestionWithFirstValue> questionDTOList, Long questionId,
                                                                                           List<DieselDistanceWithFirstValue> dieselDistanceWithValueList, EnumDieselDistanceType dieselPrice, EnumDieselDistanceType plowingField,
                                                                                           BigDecimal workingHourValue, BigDecimal amortizationValue) {
        Optional<DieselDistanceWithFirstValue> dieselPriceOptional = dieselDistanceWithValueList.stream().filter(dd -> dd.getEnumType().equals(dieselPrice)).findFirst();
        BigDecimal dieselAmount = BigDecimal.ZERO;
        if (dieselPriceOptional.isPresent()) {
            dieselAmount = new BigDecimal(dieselPriceOptional.get().getValue());
        }
        BigDecimal result1 = dieselAmount.multiply(workingHourValue).multiply(amortizationValue);
        // Günlük yevmiye
        BigDecimal dailyWage = getAsBigDecimalByQuestionId(questionDTOList, questionId);
        Optional<DieselDistanceWithFirstValue> plowingFieldOptional = dieselDistanceWithValueList.stream().filter(dd -> dd.getEnumType().equals(plowingField)).findFirst();
        BigDecimal field = BigDecimal.ZERO;
        if (plowingFieldOptional.isPresent()) {
            field = new BigDecimal(plowingFieldOptional.get().getValue());
        }
        BigDecimal result2 = new BigDecimal(0);
        if (field != null && field.compareTo(BigDecimal.ZERO) > 0) {
            result2 = dailyWage.divide(field, 3, RoundingMode.HALF_UP);
        }
        BigDecimal finalResult = result1.add(result2);
        if (workingHourValue.compareTo(BigDecimal.ZERO) > 0) {
            finalResult = finalResult.divide(workingHourValue, 3, RoundingMode.HALF_UP);
        }
        return finalResult.setScale(3, RoundingMode.HALF_UP);
    }

    //getMaterials
    private BigDecimal getAsBigDecimalByEquipmentQuestionIdAndQuestionId(List<QuestionWithFirstValue> questionDTOList, Long equipmentQuestionId, Long questionId) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(equipmentQuestionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Kiralık")) {
                return getAsBigDecimalByQuestionId(questionDTOList, questionId);
            }
        }
        return BigDecimal.ZERO;
    }

    //getManpowerHour
    private BigDecimal getAsBigDecimalByQuestionIdAndValueAndTypesEl(List<QuestionWithFirstValue> questionDTOList, Long questionId,
                                                                     List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value1, EnumCropCoefficientValue value2) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("El")) {
                return getAsBigDecimalByValueAndType(cropCoefficientList, type, value1);
            } else if (dtoOptional.get().getValue().equals("El ve mibzer")) {
                return getAsBigDecimalByValueAndType(cropCoefficientList, type, value2);
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByValueAndType(List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value) {
        Optional<CropCoefficientWithFirstValue> cropCoefficient = cropCoefficientList.stream().filter(ccl -> ccl.getEnumType().equals(type) && ccl.getEnumValue().equals(value)).findFirst();
        return cropCoefficient.map(cropCoefficientWithFirstValue -> new BigDecimal(cropCoefficientWithFirstValue.getValue())).orElse(BigDecimal.ZERO);
    }

    //getManPowerTL
    private BigDecimal getAsBigDecimalByQuestionIdAndWorkingValue(List<QuestionWithFirstValue> questionDTOList, Long questionId, BigDecimal workingHourValue) {
        BigDecimal firstParam = getAsBigDecimalByQuestionId(questionDTOList, questionId);
        BigDecimal secondParam = BigDecimal.ZERO;
        if (workingHourValue != null) {
            secondParam = workingHourValue;
        }
        if (secondParam.compareTo(BigDecimal.ZERO) > 0) {
            return firstParam.divide(secondParam, 3, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    //getMachinePowerLt
    private BigDecimal getAsBigDecimalByQuestionIdAndValueAndTypesMibzer(List<QuestionWithFirstValue> questionDTOList, Long questionId,
                                                                         List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value1, EnumCropCoefficientValue value2) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Mibzer")) {
                return getAsBigDecimalByValueAndType(cropCoefficientList, type, value1);
            } else if (dtoOptional.get().getValue().equals("El ve mibzer")) {
                return getAsBigDecimalByValueAndType(cropCoefficientList, type, value2);
            }
        }
        return BigDecimal.ZERO;
    }

    //getManpowerHour
    private BigDecimal getAsBigDecimalByQuestionIdAndValueAndTypesEl2(List<QuestionWithFirstValue> questionDTOList, Long questionId,
                                                                      List<SeedSeedlingWithFirstValue> seedSeedlingNumberWithValueList,
                                                                      List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value1, EnumCropCoefficientValue value2,
                                                                      BigDecimal workingHourValue) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        BigDecimal seedAmount = BigDecimal.ZERO;
        if (dtoOptional.isPresent()) {
            //Üretim materyali nedir?
            BigDecimal seedAmountInfo = getSeedAmountInfo(questionDTOList, 30L, 32L, 33L, seedSeedlingNumberWithValueList);
            if (dtoOptional.get().getValue().equals("El")) {
                BigDecimal cropData = getAsBigDecimalByValueAndType(cropCoefficientList, type, value1);
                if (cropData.compareTo(BigDecimal.ZERO) > 0) {
                    seedAmount = seedAmountInfo.divide(cropData, 3, RoundingMode.HALF_UP).multiply(workingHourValue);
                }
            } else if (dtoOptional.get().getValue().equals("El ve mibzer")) {
                BigDecimal cropData = getAsBigDecimalByValueAndType(cropCoefficientList, type, value2);
                if (cropData.compareTo(BigDecimal.ZERO) > 0) {
                    seedAmount = seedAmountInfo.divide(cropData, 3, RoundingMode.HALF_UP).multiply(workingHourValue);
                }
            }
            seedAmount = seedAmount.setScale(3, RoundingMode.HALF_UP);
            return seedAmount;
        }
        return seedAmount;
    }

    //getManpowerHour
    private BigDecimal getAsBigDecimalByQuestionIdAndValueAndTypesMibzer(List<QuestionWithFirstValue> questionDTOList, Long questionId,
                                                                         List<SeedSeedlingWithFirstValue> seedSeedlingNumberWithValueList,
                                                                         List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value1, EnumCropCoefficientValue value2,
                                                                         BigDecimal workingHourValue) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        BigDecimal seedAmount = BigDecimal.ZERO;
        if (dtoOptional.isPresent()) {
            //Üretim materyali nedir?
            BigDecimal seedAmountInfo = getSeedAmountInfo(questionDTOList, 30L, 32L, 33L, seedSeedlingNumberWithValueList);
            if (dtoOptional.get().getValue().equals("Mibzer")) {
                BigDecimal cropData = getAsBigDecimalByValueAndType(cropCoefficientList, type, value1);
                if (cropData.compareTo(BigDecimal.ZERO) > 0) {
                    seedAmount = seedAmountInfo.divide(cropData, 3, RoundingMode.HALF_UP).multiply(workingHourValue);
                }
            } else if (dtoOptional.get().getValue().equals("El ve mibzer")) {
                BigDecimal cropData = getAsBigDecimalByValueAndType(cropCoefficientList, type, value2);
                if (cropData.compareTo(BigDecimal.ZERO) > 0) {
                    seedAmount = seedAmountInfo.divide(cropData, 3, RoundingMode.HALF_UP).multiply(workingHourValue);
                }
            }
            seedAmount = seedAmount.setScale(3, RoundingMode.HALF_UP);
            return seedAmount;
        }
        return seedAmount;
    }

    private BigDecimal getSeedAmountInfo(List<QuestionWithFirstValue> questionDTOList, Long fideQuestionId, Long teminQuestionId, Long outSourceQuestionId,
                                         List<SeedSeedlingWithFirstValue> seedSeedlingNumberWithValueList) {
        BigDecimal seedAmountInfo = BigDecimal.ZERO;
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(fideQuestionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Fide")) {
                //Fide nasıl temin ediliyor?
                Optional<QuestionWithFirstValue> teminOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(teminQuestionId)).findFirst();
                if (teminOptional.isPresent()) {
                    Optional<SeedSeedlingWithFirstValue> seedSeedlingValue = seedSeedlingNumberWithValueList.stream().filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.LOCAL_SEED_KG)).findFirst();
                    if (dtoOptional.get().getValue().equals("Kendi yetiştiriyor")) {
                        seedAmountInfo = seedSeedlingValue.get().getValue();
                    } else {
                        //Dış kaynaktan temin edilen fide özelliği nedir?
                        BigDecimal hybridSeedling = BigDecimal.ZERO;
                        BigDecimal oneGraft = BigDecimal.ZERO;
                        BigDecimal twoGraft = BigDecimal.ZERO;
                        Optional<SeedSeedlingWithFirstValue> hybridValue = seedSeedlingNumberWithValueList.stream().filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.HYBRID_SEEDLING)).findFirst();
                        Optional<SeedSeedlingWithFirstValue> oneGraftValue = seedSeedlingNumberWithValueList.stream().filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.HYBRID_SEEDLING_ONE_GRAFT)).findFirst();
                        Optional<SeedSeedlingWithFirstValue> twoGraftValue = seedSeedlingNumberWithValueList.stream().filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.HYBRID_SEEDLING_TWO_GRAFT)).findFirst();
                        if (hybridValue.isPresent()) {
                            hybridSeedling = hybridValue.get().getValue();
                        }
                        if (oneGraftValue.isPresent()) {
                            oneGraft = oneGraftValue.get().getValue();
                        }
                        if (twoGraftValue.isPresent()) {
                            twoGraft = twoGraftValue.get().getValue();
                        }
                        Optional<QuestionWithFirstValue> outSourceOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(outSourceQuestionId)).findFirst();
                        if (outSourceOptional.isPresent()) {
                            if (outSourceOptional.get().getValue().equals("Aşısız")) {
                                seedAmountInfo = hybridSeedling;
                            } else if (outSourceOptional.get().getValue().equals("Tek aşılı")) {
                                seedAmountInfo = oneGraft;
                            } else if (outSourceOptional.get().getValue().equals("Çift aşılı")) {
                                seedAmountInfo = twoGraft;
                            } else if (outSourceOptional.get().getValue().equals("Karışık")) {
                                seedAmountInfo = hybridSeedling.add(oneGraft.add(twoGraft));
                            }
                        }
                    }
                }
            }
        }
        return seedAmountInfo;
    }

    //getManpowerHour
    private BigDecimal getAsBigDecimalByQuestionIdAndValueAndTypesVahsi(List<QuestionWithFirstValue> questionDTOList, Long questionId,
                                                                        List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Vahşi sulama")) {
                return getAsBigDecimalByValueAndType(cropCoefficientList, type, value);
            }
        }
        return BigDecimal.ZERO;
    }

    //getManpowerHour
    private BigDecimal getAsBigDecimalByQuestionIdAndValueAndTypesKendi(List<QuestionWithFirstValue> questionDTOList, Long questionId,
                                                                        List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Kendi yetiştiriyor")) {
                return getAsBigDecimalByValueAndType(cropCoefficientList, type, value);
            }
        }
        return BigDecimal.ZERO;
    }

    //getMachinePowerLt
    private BigDecimal getAsBigDecimalByDieselDistancesAndGeneralCoefficients(List<DieselDistanceWithFirstValue> dieselDistanceWithValueList, EnumDieselDistanceType parcelDistanceType, EnumDieselDistanceType tractorEmptyType) {
        BigDecimal parcelDistance = BigDecimal.ZERO;
        Optional<DieselDistanceWithFirstValue> dieselPriceOptional = dieselDistanceWithValueList.stream().filter(dd -> dd.getEnumType().equals(parcelDistanceType)).findFirst();
        if (dieselPriceOptional.isPresent()) {
            parcelDistance = new BigDecimal(dieselPriceOptional.get().getValue());
        }
        BigDecimal coefEmpty = BigDecimal.ZERO;
        Optional<DieselDistanceWithFirstValue> tractorEmptyOptional = dieselDistanceWithValueList.stream().filter(dd -> dd.getEnumType().equals(tractorEmptyType)).findFirst();
        if (tractorEmptyOptional.isPresent()) {
            coefEmpty = new BigDecimal(tractorEmptyOptional.get().getValue());
        }
        if (coefEmpty.compareTo(BigDecimal.ZERO) > 0) {
            return parcelDistance.divide(coefEmpty, 3, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    //getManPowerHourForMaintenance
    private BigDecimal getAsBigDecimalBySulamaQuestionIdsAndCropType(List<QuestionWithFirstValue> questionDTOList, Long sulamaQuestionId, Long countQuestionId,
                                                                     List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(sulamaQuestionId)).findFirst();
        if (dtoOptional.isPresent()) {
            Optional<QuestionWithFirstValue> countDtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(countQuestionId)).findFirst();
            BigDecimal wateringCount = BigDecimal.ZERO;
            if (countDtoOptional.isPresent()) {
                wateringCount = new BigDecimal(countDtoOptional.get().getValue());
            }
            BigDecimal coefDec = BigDecimal.ZERO;
            if (dtoOptional.get().getValue().equals("Damlama")) {
                //Damla – insan
                coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, type, EnumCropCoefficientValue.WATERING_DROP_HUMAN);
            } else if (dtoOptional.get().getValue().equals("Yağmurlama")) {
                //yagmur – insan
                coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, type, EnumCropCoefficientValue.WATERING_RAIN_HUMAN);
            } else if (dtoOptional.get().getValue().equals("Vahşi sulama")) {
                //vahsı – insan
                coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, type, EnumCropCoefficientValue.WATERING_WILD_HUMAN);
            }
            return wateringCount.multiply(coefDec).setScale(3, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    //getMachinePowerLtForMaintenance
    private BigDecimal getAsBigDecimalByMakineSulamaQuestionIdsAndCropType(List<QuestionWithFirstValue> questionDTOList, Long sulamaQuestionId, Long countQuestionId, Long energyQuestionId,
                                                                           List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(sulamaQuestionId)).findFirst();
        if (dtoOptional.isPresent()) {
            Optional<QuestionWithFirstValue> countDtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(countQuestionId)).findFirst();
            BigDecimal wateringCount = BigDecimal.ZERO;
            if (countDtoOptional.isPresent()) {
                wateringCount = new BigDecimal(countDtoOptional.get().getValue());
            }
            BigDecimal coefDec = BigDecimal.ZERO;
            if (dtoOptional.get().getValue().equals("Damlama")) {
                //Damla – insan
                coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, type, EnumCropCoefficientValue.WATERING_DROP_MACHINE);
            } else if (dtoOptional.get().getValue().equals("Yağmurlama")) {
                //yagmur – insan
                coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, type, EnumCropCoefficientValue.WATERING_RAIN_MACHINE);
            } else if (dtoOptional.get().getValue().equals("Vahşi sulama")) {
                //vahsı – insan
                coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, type, EnumCropCoefficientValue.WATERING_WILD_MACHINE);
            }
            Optional<QuestionWithFirstValue> energyDtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(energyQuestionId)).findFirst();
            BigDecimal powerCoef = BigDecimal.ZERO;
            if (energyDtoOptional.isPresent()) {
                if (energyDtoOptional.get().getValue().equals("Elektrik")) {
                    GeneralCoefficientValue coefficientValue = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Elektrik katsayısı");
                    powerCoef = coefficientValue.getValue();
                } else if (energyDtoOptional.get().getValue().equals("Mazot")) {
                    GeneralCoefficientValue coefficientValue = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Mazot katsayısı");
                    powerCoef = coefficientValue.getValue();
                }
            }
            return wateringCount.multiply(coefDec).multiply(powerCoef).setScale(3, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    //getMachinePowerTLForMaintenance
    private BigDecimal getAsBigDecimalByDieselDistances(List<DieselDistanceWithFirstValue> dieselDistanceWithValueList, EnumDieselDistanceType dieselPrice) {
        BigDecimal machinePower = BigDecimal.ZERO;
        Optional<DieselDistanceWithFirstValue> dieselPriceOptional = dieselDistanceWithValueList.stream().filter(dd -> dd.getEnumType().equals(dieselPrice)).findFirst();
        if (dieselPriceOptional.isPresent()) {
            BigDecimal dieselPriceValue = new BigDecimal(dieselPriceOptional.get().getValue());
            GeneralCoefficientValue coefficientValue = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Traktör makine amortisman katsayısı");
            machinePower = dieselPriceValue.multiply(coefficientValue.getValue());
        }
        return machinePower.setScale(3, RoundingMode.HALF_UP);
    }

    //getMaterialTLForMaintenance
    private BigDecimal getAsBigDecimalBySulamaQuestionIdAndCountQuestionId(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long countQuestionId) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            Optional<QuestionWithFirstValue> countDtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(countQuestionId)).findFirst();
            BigDecimal wateringCount = BigDecimal.ZERO;
            if (countDtoOptional.isPresent()) {
                wateringCount = new BigDecimal(countDtoOptional.get().getValue());
            }
            if (dtoOptional.get().getValue().equals("Damlama")) {
                if (wateringCount.compareTo(new BigDecimal(2)) > 0) {
                    GeneralCoefficientValue coefficientValue = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Damla amortisman (+2)");
                    return coefficientValue.getValue();
                }
            } else if (dtoOptional.get().getValue().equals("Yağmurlama")) {
                if (wateringCount.compareTo(new BigDecimal(2)) > 0) {
                    GeneralCoefficientValue coefficientValue = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Yağmurlama amortisman (+2))");
                    return coefficientValue.getValue();
                }
            } else if (dtoOptional.get().getValue().equals("Vahşi sulama")) {
                GeneralCoefficientValue coefficientValue = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Vahşi sulama amortisman");
                return coefficientValue.getValue();
            }
        }
        return BigDecimal.ZERO;
    }

    //manHourFertilizerDec
    private BigDecimal getAsBigDecimalByQuestionListAndCoefficientList(List<QuestionWithFirstValue> questionDTOList, List<CropCoefficientWithFirstValue> cropCoefficientList) {
        //el ile – insan
        BigDecimal coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.FERTILIZER, EnumCropCoefficientValue.FERTILIZER_HAND_HUMAN);
        BigDecimal fertilizingCountHand = getFertilizingCountWithHand(questionDTOList).multiply(coefDec);
        //makine ile – insan
        coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.FERTILIZER, EnumCropCoefficientValue.FERTILIZER_MACHINE_HUMAN);
        BigDecimal fertilizingCountMachine = getFertilizingCountWithMachine(questionDTOList).multiply(coefDec);
        //sulama sistemi ile – insan
        coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.FERTILIZER, EnumCropCoefficientValue.FERTILIZER_WATERING_HUMAN);
        BigDecimal fertilizingCountWatering = getFertilizingCountWithWatering(questionDTOList).multiply(coefDec);
        //ekim dikim ile – insan
        coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.FERTILIZER, EnumCropCoefficientValue.FERTILIZER_PLANTING_HUMAN);
        BigDecimal fertilizingCountSeedling = getFertilizingCountWithSeedling(questionDTOList).multiply(coefDec);
        return (fertilizingCountHand.add(fertilizingCountMachine).add(fertilizingCountSeedling).add(fertilizingCountWatering)).setScale(3, RoundingMode.HALF_UP);
    }

    private BigDecimal getFertilizingCountWithHand(List<QuestionWithFirstValue> questionDTOList) {
        BigDecimal fertilizingOpCount = new BigDecimal(0);
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 91L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 95L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 99L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 103L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 107L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 141L));
        return fertilizingOpCount;
    }

    private BigDecimal getFertilizingCountWithMachine(List<QuestionWithFirstValue> questionDTOList) {
        BigDecimal fertilizingOpCount = new BigDecimal(0);
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 92L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 96L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 100L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 104L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 108L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 142L));
        return fertilizingOpCount;
    }

    private BigDecimal getFertilizingCountWithWatering(List<QuestionWithFirstValue> questionDTOList) {
        BigDecimal fertilizingOpCount = new BigDecimal(0);
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 93L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 97L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 101L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 105L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 109L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 143L));
        return fertilizingOpCount;
    }

    private BigDecimal getFertilizingCountWithSeedling(List<QuestionWithFirstValue> questionDTOList) {
        BigDecimal fertilizingOpCount = new BigDecimal(0);
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 94L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 98L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 102L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 106L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 110L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 144L));
        return fertilizingOpCount;
    }

    private BigDecimal getAsBigDecimalByQuestionListAndCoefficientListMachine(List<QuestionWithFirstValue> questionDTOList, List<CropCoefficientWithFirstValue> cropCoefficientList) {
        //El ile – makine
        BigDecimal coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.FERTILIZER, EnumCropCoefficientValue.FERTILIZER_HAND_MACHINE);
        BigDecimal fertilizingCountHand = getFertilizingCountWithHand(questionDTOList).multiply(coefDec);
        //Makine ile – makine
        coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.FERTILIZER, EnumCropCoefficientValue.FERTILIZER_MACHINE_MACHINE);
        BigDecimal fertilizingCountMachine = getFertilizingCountWithMachine(questionDTOList).multiply(coefDec);
        //SEkim dikim – makine
        coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.FERTILIZER, EnumCropCoefficientValue.FERTILIZER_PLANTING_MACHINE);
        BigDecimal fertilizingCountSeedling = getFertilizingCountWithSeedling(questionDTOList).multiply(coefDec);
        //Sulama sistemi ile – makine
        coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.FERTILIZER, EnumCropCoefficientValue.FERTILIZER_WATERING_MACHINE);
        BigDecimal fertilizingCountWatering = getFertilizingCountWithWatering(questionDTOList).multiply(coefDec);
        return fertilizingCountHand.add(fertilizingCountMachine).add(fertilizingCountSeedling).add(fertilizingCountWatering);
    }

    private BigDecimal getAsBigDecimalByQuestionListAndCoefficientListPest(List<QuestionWithFirstValue> questionDTOList, List<CropCoefficientWithFirstValue> cropCoefficientList) {
        //el ile – insan
        BigDecimal coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.PEST_CONTROL, EnumCropCoefficientValue.FERTILIZER_HAND_HUMAN);
        BigDecimal medicineCountHand = getMedicineCountWithHand(questionDTOList).multiply(coefDec);
        //sırt makinesi ile – insan
        coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.PEST_CONTROL, EnumCropCoefficientValue.PEST_CONTROL_BACK_HUMAN);
        BigDecimal medicineCountMachine = getMedicineCountWithMachine(questionDTOList).multiply(coefDec);
        //sulama ile – insan
        coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.PEST_CONTROL, EnumCropCoefficientValue.FERTILIZER_WATERING_HUMAN);
        BigDecimal medicineCountWatering = getMedicineCountWithWatering(questionDTOList).multiply(coefDec);
        //traktor ile – insan
        coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.PEST_CONTROL, EnumCropCoefficientValue.PEST_CONTROL_TRACTOR_HUMAN);
        BigDecimal medicineCountTractor = getMedicineCountWithTractor(questionDTOList).multiply(coefDec);
        return (medicineCountHand.add(medicineCountMachine).add(medicineCountWatering).add(medicineCountTractor)).setScale(3, RoundingMode.HALF_UP);
    }

    private BigDecimal getMedicineCountWithHand(List<QuestionWithFirstValue> questionDTOList) {
        BigDecimal fertilizingOpCount = new BigDecimal(0);
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 118L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 122L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 126L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 130L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 134L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 138L));
        return fertilizingOpCount;
    }

    private BigDecimal getMedicineCountWithMachine(List<QuestionWithFirstValue> questionDTOList) {
        BigDecimal fertilizingOpCount = new BigDecimal(0);
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 116L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 120L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 124L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 128L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 132L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 136L));
        return fertilizingOpCount;
    }

    private BigDecimal getMedicineCountWithWatering(List<QuestionWithFirstValue> questionDTOList) {
        BigDecimal fertilizingOpCount = new BigDecimal(0);
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 119L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 123L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 127L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 131L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 135L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 139L));
        return fertilizingOpCount;
    }

    private BigDecimal getMedicineCountWithTractor(List<QuestionWithFirstValue> questionDTOList) {
        BigDecimal fertilizingOpCount = new BigDecimal(0);
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 117L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 121L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 125L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 129L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 133L));
        fertilizingOpCount = fertilizingOpCount.add(getAsBigDecimalByQuestionId(questionDTOList, 137L));
        return fertilizingOpCount;
    }

    private BigDecimal getAsBigDecimalByQuestionListAndCoefficientListPestMachine(List<QuestionWithFirstValue> questionDTOList, List<CropCoefficientWithFirstValue> cropCoefficientList) {
        //El ile – makine
        BigDecimal coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.PEST_CONTROL, EnumCropCoefficientValue.FERTILIZER_HAND_MACHINE);
        BigDecimal medicineCountHand = getMedicineCountWithHand(questionDTOList).multiply(coefDec);
        //Sırt makinesi – makine
        coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.PEST_CONTROL, EnumCropCoefficientValue.PEST_CONTROL_BACK_MACHINE);
        BigDecimal medicineCountMachine = getMedicineCountWithMachine(questionDTOList).multiply(coefDec);
        //Traktör – makine
        coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.PEST_CONTROL, EnumCropCoefficientValue.PEST_CONTROL_TRACTOR_MACHINE);
        BigDecimal medicineCountTractor = getMedicineCountWithTractor(questionDTOList).multiply(coefDec);
        //Sulama sistemi ile – makine
        coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.PEST_CONTROL, EnumCropCoefficientValue.FERTILIZER_WATERING_MACHINE);
        BigDecimal medicineCountWatering = getMedicineCountWithWatering(questionDTOList).multiply(coefDec);
        return (medicineCountHand.add(medicineCountMachine).add(medicineCountTractor).add(medicineCountWatering)).setScale(3, RoundingMode.HALF_UP);
    }

    private BigDecimal getAsBigDecimalByQuestionIdsAndCoefficientCapalama(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long capalamaQuestionId, Long capalamaCountId,
                                                                          List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientValue value1, EnumCropCoefficientValue value2) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent() && dtoOptional.get().getValue().contains("Çapa")) {
            //Çapalama sayısı
            BigDecimal weedingCount = getAsBigDecimalByQuestionId(questionDTOList, capalamaQuestionId);
            //El ile çapa ("sıra üzeri") – insan
            BigDecimal coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.WEEDING, value1);
            //Çapalama sayısı
            BigDecimal weedingCount_2 = getAsBigDecimalByQuestionId(questionDTOList, capalamaCountId);
            //El çapa makinesi ("sıra üzeri") – insan
            BigDecimal coefDec_2 = getAsBigDecimalByValueAndType(cropCoefficientList, EnumCropCoefficientType.WEEDING, value2);
            return ((weedingCount.multiply(coefDec)).add(weedingCount_2.multiply(coefDec_2))).setScale(3, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIdAndCoefficientTypeAndValue(List<QuestionWithFirstValue> questionDTOList, Long questionId,
                                                                      List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent() && dtoOptional.get().getValue().equals("Evet")) {
            return getAsBigDecimalByValueAndType(cropCoefficientList, type, value).setScale(3, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIDAndTypeAndValueMalclama(List<QuestionWithFirstValue> questionDTOList, Long questionId,
                                                                   List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent() && dtoOptional.get().getValue().contains("Malçlama")) {
            return getAsBigDecimalByValueAndType(cropCoefficientList, type, value).setScale(3, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQuestionIds(List<QuestionWithFirstValue> questionDTOList,
                                                    Long question1Id, Long question31Id, Long question32Id, Long question4Id,
                                                    Long question5Id, Long question6Id, Long question7Id, Long question8Id) {
        //Sulama sayısı
        BigDecimal wateringCount = BigDecimal.ZERO;
        BigDecimal value0 = getAsBigDecimalByQuestionId(questionDTOList, question1Id);
        GeneralCoefficientValue coefWatering = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Sulama işlem katsayısı");//Sulama katsayısı
        if (coefWatering != null) {
            wateringCount = value0.multiply(coefWatering.getValue());
        }
        BigDecimal value1 = getFertilizingCount(questionDTOList);
        GeneralCoefficientValue coefFertilizing = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Gübreleme işlem katsayısı");//Sulama katsayısı
        BigDecimal fertilizingCount = value1.multiply(coefFertilizing.getValue());
        BigDecimal value2 = getAgriculturalOp(questionDTOList);
        //Çapalama sayısı
        BigDecimal weedingCount = getAsBigDecimalByQuestionId(questionDTOList, question31Id);
        //Çapalama sayısı
        weedingCount = weedingCount.add(getAsBigDecimalByQuestionId(questionDTOList, question32Id));
        BigDecimal value3 = weedingCount.setScale(3, RoundingMode.HALF_UP);
        //Budama yapılmakta mıdır?
        BigDecimal value4 = BigDecimal.ZERO;
        Optional<QuestionWithFirstValue> dto4Optional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(question4Id)).findFirst();
        if (dto4Optional.isPresent() && dto4Optional.get().getValue().equals("Evet")) {
            value4 = BigDecimal.ONE;
        }
        //Meyve seyreltmesi yapılmakta mıdır?
        BigDecimal value5 = BigDecimal.ZERO;
        Optional<QuestionWithFirstValue> dto5Optional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(question5Id)).findFirst();
        if (dto5Optional.isPresent() && dto5Optional.get().getValue().equals("Evet")) {
            value5 = BigDecimal.ONE;
        }
        //Yaprak seyreltmesi yapılmakta mıdır?
        BigDecimal value6 = BigDecimal.ZERO;
        Optional<QuestionWithFirstValue> dto6Optional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(question6Id)).findFirst();
        if (dto6Optional.isPresent() && dto6Optional.get().getValue().equals("Evet")) {
            value6 = BigDecimal.ONE;
        }
        //Diğer bakım işlemi kaç kez yapılmaktadır?
        BigDecimal value7 = getAsBigDecimalByQuestionId(questionDTOList, question7Id);
        //malçlama
        //yabani ot kontrolü
        BigDecimal value8 = BigDecimal.ZERO;
        Optional<QuestionWithFirstValue> dto8Optional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(question8Id)).findFirst();
        if (dto8Optional.isPresent() && dto8Optional.get().getValue().contains("Malçlama")) {
            value8 = BigDecimal.ONE;
        }
        return wateringCount.add(fertilizingCount).add(value2).add(value3).add(value4).add(value5).add(value6).add(value7).add(value8);
    }

    private BigDecimal getAgriculturalOp(List<QuestionWithFirstValue> questionDTOList) {
        BigDecimal operationCount = getAgriculturalOpWithHand(questionDTOList);
        operationCount = operationCount.add(getAgriculturalOpWithMachine(questionDTOList));
        operationCount = operationCount.add(getAgriculturalOpWithTractor(questionDTOList));
        operationCount = operationCount.add(getAgriculturalOpWithWatering(questionDTOList));
        GeneralCoefficientValue generalCoef = generalCoefficientService.getFirstActiveByGeneralCoefficientName("İlaçlama işlem katsayısı)");
        operationCount = operationCount.multiply(generalCoef.getValue());
        return operationCount;
    }

    private BigDecimal getAgriculturalOpWithHand(List<QuestionWithFirstValue> questionDTOList) {
        BigDecimal operationCount = BigDecimal.ZERO;
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 118L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 122L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 126L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 130L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 134L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 138L));
        return operationCount;
    }

    private BigDecimal getAgriculturalOpWithMachine(List<QuestionWithFirstValue> questionDTOList) {
        BigDecimal operationCount = BigDecimal.ZERO;
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 116L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 120L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 124L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 128L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 132L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 136L));
        return operationCount;
    }

    private BigDecimal getAgriculturalOpWithTractor(List<QuestionWithFirstValue> questionDTOList) {
        BigDecimal operationCount = BigDecimal.ZERO;
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 117L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 121L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 125L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 129L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 133L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 137L));
        return operationCount;
    }

    private BigDecimal getAgriculturalOpWithWatering(List<QuestionWithFirstValue> questionDTOList) {
        BigDecimal operationCount = BigDecimal.ZERO;
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 119L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 123L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 127L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 131L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 135L));
        operationCount = operationCount.add(getAsBigDecimalByQuestionId(questionDTOList, 139L));
        return operationCount;
    }

    private BigDecimal getFertilizingCount(List<QuestionWithFirstValue> questionDTOList) {
        BigDecimal fertilizingOpCount = getFertilizingCountWithHand(questionDTOList);
        fertilizingOpCount = fertilizingOpCount.add(getFertilizingCountWithMachine(questionDTOList));
        fertilizingOpCount = fertilizingOpCount.add(getFertilizingCountWithWatering(questionDTOList));
        fertilizingOpCount = fertilizingOpCount.add(getFertilizingCountWithSeedling(questionDTOList));
        return fertilizingOpCount;
    }

    private BigDecimal getAsBigDecimalByQIDsAndTypeValueAndAverageValue(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long makineQuestionId,
                                                                        List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value,
                                                                        double fieldAverageValue) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            //Verim ne kadar
            BigDecimal efficiency = getAsBigDecimalByQuestionId(questionDTOList, makineQuestionId);
            if (dtoOptional.get().getValue().equals("El")) {
                BigDecimal harvestAvg = BigDecimal.valueOf(fieldAverageValue);
                if (harvestAvg.compareTo(BigDecimal.ZERO) > 0) {
                    return (efficiency.divide(harvestAvg, 3, RoundingMode.HALF_UP).multiply(new BigDecimal(8))).setScale(3, RoundingMode.HALF_UP);
                }
            } else if (dtoOptional.get().getValue().equals("El ve makine (sahipli)")) {
                //El ve makine – El
                BigDecimal haymakerCapacity = getAsBigDecimalByValueAndType(cropCoefficientList, type, value);
                if (haymakerCapacity.compareTo(BigDecimal.ZERO) > 0) {
                    return (efficiency.divide(haymakerCapacity, 3, RoundingMode.HALF_UP).multiply(new BigDecimal(8))).setScale(3, RoundingMode.HALF_UP);
                }
            }
        }
        return BigDecimal.ZERO;
    }

    //getManPowerTLForHarvest
    private BigDecimal getAsBigDecimalByGenderIdAndWorkingHour(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long womanQuestionId, Long manQuestionId, Long commonQuestionId,
                                                               BigDecimal workingHourValue) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Kadın")) {
                //Kadın yevmiyesi
                BigDecimal kadinValue = getAsBigDecimalByQuestionId(questionDTOList, womanQuestionId);
                return kadinValue.divide(workingHourValue, 3, RoundingMode.HALF_UP);
            } else if (dtoOptional.get().getValue().equals("Erkek")) {
                //erkek yevmiyesi
                BigDecimal erkekValue = getAsBigDecimalByQuestionId(questionDTOList, manQuestionId);
                return erkekValue.divide(workingHourValue, 3, RoundingMode.HALF_UP);
            } else {
                //ortak yevmiyesi
                BigDecimal ortakValue = getAsBigDecimalByQuestionId(questionDTOList, commonQuestionId);
                return ortakValue.divide(workingHourValue, 3, RoundingMode.HALF_UP);
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIdsAndTypeValues(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long hasatQuestionId,
                                                          List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value1, EnumCropCoefficientValue value2) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            BigDecimal coefDec = BigDecimal.ZERO;
            if (dtoOptional.get().getValue().equals("Makine (sahipli)")) {
                coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, type, value1);
            } else if (dtoOptional.get().getValue().equals("El ve makine (sahipli)")) {
                coefDec = getAsBigDecimalByValueAndType(cropCoefficientList, type, value2);
            }
            //Hasat sayısı
            BigDecimal harvestCount = getAsBigDecimalByQuestionId(questionDTOList, hasatQuestionId);
            return coefDec.multiply(harvestCount).setScale(3, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIDs(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long verimQuestionId, Long ambalajQuestionId) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Götürü işçilik çuval vb.")) {
                //Verim ne kadar
                BigDecimal efficiency = getAsBigDecimalByQuestionId(questionDTOList, verimQuestionId);
                //Ambalaj kapasitesi
                BigDecimal haymakerCapacity = getAsBigDecimalByQuestionId(questionDTOList, ambalajQuestionId);
                if (haymakerCapacity.compareTo(BigDecimal.ZERO) > 0) {
                    return (efficiency.divide(haymakerCapacity, 3, RoundingMode.HALF_UP));
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIDsAndGoturu(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long goturuQuestionId, Long hasatQuestionId, Long birimQuestionId) {
        //Hasat şekli
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Makine (götürü)")) {
                //Götürü
                BigDecimal harvestDec = getAsBigDecimalByQuestionId(questionDTOList, goturuQuestionId);
                //Hasat sayısı
                BigDecimal harvestCount = getAsBigDecimalByQuestionId(questionDTOList, hasatQuestionId);
                return (harvestDec.multiply(harvestCount)).setScale(3, RoundingMode.HALF_UP);
            } else if (dtoOptional.get().getValue().equals("Götürü işçilik çuval vb.")) {
                //Götürü birim fiyat
                BigDecimal unitPrice = getAsBigDecimalByQuestionId(questionDTOList, birimQuestionId);
                return unitPrice.setScale(3, RoundingMode.HALF_UP);
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIDsAndDieselTypesAndCoefficientTypeValue(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long verimQuestionId,
                                                                                  List<DieselDistanceWithFirstValue> dieselDistanceWithValueList, EnumDieselDistanceType parcelDistanceType, EnumDieselDistanceType coefFullType,
                                                                                  List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Evet")) {
                BigDecimal param1 = BigDecimal.ZERO;
                BigDecimal parcelDistance = BigDecimal.ZERO;
                BigDecimal coefFull = BigDecimal.ZERO;
                Optional<DieselDistanceWithFirstValue> parcelDistanceOptional = dieselDistanceWithValueList.stream().filter(dd -> dd.getEnumType().equals(parcelDistanceType)).findFirst();
                if (parcelDistanceOptional.isPresent()) {
                    parcelDistance = new BigDecimal(parcelDistanceOptional.get().getValue());
                }
                Optional<DieselDistanceWithFirstValue> coefFullOptional = dieselDistanceWithValueList.stream().filter(dd -> dd.getEnumType().equals(coefFullType)).findFirst();
                if (coefFullOptional.isPresent()) {
                    coefFull = new BigDecimal(coefFullOptional.get().getValue());
                }
                if (coefFull.compareTo(BigDecimal.ZERO) > 0) {
                    param1 = parcelDistance.divide(coefFull, 3, RoundingMode.HALF_UP);
                }
                //Verim ne kadar
                BigDecimal efficiency = getAsBigDecimalByQuestionId(questionDTOList, verimQuestionId);
                // El ve makine – Makine
                BigDecimal haymakerCapacity = getAsBigDecimalByValueAndType(cropCoefficientList, type, value);
                BigDecimal param2 = BigDecimal.ZERO;
                if (haymakerCapacity.compareTo(BigDecimal.ZERO) > 0) {
                    param2 = efficiency.divide(haymakerCapacity, 3, RoundingMode.HALF_UP);
                }
                return param1.multiply(param2).setScale(3, RoundingMode.HALF_UP);
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByCoefficientTypeValues(List<QuestionWithFirstValue> questionDTOList, Long questionId,
                                                              List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value1, EnumCropCoefficientValue value2) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("El")) {
                return getAsBigDecimalByValueAndType(cropCoefficientList, type, value1);
            } else if (dtoOptional.get().getValue().equals("El ve makine")) {
                return getAsBigDecimalByValueAndType(cropCoefficientList, type, value2);
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByWorkingHourValue(List<QuestionWithFirstValue> questionDTOList, Long questionId, BigDecimal workingHourValue) {
        BigDecimal manWage = getAsBigDecimalByQuestionId(questionDTOList, questionId);
        if (workingHourValue.compareTo(BigDecimal.ZERO) > 0) {
            return manWage.divide(workingHourValue, 3, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByGoturuQuestionId(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long goturuQuestionId) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Götürü")) {
                //Götürü harman fiyat
                return getAsBigDecimalByQuestionId(questionDTOList, goturuQuestionId);
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIDAndTypeValue(List<QuestionWithFirstValue> questionDTOList, Long questionId,
                                                        List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Makine sahipli")) {
                //İşçilik
                return getAsBigDecimalByValueAndType(cropCoefficientList, type, value);
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIdsAndTypeValue(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long verimQuestionId,
                                                         List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Makine götürü")) {
                return BigDecimal.ONE;
            } else if (dtoOptional.get().getValue().equals("Makine saatlik")) {
                //Verim ne kadar
                BigDecimal efficiency = getAsBigDecimalByQuestionId(questionDTOList, verimQuestionId);
                BigDecimal haymakerCapacity = getAsBigDecimalByValueAndType(cropCoefficientList, type, value);
                if (haymakerCapacity.compareTo(BigDecimal.ZERO) > 0) {
                    return efficiency.divide(haymakerCapacity, 3, RoundingMode.HALF_UP);
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIDsAndMakine(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long goturuQuestionId, Long saatlikQuestionId) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Makine götürü")) {
                //Götürü ücreti
                return getAsBigDecimalByQuestionId(questionDTOList, goturuQuestionId);
            } else if (dtoOptional.get().getValue().equals("Makine saatlik")) {
                //Saatlik ücret
                return getAsBigDecimalByQuestionId(questionDTOList, saatlikQuestionId);
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIdAndTypeValue(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long verimQuestionId,
                                                        List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Evet")) {
                //Verim ne kadar
                BigDecimal efficiency = getAsBigDecimalByQuestionId(questionDTOList, verimQuestionId);
                BigDecimal haymakerCapacity = getAsBigDecimalByValueAndType(cropCoefficientList, type, value);
                if (haymakerCapacity.compareTo(BigDecimal.ZERO) > 0) {
                    return (efficiency.divide(haymakerCapacity, 3, RoundingMode.HALF_UP).multiply(new BigDecimal(8))).setScale(3, RoundingMode.HALF_UP);
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIDsAndEvet(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long verimQuestionId) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Evet")) {
                //Malzeme birim fiyatı
                return getAsBigDecimalByQuestionId(questionDTOList, verimQuestionId);
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIDsAndUretici(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long verimQuestionId, Long answerQuestionId) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Üretici")) {
                //Verim ne kadar
                BigDecimal efficiency = getAsBigDecimalByQuestionId(questionDTOList, verimQuestionId);
                BigDecimal answerResult = getAsBigDecimalByQuestionId(questionDTOList, answerQuestionId);
                return efficiency.divide(answerResult, 3, RoundingMode.HALF_UP);
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIDsAndBale(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long verimQuestionId,
                                                    List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Sahipli")) {
                //İşçilik
                return getAsBigDecimalByValueAndType(cropCoefficientList, type, value);
            } else {
                if (dtoOptional.get().getValue().equals("Ana ürün") || dtoOptional.get().getValue().equals("Yan ürün")) {
                    //Verim ne kadar
                    BigDecimal answerMainYieldAmountResult = getAsBigDecimalByQuestionId(questionDTOList, verimQuestionId);
                    BigDecimal baleCapacity = getAsBigDecimalByValueAndType(cropCoefficientList, type, value);
                    if (baleCapacity.compareTo(BigDecimal.ZERO) > 0) {
                        return answerMainYieldAmountResult.divide(baleCapacity, 3, RoundingMode.HALF_UP);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIDsAndVerim14(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long balyaQuestionId, Long urunQuestionId, Long verimQuestionId, Long verim4QuestionId,
                                                       List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Evet")) {
                //Balya sahiplik
                Optional<QuestionWithFirstValue> balyaOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(balyaQuestionId)).findFirst();
                if (balyaOptional.isPresent()) {
                    if (dtoOptional.get().getValue().equals("Kiralık")) {
                        // ana/yan ürün
                        Optional<QuestionWithFirstValue> urunOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(urunQuestionId)).findFirst();
                        if (urunOptional.isPresent()) {
                            BigDecimal efficiency;
                            if (dtoOptional.get().getValue().equals("Ana ürün")) {
                                //Verim ne kadar
                                efficiency = getAsBigDecimalByQuestionId(questionDTOList, verimQuestionId);
                            } else {
                                //Verim ne kadar
                                efficiency = getAsBigDecimalByQuestionId(questionDTOList, verim4QuestionId);
                            }
                            BigDecimal haymakerCapacity = getAsBigDecimalByValueAndType(cropCoefficientList, type, value);
                            if (haymakerCapacity.compareTo(BigDecimal.ZERO) > 0) {
                                return efficiency.divide(haymakerCapacity, 3, RoundingMode.HALF_UP);
                            }
                        }
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByDieselEnumTypes(List<DieselDistanceWithFirstValue> dieselDistanceWithValueList,
                                                        EnumDieselDistanceType dieselDistanceType1, EnumDieselDistanceType dieselDistanceType2, EnumDieselDistanceType dieselDistanceType3) {
        Optional<DieselDistanceWithFirstValue> dieselDistance1Optional = dieselDistanceWithValueList.stream().filter(dd -> dd.getEnumType().equals(dieselDistanceType1)).findFirst();
        BigDecimal hallDistance = BigDecimal.ZERO;
        if (dieselDistance1Optional.isPresent()) {
            hallDistance = new BigDecimal(dieselDistance1Optional.get().getValue());
        }
        Optional<DieselDistanceWithFirstValue> dieselDistance2Optional = dieselDistanceWithValueList.stream().filter(dd -> dd.getEnumType().equals(dieselDistanceType2)).findFirst();
        BigDecimal tansportationCoef = BigDecimal.ZERO;
        if (dieselDistance2Optional.isPresent()) {
            tansportationCoef = new BigDecimal(dieselDistance2Optional.get().getValue());
        }
        GeneralCoefficientValue amortisman = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Taşıma aracı amortisman katsayısı");
        BigDecimal transportationVehicleCoef = amortisman.getValue();
        Optional<DieselDistanceWithFirstValue> dieselDistance3Optional = dieselDistanceWithValueList.stream().filter(dd -> dd.getEnumType().equals(dieselDistanceType3)).findFirst();
        BigDecimal dieselPrice = BigDecimal.ZERO;
        if (dieselDistance3Optional.isPresent()) {
            dieselPrice = new BigDecimal(dieselDistance3Optional.get().getValue());
        }
        if (tansportationCoef.compareTo(BigDecimal.ZERO) > 0) {
            return (hallDistance.divide(tansportationCoef, 3, RoundingMode.HALF_UP)).multiply(transportationVehicleCoef).multiply(dieselPrice).setScale(3, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByTotalAndEvet(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long question6Id) {
        //Hasat sayısı
        BigDecimal value0 = getAsBigDecimalByQuestionId(questionDTOList, questionId);
        BigDecimal value6 = BigDecimal.ZERO;
        //Balyalama yapılmaktamıdır
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(question6Id)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Evet")) {
                value6 = BigDecimal.ONE;
            }
        }
        return value0.add(value6);
    }

    private BigDecimal getTotalInputValueAsBigDecimal(List<QuestionWithFirstValue> questionDTOList,
                                                      List<SeedSeedlingWithFirstValue> seedSeedlingNumberWithValueList, List<SeedSeedlingWithFirstValue> seedSeedlingPriceWithValueList) {
        //Üretim materyali
        BigDecimal seedKgInput = getAsBigDecimalByQIDsAndSeedSeedlingKg(questionDTOList, 30L, 31L, 32L, seedSeedlingNumberWithValueList);
        //Üretim materyali
        BigDecimal seedTLInput = getAsBigDecimalByQIDsAndSeedSeedlingTL(questionDTOList, 30L, 31L, 32L, seedSeedlingPriceWithValueList);
        BigDecimal seedTotal = seedKgInput.multiply(seedTLInput).setScale(3, RoundingMode.HALF_UP);
        BigDecimal seedlingKgInput = getAsBigDecimalByIDsAndSeedSeedlingKg(questionDTOList, 30L, 33L, seedSeedlingNumberWithValueList);
        BigDecimal seedlingTLInput = getAsBigDecimalByIDsAndSeedSeedlingTL(questionDTOList, 30L, 33L, seedSeedlingPriceWithValueList);
        BigDecimal seedlingTotal = seedlingKgInput.multiply(seedlingTLInput).setScale(3, RoundingMode.HALF_UP);
        return seedTotal.add(seedlingTotal);
    }

    private BigDecimal getAsBigDecimalByIDsAndSeedSeedlingKg(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long fideQuestionId,
                                                             List<SeedSeedlingWithFirstValue> seedSeedlingNumberWithValueList) {
        //Üretim materyali
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Fide")) {
                //Dış kaynaktan temin edilen fide
                Optional<QuestionWithFirstValue> fideOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(fideQuestionId)).findFirst();
                if (fideOptional.isPresent()) {
                    BigDecimal seedlingPrice = BigDecimal.ZERO;
                    Optional<SeedSeedlingWithFirstValue> seedSeedlingValue = seedSeedlingNumberWithValueList.stream().filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.HYBRID_SEEDLING)).findFirst();
                    if (seedSeedlingValue.isPresent()) {
                        seedlingPrice = seedSeedlingValue.get().getValue();
                    }
                    BigDecimal oneGraft = BigDecimal.ZERO;
                    Optional<SeedSeedlingWithFirstValue> oneGraftSeedlingValue = seedSeedlingNumberWithValueList.stream().filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.HYBRID_SEEDLING_ONE_GRAFT)).findFirst();
                    if (oneGraftSeedlingValue.isPresent()) {
                        oneGraft = oneGraftSeedlingValue.get().getValue();
                    }
                    BigDecimal twoGraft = BigDecimal.ZERO;
                    Optional<SeedSeedlingWithFirstValue> twoGraftSeedlingValue = seedSeedlingNumberWithValueList.stream().filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.HYBRID_SEEDLING_TWO_GRAFT)).findFirst();
                    if (twoGraftSeedlingValue.isPresent()) {
                        twoGraft = twoGraftSeedlingValue.get().getValue();
                    }
                    if (fideOptional.get().getValue().equals("Aşısız")) {
                        return seedlingPrice.setScale(3, RoundingMode.HALF_UP);
                    } else if (fideOptional.get().getValue().equals("Tek aşılı")) {
                        return oneGraft.setScale(3, RoundingMode.HALF_UP);
                    } else if (fideOptional.get().getValue().equals("Çift aşılı")) {
                        return twoGraft.setScale(3, RoundingMode.HALF_UP);
                    } else if (fideOptional.get().getValue().equals("Karışık")) {
                        BigDecimal result = seedlingPrice.add(oneGraft.add(twoGraft));
                        return (result.divide(new BigDecimal(3))).setScale(3, RoundingMode.HALF_UP);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByIDsAndSeedSeedlingTL(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long fideQuestionId,
                                                             List<SeedSeedlingWithFirstValue> seedSeedlingNumberWithValueList) {
        //Üretim materyali
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Fide")) {
                //Dış kaynaktan temin edilen fide
                Optional<QuestionWithFirstValue> fideOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(fideQuestionId)).findFirst();
                if (fideOptional.isPresent()) {
                    BigDecimal seedlingPrice = BigDecimal.ZERO;
                    Optional<SeedSeedlingWithFirstValue> seedSeedlingValue = seedSeedlingNumberWithValueList.stream().filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.HYBRID_SEEDLING)).findFirst();
                    if (seedSeedlingValue.isPresent()) {
                        seedlingPrice = seedSeedlingValue.get().getValue();
                    }
                    BigDecimal oneGraft = BigDecimal.ZERO;
                    Optional<SeedSeedlingWithFirstValue> oneGraftSeedlingValue = seedSeedlingNumberWithValueList.stream().filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.HYBRID_SEEDLING_ONE_GRAFT)).findFirst();
                    if (oneGraftSeedlingValue.isPresent()) {
                        oneGraft = oneGraftSeedlingValue.get().getValue();
                    }
                    BigDecimal twoGraft = BigDecimal.ZERO;
                    Optional<SeedSeedlingWithFirstValue> twoGraftSeedlingValue = seedSeedlingNumberWithValueList.stream().filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.HYBRID_SEEDLING_TWO_GRAFT)).findFirst();
                    if (twoGraftSeedlingValue.isPresent()) {
                        twoGraft = twoGraftSeedlingValue.get().getValue();
                    }
                    if (fideOptional.get().getValue().equals("Aşısız")) {
                        return seedlingPrice.setScale(3, RoundingMode.HALF_UP);
                    } else if (fideOptional.get().getValue().equals("Tek aşılı")) {
                        return oneGraft.setScale(3, RoundingMode.HALF_UP);
                    } else if (fideOptional.get().getValue().equals("Çift aşılı")) {
                        return twoGraft.setScale(3, RoundingMode.HALF_UP);
                    } else if (fideOptional.get().getValue().equals("Karışık")) {
                        BigDecimal result = seedlingPrice.add(oneGraft.add(twoGraft));
                        return (result.divide(new BigDecimal(3))).setScale(3, RoundingMode.HALF_UP);
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIDsAndSeedSeedlingKg(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long tohumQuestionId, Long fideQuestionId,
                                                              List<SeedSeedlingWithFirstValue> seedSeedlingNumberWithValueList) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Tohum")) {
                Optional<QuestionWithFirstValue> tohumOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(tohumQuestionId)).findFirst();
                if (tohumOptional.isPresent()) {
                    if (tohumOptional.get().getValue().equals("Yerel")) {
                        Optional<SeedSeedlingWithFirstValue> seedSeedlingValue = seedSeedlingNumberWithValueList
                                .stream()
                                .filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.LOCAL_SEED_KG))
                                .findFirst();
                        if (seedSeedlingValue.isPresent()) {
                            BigDecimal localSeed = seedSeedlingValue.get().getValue();
                            return localSeed.setScale(3, RoundingMode.HALF_UP);
                        }
                    } else if (tohumOptional.get().getValue().equals("Hibrit")) {
                        Optional<SeedSeedlingWithFirstValue> seedSeedlingValue = seedSeedlingNumberWithValueList
                                .stream()
                                .filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.HYBRID_SEED_KG))
                                .findFirst();
                        if (seedSeedlingValue.isPresent()) {
                            BigDecimal hybridSeed = seedSeedlingValue.get().getValue();
                            return hybridSeed.setScale(3, RoundingMode.HALF_UP);
                        }
                    }
                }
            } else if (dtoOptional.get().getValue().equals("Fide")) {
                Optional<QuestionWithFirstValue> fideOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(fideQuestionId)).findFirst();
                if (fideOptional.isPresent()) {
                    if (!fideOptional.get().getValue().equals("Dış kaynak")) {
                        Optional<QuestionWithFirstValue> tohumOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(tohumQuestionId)).findFirst();
                        if (tohumOptional.isPresent()) {
                            if (tohumOptional.get().getValue().equals("Yerel")) {
                                Optional<SeedSeedlingWithFirstValue> seedSeedlingValue = seedSeedlingNumberWithValueList
                                        .stream()
                                        .filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.LOCAL_SEED_KG))
                                        .findFirst();
                                if (seedSeedlingValue.isPresent()) {
                                    BigDecimal localSeed = seedSeedlingValue.get().getValue();
                                    return localSeed.setScale(3, RoundingMode.HALF_UP);
                                }
                            } else if (tohumOptional.get().getValue().equals("Hibrit")) {
                                Optional<SeedSeedlingWithFirstValue> seedSeedlingValue = seedSeedlingNumberWithValueList
                                        .stream()
                                        .filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.HYBRID_SEED_KG))
                                        .findFirst();
                                if (seedSeedlingValue.isPresent()) {
                                    BigDecimal hybridSeed = seedSeedlingValue.get().getValue();
                                    return hybridSeed.setScale(3, RoundingMode.HALF_UP);
                                }
                            }
                        }
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIDsAndSeedSeedlingTL(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long tohumQuestionId, Long fideQuestionId,
                                                              List<SeedSeedlingWithFirstValue> seedSeedlingPriceWithValueList) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Tohum")) {
                Optional<QuestionWithFirstValue> tohumOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(tohumQuestionId)).findFirst();
                if (tohumOptional.isPresent()) {
                    if (tohumOptional.get().getValue().equals("Yerel")) {
                        Optional<SeedSeedlingWithFirstValue> seedSeedlingValue = seedSeedlingPriceWithValueList
                                .stream()
                                .filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.LOCAL_SEED_KG))
                                .findFirst();
                        if (seedSeedlingValue.isPresent()) {
                            BigDecimal localSeedPrice = seedSeedlingValue.get().getValue();
                            return localSeedPrice.setScale(3, RoundingMode.HALF_UP);
                        }
                    } else if (tohumOptional.get().getValue().equals("Hibrit")) {
                        Optional<SeedSeedlingWithFirstValue> seedSeedlingValue = seedSeedlingPriceWithValueList
                                .stream()
                                .filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.HYBRID_SEED_KG))
                                .findFirst();
                        if (seedSeedlingValue.isPresent()) {
                            BigDecimal hybridSeedPrice = seedSeedlingValue.get().getValue();
                            return hybridSeedPrice.setScale(3, RoundingMode.HALF_UP);
                        }
                    }
                }
            } else if (dtoOptional.get().getValue().equals("Fide")) {
                Optional<QuestionWithFirstValue> fideOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(fideQuestionId)).findFirst();
                if (fideOptional.isPresent()) {
                    if (!fideOptional.get().getValue().equals("Dış kaynak")) {
                        Optional<QuestionWithFirstValue> tohumOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(tohumQuestionId)).findFirst();
                        if (tohumOptional.isPresent()) {
                            if (tohumOptional.get().getValue().equals("Yerel")) {
                                Optional<SeedSeedlingWithFirstValue> seedSeedlingValue = seedSeedlingPriceWithValueList
                                        .stream()
                                        .filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.LOCAL_SEED_KG))
                                        .findFirst();
                                if (seedSeedlingValue.isPresent()) {
                                    BigDecimal localSeedPrice = seedSeedlingValue.get().getValue();
                                    return localSeedPrice.setScale(3, RoundingMode.HALF_UP);
                                }
                            } else if (tohumOptional.get().getValue().equals("Hibrit")) {
                                Optional<SeedSeedlingWithFirstValue> seedSeedlingValue = seedSeedlingPriceWithValueList
                                        .stream()
                                        .filter(ss -> ss.getEnumType().equals(EnumSeedSeedlingType.HYBRID_SEED_KG))
                                        .findFirst();
                                if (seedSeedlingValue.isPresent()) {
                                    BigDecimal hybridSeedPrice = seedSeedlingValue.get().getValue();
                                    return hybridSeedPrice.setScale(3, RoundingMode.HALF_UP);
                                }
                            }
                        }
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getFertilizerTotalValueAsBigDecimal(List<QuestionWithFirstValue> questionDTOList,
                                                           List<CityFertilizerWithFirstValue> cityFertilizerWithValueList) {
        //Azotlu gübre 1 öneriniz ne kadardır?
        BigDecimal value1 = getAsBigDecimalByQuestionId(questionDTOList, 80L);
        //Azotlu gübre 1 öneriniz hangi gübre çeşidi içindir?
        Optional<QuestionWithFirstValue> typeOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(81L)).findFirst();
        BigDecimal fertilizerPrice = BigDecimal.ZERO;
        if (typeOptional.isPresent()) {
            if (!typeOptional.get().getValue().equals("0")) {
                Optional<CityFertilizerWithFirstValue> fertilizerOptional = cityFertilizerWithValueList.stream().filter(cf ->
                        cf.getEnumType().equals(EnumFertilizerType.NITROGEN_FERTILIZERS) &&
                                cf.getOldFertilizerId().equals(Long.valueOf(typeOptional.get().getValue()))).findFirst();
                if (fertilizerOptional.isPresent()) {
                    fertilizerPrice = fertilizerOptional.get().getPrice();
                }
            }
        }
        GeneralCoefficientValue nitrogenPriceCoef = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Azotlu gübre fiyat artış oranı");
        BigDecimal nitrogenPriceRate = nitrogenPriceCoef.getValue();
        BigDecimal value2 = fertilizerPrice.multiply(nitrogenPriceRate).setScale(3, RoundingMode.HALF_UP);
        BigDecimal nitrogenTotal = value1.multiply(value2).setScale(3, RoundingMode.HALF_UP);
        //Azotlu gübre 2 öneriniz ne kadardır?
        BigDecimal value3 = getAsBigDecimalByQuestionId(questionDTOList, 82L);
        //Azotlu gübre 2 öneriniz hangi gübre çeşidi içindir?
        Optional<QuestionWithFirstValue> type2Optional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(83L)).findFirst();
        BigDecimal fertilizer2Price = BigDecimal.ZERO;
        if (type2Optional.isPresent()) {
            if (!type2Optional.get().getValue().equals("0")) {
                Optional<CityFertilizerWithFirstValue> fertilizerOptional = cityFertilizerWithValueList.stream().filter(cf ->
                        cf.getEnumType().equals(EnumFertilizerType.NITROGEN_FERTILIZERS) &&
                                cf.getOldFertilizerId().equals(Long.valueOf(type2Optional.get().getValue()))).findFirst();
                if (fertilizerOptional.isPresent()) {
                    fertilizer2Price = fertilizerOptional.get().getPrice();
                }
            }
        }
        BigDecimal value4 = fertilizer2Price.multiply(nitrogenPriceRate).setScale(3, RoundingMode.HALF_UP);
        BigDecimal nitrogen2Total = value3.multiply(value4).setScale(3, RoundingMode.HALF_UP);
        //Fosforlu gübre
        BigDecimal value5 = getAsBigDecimalByQuestionId(questionDTOList, 84L);
        Optional<QuestionWithFirstValue> type3Optional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(85L)).findFirst();
        BigDecimal fertilizer3Price = BigDecimal.ZERO;
        if (type3Optional.isPresent()) {
            if (!type3Optional.get().getValue().equals("0")) {
                Optional<CityFertilizerWithFirstValue> fertilizerOptional = cityFertilizerWithValueList.stream().filter(cf ->
                        cf.getEnumType().equals(EnumFertilizerType.PHOSPHOR_FERTILIZERS) &&
                                cf.getOldFertilizerId().equals(Long.valueOf(type3Optional.get().getValue()))).findFirst();
                if (fertilizerOptional.isPresent()) {
                    fertilizer3Price = fertilizerOptional.get().getPrice();
                }
            }
        }
        GeneralCoefficientValue phosphorPriceCoef = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Fosforlu gübre fiyat artış oranı");
        BigDecimal phosphorPriceRate = phosphorPriceCoef.getValue();
        BigDecimal value6 = fertilizer3Price.multiply(phosphorPriceRate).setScale(3, RoundingMode.HALF_UP);
        BigDecimal phosphorTotal = value5.multiply(value6).setScale(3, RoundingMode.HALF_UP);
        //Potasyumlu gübre
        BigDecimal value7 = getAsBigDecimalByQuestionId(questionDTOList, 86L);
        //Potasyumlu gübre
        Optional<QuestionWithFirstValue> type4Optional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(87L)).findFirst();
        BigDecimal fertilizer4Price = BigDecimal.ZERO;
        if (type4Optional.isPresent()) {
            if (!type4Optional.get().getValue().equals("0")) {
                Optional<CityFertilizerWithFirstValue> fertilizerOptional = cityFertilizerWithValueList.stream().filter(cf ->
                        cf.getEnumType().equals(EnumFertilizerType.POTASSIUM_FERTILIZERS) &&
                                cf.getOldFertilizerId().equals(Long.valueOf(type4Optional.get().getValue()))).findFirst();
                if (fertilizerOptional.isPresent()) {
                    fertilizer4Price = fertilizerOptional.get().getPrice();
                }
            }
        }
        GeneralCoefficientValue potassiumPriceCoef = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Potasyumlu gübre fiyat artış oranı)");
        BigDecimal potassiumPriceRate = potassiumPriceCoef.getValue();
        BigDecimal value8 = fertilizer4Price.multiply(potassiumPriceRate).setScale(3, RoundingMode.HALF_UP);
        BigDecimal potassiumTotal = value7.multiply(value8).setScale(3, RoundingMode.HALF_UP);
        //Kompoze gübre
        BigDecimal value9 = getAsBigDecimalByQuestionId(questionDTOList, 88L);
        //Kompoze gübre
        Optional<QuestionWithFirstValue> type5Optional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(89L)).findFirst();
        BigDecimal fertilizer5Price = BigDecimal.ZERO;
        if (type5Optional.isPresent()) {
            if (!type5Optional.get().getValue().equals("0")) {
                Optional<CityFertilizerWithFirstValue> fertilizerOptional = cityFertilizerWithValueList.stream().filter(cf ->
                        cf.getEnumType().equals(EnumFertilizerType.COMPOUND_FERTILIZERS) &&
                                cf.getOldFertilizerId().equals(Long.valueOf(type5Optional.get().getValue()))).findFirst();
                if (fertilizerOptional.isPresent()) {
                    fertilizer5Price = fertilizerOptional.get().getPrice();
                }
            }
        }
        GeneralCoefficientValue compoundPriceCoef = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Kompoze gübre fiyat artış oranı");
        BigDecimal compoundPriceRate = compoundPriceCoef.getValue();
        BigDecimal value10 = fertilizer5Price.multiply(compoundPriceRate).setScale(3, RoundingMode.HALF_UP);
        BigDecimal compoundTotal = value9.multiply(value10).setScale(3, RoundingMode.HALF_UP);
        //Sıvı gübresi uygulanıyormu
        BigDecimal value11 = getAsBigDecimalByQuestionIdOrtalama(questionDTOList, 90L);
        // price rate
        BigDecimal result = BigDecimal.ZERO;
        List<CityFertilizerWithFirstValue> foliarValueList = cityFertilizerWithValueList.stream().filter(cf -> cf.getEnumType().equals(EnumFertilizerType.FOLIAR_FERTILIZERS)).toList();
        if (foliarValueList != null && !foliarValueList.isEmpty()) {
            for (CityFertilizerWithFirstValue f : foliarValueList) {
                result = result.add(f.getPrice());
            }
        }
        GeneralCoefficientValue foliarPriceCoef = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Yaprak gübresi fiyat artış oranı)");
        BigDecimal foliarPriceRate = foliarPriceCoef.getValue();
        BigDecimal value12 = (result.divide(new BigDecimal(3), 3, RoundingMode.HALF_UP)).multiply(foliarPriceRate).setScale(3, RoundingMode.HALF_UP);
        BigDecimal liquidTotal = value11.multiply(value12).setScale(3, RoundingMode.HALF_UP);
        //Toprak düzenleyici kullanıyormu
        BigDecimal value13 = getAsBigDecimalByQIDsAndEvet(questionDTOList, 114L, 115L);
        // price rate
        BigDecimal resultHumic = BigDecimal.ZERO;
        List<CityFertilizerWithFirstValue> soilValueList = cityFertilizerWithValueList.stream().filter(cf -> cf.getEnumType().equals(EnumFertilizerType.SOIL_CONDITIONERS)).toList();
        if (soilValueList != null && !soilValueList.isEmpty()) {
            for (CityFertilizerWithFirstValue s : soilValueList) {
                resultHumic = resultHumic.add(s.getPrice());
            }
        }
        GeneralCoefficientValue soilConditionerCoef = generalCoefficientService.getFirstActiveByGeneralCoefficientName("Toprak düzenleyiciler fiyat artış oranı");
        BigDecimal soilConditionerPriceRate = soilConditionerCoef.getValue();
        BigDecimal value14 = (resultHumic.divide(new BigDecimal(3), 3, RoundingMode.HALF_UP)).multiply(soilConditionerPriceRate).setScale(3, RoundingMode.HALF_UP);
        BigDecimal humiqTotal = value13.multiply(value14).setScale(3, RoundingMode.HALF_UP);
        //Bu üründe çiftlik gübresi kullanımı varmı
        BigDecimal value15 = getAsBigDecimalByQIDsAndEvet(questionDTOList, 111L, 112L);
        //Bu üründe çiftlik gübresi kullanımı varmı
        //gübre fiyatı
        BigDecimal value16 = getAsBigDecimalByQIDsAndEvet(questionDTOList, 111L, 113L);
        BigDecimal fertilizerTotal = value15.multiply(value16).setScale(3, RoundingMode.HALF_UP);
        return nitrogenTotal.add(nitrogen2Total).add(phosphorTotal).add(potassiumTotal).add(compoundTotal).add(liquidTotal).add(humiqTotal).add(fertilizerTotal);
    }

    private BigDecimal getAsBigDecimalByQuestionIdOrtalama(List<QuestionWithFirstValue> questionDTOList, Long questionId) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Evet")) {
                //Ortalama uygulama sayısı
                BigDecimal count = getAsBigDecimalByQuestionId(questionDTOList, 107L);
                count = count.add(getAsBigDecimalByQuestionId(questionDTOList, 108L));
                count = count.add(getAsBigDecimalByQuestionId(questionDTOList, 109L));
                count = count.add(getAsBigDecimalByQuestionId(questionDTOList, 110L));
                return count.setScale(3, RoundingMode.HALF_UP);
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getMedicineTotalValueAsBigDecimal(List<QuestionWithFirstValue> questionDTOList) {
        GeneralCoefficientValue medicineRateCoef = generalCoefficientService.getFirstActiveByGeneralCoefficientName("İlaç fiyat artış oranı");
        //Yabani Ot Konrolü için hangi yöntemler kullanılmaktadır?
        medicineRateCoef.getValue();
        BigDecimal value1 = getAsBigDecimalBy(questionDTOList, 48L);
        //TODO
        return BigDecimal.ZERO;
//        BigDecimal value2 =;
//
//        BigDecimal herbisitTotal = value1.multiply(new BigDecimal(medicineMaterialTLList.get(0))).setScale(3, RoundingMode.HALF_UP);
//
//
//        return herbisitTotal.add(insektisitTotal).add(fungusitTotal).add(akarisitTotal).add(prophylacticTotal).add(hormoneTotal);
    }

    //getOperationForMedicine
    private BigDecimal getAsBigDecimalBy(List<QuestionWithFirstValue> questionDTOList, Long questionId) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("İlaç")) {
                BigDecimal herbisitCount = getAsBigDecimalByQuestionId(questionDTOList, 116L);//herbisit-sırt mak
                herbisitCount = herbisitCount.add(getAsBigDecimalByQuestionId(questionDTOList, 117L));//herbisit-traktor
                herbisitCount = herbisitCount.add(getAsBigDecimalByQuestionId(questionDTOList, 118L));//herbisit-el
                herbisitCount = herbisitCount.add(getAsBigDecimalByQuestionId(questionDTOList, 119L));//herbisit-sulama
                return herbisitCount.setScale(3, RoundingMode.HALF_UP);
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getWaterPriceTotalValueAsBigDecimal(Optional<CityCropWateringValue> wateringValueOptional) {
        if (wateringValueOptional.isPresent()) {
            return wateringValueOptional.get().getMaintenance().setScale(3, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getAsBigDecimalByQIdAndTypeValueNakil(List<QuestionWithFirstValue> questionDTOList, Long questionId, Long verimQuestionId,
                                                             List<CropCoefficientWithFirstValue> cropCoefficientList, EnumCropCoefficientType type, EnumCropCoefficientValue value) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(questionId)).findFirst();
        if (dtoOptional.isPresent()) {
            if (dtoOptional.get().getValue().equals("Evet")) {
                //Verim ne kadar
                BigDecimal efficiency = getAsBigDecimalByQuestionId(questionDTOList, verimQuestionId);
                BigDecimal haymakerCapacity = getAsBigDecimalByValueAndType(cropCoefficientList, type, value);
                if (haymakerCapacity.compareTo(BigDecimal.ZERO) > 0) {
                    return efficiency.divide(haymakerCapacity, 3, RoundingMode.HALF_UP);
                }
            }
        }
        return BigDecimal.ZERO;
    }


}

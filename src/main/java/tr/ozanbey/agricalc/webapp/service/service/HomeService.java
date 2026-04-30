package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.domain.CityCrop;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropWateringValue;
import tr.ozanbey.agricalc.webapp.service.domain.Crop;
import tr.ozanbey.agricalc.webapp.service.dto.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class HomeService extends BaseService {

    private final CityService cityService;
    private final CropService cropService;
    private final CityCropService cityCropService;
    private final CityCropQuestionService cityCropQuestionService;
    private final ExpenseService expenseService;
    private final CropCoefficientService cropCoefficientService;
    private final CityDieselDistanceService dieselDistanceService;
    private final CityCropSeedAndSeedlingNumberService seedSeedlingNumberService;
    private final CityCropFieldHarvestAverageValueService fieldHarvestAverageValueService;
    private final CityFertilizerService cityFertilizerService;
    private final CityCropWateringValueService wateringValueService;

    public HomeService(CityService cityService,
                       CropService cropService,
                       CityCropService cityCropService,
                       CityCropQuestionService cityCropQuestionService,
                       ExpenseService expenseService,
                       CropCoefficientService cropCoefficientService,
                       CityDieselDistanceService dieselDistanceService,
                       CityCropSeedAndSeedlingNumberService seedSeedlingNumberService,
                       CityCropFieldHarvestAverageValueService fieldHarvestAverageValueService,
                       CityFertilizerService cityFertilizerService,
                       CityCropWateringValueService wateringValueService
    ) {
        this.cityService = cityService;
        this.cropService = cropService;
        this.cityCropService = cityCropService;
        this.cityCropQuestionService = cityCropQuestionService;
        this.expenseService = expenseService;
        this.cropCoefficientService = cropCoefficientService;
        this.dieselDistanceService = dieselDistanceService;
        this.seedSeedlingNumberService = seedSeedlingNumberService;
        this.fieldHarvestAverageValueService = fieldHarvestAverageValueService;
        this.cityFertilizerService = cityFertilizerService;
        this.wateringValueService = wateringValueService;
    }

    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    public List<Crop> getAllActiveCrops() {
        return cropService.getByStatus(EnumStatus.ACTIVE);
    }

    public Map<String, BigDecimal> calculate(Long selectedCityId, Long selectedCropId) {
        Map<String, BigDecimal> returnMap = new HashMap<>();
        CityCrop cityCrop = cityCropService.getByStatusAndCityIdAndCropId(EnumStatus.ACTIVE, selectedCityId, selectedCropId);
        if (cityCrop != null) {
            List<QuestionWithFirstAnswer> questionDTOList = cityCropQuestionService.getDTOsByStatusAndCityCropId(EnumStatus.ACTIVE, cityCrop.getId());
            List<QuestionWithFirstAnswer> selectedDTOList = questionDTOList.stream().filter(dto -> new ArrayList<>(List.of(1L, 3L, 4L)).contains(dto.getQuestionId())).toList();
            List<BigDecimal> yieldList = calculateYield(selectedDTOList);
            returnMap.put("yield", yieldList.getFirst());
            selectedDTOList = questionDTOList.stream().filter(dto -> new ArrayList<>(List.of(10L, 12L, 16L)).contains(dto.getQuestionId())).toList();
            List<BigDecimal> priceList = calculatePrice(selectedDTOList);
            returnMap.put("price", priceList.getFirst());
            BigDecimal income = calculateIncome(yieldList.getFirst(), priceList.getFirst());
            returnMap.put("income", income);
            List<CropCoefficientWithFirstValue> cropCoefficientList = cropCoefficientService.getDTOsByStatusAndCropId(EnumStatus.ACTIVE, cityCrop.getCrop().getId());
            List<DieselDistanceWithFirstValue> dieselDistanceWithValueList = dieselDistanceService.getDTOsByCityId(cityCrop.getCity().getId());
            List<SeedSeedlingNumberWithFirstValue> seedSeedlingNumberWithValueList = seedSeedlingNumberService.getDTOsByStatusAndCityCropId(EnumStatus.ACTIVE, cityCrop.getId());
            List<CityFertilizerWithFirstValue> cityFertilizerWithValueList = cityFertilizerService.getDTOsByStatusAndCityId(EnumStatus.ACTIVE, cityCrop.getCity().getId());
            double fieldAverageValue = fieldHarvestAverageValueService.getAverageValueByCityCropId(cityCrop.getId());
            Optional<CityCropWateringValue> wateringValueOptional = wateringValueService.getFirstByCityCropIdOrderByInsertDateDesc(cityCrop.getId());
            BigDecimal expense = expenseService.calculateExpense(questionDTOList, cropCoefficientList, dieselDistanceWithValueList, seedSeedlingNumberWithValueList, cityFertilizerWithValueList, fieldAverageValue, wateringValueOptional);
            returnMap.put("expense", expense);
        }
        return returnMap;
    }

    private List<BigDecimal> calculateYield(List<QuestionWithFirstAnswer> selectedDTOList) {
        List<BigDecimal> yieldList = new ArrayList<>();
        BigDecimal answer1 = BigDecimal.ZERO;
        BigDecimal returnVal1 = BigDecimal.ZERO;
        BigDecimal returnVal2 = BigDecimal.ZERO;
        BigDecimal returnVal3 = BigDecimal.ZERO;
        for (QuestionWithFirstAnswer dto : selectedDTOList) {
            BigDecimal value = new BigDecimal(dto.getValue()).setScale(3, RoundingMode.HALF_UP);
            if (dto.getQuestionId().equals(1L)) {
                returnVal1 = value;
            } else if (dto.getQuestionId().equals(3L)) {
                BigDecimal divide = value.multiply(answer1).divide(new BigDecimal(100), 3, RoundingMode.HALF_UP);
                returnVal1 = answer1.subtract(divide);
                returnVal2 = divide;
            } else if (dto.getQuestionId().equals(4L)) {
                returnVal3 = value;
            }
        }
        yieldList.add(0, returnVal1);
        yieldList.add(1, returnVal2);
        yieldList.add(2, returnVal3);
        yieldList.add(3, BigDecimal.ZERO);
        return yieldList;
    }

    private List<BigDecimal> calculatePrice(List<QuestionWithFirstAnswer> selectedDTOList) {
        List<BigDecimal> priceList = new ArrayList<>();
        BigDecimal returnVal1 = BigDecimal.ZERO;
        BigDecimal returnVal2 = BigDecimal.ZERO;
        BigDecimal returnVal3 = BigDecimal.ZERO;
        BigDecimal returnVal4 = BigDecimal.ZERO;
        for (QuestionWithFirstAnswer dto : selectedDTOList) {
            BigDecimal value = new BigDecimal(dto.getValue()).setScale(3, RoundingMode.HALF_UP);
            if (dto.getQuestionId().equals(10L)) {
                returnVal1 = value;
                returnVal4 = value;
            } else if (dto.getQuestionId().equals(12L)) {
                returnVal2 = value;
            } else if (dto.getQuestionId().equals(16L)) {
                returnVal3 = value;
            }
        }
        priceList.add(0, returnVal1);
        priceList.add(1, returnVal2);
        priceList.add(2, returnVal3);
        priceList.add(3, returnVal4);
        return priceList;
    }

    private BigDecimal calculateIncome(BigDecimal yield, BigDecimal price) {
        return yield.multiply(price).setScale(3, RoundingMode.HALF_UP);
    }
    
}

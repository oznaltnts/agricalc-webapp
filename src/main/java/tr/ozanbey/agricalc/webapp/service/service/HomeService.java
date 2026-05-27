package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.CityCrop;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropWateringValue;
import tr.ozanbey.agricalc.webapp.service.dto.*;
import tr.ozanbey.agricalc.webapp.webapp.view.HomePageView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HomeService extends BaseService {

    private final CityCropService cityCropService;
    private final CityCropQuestionService cityCropQuestionService;
    private final ExpenseService expenseService;
    private final CropCoefficientService cropCoefficientService;
    private final CityDieselDistanceService dieselDistanceService;
    private final CityCropSeedSeedlingNumberService seedSeedlingNumberService;
    private final CityCropSeedSeedlingPriceService seedSeedlingPriceService;
    private final CityCropFieldHarvestAverageValueService fieldHarvestAverageValueService;
    private final CityFertilizerService cityFertilizerService;
    private final CityCropWateringValueService wateringValueService;

    public HomeService(CityCropService cityCropService,
                       CityCropQuestionService cityCropQuestionService,
                       ExpenseService expenseService,
                       CropCoefficientService cropCoefficientService,
                       CityDieselDistanceService dieselDistanceService,
                       CityCropSeedSeedlingNumberService seedSeedlingNumberService,
                       CityCropSeedSeedlingPriceService seedSeedlingPriceService,
                       CityCropFieldHarvestAverageValueService fieldHarvestAverageValueService,
                       CityFertilizerService cityFertilizerService,
                       CityCropWateringValueService wateringValueService
    ) {
        this.cityCropService = cityCropService;
        this.cityCropQuestionService = cityCropQuestionService;
        this.expenseService = expenseService;
        this.cropCoefficientService = cropCoefficientService;
        this.dieselDistanceService = dieselDistanceService;
        this.seedSeedlingNumberService = seedSeedlingNumberService;
        this.seedSeedlingPriceService = seedSeedlingPriceService;
        this.fieldHarvestAverageValueService = fieldHarvestAverageValueService;
        this.cityFertilizerService = cityFertilizerService;
        this.wateringValueService = wateringValueService;
    }

    public List<CityCrop> getAllActiveCityCrop() {
        return cityCropService.getAllActiveCityCrop();
    }

    public List<HomePageView> calculate(CityCrop cityCrop) {
        List<HomePageView> returnList = new ArrayList<>();
        if (cityCrop != null) {
            List<QuestionWithFirstValue> questionDTOList = cityCropQuestionService.getActiveDTOsByCityCropId(cityCrop.getId());
            List<QuestionWithFirstValue> selectedDTOList = questionDTOList.stream().filter(dto -> new ArrayList<>(List.of(1L, 3L, 4L)).contains(dto.getQuestionId())).toList();
            List<BigDecimal> yieldList = calculateYield(selectedDTOList);
            returnList.add(new HomePageView("Verim", yieldList.getFirst().toString(), false));
            selectedDTOList = questionDTOList.stream().filter(dto -> new ArrayList<>(List.of(10L, 12L, 16L)).contains(dto.getQuestionId())).toList();
            List<BigDecimal> priceList = calculatePrice(selectedDTOList);
            returnList.add(new HomePageView("Fiyat", priceList.getFirst().toString(), true));
            BigDecimal income = calculateIncome(yieldList.getFirst(), priceList.getFirst());
            returnList.add(new HomePageView("Gelir", income.toString(), true));
            List<CropCoefficientWithFirstValue> cropCoefficientList = cropCoefficientService.getActiveDTOsByCropId(cityCrop.getCrop().getId());
            List<DieselDistanceWithFirstValue> dieselDistanceWithValueList = dieselDistanceService.getActiveDTOsByCityId(cityCrop.getCity().getId());
            List<SeedSeedlingWithFirstValue> seedSeedlingNumberWithValueList = seedSeedlingNumberService.getActiveDTOsByCityCropId(cityCrop.getId());
            List<SeedSeedlingWithFirstValue> seedSeedlingPriceWithValueList = seedSeedlingPriceService.getActiveDTOsByCityCropId(cityCrop.getId());
            List<CityFertilizerWithFirstValue> cityFertilizerWithValueList = cityFertilizerService.getActiveDTOsByCityId(cityCrop.getCity().getId());
            double fieldAverageValue = fieldHarvestAverageValueService.getAverageValueByCityCropId(cityCrop.getId());
            Optional<CityCropWateringValue> wateringValueOptional = wateringValueService.getFirstByCityCropIdOrderByInsertDateDesc(cityCrop.getId());
            BigDecimal expense = expenseService.calculateExpense(questionDTOList, cropCoefficientList, dieselDistanceWithValueList,
                    seedSeedlingNumberWithValueList, seedSeedlingPriceWithValueList,
                    cityFertilizerWithValueList, fieldAverageValue, wateringValueOptional);
            returnList.add(new HomePageView("Gider", expense.toString(), true));
            String maturity = calculateMaturity(questionDTOList);
            returnList.add(new HomePageView("Hasat ayı", maturity, false));
        }
        return returnList;
    }

    private String calculateMaturity(List<QuestionWithFirstValue> questionDTOList) {
        Optional<QuestionWithFirstValue> dtoOptional = questionDTOList.stream().filter(dto -> dto.getQuestionId().equals(20L)).findFirst();
        if (dtoOptional.isPresent()) {
            return dtoOptional.get().getValue();
        }
        return "0";
    }

    private List<BigDecimal> calculateYield(List<QuestionWithFirstValue> selectedDTOList) {
        List<BigDecimal> yieldList = new ArrayList<>();
        BigDecimal questionValue1 = BigDecimal.ZERO;
        BigDecimal returnVal1 = BigDecimal.ZERO;
        BigDecimal returnVal2 = BigDecimal.ZERO;
        BigDecimal returnVal3 = BigDecimal.ZERO;
        for (QuestionWithFirstValue dto : selectedDTOList) {
            BigDecimal value = new BigDecimal(dto.getValue()).setScale(3, RoundingMode.HALF_UP);
            if (dto.getQuestionId().equals(1L)) {
                returnVal1 = value;
            } else if (dto.getQuestionId().equals(3L)) {
                BigDecimal divide = value.multiply(questionValue1).divide(new BigDecimal(100), 3, RoundingMode.HALF_UP);
                returnVal1 = questionValue1.subtract(divide);
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

    private List<BigDecimal> calculatePrice(List<QuestionWithFirstValue> selectedDTOList) {
        List<BigDecimal> priceList = new ArrayList<>();
        BigDecimal returnVal1 = BigDecimal.ZERO;
        BigDecimal returnVal2 = BigDecimal.ZERO;
        BigDecimal returnVal3 = BigDecimal.ZERO;
        BigDecimal returnVal4 = BigDecimal.ZERO;
        for (QuestionWithFirstValue dto : selectedDTOList) {
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

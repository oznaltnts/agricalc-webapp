package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropQuestionAnswer;
import tr.ozanbey.agricalc.webapp.service.domain.Crop;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeService extends BaseService {

    private final CityService cityService;
    private final CropService cropService;
    private final CityCropService cityCropService;
    private final CityCropQuestionService cityCropQuestionService;

    public HomeService(CityService cityService,
                       CropService cropService,
                       CityCropService cityCropService,
                       CityCropQuestionService cityCropQuestionService
    ) {
        this.cityService = cityService;
        this.cropService = cropService;
        this.cityCropService = cityCropService;
        this.cityCropQuestionService = cityCropQuestionService;
    }

    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    public List<Crop> getAllActiveCrops() {
        return cropService.getByStatus(EnumStatus.ACTIVE);
    }

    public Map<String, BigDecimal> calculate(Long selectedCityId, Long selectedCropId) {
        Map<String, BigDecimal> returnMap = new HashMap<>();
        Long cityCropId = cityCropService.getByStatusAndCityIdAndCropId(EnumStatus.ACTIVE, selectedCityId, selectedCropId).getId();
        if (cityCropId != null) {
            List<CityCropQuestion> cityCropQuestionList = cityCropQuestionService.getByStatusAndCityCropIdOrderByQuestionId(EnumStatus.ACTIVE, cityCropId);
            List<CityCropQuestion> selectedQuestionList = cityCropQuestionList.stream().filter(ccq -> new ArrayList<>(List.of(1L, 3L, 4L)).contains(ccq.getQuestion().getId())).toList();
            List<BigDecimal> yieldList = calculateYield(selectedQuestionList);
            returnMap.put("yield", yieldList.getFirst());
            selectedQuestionList = cityCropQuestionList.stream().filter(ccq -> new ArrayList<>(List.of(10L, 12L, 16L)).contains(ccq.getQuestion().getId())).toList();
            List<BigDecimal> priceList = calculatePrice(selectedQuestionList);
            returnMap.put("price", priceList.getFirst());
            BigDecimal income = calculateIncome(yieldList.getFirst(), priceList.getFirst());
            returnMap.put("income", income);
        }
        return returnMap;
    }

    private List<BigDecimal> calculateYield(List<CityCropQuestion> selectedQuestionList) {
        List<BigDecimal> yieldList = new ArrayList<>();
        BigDecimal answer1 = BigDecimal.ZERO;
        BigDecimal returnVal1 = BigDecimal.ZERO;
        BigDecimal returnVal2 = BigDecimal.ZERO;
        BigDecimal returnVal3 = BigDecimal.ZERO;
        for (CityCropQuestion cityCropQuestion : selectedQuestionList) {
            CityCropQuestionAnswer answer = cityCropQuestion.getCityCropQuestionAnswerList().getFirst();
            BigDecimal value = new BigDecimal(answer.getValue()).setScale(3, RoundingMode.HALF_UP);
            if (answer.getCityCropQuestion().getQuestion().getId().equals(1L)) {
                answer1 = new BigDecimal(answer.getValue());
                returnVal1 = answer1;
            } else if (answer.getCityCropQuestion().getQuestion().getId().equals(3L)) {
                BigDecimal divide = value.multiply(answer1).divide(new BigDecimal(100), 3, RoundingMode.HALF_UP);
                returnVal1 = answer1.subtract(divide);
                returnVal2 = divide;
            } else if (cityCropQuestion.getQuestion().getId().equals(4L)) {
                returnVal3 = value;
            }
        }
        yieldList.add(0, returnVal1);
        yieldList.add(1, returnVal2);
        yieldList.add(2, returnVal3);
        yieldList.add(3, BigDecimal.ZERO);
        return yieldList;
    }

    private List<BigDecimal> calculatePrice(List<CityCropQuestion> selectedQuestionList) {
        List<BigDecimal> priceList = new ArrayList<>();
        BigDecimal returnVal1 = BigDecimal.ZERO;
        BigDecimal returnVal2 = BigDecimal.ZERO;
        BigDecimal returnVal3 = BigDecimal.ZERO;
        BigDecimal returnVal4 = BigDecimal.ZERO;
        for (CityCropQuestion cityCropQuestion : selectedQuestionList) {
            CityCropQuestionAnswer answer = cityCropQuestion.getCityCropQuestionAnswerList().getFirst();
            BigDecimal value = new BigDecimal(answer.getValue()).setScale(3, RoundingMode.HALF_UP);
            if (answer.getCityCropQuestion().getQuestion().getId().equals(10L)) {
                returnVal1 = value;
                returnVal4 = value;
            } else if (answer.getCityCropQuestion().getQuestion().getId().equals(12L)) {
                returnVal2 = value;
            } else if (answer.getCityCropQuestion().getQuestion().getId().equals(16L)) {
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

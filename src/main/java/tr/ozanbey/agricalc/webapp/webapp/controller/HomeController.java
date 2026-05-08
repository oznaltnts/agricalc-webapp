package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.domain.Crop;
import tr.ozanbey.agricalc.webapp.service.service.HomeService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component("homeController")
@ViewScoped
@Getter
@Setter
public class HomeController extends BaseController {

    @Autowired
    private HomeService homeService;

    private List<City> cityList;
    private Long selectedCityId;
    private List<Crop> cropList;
    private Long selectedCropId;
    private Map<String, BigDecimal> calculateMap;

    @PostConstruct
    public void init() {
        cityList = homeService.getAllCities();
        cropList = homeService.getAllActiveCrops();
    }

    public void calculateIncome() {
        calculateMap = homeService.calculate(selectedCityId, selectedCropId);
    }
}

package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.domain.CityCrop;
import tr.ozanbey.agricalc.webapp.service.domain.Crop;
import tr.ozanbey.agricalc.webapp.service.service.HomeService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component("homeController")
@ViewScoped
@Getter
@Setter
public class HomeController extends BaseController {

    @Autowired
    private HomeService homeService;

    private List<CityCrop> cityCropList;
    private Set<City> cityList = new LinkedHashSet<>();
    private Long selectedCityId;
    private Set<Crop> cropList = new LinkedHashSet<>();
    private Long selectedCropId;
    private Map<String, BigDecimal> calculateMap;

    @PostConstruct
    public void init() {
        cityCropList = homeService.getAllActiveCityCrop();
        for (CityCrop cityCrop : cityCropList) {
            cityList.add(cityCrop.getCity());
            cropList.add(cityCrop.getCrop());
        }
    }

    public void calculateIncome() {
        if (selectedCityId == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Şehir bulunamadı", "Eşleşme başarısız"));
        }
        if (selectedCropId == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Ürün bulunamadı", "Eşleşme başarısız"));
        }
        Optional<CityCrop> selectedCityCrop = cityCropList.stream().filter(cc -> cc.getCrop().getId().equals(selectedCropId) && cc.getCity().getId().equals(selectedCityId)).findFirst();
        if (selectedCityCrop.isPresent()) {
            calculateMap = homeService.calculate(selectedCityCrop.get());
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Şehir - Ürün bulunamadı", "Eşleşme başarısız"));
        }
    }

    public void handleCityMenuOnSelect() {
        if (selectedCropId != null) {
            cityList = cityCropList.stream()
                    .filter(c -> c.getCrop().getId().equals(selectedCropId))
                    .map(CityCrop::getCity)
                    .distinct()
                    .sorted(Comparator.comparing(City::getName))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            if (selectedCityId != null && cityList.stream().noneMatch(c -> c.getId().equals(selectedCityId))) {
                selectedCityId = null;
            }
        } else {
            cityList = cityCropList.stream()
                    .map(CityCrop::getCity)
                    .distinct()
                    .sorted(Comparator.comparing(City::getName))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }

    public void handleCropMenuOnSelect() {
        if (selectedCityId != null) {
            cropList = cityCropList.stream()
                    .filter(c -> c.getCity().getId().equals(selectedCityId))
                    .map(CityCrop::getCrop)
                    .distinct()
                    .sorted(Comparator.comparing(Crop::getName))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            if (selectedCropId != null && cropList.stream().noneMatch(c -> c.getId().equals(selectedCropId))) {
                selectedCropId = null;
            }
        } else {
            cropList = cityCropList.stream()
                    .map(CityCrop::getCrop)
                    .distinct()
                    .sorted(Comparator.comparing(Crop::getName))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }

}

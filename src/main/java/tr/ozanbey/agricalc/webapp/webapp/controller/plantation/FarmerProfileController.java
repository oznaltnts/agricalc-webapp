package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumFarmerType;
import tr.ozanbey.agricalc.webapp.service.service.CityService;
import tr.ozanbey.agricalc.webapp.service.service.UserService;
import tr.ozanbey.agricalc.webapp.webapp.view.FarmerProfileView;

import java.util.List;

@Component
@ViewScoped
@Getter
@Setter
public class FarmerProfileController extends PlantationController {

    @Autowired
    private UserService userService;

    @Autowired
    private CityService cityService;

    private List<City> cityList;
    private FarmerProfileView farmerView;
    private EnumFarmerType[] farmerTypes = EnumFarmerType.values();

    @PostConstruct
    public void init() {
        cityList = cityService.getAllCities();
        fillUserInfo();
    }

    private void fillUserInfo() {
        farmerView = new FarmerProfileView();
        //TODO from db
    }

}

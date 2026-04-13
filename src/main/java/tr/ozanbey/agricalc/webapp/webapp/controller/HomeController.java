package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.service.HomeService;

import java.util.List;

@Component("homeController")
@Scope("view")
@Getter
@Setter
public class HomeController extends BaseController {

    @Autowired
    private HomeService homeService;

    private List<City> cityList;

    @PostConstruct
    public void init() {
        cityList = homeService.getAllCities();
    }
}

package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.City;

import java.util.List;

@Service
public class HomeService extends BaseService {

    private final CityService cityService;

    public HomeService(CityService cityService) {
        this.cityService = cityService;
    }

    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

}

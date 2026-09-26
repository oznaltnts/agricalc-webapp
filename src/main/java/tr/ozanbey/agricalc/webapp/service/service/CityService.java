package tr.ozanbey.agricalc.webapp.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.domain.CityDistrict;
import tr.ozanbey.agricalc.webapp.service.repository.CityDistrictRepository;
import tr.ozanbey.agricalc.webapp.service.repository.CityRepository;

import java.util.List;

@Service
@Slf4j
public class CityService {

    private final CityRepository cityRepository;
    private final CityDistrictRepository cityDistrictRepository;

    public CityService(CityRepository cityRepository,
                       CityDistrictRepository cityDistrictRepository) {
        this.cityRepository = cityRepository;
        this.cityDistrictRepository = cityDistrictRepository;
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public List<CityDistrict> getDistrictListByCityId(Long cityId) {
        return cityDistrictRepository.findByCity_Id(cityId);
    }

}

package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.dto.CityFertilizerWithFirstValue;
import tr.ozanbey.agricalc.webapp.service.repository.CityFertilizerRepository;
import tr.ozanbey.agricalc.webapp.service.repository.CityFertilizerValueRepository;

import java.util.List;

@Service
public class CityFertilizerService extends BaseService {

    @Autowired
    private CityFertilizerRepository cityFertilizerRepository;

    @Autowired
    private CityFertilizerValueRepository cityFertilizerValueRepository;

    public List<CityFertilizerWithFirstValue> getActiveDTOsByCityId(Long cityId) {
        return cityFertilizerRepository.findActiveDTOsByCityId(cityId);
    }

}

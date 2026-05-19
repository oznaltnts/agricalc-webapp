package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.dto.DieselDistanceWithFirstValue;
import tr.ozanbey.agricalc.webapp.service.repository.CityDieselDistanceRepository;
import tr.ozanbey.agricalc.webapp.service.repository.CityDieselDistanceValueRepository;

import java.util.List;

@Service
public class CityDieselDistanceService extends BaseService {

    @Autowired
    private CityDieselDistanceRepository dieselDistanceRepository;

    @Autowired
    private CityDieselDistanceValueRepository dieselDistanceValueRepository;

    public List<DieselDistanceWithFirstValue> getActiveDTOsByCityId(Long cityId) {
        return dieselDistanceRepository.findActiveDTOsByCityId(cityId);
    }
}

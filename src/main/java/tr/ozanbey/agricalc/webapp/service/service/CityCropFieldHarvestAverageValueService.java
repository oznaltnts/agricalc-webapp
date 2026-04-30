package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropFieldHarvestAverageValueRepository;

@Service
public class CityCropFieldHarvestAverageValueService extends BaseService {

    @Autowired
    private CityCropFieldHarvestAverageValueRepository valueRepository;

    public double getAverageValueByCityCropId(Long cityCropId) {
        return valueRepository.findAverageValueByCityCropId(cityCropId);
    }
}

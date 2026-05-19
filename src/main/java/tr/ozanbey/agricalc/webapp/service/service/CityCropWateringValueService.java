package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropWateringValue;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropWateringValueRepository;

import java.util.Optional;

@Service
public class CityCropWateringValueService extends BaseService {

    @Autowired
    private CityCropWateringValueRepository wateringValueRepository;

    public Optional<CityCropWateringValue> getFirstByCityCropIdOrderByInsertDateDesc(Long cityCropId) {
        return wateringValueRepository.findFirstByCityCrop_IdOrderByInsertDateDesc(cityCropId);
    }

}

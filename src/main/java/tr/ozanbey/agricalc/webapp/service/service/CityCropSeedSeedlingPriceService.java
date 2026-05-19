package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.dto.SeedSeedlingWithFirstValue;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropSeedSeedlingPriceRepository;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropSeedSeedlingPriceValueRepository;

import java.util.List;

@Service
public class CityCropSeedSeedlingPriceService extends BaseService {

    @Autowired
    private CityCropSeedSeedlingPriceRepository repository;

    @Autowired
    private CityCropSeedSeedlingPriceValueRepository valueRepository;

    public List<SeedSeedlingWithFirstValue> getActiveDTOsByCityCropId(Long cityCropId) {
        return repository.findActiveDTOsByCityCropId(cityCropId);
    }

}

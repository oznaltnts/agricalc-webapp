package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.dto.SeedSeedlingWithFirstValue;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropSeedSeedlingNumberRepository;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropSeedSeedlingNumberValueRepository;

import java.util.List;

@Service
public class CityCropSeedSeedlingNumberService extends BaseService {

    @Autowired
    private CityCropSeedSeedlingNumberRepository repository;

    @Autowired
    private CityCropSeedSeedlingNumberValueRepository valueRepository;

    public List<SeedSeedlingWithFirstValue> getActiveDTOsByCityCropId(Long cityCropId) {
        return repository.findActiveDTOsByCityCropId(cityCropId);
    }

}

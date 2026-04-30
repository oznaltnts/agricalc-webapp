package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.dto.SeedSeedlingNumberWithFirstValue;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropSeedAndSeedlingNumberRepository;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropSeedAndSeedlingNumberValueRepository;

import java.util.List;

@Service
public class CityCropSeedAndSeedlingNumberService extends BaseService {

    @Autowired
    private CityCropSeedAndSeedlingNumberRepository repository;

    @Autowired
    private CityCropSeedAndSeedlingNumberValueRepository valueRepository;

    public List<SeedSeedlingNumberWithFirstValue> getDTOsByStatusAndCityCropId(EnumStatus status, Long cityCropId) {
        return repository.findDTOsByStatusAndCityCropId(status.getValue(), cityCropId);
    }

}

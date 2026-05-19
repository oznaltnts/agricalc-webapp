package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.CityCrop;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropRepository;

import java.util.List;

@Service
public class CityCropService extends BaseService {

    @Autowired
    private CityCropRepository cityCropRepository;

    public List<CityCrop> getAllActiveCityCrop() {
        return cityCropRepository.getByStatusAndCrop_StatusOrderByCity_CodeAscCrop_NameAsc(EnumStatus.ACTIVE, EnumStatus.ACTIVE);
    }

}

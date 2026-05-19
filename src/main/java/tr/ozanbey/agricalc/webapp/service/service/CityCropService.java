package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.CityCrop;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.CityCropRepository;

@Service
public class CityCropService extends BaseService {

    @Autowired
    private CityCropRepository cityCropRepository;

    public CityCrop getActiveByCityIdAndCropId(Long cityId, Long cropId) {
        return cityCropRepository.getByStatusAndCity_IdAndCrop_StatusAndCrop_Id(EnumStatus.ACTIVE, cityId, EnumStatus.ACTIVE, cropId);
    }
}

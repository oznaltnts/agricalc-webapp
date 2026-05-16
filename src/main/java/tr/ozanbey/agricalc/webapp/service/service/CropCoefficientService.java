package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.dto.CropCoefficientWithFirstValue;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.CropCoefficientRepository;
import tr.ozanbey.agricalc.webapp.service.repository.CropCoefficientValueRepository;

import java.util.List;

@Service
public class CropCoefficientService extends BaseService {

    @Autowired
    private CropCoefficientRepository cropCoefficientRepository;

    @Autowired
    private CropCoefficientValueRepository cropCoefficientValueRepository;

    public List<CropCoefficientWithFirstValue> getDTOsByStatusAndCropId(EnumStatus status, Long cropId) {
        return cropCoefficientRepository.findDTOsByStatusAndCropId(status.getValue(), cropId);
    }

}

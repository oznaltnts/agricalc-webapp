package tr.ozanbey.agricalc.webapp.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.CropCoefficient;
import tr.ozanbey.agricalc.webapp.service.domain.GeneralCoefficientValue;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumCropCoefficientType;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumCropCoefficientValue;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.CropCoefficientRepository;
import tr.ozanbey.agricalc.webapp.service.repository.GeneralCoefficientRepository;
import tr.ozanbey.agricalc.webapp.service.repository.GeneralCoefficientValueRepository;

import java.util.List;

@Service
public class GeneralCoefficientService extends BaseService {

    @Autowired
    private GeneralCoefficientRepository generalCoefficientRepository;

    @Autowired
    private GeneralCoefficientValueRepository generalCoefficientValueRepository;

    public GeneralCoefficientValue getFirstByGeneralCoefficientStatusAndGeneralCoefficientName(EnumStatus status, String name) {
        return generalCoefficientValueRepository.findFirstByGeneralCoefficientStatusAndGeneralCoefficientNameOrderByInsertDateDesc(status, name);
    }

}

package tr.ozanbey.agricalc.webapp.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.Crop;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.CropRepository;

import java.util.List;

@Service
@Slf4j
public class CropService extends BaseService {

    @Autowired
    private CropRepository cropRepository;

    public List<Crop> getByStatus(EnumStatus status) {
        return cropRepository.findByStatus(status);
    }
}

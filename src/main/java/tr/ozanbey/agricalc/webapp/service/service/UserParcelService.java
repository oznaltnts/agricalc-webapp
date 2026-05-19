package tr.ozanbey.agricalc.webapp.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.repository.UserPlantParcelRepository;

@Service
@Slf4j
public class UserParcelService extends BaseService {

    @Autowired
    private UserPlantParcelRepository plantParcelRepository;


}

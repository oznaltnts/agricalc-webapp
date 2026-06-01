package tr.ozanbey.agricalc.webapp.service.service.animal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCow;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowRepository;
import tr.ozanbey.agricalc.webapp.service.service.BaseService;

import java.util.List;

@Service
@Slf4j
public class AnimalDashboardService extends BaseService {

    @Autowired
    private UserDairyCowRepository dairyCowRepository;

    public List<UserDairyCow> getActiveFeedAsViewList(Long userId) {
        return dairyCowRepository.findByStatusInAndUser_Id(new EnumStatus[]{EnumStatus.ACTIVE, EnumStatus.PASSIVE}, userId);
    }

}

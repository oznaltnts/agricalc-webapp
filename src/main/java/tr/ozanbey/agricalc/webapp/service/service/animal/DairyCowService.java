package tr.ozanbey.agricalc.webapp.service.service.animal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.FeedRepository;
import tr.ozanbey.agricalc.webapp.service.service.BaseService;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowView;

import java.util.List;

@Service
@Slf4j
public class DairyCowService extends BaseService {

    @Autowired
    private FeedRepository feedRepository;

    public List<DairyCowView> getActiveFeedAsViewList() {
        return feedRepository.findAllAsCowView(EnumStatus.ACTIVE);
    }

}

package tr.ozanbey.agricalc.webapp.service.service.animal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowFeed;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.FeedRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowFeedRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowRepository;
import tr.ozanbey.agricalc.webapp.service.service.BaseService;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowFeedView;

import java.util.List;

@Service
@Slf4j
public class DairyCowService extends BaseService {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserDairyCowRepository dairyCowRepository;

    @Autowired
    private UserDairyCowFeedRepository userFeedRepository;

    public List<DairyCowFeedView> getActiveFeedAsViewList(Long dairyCowId, Long userId) {
        return userFeedRepository.findAllAsCowView(EnumStatus.ACTIVE, dairyCowId, userId);
    }

    @Transactional
    public void saveUserFeed(DairyCowFeedView selectedFeedView, Long dairyCowId, Long selectedFeedId) {
        UserDairyCowFeed userDairyCowFeed;
        if (selectedFeedView.getUserFeedId() == null) {
            userDairyCowFeed = new UserDairyCowFeed();
        } else {
            userDairyCowFeed = userFeedRepository.getReferenceById(selectedFeedView.getUserFeedId());
        }
        userDairyCowFeed.setUserDairyCow(dairyCowRepository.getReferenceById(dairyCowId));
        userDairyCowFeed.setFeed(feedRepository.getReferenceById(selectedFeedId));
        userDairyCowFeed.setAmountKg(selectedFeedView.getAmountKg());
        userDairyCowFeed.setBuyingDate(selectedFeedView.getBuyingDate());
        userDairyCowFeed.setBuyingPrice(selectedFeedView.getBuyingPriceKg());
        userFeedRepository.save(userDairyCowFeed);
    }

}

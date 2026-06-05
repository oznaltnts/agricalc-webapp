package tr.ozanbey.agricalc.webapp.service.service.animal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowFeed;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.FeedRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowBarnRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowFeedRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowFeedView;

import java.util.List;

@Service
@Slf4j
public class DairyCowFeedService {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserDairyCowBarnRepository barnRepository;

    @Autowired
    private UserDairyCowFeedRepository dairyCowFeedRepository;

    public List<DairyCowFeedView> getActiveFeedAsViewList(Long barnId) {
        return dairyCowFeedRepository.findAllAsCowView(EnumStatus.ACTIVE, barnId);
    }

    @Transactional
    public void saveUserFeed(DairyCowFeedView selectedFeedView, Long barnId, Long selectedFeedId) {
        UserDairyCowFeed userDairyCowFeed;
        if (selectedFeedView.getUserFeedId() == null) {
            userDairyCowFeed = new UserDairyCowFeed();
        } else {
            userDairyCowFeed = dairyCowFeedRepository.getReferenceById(selectedFeedView.getUserFeedId());
        }
        userDairyCowFeed.setUserDairyCowBarn(barnRepository.getReferenceById(barnId));
        userDairyCowFeed.setFeed(feedRepository.getReferenceById(selectedFeedId));
        userDairyCowFeed.setAmountKg(selectedFeedView.getAmountKg());
        userDairyCowFeed.setBuyingDate(selectedFeedView.getBuyingDate());
        userDairyCowFeed.setBuyingPrice(selectedFeedView.getBuyingPriceKg());
        dairyCowFeedRepository.save(userDairyCowFeed);
    }
}

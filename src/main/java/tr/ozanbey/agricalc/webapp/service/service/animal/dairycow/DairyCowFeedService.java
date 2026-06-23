package tr.ozanbey.agricalc.webapp.service.service.animal.dairycow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.Feed;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.UserDairyCowFeed;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.FeedRepository;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.UserDairyCowBarnRepository;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.UserDairyCowFeedRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowFeedView;

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

    public List<Feed> getFeedsByStatuses(EnumStatus[] statuses) {
        return feedRepository.findByStatusInOrderByFeedCategoryAscFeedTypeAsc(statuses);
    }

    @Transactional
    public void saveFeed(Feed selectedFeed) {
        feedRepository.save(selectedFeed);
    }

    public List<DairyCowFeedView> getActiveFeedAsViewList(Long barnId) {
        return dairyCowFeedRepository.findAllAsCowFeedView(EnumStatus.ACTIVE, barnId);
    }

    public List<DairyCowFeedView> getFeedRasyonAsViewList(Long barnId) {
        return dairyCowFeedRepository.findAllAsCowRasyonView(EnumStatus.ACTIVE, barnId);
    }

    @Transactional
    public void saveUserFeed(DairyCowFeedView selectedFeedView, Long barnId) {
        UserDairyCowFeed userDairyCowFeed;
        if (selectedFeedView.getUserFeedId() == null) {
            userDairyCowFeed = new UserDairyCowFeed();
        } else {
            userDairyCowFeed = dairyCowFeedRepository.getReferenceById(selectedFeedView.getUserFeedId());
        }
        userDairyCowFeed.setUserDairyCowBarn(barnRepository.getReferenceById(barnId));
        userDairyCowFeed.setFeed(feedRepository.getReferenceById(selectedFeedView.getSelectedFeedId()));
        userDairyCowFeed.setAmountKg(selectedFeedView.getAmountKg());
        userDairyCowFeed.setBuyingDate(selectedFeedView.getBuyingDate());
        userDairyCowFeed.setBuyingPrice(selectedFeedView.getBuyingPriceKg());
        dairyCowFeedRepository.save(userDairyCowFeed);
    }

    @Transactional
    public void removeUserFeed(Long selectedUserFeedId) {
        dairyCowFeedRepository.delete(dairyCowFeedRepository.getReferenceById(selectedUserFeedId));
    }

    @Transactional
    public void saveFeedRasyonFromViewList(List<DairyCowFeedView> dairyCowFeedViewList) {
        for (DairyCowFeedView selectedFeedView : dairyCowFeedViewList) {
            dairyCowFeedRepository.saveRasyonValuesFromViewList(selectedFeedView.getUserFeedId(), selectedFeedView.getLactationRasyon(), selectedFeedView.getRoughageRasyon());
        }
    }

    @Transactional
    public void updateLactationValues(DairyCowFeedView view) {
        dairyCowFeedRepository.saveRasyonValuesFromViewList(view.getUserFeedId(), view.getLactationRasyon(), view.getRoughageRasyon());

    }
}

package tr.ozanbey.agricalc.webapp.service.service.animal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCow;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowFeed;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumFeedCategory;
import tr.ozanbey.agricalc.webapp.service.repository.FeedRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowFeedRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowRepository;
import tr.ozanbey.agricalc.webapp.service.service.BaseService;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowFeedView;

import java.util.ArrayList;
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

    public void validateBeforeSave(List<DairyCowFeedView> selectedFeedList) {//TODO
    }

    @Transactional
    public void saveNewFeedList(List<DairyCowFeedView> selectedFeedList, Long dairyCowId, EnumFeedCategory category) {
        if (selectedFeedList.isEmpty()) {
            userFeedRepository.deleteByUserDairyCow_IdAndFeed_FeedCategory(
                    dairyCowId,
                    category);
        } else {
            userFeedRepository.deleteByIdNotInAndUserDairyCow_IdAndFeed_FeedCategory(
                    selectedFeedList.stream().map(f -> f.getUserFeed().getId() != null ? f.getUserFeed().getId() : 0L).toList(),
                    dairyCowId,
                    category);
            saveSelectedFeedList(selectedFeedList, dairyCowId);
        }

    }

    private void saveSelectedFeedList(List<DairyCowFeedView> selectedFeedList, Long dairyCowId) {
        List<UserDairyCowFeed> savelist = new ArrayList<>();
        UserDairyCow dairyCow = dairyCowRepository.getReferenceById(dairyCowId);
        for (DairyCowFeedView dairyCowFeedView : selectedFeedList) {
            UserDairyCowFeed dairyCowFeed = dairyCowFeedView.getUserFeed();
            dairyCowFeed.setUserDairyCow(dairyCow);
            dairyCowFeed.setFeed(dairyCowFeedView.getUserFeed().getFeed());
            dairyCowFeed.setAmountKg(dairyCowFeedView.getAmountKg());
            dairyCowFeed.setBuyingDate(dairyCowFeedView.getBuyingDate());
            dairyCowFeed.setBuyingPrice(dairyCowFeedView.getBuyingPriceKg());
            savelist.add(dairyCowFeed);
        }
        userFeedRepository.saveAll(savelist);
    }
}

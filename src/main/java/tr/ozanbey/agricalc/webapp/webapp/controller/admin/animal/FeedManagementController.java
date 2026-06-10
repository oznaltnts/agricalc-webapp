package tr.ozanbey.agricalc.webapp.webapp.controller.admin.animal;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.Feed;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumFeedCategory;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumFeedType;
import tr.ozanbey.agricalc.webapp.service.service.animal.DairyCowFeedService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.util.List;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class FeedManagementController extends BaseController {

    @Autowired
    private DairyCowFeedService dairyCowFeedService;

    private EnumStatus[] statuses = EnumStatus.values();
    private EnumFeedCategory[] feedCategories = EnumFeedCategory.values();
    private EnumFeedType[] feedTypes = EnumFeedType.values();
    private List<Feed> feedList;
    private Feed selectedFeed;

    @PostConstruct
    public void init() {
        fillDataTableValues();
    }

    private void fillDataTableValues() {
        feedList = dairyCowFeedService.getFeedsByStatuses(statuses);
    }

    public void addNewFeedToCategory() {
        selectedFeed = new Feed();
        selectedFeed.setStatus(EnumStatus.ACTIVE);
    }

    public void editFeed(Feed feed) {
        selectedFeed = new Feed();
        selectedFeed.setId(feed.getId());
        selectedFeed.setStatus(feed.getStatus());
        selectedFeed.setFeedCategory(feed.getFeedCategory());
        selectedFeed.setFeedType(feed.getFeedType());
        selectedFeed.setName(feed.getName());
    }

    public void saveSelectedFeed() {
        if (checkIsThereChange()) {
            dairyCowFeedService.saveFeed(selectedFeed);
            fillDataTableValues();
        }
        selectedFeed = null;
    }

    public boolean checkIsThereChange() {
        if (selectedFeed.getId() != null) {
            Optional<Feed> optionalFeed = feedList.stream().filter(v -> v.getId().equals(selectedFeed.getId())).findFirst();
            if (optionalFeed.isPresent()) {
                Feed feed = optionalFeed.get();
                return !selectedFeed.getFeedCategory().equals(feed.getFeedCategory())
                        || !selectedFeed.getFeedType().equals(feed.getFeedType())
                        || !selectedFeed.getName().equals(feed.getName())
                        || !selectedFeed.getStatus().equals(feed.getStatus());
            }
        }
        return true;
    }

}

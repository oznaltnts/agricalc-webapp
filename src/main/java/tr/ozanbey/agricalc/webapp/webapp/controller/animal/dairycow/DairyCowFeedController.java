package tr.ozanbey.agricalc.webapp.webapp.controller.animal.dairycow;


import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;
import jakarta.faces.model.SelectItemGroup;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.Feed;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.dairycow.EnumFeedCategory;
import tr.ozanbey.agricalc.webapp.service.service.animal.dairycow.DairyCowFeedService;
import tr.ozanbey.agricalc.webapp.webapp.util.JSFUtils;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowFeedView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class DairyCowFeedController extends DairyCowController {

    @Autowired
    private DairyCowFeedService dairyCowFeedService;

    private List<DairyCowFeedView> dairyCowFeedViewList;
    private DairyCowFeedView selectedFeedView;

    private List<SelectItem> feedList;
    private LocalDateTime today = LocalDateTime.now();

    @PostConstruct
    public void init() {
    }

    public void fillDataTableValues() {
        dairyCowFeedViewList = dairyCowFeedService.getActiveFeedAsViewList(super.getBarnId());
    }

    public void addNewFeedView() {
        selectedFeedView = new DairyCowFeedView();

        feedList = new ArrayList<>();
        List<Feed> activeFeedList = dairyCowFeedService.getFeedsByStatuses(new EnumStatus[]{EnumStatus.ACTIVE});
        for (EnumFeedCategory category : EnumFeedCategory.values()) {
            SelectItemGroup subFeeds = new SelectItemGroup(JSFUtils.getLocaleMessage(category.name()));
            SelectItem[] selectItems = activeFeedList.stream()
                    .filter(f -> f.getFeedCategory().equals(category))
                    .map(f -> new SelectItem(
                            f.getId(),
                            JSFUtils.getLocaleMessage(f.getFeedType().name()) + " - " + f.getName(),
                            "",
                            checkFeedAdded(f))
                    ).toArray(SelectItem[]::new);
            subFeeds.setSelectItems(selectItems);
            feedList.add(subFeeds);
        }
    }

    public void editUserFeed(DairyCowFeedView feedView) {
        selectedFeedView = new DairyCowFeedView();
        selectedFeedView.setUserFeedId(feedView.getUserFeedId());
        selectedFeedView.setFeed(feedView.getFeed());
        selectedFeedView.setSelectedFeedId(feedView.getFeed().getId());
        selectedFeedView.setAmountKg(feedView.getAmountKg());
        selectedFeedView.setBuyingDate(feedView.getBuyingDate());
        selectedFeedView.setBuyingPriceKg(feedView.getBuyingPriceKg());

        feedList = new ArrayList<>();
        SelectItemGroup subFeeds = new SelectItemGroup(JSFUtils.getLocaleMessage(feedView.getFeed().getFeedCategory().name()));
        subFeeds.setSelectItems(new SelectItem(
                feedView.getFeed().getId(),
                JSFUtils.getLocaleMessage(feedView.getFeed().getFeedType().name()) + " - " + feedView.getFeed().getName(),
                "",
                checkFeedAdded(feedView.getFeed())));
        feedList.add(subFeeds);
    }

    public void saveSelectedFeed() {
        if (checkIsThereChange()) {
            dairyCowFeedService.saveUserFeed(selectedFeedView, getBarnId());
            fillDataTableValues();
        }
        selectedFeedView = null;
    }

    public boolean checkIsThereChange() {
        if (selectedFeedView.getUserFeedId() != null) {
            Optional<DairyCowFeedView> optionalFeed = dairyCowFeedViewList.stream()
                    .filter(v -> v.getUserFeedId().equals(selectedFeedView.getUserFeedId()))
                    .findFirst();
            if (optionalFeed.isPresent()) {
                DairyCowFeedView feed = optionalFeed.get();
                return selectedFeedView.getAmountKg() != feed.getAmountKg()
                        || !selectedFeedView.getBuyingDate().equals(feed.getBuyingDate())
                        || !selectedFeedView.getBuyingPriceKg().equals(feed.getBuyingPriceKg());
            }
        }
        return true;
    }

    private boolean checkFeedAdded(Feed feed) {
        return dairyCowFeedViewList.stream().anyMatch(v -> v.getFeed().getId().equals(feed.getId()));
    }

    public void deleteUserFeed(DairyCowFeedView feedView) {
        dairyCowFeedService.removeUserFeed(feedView.getUserFeedId());
        fillDataTableValues();
        selectedFeedView = null;
    }

}

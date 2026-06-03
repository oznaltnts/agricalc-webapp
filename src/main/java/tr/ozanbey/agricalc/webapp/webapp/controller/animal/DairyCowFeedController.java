package tr.ozanbey.agricalc.webapp.webapp.controller.animal;


import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;
import jakarta.faces.model.SelectItemGroup;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.Feed;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumFeedCategory;
import tr.ozanbey.agricalc.webapp.service.service.FeedService;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowFeedView;

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
    private FeedService feedService;

    private List<DairyCowFeedView> dairyCowFeedViewList = new ArrayList<>();
    private DairyCowFeedView selectedFeedView;
    private EnumFeedCategory[] feedCategories = EnumFeedCategory.values();

    private List<SelectItem> feedList;
    private Long selectedFeedId;
    private LocalDateTime today = LocalDateTime.now();

    @PostConstruct
    public void init() {
    }

    public void fillDataTableValues() {
        dairyCowFeedViewList = super.getDairyCowService().getActiveFeedAsViewList(super.getDairyCowId(), getCurrentUser().getUser().getId());
    }

    public List<DairyCowFeedView> getDataTableList(EnumFeedCategory category) {
        return dairyCowFeedViewList.stream().filter(v -> v.getFeed().getFeedCategory().equals(category)).toList();
    }

    public void addNewFeedView() {
        selectedFeedId = null;
        selectedFeedView = new DairyCowFeedView();

        feedList = new ArrayList<>();
        List<Feed> activeFeedList = feedService.getActiveFeedListByOrderByCategoryAndType();
        for (EnumFeedCategory category : feedCategories) {
            SelectItemGroup subFeeds = new SelectItemGroup(getLocaleMessage(category.name()));
            SelectItem[] selectItems = activeFeedList.stream()
                    .filter(f -> f.getFeedCategory().equals(category))
                    .map(f -> new SelectItem(
                            f.getId(),
                            getLocaleMessage(f.getFeedType().name()) + " - " + f.getName(),
                            "",
                            checkFeedAdded(f))
                    ).toArray(SelectItem[]::new);
            subFeeds.setSelectItems(selectItems);
            feedList.add(subFeeds);
        }
    }

    public void editUserFeed(DairyCowFeedView feedView) {
        selectedFeedId = feedView.getFeed().getId();
        selectedFeedView = new DairyCowFeedView();
        selectedFeedView.setUserFeedId(feedView.getUserFeedId());
        selectedFeedView.setFeed(feedView.getFeed());
        selectedFeedView.setAmountKg(feedView.getAmountKg());
        selectedFeedView.setBuyingDate(feedView.getBuyingDate());
        selectedFeedView.setBuyingPriceKg(feedView.getBuyingPriceKg());

        feedList = new ArrayList<>();
        SelectItemGroup subFeeds = new SelectItemGroup(getLocaleMessage(feedView.getFeed().getFeedCategory().name()));
        subFeeds.setSelectItems(new SelectItem(
                feedView.getFeed().getId(),
                getLocaleMessage(feedView.getFeed().getFeedType().name()) + " - " + feedView.getFeed().getName(),
                "",
                checkFeedAdded(feedView.getFeed())));
        feedList.add(subFeeds);
    }

    public void saveSelectedFeed() {
        if (checkIsThereChange()) {
            super.getDairyCowService().saveUserFeed(selectedFeedView, getDairyCowId(), selectedFeedId);
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
                return !selectedFeedView.getAmountKg().equals(feed.getAmountKg())
                        || !selectedFeedView.getBuyingDate().equals(feed.getBuyingDate())
                        || !selectedFeedView.getBuyingPriceKg().equals(feed.getBuyingPriceKg());
            }
        }
        return true;
    }

    private boolean checkFeedAdded(Feed feed) {
        return dairyCowFeedViewList.stream().anyMatch(v -> v.getFeed().getId().equals(feed.getId()));
    }

}

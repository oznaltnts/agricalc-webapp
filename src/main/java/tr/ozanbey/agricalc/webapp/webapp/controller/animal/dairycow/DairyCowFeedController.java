package tr.ozanbey.agricalc.webapp.webapp.controller.animal.dairycow;


import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;
import jakarta.faces.model.SelectItemGroup;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.RowEditEvent;
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
import java.util.Objects;

@Component
@ViewScoped
@Getter
@Setter
public class DairyCowFeedController extends DairyCowController {

    @Autowired
    private DairyCowFeedService dairyCowFeedService;

    private List<DairyCowFeedView> dairyCowFeedViewList;
    private DairyCowFeedView referenceFeedView;

    private List<SelectItem> feedList = new ArrayList<>();
    private LocalDateTime today = LocalDateTime.now();

    @PostConstruct
    public void init() {
        List<Feed> activeFeedList = dairyCowFeedService.getFeedsByStatuses(new EnumStatus[]{EnumStatus.ACTIVE});
        for (EnumFeedCategory category : EnumFeedCategory.values()) {
            SelectItemGroup subFeeds = new SelectItemGroup(JSFUtils.getLocaleMessage(category.name()));
            SelectItem[] selectItems = activeFeedList.stream()
                    .filter(f -> f.getFeedCategory().equals(category))
                    .map(f -> new SelectItem(
                            f.getId(),
                            f.getName())
                    ).toArray(SelectItem[]::new);
            subFeeds.setSelectItems(selectItems);
            feedList.add(subFeeds);
        }
    }

    public void fillDataTableValues() {
        dairyCowFeedViewList = dairyCowFeedService.getActiveFeedAsViewList(super.getBarnId());
    }

    public void onRowEditInit(RowEditEvent<DairyCowFeedView> event) {
        DairyCowFeedView view = event.getObject();

        referenceFeedView = new DairyCowFeedView();
        referenceFeedView.setUserFeedId(view.getUserFeedId());
        referenceFeedView.setFeed(view.getFeed());
        referenceFeedView.setSelectedFeedId(view.getFeed().getId());
        referenceFeedView.setAmountKg(view.getAmountKg());
        referenceFeedView.setBuyingDate(view.getBuyingDate());
        referenceFeedView.setBuyingPriceKg(view.getBuyingPriceKg());
    }

    public void onRowEdit(RowEditEvent<DairyCowFeedView> event) {
        DairyCowFeedView view = event.getObject();
        if (checkIsThereChange(view)) {
            dairyCowFeedService.saveUserFeed(view, getBarnId());
            fillDataTableValues();
        }
        referenceFeedView = null;
    }

    private boolean checkIsThereChange(DairyCowFeedView view) {
        if (referenceFeedView != null) {
            return !Objects.equals(referenceFeedView.getAmountKg(), view.getAmountKg())
                    || !referenceFeedView.getBuyingDate().equals(view.getBuyingDate())
                    || !referenceFeedView.getBuyingPriceKg().equals(view.getBuyingPriceKg());
        }
        return true;
    }

    public void onRowCancel(RowEditEvent<DairyCowFeedView> event) {
        DairyCowFeedView view = event.getObject();
        if (view.getSelectedFeedId() == null
                || view.getAmountKg() == null
                || view.getBuyingDate() == null
                || view.getBuyingPriceKg() == null) {
            dairyCowFeedViewList.removeLast();
        }
        referenceFeedView = null;
    }

    public void onAddNew() {
        dairyCowFeedViewList.add(new DairyCowFeedView());
        referenceFeedView = null;
    }

    public void deleteUserFeed(DairyCowFeedView feedView) {
        dairyCowFeedService.removeUserFeed(feedView.getUserFeedId());
        fillDataTableValues();
        referenceFeedView = null;
    }

}

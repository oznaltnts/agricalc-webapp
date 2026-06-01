package tr.ozanbey.agricalc.webapp.webapp.controller.animal;


import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.TabChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumFeedCategory;
import tr.ozanbey.agricalc.webapp.service.service.animal.DairyCowService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;
import tr.ozanbey.agricalc.webapp.webapp.controller.NavigationController;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowFeedView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component("dairyCowController")
@ViewScoped
@Getter
@Setter
public class DairyCowController extends BaseController {

    @Autowired
    private DairyCowService dairyCowService;

    private Long dairyCowId;

    private EnumFeedCategory[] feedCategories = EnumFeedCategory.values();
    private EnumFeedCategory selectedCategory = EnumFeedCategory.ROUGHAGE;

    private LocalDateTime today = LocalDateTime.now();
    private List<DairyCowFeedView> selectedFeedList = new ArrayList<>();
    private List<DairyCowFeedView> dairyCowFeedViewList = new ArrayList<>();

    @PostConstruct
    public void init() {
    }

    public void loadDairyCowId(EnumFeedCategory category) {
        dairyCowFeedViewList = dairyCowService.getActiveFeedAsViewList(dairyCowId, getCurrentUser().getUser().getId());
        selectedFeedList = dairyCowFeedViewList.stream().filter(v -> v.getUserFeed().getId() != null
                && v.getUserFeed().getFeed().getFeedCategory().equals(category)
        ).toList();
    }

    public void handleTabChange(TabChangeEvent event) {
        if (checkValidationOnChange()) {
            // PrimeFaces.current().executeScript("PF('feedWidgetVar').select(" + previousTabIndex + ");");
            return;
        }
        saveSelected(selectedCategory);
        selectedCategory = (EnumFeedCategory) event.getData();
        loadDairyCowId(selectedCategory);
    }

    private boolean checkValidationOnChange() {
        if (!selectedFeedList.isEmpty()) {
            for (DairyCowFeedView v : selectedFeedList) {
                if (v.getAmountKg() == null || v.getBuyingDate() == null || v.getBuyingPriceKg() == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<DairyCowFeedView> getDataTableList(EnumFeedCategory category) {
        return dairyCowFeedViewList.stream().filter(v -> v.getUserFeed().getFeed().getFeedCategory().equals(category)).toList();
    }

    private void saveSelected(EnumFeedCategory category) {
        dairyCowService.saveNewFeedList(selectedFeedList, dairyCowId, category);
    }

    public void saveButton(EnumFeedCategory category) {
        saveSelected(category);
        loadDairyCowId(category);
    }

    private boolean validateSave() {
        dairyCowService.validateBeforeSave(selectedFeedList);
        return true;
    }

    public void redirectIfValidationFailed() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!context.isPostback() && context.isValidationFailed()) {
            NavigationController.redirectToUrl("/secured/animal/dashboard");
        }
    }
}

package tr.ozanbey.agricalc.webapp.webapp.controller.animal;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.service.animal.DairyCowFeedService;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowFeedView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@ViewScoped
@Getter
@Setter
public class DairyCowFeedRasyonController extends DairyCowController {

    @Autowired
    private DairyCowFeedService dairyCowFeedService;

    private List<DairyCowFeedView> referenceViewList;
    private List<DairyCowFeedView> dairyCowFeedViewList;

    @PostConstruct
    public void init() {
    }

    public void fillDataTableValues() {
        dairyCowFeedViewList = dairyCowFeedService.getFeedRasyonAsViewList(super.getBarnId());
        dairyCowFeedViewList.forEach(v -> {
            DairyCowFeedView dv = new DairyCowFeedView();
            dv.setUserFeedId(v.getUserFeedId());
            dv.setLactationRasyon(v.getLactationRasyon());
            dv.setRoughageRasyon(v.getRoughageRasyon());
            referenceViewList.add(dv);
        });
    }

    public void nextSaveFeedRasyon() throws IOException {
        List<DairyCowFeedView> saveList = generateSaveList();
        if (!saveList.isEmpty()) {
            dairyCowFeedService.saveFeedRasyonFromViewList(dairyCowFeedViewList);
            super.getNavigationController().redirectToUrl("/secured/animal/dairy-cow-expense?barnId=" + getBarnId());
        }
    }

    public void backSaveFeedRasyon() throws IOException {
        List<DairyCowFeedView> saveList = generateSaveList();
        if (!saveList.isEmpty()) {
            dairyCowFeedService.saveFeedRasyonFromViewList(dairyCowFeedViewList);
            super.getNavigationController().redirectToUrl("/secured/animal/dairy-cow-feed?barnId=" + getBarnId());
        }
    }

    private List<DairyCowFeedView> generateSaveList() {
        List<DairyCowFeedView> returnSaveList = new ArrayList<>();
        for (DairyCowFeedView dv : dairyCowFeedViewList) {
            DairyCowFeedView refView = referenceViewList.stream().filter(r -> r.getUserFeedId().equals(dv.getUserFeedId())).findFirst().get();
            if (!refView.getLactationRasyon().equals(dv.getLactationRasyon()) || !refView.getRoughageRasyon().equals(dv.getRoughageRasyon())) {
                returnSaveList.add(dv);
            }
        }
        return returnSaveList;
    }


}

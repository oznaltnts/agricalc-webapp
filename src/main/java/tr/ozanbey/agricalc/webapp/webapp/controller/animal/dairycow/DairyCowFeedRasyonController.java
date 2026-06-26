package tr.ozanbey.agricalc.webapp.webapp.controller.animal.dairycow;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.service.animal.dairycow.DairyCowFeedService;
import tr.ozanbey.agricalc.webapp.webapp.util.JSFUtils;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowFeedView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@ViewScoped
@Getter
@Setter
public class DairyCowFeedRasyonController extends DairyCowController {

    @Autowired
    private DairyCowFeedService dairyCowFeedService;

    private List<DairyCowFeedView> dairyCowFeedViewList;
    private DairyCowFeedView referenceFeedView;

    @PostConstruct
    public void init() {
    }

    public void fillDataTableValues() {
        dairyCowFeedViewList = dairyCowFeedService.getFeedRasyonAsViewList(super.getBarnId());
    }

    public void onRowEditInit(RowEditEvent<DairyCowFeedView> event) {
        DairyCowFeedView view = event.getObject();
        referenceFeedView = new DairyCowFeedView();
        referenceFeedView.setLactationRasyon(view.getLactationRasyon());
        referenceFeedView.setRoughageRasyon(view.getRoughageRasyon());
    }

    public void onRowEdit(RowEditEvent<DairyCowFeedView> event) {
        DairyCowFeedView view = event.getObject();
        int rowIndex = ((DataTable) event.getComponent()).getRowIndex();
        if (checkRowValidation(view, rowIndex)) {
            if (checkIsThereChange(view)) {
                dairyCowFeedService.updateLactationValues(view);
                fillDataTableValues();
            }
            referenceFeedView = null;
        }
    }

    private boolean checkIsThereChange(DairyCowFeedView view) {
        if (referenceFeedView != null) {
            return !Objects.equals(referenceFeedView.getLactationRasyon(), view.getLactationRasyon())
                    || !Objects.equals(referenceFeedView.getRoughageRasyon(), view.getRoughageRasyon());
        }
        return true;
    }

    public void onRowCancel(RowEditEvent<DairyCowFeedView> event) {
        referenceFeedView = null;
    }

    public void nextSaveFeedRasyon() throws IOException {
        if (checkPageValidation()) {
            super.getNavigationController().redirectToUrl("/secured/animal/dairy-cow/cost-new?barnId=" + getBarnId());
        }
    }

    public void backSaveFeedRasyon() throws IOException {
        if (checkPageValidation()) {
            super.getNavigationController().redirectToUrl("/secured/animal/dairy-cow/feed-new?barnId=" + getBarnId());
        }
    }

    private boolean checkPageValidation() {
        int rowIndex = 0;
        for (DairyCowFeedView view : dairyCowFeedViewList) {
            if (!checkRowValidation(view, rowIndex)) {
                return false;
            }
            rowIndex++;
        }
        return true;
    }

    private boolean checkRowValidation(DairyCowFeedView view, int rowIndex) {
        if (view.getLactationRasyon() != null || view.getRoughageRasyon() != null) {
            return true;
        }
        PrimeFaces.current().executeScript("PF('feedTableWidget').showRowEditors(PF('feedTableWidget').tbody.children().eq(" + rowIndex + "));");
        JSFUtils.addErrorMessage("growl", "Laktasyon dönemi ve/veya kuru dönem kullanım miktarı giriniz.", view.getFeed().getName());
        return false;
    }

}

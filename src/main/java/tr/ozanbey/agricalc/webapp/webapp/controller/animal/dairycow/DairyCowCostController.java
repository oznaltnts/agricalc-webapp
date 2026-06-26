package tr.ozanbey.agricalc.webapp.webapp.controller.animal.dairycow;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.DairyCowCost;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.service.animal.dairycow.DairyCowCostService;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowCostView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@ViewScoped
@Getter
@Setter
public class DairyCowCostController extends DairyCowController {

    @Autowired
    private DairyCowCostService dairyCowCostService;

    private List<DairyCowCostView> dairyCowCostViewList;
    private DairyCowCostView referenceCostView;

    private List<DairyCowCost> costList = new ArrayList<>();

    @PostConstruct
    public void init() {
        costList = dairyCowCostService.getCostsByStatuses(new EnumStatus[]{EnumStatus.ACTIVE});
    }

    public void fillDataTableValues() {
        dairyCowCostViewList = dairyCowCostService.getActiveCostAsViewList(super.getBarnId());
    }

    public void onRowEditInit(RowEditEvent<DairyCowCostView> event) {
        DairyCowCostView view = event.getObject();
        referenceCostView = new DairyCowCostView();
        referenceCostView.setUserCostId(view.getUserCostId());
        referenceCostView.setSelectedCostId(view.getSelectedCostId());
        referenceCostView.setSelectedDairyCowCost(view.getSelectedDairyCowCost());
        referenceCostView.setSelectedCostName(view.getSelectedCostName());
        referenceCostView.setCount(view.getCount());
        referenceCostView.setTotalCost(view.getTotalCost());
        referenceCostView.setHourlyOrInterest(view.getHourlyOrInterest());
    }

    public void onRowEdit(RowEditEvent<DairyCowCostView> event) {
        DairyCowCostView view = event.getObject();
        if (checkIsThereChange(view)) {
            dairyCowCostService.saveUserCost(view, getBarnId());
            fillDataTableValues();
        }
        referenceCostView = null;
    }

    private boolean checkIsThereChange(DairyCowCostView view) {
        if (referenceCostView != null) {
            return !Objects.equals(referenceCostView.getCount(), view.getCount())
                    || !Objects.equals(referenceCostView.getTotalCost(), view.getTotalCost())
                    || !Objects.equals(referenceCostView.getHourlyOrInterest(), view.getHourlyOrInterest())
                    || !Objects.equals(referenceCostView.getSelectedCostId(), view.getSelectedCostId())
                    || !Objects.equals(referenceCostView.getSelectedCostName(), view.getSelectedCostName());
        }
        return true;
    }

    public void onRowCancel(RowEditEvent<DairyCowCostView> event) {
        DairyCowCostView view = event.getObject();
        if ((view.getSelectedCostId() == null) ||
                (view.getSelectedDairyCowCost().getCostType().getColumns().contains("COUNT") && view.getCount() == null) ||
                (view.getSelectedDairyCowCost().getCostType().getColumns().contains("HOUR") && view.getHourlyOrInterest() == null) ||
                (view.getSelectedDairyCowCost().getCostType().getColumns().contains("COST") && view.getTotalCost() == null)) {
            dairyCowCostViewList.removeLast();
        }
        referenceCostView = null;
    }

    public void onAddNew() {
        dairyCowCostViewList.add(new DairyCowCostView());
        referenceCostView = null;
    }

    public void deleteUserCost(DairyCowCostView costView) {
        dairyCowCostService.removeUserCost(costView.getUserCostId());
        fillDataTableValues();
        referenceCostView = null;
    }

    public void handleCostSelect(DairyCowCostView view) {
        view.setSelectedDairyCowCost(costList.stream().filter(c -> c.getId().equals(view.getSelectedCostId())).findFirst().get());
    }

}

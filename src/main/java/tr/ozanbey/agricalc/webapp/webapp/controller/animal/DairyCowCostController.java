package tr.ozanbey.agricalc.webapp.webapp.controller.animal;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.DairyCowCost;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumCostType;
import tr.ozanbey.agricalc.webapp.service.service.animal.DairyCowCostService;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowCostView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class DairyCowCostController extends DairyCowController {

    @Autowired
    private DairyCowCostService dairyCowCostService;

    private List<DairyCowCostView> dairyCowCostViewList;
    private DairyCowCostView selectedCostView;

    private EnumCostType[] costTypes = EnumCostType.values();
    private List<DairyCowCost> referenceList;
    private LocalDateTime today = LocalDateTime.now();

    @PostConstruct
    public void init() {
    }

    public void fillDataTableValues() {
        dairyCowCostViewList = dairyCowCostService.getActiveCostAsViewList(super.getBarnId());
        referenceList = dairyCowCostService.getCostsByStatuses(new EnumStatus[]{EnumStatus.ACTIVE});
    }

    public void addNewCostView() {
        selectedCostView = new DairyCowCostView();
        selectedCostView.setSelectedDairyCowCost(new DairyCowCost());
    }

    public List<DairyCowCost> getFromReferenceList() {
        return referenceList.stream().filter(r -> r.getCostType().equals(selectedCostView.getSelectedDairyCowCost().getCostType())).toList();
    }

    public void editUserCost(DairyCowCostView costView) {
        selectedCostView = new DairyCowCostView();
        selectedCostView.setUserCostId(costView.getUserCostId());
        selectedCostView.setSelectedCostId(costView.getSelectedDairyCowCost().getId());
        selectedCostView.setSelectedDairyCowCost(costView.getSelectedDairyCowCost());
        if (costView.getSelectedCostName() != null)
            selectedCostView.setSelectedCostName(costView.getSelectedCostName());
        else
            selectedCostView.setSelectedCostName(costView.getSelectedDairyCowCost().getName());
        selectedCostView.setCount(costView.getCount());
        selectedCostView.setTotalCost(costView.getTotalCost());
        selectedCostView.setHourlyOrInterest(costView.getHourlyOrInterest());
    }

    public void saveSelectedCost() {
        if (checkIsThereChange()) {
            dairyCowCostService.saveUserCost(selectedCostView, getBarnId());
            fillDataTableValues();
        }
        selectedCostView = null;
    }

    public boolean checkIsThereChange() {
        if (selectedCostView.getUserCostId() != null) {
            Optional<DairyCowCostView> optionalCost = dairyCowCostViewList.stream()
                    .filter(v -> v.getUserCostId().equals(selectedCostView.getUserCostId()))
                    .findFirst();
            if (optionalCost.isPresent()) {
                DairyCowCostView cost = optionalCost.get();
                return !Objects.equals(selectedCostView.getCount(), cost.getCount())
                        || !Objects.equals(selectedCostView.getTotalCost(), cost.getTotalCost())
                        || !Objects.equals(selectedCostView.getHourlyOrInterest(), cost.getHourlyOrInterest())
                        || !Objects.equals(selectedCostView.getSelectedCostName(), cost.getSelectedCostName());
            }
        }
        return true;
    }

    public boolean checkCostAdded(DairyCowCost dairyCowCost) {
        return dairyCowCostViewList.stream().anyMatch(v ->
                !dairyCowCost.getCostType().equals(EnumCostType.MAINTENANCE_SALARY)
                        && !dairyCowCost.getCostType().equals(EnumCostType.MAINTENANCE_HOURLY)
                        && v.getSelectedDairyCowCost().getId().equals(dairyCowCost.getId())
        );
    }

    public void deleteUserCost(DairyCowCostView costView) {
        dairyCowCostService.removeUserCost(costView.getUserCostId());
        fillDataTableValues();
        selectedCostView = null;
    }

    public void handleCostTypeSelect() {
        Optional<DairyCowCost> optionalCost = referenceList.stream().filter(c -> c.getId().equals(selectedCostView.getSelectedCostId())).findFirst();
        if (optionalCost.isPresent()) {
            selectedCostView.setSelectedDairyCowCost(optionalCost.get());
            if (selectedCostView.getSelectedCostName() == null) {
                selectedCostView.setSelectedCostName(optionalCost.get().getName());
            }
            if (optionalCost.get().getCostType().equals(EnumCostType.INSEMINATION)) {
                selectedCostView.setCount(getUserDairyCowBarn().getInseminationRate());
            } else if (optionalCost.get().getCostType().equals(EnumCostType.TARSIM)) {
                selectedCostView.setCount(getUserDairyCowBarn().getAverageFeedTotalCount());
            }
        }
    }

}

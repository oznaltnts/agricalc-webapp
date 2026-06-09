package tr.ozanbey.agricalc.webapp.webapp.controller.animal;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.Cost;
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
    private List<Cost> referenceList;
    private LocalDateTime today = LocalDateTime.now();

    @PostConstruct
    public void init() {
    }

    public void fillDataTableValues() {
        dairyCowCostViewList = dairyCowCostService.getActiveCostAsViewList(super.getBarnId());
        referenceList = super.getDairyCowService().getCostsByStatuses(new EnumStatus[]{EnumStatus.ACTIVE});
    }

    public void addNewCostView() {
        selectedCostView = new DairyCowCostView();
        selectedCostView.setSelectedCost(new Cost());
    }

    public List<Cost> getFromReferenceList() {
        return referenceList.stream().filter(r -> r.getCostType().equals(selectedCostView.getSelectedCost().getCostType())).toList();
    }

    public void editUserCost(DairyCowCostView costView) {
        selectedCostView = new DairyCowCostView();
        selectedCostView.setUserCostId(costView.getUserCostId());
        selectedCostView.setSelectedCostId(costView.getSelectedCost().getId());
        selectedCostView.setSelectedCost(costView.getSelectedCost());
        if (costView.getSelectedCostName() != null)
            selectedCostView.setSelectedCostName(costView.getSelectedCostName());
        else
            selectedCostView.setSelectedCostName(costView.getSelectedCost().getName());
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

    public boolean checkCostAdded(Cost cost) {
        return dairyCowCostViewList.stream().anyMatch(v ->
                !cost.getCostType().equals(EnumCostType.MAINTENANCE_SALARY)
                        && !cost.getCostType().equals(EnumCostType.MAINTENANCE_HOURLY)
                        && v.getSelectedCost().getId().equals(cost.getId())
        );
    }

    public void deleteUserCost(DairyCowCostView costView) {
        dairyCowCostService.removeUserCost(costView.getUserCostId());
        fillDataTableValues();
        selectedCostView = null;
    }

    public void handleCostTypeSelect() {
        Optional<Cost> optionalCost = referenceList.stream().filter(c -> c.getId().equals(selectedCostView.getSelectedCostId())).findFirst();
        if (optionalCost.isPresent()) {
            selectedCostView.setSelectedCost(optionalCost.get());
            if (selectedCostView.getSelectedCostName() == null)
                selectedCostView.setSelectedCostName(optionalCost.get().getName());
            if (optionalCost.get().getCostType().equals(EnumCostType.INSEMINATION)) {
                selectedCostView.setCount(getUserDairyCowBarn().getInseminationRate());
            }
        }
    }

}

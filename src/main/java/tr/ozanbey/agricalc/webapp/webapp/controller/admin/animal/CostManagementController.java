package tr.ozanbey.agricalc.webapp.webapp.controller.admin.animal;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.Cost;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumCostType;
import tr.ozanbey.agricalc.webapp.service.service.CostService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.util.List;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class CostManagementController extends BaseController {

    @Autowired
    private CostService costService;

    private EnumStatus[] statuses = EnumStatus.values();
    private EnumCostType[] costTypes = EnumCostType.values();
    private List<Cost> costList;
    private Cost selectedCost;

    @PostConstruct
    public void init() {
        fillDataTableValues();
    }

    private void fillDataTableValues() {
        costList = costService.getCostsByStatuses(statuses);
    }

    public void addNewCostToCategory() {
        selectedCost = new Cost();
        selectedCost.setStatus(EnumStatus.ACTIVE);
    }

    public void editCost(Cost cost) {
        selectedCost = new Cost();
        selectedCost.setId(cost.getId());
        selectedCost.setStatus(cost.getStatus());
        selectedCost.setCostType(cost.getCostType());
        selectedCost.setName(cost.getName());
    }

    public void saveSelectedCost() {
        if (checkIsThereChange()) {
            costService.saveCost(selectedCost);
            fillDataTableValues();
        }
        selectedCost = null;
    }

    public boolean checkIsThereChange() {
        if (selectedCost.getId() != null) {
            Optional<Cost> optionalCost = costList.stream().filter(v -> v.getId().equals(selectedCost.getId())).findFirst();
            if (optionalCost.isPresent()) {
                Cost cost = optionalCost.get();
                return !selectedCost.getCostType().equals(cost.getCostType())
                        || !selectedCost.getName().equals(cost.getName())
                        || !selectedCost.getStatus().equals(cost.getStatus());
            }
        }
        return true;
    }

}

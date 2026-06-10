package tr.ozanbey.agricalc.webapp.webapp.controller.admin.animal;


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
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.util.List;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class CostManagementController extends BaseController {

    @Autowired
    private DairyCowCostService dairyCowCostService;

    private EnumStatus[] statuses = EnumStatus.values();
    private EnumCostType[] costTypes = EnumCostType.values();
    private List<DairyCowCost> dairyCowCostList;
    private DairyCowCost selectedDairyCowCost;

    @PostConstruct
    public void init() {
        fillDataTableValues();
    }

    private void fillDataTableValues() {
        dairyCowCostList = dairyCowCostService.getCostsByStatuses(statuses);
    }

    public void addNewCostToCategory() {
        selectedDairyCowCost = new DairyCowCost();
        selectedDairyCowCost.setStatus(EnumStatus.ACTIVE);
    }

    public void editCost(DairyCowCost dairyCowCost) {
        selectedDairyCowCost = new DairyCowCost();
        selectedDairyCowCost.setId(dairyCowCost.getId());
        selectedDairyCowCost.setStatus(dairyCowCost.getStatus());
        selectedDairyCowCost.setCostType(dairyCowCost.getCostType());
        selectedDairyCowCost.setName(dairyCowCost.getName());
    }

    public void saveSelectedCost() {
        if (checkIsThereChange()) {
            dairyCowCostService.saveCost(selectedDairyCowCost);
            fillDataTableValues();
        }
        selectedDairyCowCost = null;
    }

    public boolean checkIsThereChange() {
        if (selectedDairyCowCost.getId() != null) {
            Optional<DairyCowCost> optionalCost = dairyCowCostList.stream().filter(v -> v.getId().equals(selectedDairyCowCost.getId())).findFirst();
            if (optionalCost.isPresent()) {
                DairyCowCost dairyCowCost = optionalCost.get();
                return !selectedDairyCowCost.getCostType().equals(dairyCowCost.getCostType())
                        || !selectedDairyCowCost.getName().equals(dairyCowCost.getName())
                        || !selectedDairyCowCost.getStatus().equals(dairyCowCost.getStatus());
            }
        }
        return true;
    }

}

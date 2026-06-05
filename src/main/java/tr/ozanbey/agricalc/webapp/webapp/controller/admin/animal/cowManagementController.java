package tr.ozanbey.agricalc.webapp.webapp.controller.admin.animal;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.DairyCow;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.service.animal.DairyCowService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.util.List;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class cowManagementController extends BaseController {

    @Autowired
    private DairyCowService dairyCowService;

    private EnumStatus[] statuses = EnumStatus.values();
    private List<DairyCow> dairyCowList;
    private DairyCow selectedDairyCow;

    @PostConstruct
    public void init() {
        fillDataTableValues();
    }

    private void fillDataTableValues() {
        dairyCowList = dairyCowService.getDairyCowsByStatuses(EnumStatus.values());
    }

    public void editDairyCow(DairyCow dairyCow) {
        selectedDairyCow = new DairyCow();
        selectedDairyCow.setId(dairyCow.getId());
        selectedDairyCow.setStatus(dairyCow.getStatus());
        selectedDairyCow.setName(dairyCow.getName());
    }

    public void addNewDairyCow() {
        selectedDairyCow = new DairyCow();
        selectedDairyCow.setStatus(EnumStatus.ACTIVE);
    }

    public void saveSelectedDairyCow() {
        if (checkIsThereChange()) {
            dairyCowService.saveDairyCow(selectedDairyCow);
            fillDataTableValues();
        }
        selectedDairyCow = null;
    }

    private boolean checkIsThereChange() {
        if (selectedDairyCow.getId() != null) {
            Optional<DairyCow> optional = dairyCowList.stream().filter(d -> d.getId().equals(selectedDairyCow.getId())).findFirst();
            if (optional.isPresent()) {
                DairyCow dairyCow = optional.get();
                return !selectedDairyCow.getName().equals(dairyCow.getName())
                        || !selectedDairyCow.getStatus().equals(dairyCow.getStatus());
            }
        }
        return true;
    }
}

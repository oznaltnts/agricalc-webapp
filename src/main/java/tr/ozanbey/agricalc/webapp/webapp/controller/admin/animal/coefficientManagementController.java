package tr.ozanbey.agricalc.webapp.webapp.controller.admin.animal;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.DairyCowCoefficient;
import tr.ozanbey.agricalc.webapp.service.service.animal.DairyCowService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.util.List;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class coefficientManagementController extends BaseController {

    @Autowired
    private DairyCowService dairyCowService;

    private List<DairyCowCoefficient> coefficientList;
    private DairyCowCoefficient selectedCoefficient;

    @PostConstruct
    public void init() {
        fillDataTableValues();
    }

    private void fillDataTableValues() {
        coefficientList = dairyCowService.getDairyCowCoefficients();
    }

    public void editCoefficient(DairyCowCoefficient coefficient) {
        selectedCoefficient = new DairyCowCoefficient();
        selectedCoefficient.setId(coefficient.getId());
        selectedCoefficient.setCowType(coefficient.getCowType());
        selectedCoefficient.setValue(coefficient.getValue());
    }

    public void saveSelectedDairyCow() {
        if (checkIsThereChange()) {
            dairyCowService.saveDairyCowCoefficient(selectedCoefficient);
            fillDataTableValues();
        }
        selectedCoefficient = null;
    }

    private boolean checkIsThereChange() {
        Optional<DairyCowCoefficient> optionalCoef = coefficientList.stream()
                .filter(c -> c.getId().equals(selectedCoefficient.getId()))
                .findAny();
        return selectedCoefficient.getValue() != optionalCoef.get().getValue();
    }

}

package tr.ozanbey.agricalc.webapp.webapp.controller.animal.dairycow;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.service.animal.dairycow.DairyCowIncomeService;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowIncomeView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@ViewScoped
@Getter
@Setter
public class DairyCowIncomeController extends DairyCowController {

    @Autowired
    private DairyCowIncomeService incomeService;

    private List<DairyCowIncomeView> referenceViewList;
    private List<DairyCowIncomeView> incomeViewList;

    @PostConstruct
    public void init() {
    }

    public void fillDataTableValues() {
        referenceViewList = new ArrayList<>();
        incomeViewList = incomeService.getDairyCowIncomeAsViewList(super.getBarnId());
        incomeViewList.forEach(v -> {
            DairyCowIncomeView dv = new DairyCowIncomeView();
            dv.setUserIncomeId(v.getUserIncomeId());
            dv.setIncomeValue(v.getIncomeValue());
            referenceViewList.add(dv);
        });
    }

    public void nextSaveIncome() throws IOException {
        List<DairyCowIncomeView> saveList = generateSaveList();
        if (!saveList.isEmpty()) {
            incomeService.saveIncomeFromViewList(saveList, getBarnId());
        }
        super.getNavigationController().redirectToUrl("/secured/animal/dairy-cow/result?barnId=" + getBarnId());
    }

    public void backSaveIncome() throws IOException {
        List<DairyCowIncomeView> saveList = generateSaveList();
        if (!saveList.isEmpty()) {
            incomeService.saveIncomeFromViewList(saveList, getBarnId());
        }
        super.getNavigationController().redirectToUrl("/secured/animal/dairy-cow/cost?barnId=" + getBarnId());
    }

    private List<DairyCowIncomeView> generateSaveList() {
        List<DairyCowIncomeView> returnSaveList = new ArrayList<>();
        for (DairyCowIncomeView dv : incomeViewList) {
            if (dv.getUserIncomeId() == null) {
                if (dv.getIncomeValue() != null)
                    returnSaveList.add(dv);
            } else {
                DairyCowIncomeView refView = referenceViewList.stream().filter(r -> dv.getUserIncomeId().equals(r.getUserIncomeId())).findFirst().get();
                if (!Objects.equals(refView.getIncomeValue(), dv.getIncomeValue())) {
                    returnSaveList.add(dv);
                }
            }
        }
        return returnSaveList;
    }

}

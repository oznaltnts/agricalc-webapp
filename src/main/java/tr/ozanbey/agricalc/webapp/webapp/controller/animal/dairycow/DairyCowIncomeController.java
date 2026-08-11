package tr.ozanbey.agricalc.webapp.webapp.controller.animal.dairycow;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.service.animal.dairycow.DairyCowIncomeService;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowIncomeView;

import java.util.List;
import java.util.Objects;

@Component
@ViewScoped
@Getter
@Setter
public class DairyCowIncomeController extends DairyCowController {

    @Autowired
    private DairyCowIncomeService incomeService;

    private List<DairyCowIncomeView> incomeViewList;
    private DairyCowIncomeView referenceView;

    @PostConstruct
    public void init() {
    }

    public void fillDataTableValues() {
        incomeViewList = incomeService.getDairyCowIncomeAsViewList(super.getBarnId());
    }

    public void onRowEditInit(RowEditEvent<DairyCowIncomeView> event) {
        DairyCowIncomeView view = event.getObject();
        referenceView = new DairyCowIncomeView();
        referenceView.setUserIncomeId(view.getUserIncomeId());
        referenceView.setIncomeId(view.getIncomeId());
        referenceView.setIncomeName(view.getIncomeName());
        referenceView.setIncomeUnit(view.getIncomeUnit());
        referenceView.setIncomeValue(view.getIncomeValue());
    }

    public void onRowEdit(RowEditEvent<DairyCowIncomeView> event) {
        DairyCowIncomeView view = event.getObject();
        if (checkIsThereChange(view)) {
            incomeService.saveIncomeFromView(view, getBarnId());
            fillDataTableValues();
        }
        referenceView = null;
    }

    private boolean checkIsThereChange(DairyCowIncomeView view) {
        if (referenceView != null) {
            return !Objects.equals(referenceView.getIncomeValue(), view.getIncomeValue());
        }
        return true;
    }

}

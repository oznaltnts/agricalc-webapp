package tr.ozanbey.agricalc.webapp.webapp.controller.animal.dairycow;


import jakarta.annotation.PostConstruct;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.DairyCow;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.service.animal.dairycow.DairyCowBarnService;
import tr.ozanbey.agricalc.webapp.webapp.util.JSFUtils;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowBarnView;

import java.util.List;
import java.util.Objects;

@Component
@ViewScoped
@Getter
@Setter
public class DairyCowBarnController extends DairyCowController {

    @Autowired
    private DairyCowBarnService dairyCowBarnService;

    private List<DairyCowBarnView> dairyCowBarnViews;
    private DairyCowBarnView referenceBarnView;

    private List<DairyCow> dairyCowList;

    @PostConstruct
    public void init() {
        fillDataTableValues();
        dairyCowList = super.getDairyCowService().getDairyCowsByStatuses(new EnumStatus[]{EnumStatus.ACTIVE});
    }

    private void fillDataTableValues() {
        dairyCowBarnViews = dairyCowBarnService.getBarnsAsViewList(getCurrentUser().getUser().getId());
    }

    public void onRowEditInit(RowEditEvent<DairyCowBarnView> event) {
        DairyCowBarnView view = event.getObject();

        referenceBarnView = new DairyCowBarnView();
        referenceBarnView.setBarnId(view.getBarnId());
        referenceBarnView.setDairyCowName(view.getDairyCowName());
        referenceBarnView.setSelectedDairyCowId(view.getSelectedDairyCowId());
        referenceBarnView.setBarnCapacity(view.getBarnCapacity());
        referenceBarnView.setMilkingCapacity(view.getMilkingCapacity());
        referenceBarnView.setBarnPrice(view.getBarnPrice());
        referenceBarnView.setBirthRate(view.getBirthRate());
        referenceBarnView.setDeathRate(view.getDeathRate());
        referenceBarnView.setInseminationRate(view.getInseminationRate());
        referenceBarnView.setMilkYield(view.getMilkYield());
        referenceBarnView.setLactationPeriod(view.getLactationPeriod());
        referenceBarnView.setUnknownBirthRate(view.isUnknownBirthRate());
        referenceBarnView.setUnknownDeathRate(view.isUnknownDeathRate());
        referenceBarnView.setUnknownInseminationRate(view.isUnknownInseminationRate());
        referenceBarnView.setUnknownMilkYield(view.isUnknownMilkYield());
        referenceBarnView.setTotalCount(view.getTotalCount());
        referenceBarnView.setEndYearTotalCount(view.getEndYearTotalCount());
        referenceBarnView.setAverageFeedTotalCount(view.getAverageFeedTotalCount());
        referenceBarnView.setAverageMilkingCount(view.getAverageMilkingCount());
    }

    public void onRowEdit(RowEditEvent<DairyCowBarnView> event) {
        DairyCowBarnView view = event.getObject();
        if (checkIsThereChange(view)) {
            dairyCowBarnService.saveUserBarn(view, getCurrentUser().getUser());
            fillDataTableValues();
        }
        referenceBarnView = null;
    }

    private boolean checkIsThereChange(DairyCowBarnView view) {
        if (referenceBarnView != null) {
            return !Objects.equals(referenceBarnView.getBarnCapacity(), view.getBarnCapacity())
                    || !Objects.equals(referenceBarnView.getMilkingCapacity(), view.getMilkingCapacity())
                    || !referenceBarnView.getSelectedDairyCowId().equals(view.getSelectedDairyCowId())
                    || !Objects.equals(referenceBarnView.getBarnPrice(), view.getBarnPrice())
                    || referenceBarnView.isUnknownBirthRate() != view.isUnknownBirthRate()
                    || !Objects.equals(referenceBarnView.getBirthRate(), view.getBirthRate())
                    || referenceBarnView.isUnknownDeathRate() != view.isUnknownDeathRate()
                    || !Objects.equals(referenceBarnView.getDeathRate(), view.getDeathRate())
                    || referenceBarnView.isUnknownInseminationRate() != view.isUnknownInseminationRate()
                    || !Objects.equals(referenceBarnView.getInseminationRate(), view.getInseminationRate())
                    || referenceBarnView.isUnknownMilkYield() != view.isUnknownMilkYield()
                    || !Objects.equals(referenceBarnView.getMilkYield(), view.getMilkYield())
                    || !Objects.equals(referenceBarnView.getLactationPeriod(), view.getLactationPeriod());
        }
        return true;
    }

    public void onRowCancel(RowEditEvent<DairyCowBarnView> event) {
        DairyCowBarnView view = event.getObject();
        if (view.getSelectedDairyCowId() == null
                || view.getBarnCapacity() == null
                || view.getMilkingCapacity() == null
                || view.getLactationPeriod() == null) {
            dairyCowBarnViews.removeLast();
        }
        referenceBarnView = null;
    }

    public void onAddNew() {
        dairyCowBarnViews.add(new DairyCowBarnView());
        referenceBarnView = null;
    }

    public void validateMilkingCapacity(DairyCowBarnView inputView) {
        if (inputView.getBarnCapacity() != null && inputView.getMilkingCapacity() != null && inputView.getBarnCapacity() * 0.9 < inputView.getMilkingCapacity()) {
            JSFUtils.addErrorMessage("growl", "Sağmal İnek Kapasitesi", "Ahır kapasitesinin %90'nı geçemez.");
            int viewIndex = dairyCowBarnViews.indexOf(inputView);
            DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("barnListForm:dairyCowTable");
            table.setRowIndex(viewIndex);
            UIInput input = (UIInput) table.findComponent("milkingCapacityInput");
            if (input != null) {
                input.setValid(false);
                inputView.setMilkingCapacity(null);
            }
        }
    }

}

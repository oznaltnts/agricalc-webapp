package tr.ozanbey.agricalc.webapp.webapp.controller.animal;


import jakarta.annotation.PostConstruct;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.DairyCow;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.service.animal.DairyCowBarnService;
import tr.ozanbey.agricalc.webapp.webapp.util.JSFUtils;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowBarnView;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class DairyCowBarnController extends DairyCowController {

    @Autowired
    private DairyCowBarnService dairyCowBarnService;

    private List<DairyCowBarnView> dairyCowBarnViews;
    private DairyCowBarnView selectedBarnView;

    private List<DairyCow> dairyCowList;

    @PostConstruct
    public void init() {
        fillDataTableValues();
    }

    private void fillDataTableValues() {
        dairyCowBarnViews = dairyCowBarnService.getBarnsAsViewList(getCurrentUser().getUser().getId());
    }

    public void editBarn(DairyCowBarnView view) {
        selectedBarnView = new DairyCowBarnView();
        selectedBarnView.setDairyCowName(view.getDairyCowName());
        selectedBarnView.setSelectedDairyCowId(view.getSelectedDairyCowId());
        selectedBarnView.setBarnId(view.getBarnId());
        selectedBarnView.setBarnCapacity(view.getBarnCapacity());
        selectedBarnView.setMilkingCapacity(view.getMilkingCapacity());
        selectedBarnView.setBarnPrice(view.getBarnPrice());
        selectedBarnView.setBirthRate(view.getBirthRate());
        selectedBarnView.setDeathRate(view.getDeathRate());
        selectedBarnView.setInseminationRate(view.getInseminationRate());
        selectedBarnView.setMilkYield(view.getMilkYield());
        selectedBarnView.setLactationPeriod(view.getLactationPeriod());
        selectedBarnView.setUnknownBirthRate(view.isUnknownBirthRate());
        selectedBarnView.setUnknownDeathRate(view.isUnknownDeathRate());
        selectedBarnView.setUnknownInseminationRate(view.isUnknownInseminationRate());
        selectedBarnView.setUnknownMilkYield(view.isUnknownMilkYield());
        selectedBarnView.setTotalCount(view.getTotalCount());
        selectedBarnView.setEndYearTotalCount(view.getEndYearTotalCount());
        selectedBarnView.setAverageFeedTotalCount(view.getAverageFeedTotalCount());
        selectedBarnView.setAverageMilkingCount(view.getAverageMilkingCount());

        if (dairyCowList == null || dairyCowList.isEmpty()) {
            dairyCowList = super.getDairyCowService().getDairyCowsByStatuses(new EnumStatus[]{EnumStatus.ACTIVE});
        }
    }

    public void addNewBarnView() {
        selectedBarnView = new DairyCowBarnView();
        if (dairyCowList == null || dairyCowList.isEmpty()) {
            dairyCowList = super.getDairyCowService().getDairyCowsByStatuses(new EnumStatus[]{EnumStatus.ACTIVE});
        }
    }

    public void saveSelectedBarn() {
        if (checkIsThereChange()) {
            if (isValuesValid()) {
                dairyCowBarnService.saveUserBarn(selectedBarnView, getCurrentUser().getUser());
                fillDataTableValues();
                selectedBarnView = null;
            }
        } else {
            selectedBarnView = null;
        }
    }

    private boolean checkIsThereChange() {
        if (selectedBarnView.getBarnId() != null) {
            Optional<DairyCowBarnView> optionalBarn = dairyCowBarnViews.stream()
                    .filter(b -> b.getBarnId().equals(selectedBarnView.getBarnId()))
                    .findAny();
            if (optionalBarn.isPresent()) {
                DairyCowBarnView barnView = optionalBarn.get();
                return !Objects.equals(selectedBarnView.getBarnCapacity(), barnView.getBarnCapacity())
                        || !Objects.equals(selectedBarnView.getMilkingCapacity(), barnView.getMilkingCapacity())
                        || !selectedBarnView.getSelectedDairyCowId().equals(barnView.getSelectedDairyCowId())
                        || !Objects.equals(selectedBarnView.getBarnPrice(), barnView.getBarnPrice())
                        || selectedBarnView.isUnknownBirthRate() != barnView.isUnknownBirthRate()
                        || !Objects.equals(selectedBarnView.getBirthRate(), barnView.getBirthRate())
                        || selectedBarnView.isUnknownDeathRate() != barnView.isUnknownDeathRate()
                        || !Objects.equals(selectedBarnView.getDeathRate(), barnView.getDeathRate())
                        || selectedBarnView.isUnknownInseminationRate() != barnView.isUnknownInseminationRate()
                        || !Objects.equals(selectedBarnView.getInseminationRate(), barnView.getInseminationRate())
                        || selectedBarnView.isUnknownMilkYield() != barnView.isUnknownMilkYield()
                        || !Objects.equals(selectedBarnView.getMilkYield(), barnView.getMilkYield())
                        || !Objects.equals(selectedBarnView.getLactationPeriod(), barnView.getLactationPeriod());
            }
        }
        return true;
    }

    private boolean isValuesValid() {
        if (selectedBarnView.getBarnCapacity() * 0.9 < selectedBarnView.getMilkingCapacity()) {
            JSFUtils.addErrorMessage(null, "Sağmal İnek Kapasitesi", "Ahır kapasitesinin %90'nı geçemez.");
            UIInput inputComponent = (UIInput) FacesContext.getCurrentInstance().getViewRoot().findComponent("barnEditForm:milkingCapacity");
            if (inputComponent != null) {
                inputComponent.setValid(false);
                PrimeFaces.current().focus("barnEditForm:milkingCapacity");
            }
            return false;
        }
        return true;
    }

}

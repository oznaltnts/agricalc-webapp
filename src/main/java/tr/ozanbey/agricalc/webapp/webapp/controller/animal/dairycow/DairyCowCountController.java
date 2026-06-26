package tr.ozanbey.agricalc.webapp.webapp.controller.animal.dairycow;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.UserDairyCowBarn;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.dairycow.EnumCowType;
import tr.ozanbey.agricalc.webapp.service.service.animal.dairycow.DairyCowCountService;
import tr.ozanbey.agricalc.webapp.webapp.util.JSFUtils;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowCountCalculateView;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowCountView;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class DairyCowCountController extends DairyCowController {

    @Autowired
    private DairyCowCountService dairyCowCountService;

    private List<DairyCowCountView> dairyCowCountViewList;
    private DairyCowCountView referenceCountView;

    private DairyCowCountCalculateView calculateView;

    @PostConstruct
    public void init() {
    }

    public void fillDataTableValues() {
        dairyCowCountViewList = dairyCowCountService.getCowCountListByBarnId(getBarnId());
        Optional<UserDairyCowBarn> optionalBarn = getDairyCowService().getUserBarnByIdAndUserId(getBarnId(), getCurrentUser().getUser().getId());
        if (optionalBarn.isPresent())
            setUserDairyCowBarn(optionalBarn.get());
        generateCalculateView();
    }

    private void generateCalculateView() {
        calculateView = new DairyCowCountCalculateView(
                getUserDairyCowBarn().getTotalCount(),
                getUserDairyCowBarn().getTotalCount() / getUserDairyCowBarn().getBarnCapacity() * 100,
                getUserDairyCowBarn().getEndYearTotalCount(),
                getUserDairyCowBarn().getEndYearTotalCount() / getUserDairyCowBarn().getBarnCapacity() * 100,
                getUserDairyCowBarn().getAverageFeedTotalCount()
        );
    }

    public void onRowEditInit(RowEditEvent<DairyCowCountView> event) {
        DairyCowCountView view = event.getObject();
        referenceCountView = new DairyCowCountView();
        referenceCountView.setCountId(view.getCountId());
        referenceCountView.setCoefId(view.getCoefId());
        referenceCountView.setCowType(view.getCowType());
        referenceCountView.setCoefficientValue(view.getCoefficientValue());
        referenceCountView.setCurrentCount(view.getCurrentCount());
        referenceCountView.setPurchaseCount(view.getPurchaseCount());
        referenceCountView.setSellCount(view.getSellCount());
        referenceCountView.setEndYearCount(view.getEndYearCount());
        referenceCountView.setAverageFeedCount(view.getAverageFeedCount());
    }

    public void onRowEdit(RowEditEvent<DairyCowCountView> event) {
        DairyCowCountView view = event.getObject();
        if (checkIsThereChange(view)) {
            dairyCowCountService.saveCountList(dairyCowCountViewList, view, getUserDairyCowBarn());
            fillDataTableValues();
        }
        referenceCountView = null;
    }

    private boolean checkIsThereChange(DairyCowCountView view) {
        if (referenceCountView != null) {
            return !Objects.equals(referenceCountView.getCurrentCount(), view.getCurrentCount())
                    || referenceCountView.getPurchaseCount() != view.getPurchaseCount()
                    || referenceCountView.getSellCount() != view.getSellCount();
        }
        return true;
    }

    public void onRowCancel(RowEditEvent<DairyCowCountView> event) {
        referenceCountView = null;
    }

    public void validateBarnCapacity(DairyCowCountView view) {
        if (getUserDairyCowBarn().getBarnCapacity() <
                calculateView.getTotalCount()
                        - (referenceCountView.getCurrentCount() * referenceCountView.getCoefficientValue())
                        + (view.getCurrentCount() * view.getCoefficientValue())
        ) {
            if (view.getCowType().equals(EnumCowType.COW)) {
                JSFUtils.addErrorMessage(null, "Ahırda fazla hayvan var", "Mevcut inek varlığı azaltın");
            } else if (view.getCowType().equals(EnumCowType.PREGNANT_HEIFER)) {
                JSFUtils.addErrorMessage(null, "Ahırda fazla hayvan var", "Mevcut gebe düve varlığı azaltın");
            } else if (view.getCowType().equals(EnumCowType.HEIFER)) {
                JSFUtils.addErrorMessage(null, "Ahırda fazla hayvan var", "Mevcut düve varlığı azaltın");
            } else if (view.getCowType().equals(EnumCowType.STEER)) {
                JSFUtils.addErrorMessage(null, "Ahırda fazla hayvan var", "Mevcut dana varlığı azaltın");
            } else if (view.getCowType().equals(EnumCowType.CALF)) {
                JSFUtils.addErrorMessage(null, "Ahırda fazla hayvan var", "Mevcut buzağı varlığı azaltın");
            }
        }
        if (view.getCowType().equals(EnumCowType.COW) && getUserDairyCowBarn().getMilkingCapacity() < view.getCurrentCount()) {
            JSFUtils.addErrorMessage(null, "Sağmal İnek Kapasitesi aşıldı", "Mevcut inek varlığı azaltın");
        }
    }

}

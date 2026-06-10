package tr.ozanbey.agricalc.webapp.webapp.controller.animal;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumCowType;
import tr.ozanbey.agricalc.webapp.service.service.animal.DairyCowCountService;
import tr.ozanbey.agricalc.webapp.webapp.util.JSFUtils;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowCountCalculateView;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowCountView;

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

    private EnumCowType[] cowTypes = EnumCowType.values();
    private List<DairyCowCountView> dairyCowCountViewList;
    private DairyCowCountView selectedCountView;
    private DairyCowCountCalculateView calculateView;

    @PostConstruct
    public void init() {
    }

    public void fillDataTableValues() {
        dairyCowCountViewList = dairyCowCountService.getCowCountListByBarnId(getBarnId());
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

    public void editCount(DairyCowCountView view) {
        selectedCountView = new DairyCowCountView();
        selectedCountView.setCountId(view.getCountId());
        selectedCountView.setCoefId(view.getCoefId());
        selectedCountView.setCowType(view.getCowType());
        selectedCountView.setCoefficientValue(view.getCoefficientValue());
        selectedCountView.setCurrentCount(view.getCurrentCount());
        selectedCountView.setPurchaseCount(view.getPurchaseCount());
        selectedCountView.setSellCount(view.getSellCount());
        selectedCountView.setEndYearCount(view.getEndYearCount());
        selectedCountView.setAverageFeedCount(view.getAverageFeedCount());
    }

    public void saveSelectedCount() {
        if (checkIsThereChange()) {
            if (checkCurrentCountOK()) {
                dairyCowCountService.saveCountList(dairyCowCountViewList, selectedCountView, getUserDairyCowBarn());
                fillDataTableValues();
                selectedCountView = null;
            }
        } else {
            selectedCountView = null;
        }
    }

    private boolean checkIsThereChange() {
        if (selectedCountView.getCountId() != null) {
            Optional<DairyCowCountView> optionalCount = dairyCowCountViewList.stream()
                    .filter(c -> c.getCountId().equals(selectedCountView.getCountId()))
                    .findAny();
            if (optionalCount.isPresent()) {
                DairyCowCountView view = optionalCount.get();
                return !Objects.equals(selectedCountView.getCurrentCount(), view.getCurrentCount())
                        || selectedCountView.getPurchaseCount() != view.getPurchaseCount()
                        || selectedCountView.getSellCount() != view.getSellCount();
            }
        }
        return true;
    }

    private boolean checkCurrentCountOK() {
        boolean isCountsOk = true;
        Optional<DairyCowCountView> optionalCount = dairyCowCountViewList.stream()
                .filter(v -> v.getCowType().equals(selectedCountView.getCowType()))
                .findAny();
        if (optionalCount.isPresent()) {
            DairyCowCountView view = optionalCount.get();
            if (getUserDairyCowBarn().getBarnCapacity() < calculateView.getTotalCount() - (view.getCurrentCount() * view.getCoefficientValue()) + (selectedCountView.getCurrentCount() * view.getCoefficientValue())) {
                if (selectedCountView.getCowType().equals(EnumCowType.COW)) {
                    JSFUtils.addErrorMessage(null, "Ahırda fazla hayvan var", "Mevcut inek varlığı azaltın");
                } else if (selectedCountView.getCowType().equals(EnumCowType.PREGNANT_HEIFER)) {
                    JSFUtils.addErrorMessage(null, "Ahırda fazla hayvan var", "Mevcut gebe düve varlığı azaltın");
                } else if (selectedCountView.getCowType().equals(EnumCowType.HEIFER)) {
                    JSFUtils.addErrorMessage(null, "Ahırda fazla hayvan var", "Mevcut düve varlığı azaltın");
                } else if (selectedCountView.getCowType().equals(EnumCowType.STEER)) {
                    JSFUtils.addErrorMessage(null, "Ahırda fazla hayvan var", "Mevcut dana varlığı azaltın");
                } else if (selectedCountView.getCowType().equals(EnumCowType.CALF)) {
                    JSFUtils.addErrorMessage(null, "Ahırda fazla hayvan var", "Mevcut buzağı varlığı azaltın");
                }
                isCountsOk = false;
            }
            if (selectedCountView.getCowType().equals(EnumCowType.COW) && getUserDairyCowBarn().getMilkingCapacity() < selectedCountView.getCurrentCount()) {
                JSFUtils.addErrorMessage(null, "Sağmal İnek Kapasitesi aşıldı", "Mevcut inek varlığı azaltın");
                isCountsOk = false;
            }
        }
        return isCountsOk;
    }

}

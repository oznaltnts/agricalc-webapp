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
    private int totalCount;
    private int endYearTotalCount;

    @PostConstruct
    public void init() {
    }

    public void fillDataTableValues() {
        dairyCowCountViewList = dairyCowCountService.getCowCountListByBarnId(getBarnId());
        calculateTotalRowCount();
    }

    private void calculateTotalRowCount() {
        double totalCowCount = 0;
        double totalEndYearCowCount = 0;
        for (DairyCowCountView view : dairyCowCountViewList) {
            totalCowCount = totalCowCount + (view.getCurrentCount() * view.getCoefficientValue());
            totalEndYearCowCount = totalEndYearCowCount;
        }
        totalCount = (int) Math.round(totalCowCount);
//            if (view.getCowType().equals(EnumCowType.COW)) {
//                totalEndYearCowCount = totalEndYearCowCount + view.getEndYearCount();
//                totalInYearFeedCount = totalInYearFeedCount + view.getEndYearFeedCount();
//            } else if (view.getCowType().equals(EnumCowType.PREGNANT_HEIFER)) {
//                totalEndYearCowCount = totalEndYearCowCount + (view.getEndYearCount() * 0.85);
//                totalInYearFeedCount = totalInYearFeedCount + (view.getEndYearFeedCount() * 0.85);
//            } else if (view.getCowType().equals(EnumCowType.HEIFER)) {
//                totalEndYearCowCount = totalEndYearCowCount + (view.getEndYearCount() * 0.6);
//                totalInYearFeedCount = totalInYearFeedCount + (view.getEndYearFeedCount() * 0.6);
//            } else if (view.getCowType().equals(EnumCowType.STEER)) {
//                totalEndYearCowCount = totalEndYearCowCount + (view.getEndYearCount() * 0.4);
//                totalInYearFeedCount = totalInYearFeedCount + (view.getEndYearFeedCount() * 0.4);
//            } else if (view.getCowType().equals(EnumCowType.CALF)) {
//                totalEndYearCowCount = totalEndYearCowCount + (view.getEndYearCount() * 0.25);
//                totalInYearFeedCount = totalInYearFeedCount + (view.getEndYearFeedCount() * 0.25);
//            }
        endYearTotalCount = (int) Math.round(totalEndYearCowCount);
//        inYearTotalFeedCount = totalInYearFeedCount;
//        totalBarnUsageRate = totalCowCount / getUserDairyCowBarn().getBarnCapacity() * 100;
//        totalEndYearBarnUsageRate = totalEndYearCowCount / getUserDairyCowBarn().getBarnCapacity() * 100;
    }

    public void editCount(DairyCowCountView view) {
        selectedCountView = new DairyCowCountView();
        selectedCountView.setCountId(view.getCountId());
        selectedCountView.setCoefId(view.getCoefId());
        selectedCountView.setCowType(view.getCowType());
        selectedCountView.setCurrentCount(view.getCurrentCount());
        selectedCountView.setPurchaseCount(view.getPurchaseCount());
        selectedCountView.setSellCount(view.getSellCount());
    }

    public void saveSelectedCount() {
        if (checkIsThereChange()) {
            if (checkCurrentCountOK()) {
                dairyCowCountService.saveUserCount(selectedCountView, getBarnId());
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
            if (getUserDairyCowBarn().getBarnCapacity() < totalCount - (view.getCurrentCount() * view.getCoefficientValue()) + (selectedCountView.getCurrentCount() * view.getCoefficientValue())) {
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

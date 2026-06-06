package tr.ozanbey.agricalc.webapp.service.service.animal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowCount;
import tr.ozanbey.agricalc.webapp.service.repository.DairyCowCoefficientRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowBarnRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowCountRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowCountView;

import java.util.List;

@Service
@Slf4j
public class DairyCowCountService {

    @Autowired
    private DairyCowCoefficientRepository coefficientRepository;

    @Autowired
    private UserDairyCowBarnRepository barnRepository;

    @Autowired
    private UserDairyCowCountRepository countRepository;

    public List<DairyCowCountView> getCowCountListByBarnId(Long barnId) {
        return countRepository.findAsViewListByBarnId(barnId);
    }

//    private List<DairyCowCountView> calculateCowCount(List<DairyCowCountView> returnList, double milkCapacity, double birthRate, double deathRate, double barnCapacity) {
//        double endYearCowCount = 0;
//        double endYearFeedCowCount = 0;
//        double endYearPregnantCount = 0;
//        double endYearHeiferCount = 0;
//        double endYearSteerCount = 0;
//        for (DairyCowCountView view : returnList) {
//            if (view.getCowType().equals(EnumCowType.COW)) {
//                endYearCowCount = endYearCowCount + view.getCurrentCount() + view.getPurchaseCount() - view.getSellCount();
//                endYearFeedCowCount = endYearFeedCowCount + view.getCurrentCount() + ((double) (view.getPurchaseCount() - view.getSellCount()) / 2);
//            } else if (view.getCowType().equals(EnumCowType.PREGNANT_HEIFER)) {
//                endYearCowCount = endYearCowCount + view.getCurrentCount() + view.getPurchaseCount() - view.getSellCount();
//                endYearFeedCowCount = endYearFeedCowCount + ((double) (view.getCurrentCount() + view.getPurchaseCount() - view.getSellCount()) / 2);
//            } else if (view.getCowType().equals(EnumCowType.HEIFER)) {
//                endYearPregnantCount = endYearPregnantCount + view.getCurrentCount() + view.getPurchaseCount() - view.getSellCount();
//            } else if (view.getCowType().equals(EnumCowType.STEER)) {
//                endYearHeiferCount = endYearHeiferCount + view.getCurrentCount() + view.getPurchaseCount() - view.getSellCount();
//            } else if (view.getCowType().equals(EnumCowType.CALF)) {
//                endYearSteerCount = endYearSteerCount + view.getCurrentCount() + view.getPurchaseCount() - view.getSellCount();
//            }
//        }
//        for (DairyCowCountView view : returnList) {
//            if (view.getCowType().equals(EnumCowType.COW)) {
//                view.setEndYearCount(endYearCowCount);
//                view.setEndYearFeedCount(endYearFeedCowCount);
//                view.setStartMilkUsageRate(view.getCurrentCount() * 100 / milkCapacity);
//                view.setEndMilkUsageRate(view.getEndYearCount() * 100 / milkCapacity);
//            } else if (view.getCowType().equals(EnumCowType.PREGNANT_HEIFER)) {
//                view.setEndYearCount(endYearPregnantCount);
//                view.setEndYearFeedCount((view.getCurrentCount() + endYearPregnantCount) / 2);
//            } else if (view.getCowType().equals(EnumCowType.HEIFER)) {
//                view.setEndYearCount(endYearHeiferCount);
//                view.setEndYearFeedCount((view.getCurrentCount() + endYearHeiferCount) / 2);
//            } else if (view.getCowType().equals(EnumCowType.STEER)) {
//                view.setEndYearCount(endYearSteerCount);
//                view.setEndYearFeedCount((view.getCurrentCount() + endYearSteerCount) / 2);
//            } else if (view.getCowType().equals(EnumCowType.CALF)) {
//                double endYearCalfCount = (endYearCowCount * (birthRate / 100) * ((100 - deathRate) / 100)) + view.getPurchaseCount() - view.getSellCount();
//                view.setEndYearCount(endYearCalfCount);
//                view.setEndYearFeedCount((view.getCurrentCount() + endYearCalfCount) / 2);
//            }
//            view.setStartBarnUsageRate(view.getCurrentCount() * 100 / barnCapacity);
//            view.setEndBarnUsageRate(view.getEndYearCount() * 100 / barnCapacity);
//        }
//        return returnList;
//    }

    @Transactional
    public void saveUserCount(DairyCowCountView selectedCountView, Long barnId) {
        UserDairyCowCount cowCount;
        if (selectedCountView.getCountId() != null)
            cowCount = countRepository.getReferenceById(selectedCountView.getCountId());
        else {
            cowCount = new UserDairyCowCount();
            cowCount.setUserDairyCowBarn(barnRepository.getReferenceById(barnId));
            cowCount.setDairyCowCoefficient(coefficientRepository.getReferenceById(selectedCountView.getCoefId()));
        }
        cowCount.setCurrentCount(selectedCountView.getCurrentCount());
        cowCount.setPurchaseCount(selectedCountView.getPurchaseCount());
        cowCount.setSellCount(selectedCountView.getSellCount());
        countRepository.save(cowCount);
    }

}

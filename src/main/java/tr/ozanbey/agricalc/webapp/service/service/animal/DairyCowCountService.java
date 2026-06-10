package tr.ozanbey.agricalc.webapp.service.service.animal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowCount;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumCowType;
import tr.ozanbey.agricalc.webapp.service.repository.DairyCowCoefficientRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowBarnRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowCountRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowCountCalculateView;
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

    public List<DairyCowCountView> calculateCowCount(List<DairyCowCountView> dairyCowCountViewList, Double birthRate, Double deathRate) {
        double endYearCowOne = 0;
        double endYearCowPregnant = 0;
        double endYearPregnantHeifer = 0;
        double endYearHeiferSteer = 0;
        double endYearSteerCalf = 0;
        double endYearCalf = 0;

        for (DairyCowCountView cowCountView : dairyCowCountViewList) {
            if (cowCountView.getCowType().equals(EnumCowType.COW)) {
                endYearCowOne = cowCountView.getCurrentCount() + cowCountView.getPurchaseCount() - cowCountView.getSellCount();
            } else if (cowCountView.getCowType().equals(EnumCowType.PREGNANT_HEIFER)) {
                endYearCowPregnant = cowCountView.getCurrentCount() + cowCountView.getPurchaseCount() - cowCountView.getSellCount();
            } else if (cowCountView.getCowType().equals(EnumCowType.HEIFER)) {
                endYearPregnantHeifer = cowCountView.getCurrentCount() + cowCountView.getPurchaseCount() - cowCountView.getSellCount();
            } else if (cowCountView.getCowType().equals(EnumCowType.STEER)) {
                endYearHeiferSteer = cowCountView.getCurrentCount() + cowCountView.getPurchaseCount() - cowCountView.getSellCount();
            } else if (cowCountView.getCowType().equals(EnumCowType.CALF)) {
                endYearSteerCalf = cowCountView.getCurrentCount() + cowCountView.getPurchaseCount() - cowCountView.getSellCount();
                endYearCalf = cowCountView.getPurchaseCount() - cowCountView.getSellCount();
            }
        }

        double endYearCowCount = endYearCowOne + endYearCowPregnant;

        if (birthRate != null && deathRate != null) {
            endYearCalf = endYearCowCount * birthRate / 100 * ((100 - deathRate) / 100) + endYearCalf;
        } else if (birthRate != null) {
            endYearCalf = endYearCowCount * birthRate / 100 * ((100 - 2) / 100) + endYearCalf;
        } else if (deathRate != null) {
            endYearCalf = endYearCowCount * 95 / 100 * ((100 - deathRate) / 100) + endYearCalf;
        } else {
            endYearCalf = endYearCowCount * 95 / 100 * ((100 - 2) / 100) + endYearCalf;
        }

        for (DairyCowCountView cowCountView : dairyCowCountViewList) {
            if (cowCountView.getCowType().equals(EnumCowType.COW)) {
                cowCountView.setEndYearCount(endYearCowCount);
                cowCountView.setEndYearFeedCount(cowCountView.getCurrentCount() + ((double) (cowCountView.getPurchaseCount() - cowCountView.getSellCount()) / 2) + (endYearCowPregnant / 2));
            } else if (cowCountView.getCowType().equals(EnumCowType.PREGNANT_HEIFER)) {
                cowCountView.setEndYearCount(endYearPregnantHeifer);
                cowCountView.setEndYearFeedCount((cowCountView.getCurrentCount() + endYearPregnantHeifer) / 2);
            } else if (cowCountView.getCowType().equals(EnumCowType.HEIFER)) {
                cowCountView.setEndYearCount(endYearHeiferSteer);
                cowCountView.setEndYearFeedCount((cowCountView.getCurrentCount() + endYearHeiferSteer) / 2);
            } else if (cowCountView.getCowType().equals(EnumCowType.STEER)) {
                cowCountView.setEndYearCount(endYearSteerCalf);
                cowCountView.setEndYearFeedCount((cowCountView.getCurrentCount() + endYearSteerCalf) / 2);
            } else if (cowCountView.getCowType().equals(EnumCowType.CALF)) {
                cowCountView.setEndYearCount(endYearCalf);
                cowCountView.setEndYearFeedCount((cowCountView.getCurrentCount() + endYearCalf) / 2);
            }
        }
        return dairyCowCountViewList;
    }

    @Transactional
    public DairyCowCountCalculateView calculateTotalRowCount(List<DairyCowCountView> dairyCowCountViewList, Long barnId, int barnCapacity) {
        DairyCowCountCalculateView calculateView = new DairyCowCountCalculateView();
        double totalCowCount = 0;
        double totalEndYearCowCount = 0;
        double totalAverageFeedCount = 0;
        for (DairyCowCountView view : dairyCowCountViewList) {
            totalCowCount = totalCowCount + (view.getCurrentCount() * view.getCoefficientValue());
            totalEndYearCowCount = totalEndYearCowCount + (view.getEndYearCount() * view.getCoefficientValue());
            totalAverageFeedCount = totalAverageFeedCount + (view.getEndYearFeedCount() * view.getCoefficientValue());
        }
        calculateView.setTotalCount(totalCowCount);
        calculateView.setTotalBarnUsageRate(totalCowCount / barnCapacity * 100);
        calculateView.setEndYearTotalCount(totalEndYearCowCount);
        calculateView.setEndYearBarnUsageRate(totalEndYearCowCount / barnCapacity * 100);
        calculateView.setEndYearTotalFeedCount(totalAverageFeedCount);

        saveAverageValue(barnId, totalEndYearCowCount, totalAverageFeedCount);
        return calculateView;
    }

    public void saveAverageValue(Long barnId, double totalAverageCount, double totalAverageFeedCount) {
        barnRepository.saveAverageValuesFromCountList(barnId, totalAverageCount, totalAverageFeedCount);
    }

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

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

    private List<DairyCowCountView> getCowCountListByBarnId(Long barnId) {
        return countRepository.findAsViewListByBarnId(barnId);
    }

    public List<DairyCowCountView> calculateCowCount(Long barnId, Double birthRate, Double deathRate) {
        List<DairyCowCountView> returnList = getCowCountListByBarnId(barnId);
        int endYearCowCount = 0;
        int endYearPregnantHeiferCount = 0;
        int endYearHeiferCount = 0;
        int endYearSteerCount = 0;
        int endYearCalfCount = 0;
        int calfCalcCount = 0;
        for (DairyCowCountView cowCountView : returnList) {
            if (cowCountView.getCowType().equals(EnumCowType.COW)) {
                endYearCowCount = endYearCowCount + (cowCountView.getCurrentCount() + cowCountView.getPurchaseCount() - cowCountView.getSellCount());
            } else if (cowCountView.getCowType().equals(EnumCowType.PREGNANT_HEIFER)) {
                endYearCowCount = endYearCowCount + (cowCountView.getCurrentCount() + cowCountView.getPurchaseCount() - cowCountView.getSellCount());
            } else if (cowCountView.getCowType().equals(EnumCowType.HEIFER)) {
                endYearPregnantHeiferCount = cowCountView.getCurrentCount() + cowCountView.getPurchaseCount() - cowCountView.getSellCount();
            } else if (cowCountView.getCowType().equals(EnumCowType.STEER)) {
                endYearHeiferCount = cowCountView.getCurrentCount() + cowCountView.getPurchaseCount() - cowCountView.getSellCount();
            } else if (cowCountView.getCowType().equals(EnumCowType.CALF)) {
                endYearSteerCount = cowCountView.getCurrentCount() + cowCountView.getPurchaseCount() - cowCountView.getSellCount();
                calfCalcCount = cowCountView.getPurchaseCount() - cowCountView.getSellCount();
            }
        }
        if (birthRate != null && deathRate != null) {
            endYearCalfCount = (int) Math.round(endYearCowCount * birthRate / 100 * ((100 - deathRate) / 100) + calfCalcCount);
        } else if (birthRate != null) {
            endYearCalfCount = (int) Math.round(endYearCowCount * birthRate / 100 * ((100 - 2) / 100) + calfCalcCount);
        } else if (deathRate != null) {
            endYearCalfCount = (int) Math.round(endYearCowCount * 95 / 100 * ((100 - deathRate) / 100) + calfCalcCount);
        } else {
            endYearCalfCount = (int) Math.round(endYearCowCount * 95 / 100 * ((100 - 2) / 100) + calfCalcCount);
        }

        for (DairyCowCountView cowCountView : returnList) {
            if (cowCountView.getCowType().equals(EnumCowType.COW)) {
                cowCountView.setEndYearCount(endYearCowCount);
            } else if (cowCountView.getCowType().equals(EnumCowType.PREGNANT_HEIFER)) {
                cowCountView.setEndYearCount(endYearPregnantHeiferCount);
            } else if (cowCountView.getCowType().equals(EnumCowType.HEIFER)) {
                cowCountView.setEndYearCount(endYearHeiferCount);
            } else if (cowCountView.getCowType().equals(EnumCowType.STEER)) {
                cowCountView.setEndYearCount(endYearSteerCount);
            } else if (cowCountView.getCowType().equals(EnumCowType.CALF)) {
                cowCountView.setEndYearCount(endYearCalfCount);
            }
        }
        return returnList;
    }

    public DairyCowCountCalculateView calculateTotalRowCount(List<DairyCowCountView> dairyCowCountViewList, int barnCapacity) {
        DairyCowCountCalculateView calculateView = new DairyCowCountCalculateView();
        double totalCowCount = 0;
        double totalEndYearCowCount = 0;
        for (DairyCowCountView view : dairyCowCountViewList) {
            totalCowCount = totalCowCount + (view.getCurrentCount() * view.getCoefficientValue());
            totalEndYearCowCount = totalEndYearCowCount + (view.getEndYearCount() * view.getCoefficientValue());
        }
        calculateView.setTotalCount((int) Math.round(totalCowCount));
        calculateView.setTotalBarnUsageRate(totalCowCount / barnCapacity * 100);
        calculateView.setEndYearTotalCount((int) Math.round(totalEndYearCowCount));
        calculateView.setEndYearBarnUsageRate(totalEndYearCowCount / barnCapacity * 100);
        return calculateView;
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

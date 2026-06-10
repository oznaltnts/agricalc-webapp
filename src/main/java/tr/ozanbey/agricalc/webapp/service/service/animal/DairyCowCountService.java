package tr.ozanbey.agricalc.webapp.service.service.animal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowBarn;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowCount;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumCowType;
import tr.ozanbey.agricalc.webapp.service.repository.DairyCowCoefficientRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowBarnRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowCountRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowCountView;

import java.util.ArrayList;
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

    @Transactional
    public void saveCountList(List<DairyCowCountView> dairyCowCountViewList, DairyCowCountView selectedCountView, UserDairyCowBarn barn) {
        dairyCowCountViewList.stream().filter(v -> v.getCowType().equals(selectedCountView.getCowType())).forEach(match -> {
            match.setCountId(selectedCountView.getCountId());
            match.setCoefId(selectedCountView.getCoefId());
            match.setCowType(selectedCountView.getCowType());
            match.setCoefficientValue(selectedCountView.getCoefficientValue());
            match.setCurrentCount(selectedCountView.getCurrentCount());
            match.setPurchaseCount(selectedCountView.getPurchaseCount());
            match.setSellCount(selectedCountView.getSellCount());
            match.setEndYearCount(selectedCountView.getEndYearCount());
            match.setAverageFeedCount(selectedCountView.getAverageFeedCount());
        });
        List<DairyCowCountView> saveViewList = calculateCowCount(dairyCowCountViewList, barn.getBirthRate(), barn.getDeathRate());
        List<UserDairyCowCount> saveList = assignViewListToEntityList(saveViewList, barn.getId());
        countRepository.saveAll(saveList);
        calculateTotalRowCount(saveViewList, barn.getId());
    }

    private List<DairyCowCountView> calculateCowCount(List<DairyCowCountView> dairyCowCountViewList, Double birthRate, Double deathRate) {
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
                cowCountView.setAverageFeedCount(cowCountView.getCurrentCount() + ((double) (cowCountView.getPurchaseCount() - cowCountView.getSellCount()) / 2) + (endYearCowPregnant / 2));
            } else if (cowCountView.getCowType().equals(EnumCowType.PREGNANT_HEIFER)) {
                cowCountView.setEndYearCount(endYearPregnantHeifer);
                cowCountView.setAverageFeedCount((cowCountView.getCurrentCount() + endYearPregnantHeifer) / 2);
            } else if (cowCountView.getCowType().equals(EnumCowType.HEIFER)) {
                cowCountView.setEndYearCount(endYearHeiferSteer);
                cowCountView.setAverageFeedCount((cowCountView.getCurrentCount() + endYearHeiferSteer) / 2);
            } else if (cowCountView.getCowType().equals(EnumCowType.STEER)) {
                cowCountView.setEndYearCount(endYearSteerCalf);
                cowCountView.setAverageFeedCount((cowCountView.getCurrentCount() + endYearSteerCalf) / 2);
            } else if (cowCountView.getCowType().equals(EnumCowType.CALF)) {
                cowCountView.setEndYearCount(endYearCalf);
                cowCountView.setAverageFeedCount((cowCountView.getCurrentCount() + endYearCalf) / 2);
            }
        }
        return dairyCowCountViewList;
    }

    private List<UserDairyCowCount> assignViewListToEntityList(List<DairyCowCountView> saveViewList, Long barnId) {
        List<UserDairyCowCount> returnList = new ArrayList<>();
        for (DairyCowCountView dairyCowCountView : saveViewList) {
            UserDairyCowCount cowCount;
            if (dairyCowCountView.getCountId() != null)
                cowCount = countRepository.getReferenceById(dairyCowCountView.getCountId());
            else {
                cowCount = new UserDairyCowCount();
                cowCount.setUserDairyCowBarn(barnRepository.getReferenceById(barnId));
                cowCount.setDairyCowCoefficient(coefficientRepository.getReferenceById(dairyCowCountView.getCoefId()));
            }
            cowCount.setCurrentCount(dairyCowCountView.getCurrentCount());
            cowCount.setPurchaseCount(dairyCowCountView.getPurchaseCount());
            cowCount.setSellCount(dairyCowCountView.getSellCount());
            cowCount.setEndYearCount(dairyCowCountView.getEndYearCount());
            cowCount.setAverageFeedCount(dairyCowCountView.getAverageFeedCount());
            returnList.add(cowCount);
        }
        return returnList;
    }

    private void calculateTotalRowCount(List<DairyCowCountView> dairyCowCountViewList, Long barnId) {
        double totalCowCount = 0;
        double totalEndYearCowCount = 0;
        double totalAverageFeedCount = 0;
        for (DairyCowCountView view : dairyCowCountViewList) {
            totalCowCount = totalCowCount + (view.getCurrentCount() * view.getCoefficientValue());
            totalEndYearCowCount = totalEndYearCowCount + (view.getEndYearCount() * view.getCoefficientValue());
            totalAverageFeedCount = totalAverageFeedCount + (view.getAverageFeedCount() * view.getCoefficientValue());
        }

        saveAverageValue(barnId, totalCowCount, totalEndYearCowCount, totalAverageFeedCount);
    }

    private void saveAverageValue(Long barnId, double totalCowCount, double totalEndYearCowCount, double totalAverageFeedCount) {
        barnRepository.saveAverageValuesFromCountList(barnId, totalCowCount, totalEndYearCowCount, totalAverageFeedCount);
    }

}

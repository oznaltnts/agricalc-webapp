package tr.ozanbey.agricalc.webapp.service.service.animal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.DairyCowCost;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowCost;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.DairyCowCostRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowBarnRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowCostRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowCostView;

import java.util.List;

@Service
@Slf4j
public class DairyCowCostService {

    @Autowired
    private DairyCowCostRepository dairyCowCostRepository;

    @Autowired
    private UserDairyCowBarnRepository barnRepository;

    @Autowired
    private UserDairyCowCostRepository userDairyCowCostRepository;

    public List<DairyCowCost> getCostsByStatuses(EnumStatus[] statuses) {
        return dairyCowCostRepository.findByStatusInOrderByCostTypeAsc(statuses);
    }

    @Transactional
    public void saveCost(DairyCowCost selectedDairyCowCost) {
        dairyCowCostRepository.save(selectedDairyCowCost);
    }

    public List<DairyCowCostView> getActiveCostAsViewList(Long barnId) {
        return userDairyCowCostRepository.findAllAsCowCostView(EnumStatus.ACTIVE, barnId);
    }

    @Transactional
    public void saveUserCost(DairyCowCostView selectedCostView, Long barnId) {
        UserDairyCowCost userDairyCowCost;
        if (selectedCostView.getUserCostId() == null) {
            userDairyCowCost = new UserDairyCowCost();
        } else {
            userDairyCowCost = userDairyCowCostRepository.getReferenceById(selectedCostView.getUserCostId());
        }
        userDairyCowCost.setUserDairyCowBarn(barnRepository.getReferenceById(barnId));
        userDairyCowCost.setDairyCowCost(selectedCostView.getSelectedDairyCowCost());
        userDairyCowCost.setCostName(selectedCostView.getSelectedCostName());
        userDairyCowCost.setCount(selectedCostView.getCount());
        userDairyCowCost.setTotalCost(selectedCostView.getTotalCost());
        userDairyCowCost.setHourlyOrInterest(selectedCostView.getHourlyOrInterest());
        userDairyCowCostRepository.save(userDairyCowCost);
    }

    @Transactional
    public void removeUserCost(Long selectedUserCostId) {
        userDairyCowCostRepository.delete(userDairyCowCostRepository.getReferenceById(selectedUserCostId));
    }

}

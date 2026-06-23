package tr.ozanbey.agricalc.webapp.service.service.animal.dairycow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.DairyCowCost;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.UserDairyCowCost;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.DairyCowCostRepository;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.UserDairyCowBarnRepository;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.UserDairyCowCostRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowCostView;

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
        userDairyCowCost.setDairyCowCost(dairyCowCostRepository.getReferenceById(selectedCostView.getSelectedCostId()));
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

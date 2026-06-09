package tr.ozanbey.agricalc.webapp.service.service.animal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowCost;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.CostRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowBarnRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowCostRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowCostView;

import java.util.List;

@Service
@Slf4j
public class DairyCowCostService {

    @Autowired
    private CostRepository costRepository;

    @Autowired
    private UserDairyCowBarnRepository barnRepository;

    @Autowired
    private UserDairyCowCostRepository dairyCowCostRepository;

    public List<DairyCowCostView> getActiveCostAsViewList(Long barnId) {
        return dairyCowCostRepository.findAllAsCowCostView(EnumStatus.ACTIVE, barnId);
    }

    @Transactional
    public void saveUserCost(DairyCowCostView selectedCostView, Long barnId) {
        UserDairyCowCost userDairyCowCost;
        if (selectedCostView.getUserCostId() == null) {
            userDairyCowCost = new UserDairyCowCost();
        } else {
            userDairyCowCost = dairyCowCostRepository.getReferenceById(selectedCostView.getUserCostId());
        }
        userDairyCowCost.setUserDairyCowBarn(barnRepository.getReferenceById(barnId));
        userDairyCowCost.setCost(selectedCostView.getSelectedCost());
        userDairyCowCost.setCostName(selectedCostView.getSelectedCostName());
        userDairyCowCost.setCount(selectedCostView.getCount());
        userDairyCowCost.setTotalCost(selectedCostView.getTotalCost());
        userDairyCowCost.setHourlyOrInterest(selectedCostView.getHourlyOrInterest());
        dairyCowCostRepository.save(userDairyCowCost);
    }

    @Transactional
    public void removeUserCost(Long selectedUserCostId) {
        dairyCowCostRepository.delete(dairyCowCostRepository.getReferenceById(selectedUserCostId));

    }

}

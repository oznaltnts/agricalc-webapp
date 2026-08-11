package tr.ozanbey.agricalc.webapp.service.service.animal.dairycow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.UserDairyCowIncome;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.DairyCowIncomeRepository;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.UserDairyCowBarnRepository;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.UserDairyCowIncomeRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowIncomeView;

import java.util.List;

@Service
@Slf4j
public class DairyCowIncomeService {

    @Autowired
    private DairyCowIncomeRepository dairyCowIncomeRepository;

    @Autowired
    private UserDairyCowBarnRepository barnRepository;

    @Autowired
    private UserDairyCowIncomeRepository userDairyCowIncomeRepository;


    public List<DairyCowIncomeView> getDairyCowIncomeAsViewList(Long barnId) {
        return userDairyCowIncomeRepository.findAsViewListByBarnId(barnId);
    }

    @Transactional
    public void saveIncomeFromViewList(List<DairyCowIncomeView> dairyCowIncomeViewList, Long barnId) {
        for (DairyCowIncomeView view : dairyCowIncomeViewList) {
            UserDairyCowIncome userDairyCowIncome;
            if (view.getUserIncomeId() == null) {
                userDairyCowIncome = new UserDairyCowIncome();
            } else {
                userDairyCowIncome = userDairyCowIncomeRepository.getReferenceById(view.getUserIncomeId());
            }
            userDairyCowIncome.setUserDairyCowBarn(barnRepository.getReferenceById(barnId));
            userDairyCowIncome.setDairyCowIncome(dairyCowIncomeRepository.getReferenceById(view.getIncomeId()));
            userDairyCowIncome.setIncomeValue(view.getIncomeValue());
            userDairyCowIncomeRepository.save(userDairyCowIncome);
        }
    }

    @Transactional
    public void saveIncomeFromView(DairyCowIncomeView view, Long barnId) {
        UserDairyCowIncome userDairyCowIncome;
        if (view.getUserIncomeId() == null) {
            userDairyCowIncome = new UserDairyCowIncome();
        } else {
            userDairyCowIncome = userDairyCowIncomeRepository.getReferenceById(view.getUserIncomeId());
        }
        userDairyCowIncome.setUserDairyCowBarn(barnRepository.getReferenceById(barnId));
        userDairyCowIncome.setDairyCowIncome(dairyCowIncomeRepository.getReferenceById(view.getIncomeId()));
        userDairyCowIncome.setIncomeValue(view.getIncomeValue());
        userDairyCowIncomeRepository.save(userDairyCowIncome);
    }

}

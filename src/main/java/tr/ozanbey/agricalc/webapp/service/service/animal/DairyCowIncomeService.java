package tr.ozanbey.agricalc.webapp.service.service.animal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowIncome;
import tr.ozanbey.agricalc.webapp.service.repository.DairyCowIncomeRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowBarnRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowIncomeRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowIncomeView;

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

}

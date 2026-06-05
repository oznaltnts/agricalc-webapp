package tr.ozanbey.agricalc.webapp.service.service.animal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.User;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowBarn;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.DairyCowRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowBarnRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowBarnView;

import java.util.List;

@Service
@Slf4j
public class DairyCowBarnService {

    @Autowired
    private DairyCowRepository dairyCowRepository;

    @Autowired
    private UserDairyCowBarnRepository barnRepository;

    public List<UserDairyCowBarn> getBarnsAsViewList(Long userId) {
        return barnRepository.findByStatusInAndUser_Id(new EnumStatus[]{EnumStatus.ACTIVE, EnumStatus.PASSIVE}, userId);
    }

    @Transactional
    public void saveUserBarn(DairyCowBarnView editedDairyCowBarnView, User user) {
        UserDairyCowBarn userDairyCowBarn;
        if (editedDairyCowBarnView.getBarnId() != null)
            userDairyCowBarn = barnRepository.getReferenceById(editedDairyCowBarnView.getBarnId());
        else {
            userDairyCowBarn = new UserDairyCowBarn();
            userDairyCowBarn.setStatus(EnumStatus.ACTIVE);
            userDairyCowBarn.setUser(user);
        }
        userDairyCowBarn.setDairyCow(dairyCowRepository.getReferenceById(editedDairyCowBarnView.getSelectedDairyCowId()));
        userDairyCowBarn.setBarnCapacity(editedDairyCowBarnView.getBarnCapacity());
        userDairyCowBarn.setMilkingCapacity(editedDairyCowBarnView.getMilkingCapacity());
        userDairyCowBarn.setBirthRate(editedDairyCowBarnView.getBirthRate());
        userDairyCowBarn.setDeathRate(editedDairyCowBarnView.getDeathRate());
        userDairyCowBarn.setInseminationRate(editedDairyCowBarnView.getInseminationRate());
        userDairyCowBarn.setMilkYield(editedDairyCowBarnView.getMilkYield());
        barnRepository.save(userDairyCowBarn);
    }

}

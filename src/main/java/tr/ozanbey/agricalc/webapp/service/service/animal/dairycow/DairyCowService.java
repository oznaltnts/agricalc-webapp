package tr.ozanbey.agricalc.webapp.service.service.animal.dairycow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.DairyCow;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.DairyCowCoefficient;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.UserDairyCowBarn;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.DairyCowCoefficientRepository;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.DairyCowRepository;
import tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow.UserDairyCowBarnRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DairyCowService {

    @Autowired
    private DairyCowRepository dairyCowRepository;

    @Autowired
    private DairyCowCoefficientRepository coefficientRepository;

    @Autowired
    private UserDairyCowBarnRepository barnRepository;

    public List<DairyCow> getDairyCowsByStatuses(EnumStatus[] statuses) {
        return dairyCowRepository.findByStatusIn(statuses);
    }

    @Transactional
    public void saveDairyCow(DairyCow selectedDairyCow) {
        dairyCowRepository.save(selectedDairyCow);
    }

    public List<DairyCowCoefficient> getDairyCowCoefficients() {
        return coefficientRepository.findAll();
    }

    @Transactional
    public void saveDairyCowCoefficient(DairyCowCoefficient selectedCoefficient) {
        coefficientRepository.save(selectedCoefficient);
    }

    public Optional<UserDairyCowBarn> getUserBarnByIdAndUserId(Long barnId, Long userId) {
        return barnRepository.findByIdAndUser_Id(barnId, userId);
    }

}

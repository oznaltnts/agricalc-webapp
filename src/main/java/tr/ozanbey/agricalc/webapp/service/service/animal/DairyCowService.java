package tr.ozanbey.agricalc.webapp.service.service.animal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.DairyCow;
import tr.ozanbey.agricalc.webapp.service.domain.DairyCowCoefficient;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowBarn;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.DairyCowCoefficientRepository;
import tr.ozanbey.agricalc.webapp.service.repository.DairyCowRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserDairyCowBarnRepository;

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

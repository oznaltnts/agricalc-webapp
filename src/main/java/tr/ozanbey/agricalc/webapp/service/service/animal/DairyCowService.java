package tr.ozanbey.agricalc.webapp.service.service.animal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.*;

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
    private CostRepository costRepository;

    @Autowired
    private FeedRepository feedRepository;

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

    public List<Feed> getFeedsByStatuses(EnumStatus[] statuses) {
        return feedRepository.findByStatusInOrderByFeedCategoryAscFeedTypeAsc(statuses);
    }

    public List<Cost> getCostsByStatuses(EnumStatus[] statuses) {
        return costRepository.findByStatusInOrderByCostTypeAsc(statuses);
    }

    public Optional<UserDairyCowBarn> getUserBarnByIdAndUserId(Long barnId, Long userId) {
        return barnRepository.findByIdAndUser_Id(barnId, userId);
    }

}

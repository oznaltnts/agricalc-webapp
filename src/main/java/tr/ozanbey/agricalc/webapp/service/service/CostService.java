package tr.ozanbey.agricalc.webapp.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.Cost;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.CostRepository;

import java.util.List;

@Service
@Slf4j
public class CostService {

    @Autowired
    private CostRepository costRepository;

    public List<Cost> getCostsByStatuses(EnumStatus[] statuses) {
        return costRepository.findByStatusInOrderByCostTypeAsc(statuses);
    }

    @Transactional
    public void saveCost(Cost selectedCost) {
        costRepository.save(selectedCost);
    }

}

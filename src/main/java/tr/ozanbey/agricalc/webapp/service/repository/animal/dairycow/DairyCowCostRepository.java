package tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.DairyCowCost;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;

public interface DairyCowCostRepository extends JpaRepository<DairyCowCost, Long> {

    List<DairyCowCost> findByStatusInOrderByCostTypeAsc(EnumStatus[] statuses);

}

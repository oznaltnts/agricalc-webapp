package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.DairyCowCost;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;

public interface DairyCowCostRepository extends JpaRepository<DairyCowCost, Long> {

    List<DairyCowCost> findByStatusInOrderByCostTypeAsc(EnumStatus[] statuses);

}

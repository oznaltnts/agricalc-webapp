package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.Cost;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;

public interface CostRepository extends JpaRepository<Cost, Long> {

    List<Cost> findByStatusInOrderByCostTypeAsc(EnumStatus[] statuses);

}

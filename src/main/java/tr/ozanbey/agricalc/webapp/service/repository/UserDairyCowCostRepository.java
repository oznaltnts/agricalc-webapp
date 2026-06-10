package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowCost;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowCostView;

import java.util.List;

public interface UserDairyCowCostRepository extends JpaRepository<UserDairyCowCost, Long> {

    @Query("""
            SELECT new tr.ozanbey.agricalc.webapp.webapp.view.DairyCowCostView(t1.id, t1.dairyCowCost.id, t1.dairyCowCost, t1.costName, t1.count, t1.totalCost, t1.hourlyOrInterest)
            FROM UserDairyCowCost t1
            WHERE t1.userDairyCowBarn.id = :barnId
            AND t1.dairyCowCost.status = :status
            ORDER BY t1.dairyCowCost.costType ASC
            """)
    List<DairyCowCostView> findAllAsCowCostView(@Param("status") EnumStatus status, @Param("barnId") Long barnId);

}

package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowCount;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowCountView;

import java.util.List;


public interface UserDairyCowCountRepository extends JpaRepository<UserDairyCowCount, Long> {

    @Query("""
            SELECT new tr.ozanbey.agricalc.webapp.webapp.view.DairyCowCountView(t2.id, t1.id, t1.cowType, t1.value,
                        COALESCE(t2.currentCount, 0),
                        COALESCE(t2.purchaseCount, 0),
                        COALESCE(t2.sellCount, 0),
                        0,
                        0
                    )
            FROM DairyCowCoefficient t1
            left join UserDairyCowCount t2 on t1.id = t2.dairyCowCoefficient.id and t2.userDairyCowBarn.id = :barnId
            ORDER BY t1.cowType
            """)
    List<DairyCowCountView> findAsViewListByBarnId(@Param("barnId") Long barnId);

}

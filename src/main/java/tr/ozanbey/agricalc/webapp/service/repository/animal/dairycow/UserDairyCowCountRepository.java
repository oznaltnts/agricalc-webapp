package tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.UserDairyCowCount;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowCountView;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowResultCountView;

import java.util.List;


public interface UserDairyCowCountRepository extends JpaRepository<UserDairyCowCount, Long> {

    @Query("""
            SELECT new tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowCountView(
                        t2.id, t1.id, t1.cowType, t1.value,
                        COALESCE(t2.currentCount, 0),
                        COALESCE(t2.purchaseCount, 0),
                        COALESCE(t2.sellCount, 0),
                        COALESCE(t2.endYearCount, 0),
                        COALESCE(t2.averageFeedCount, 0)
                    )
            FROM DairyCowCoefficient t1
            LEFT JOIN UserDairyCowCount t2 ON t1.id = t2.dairyCowCoefficient.id AND t2.userDairyCowBarn.id = :barnId
            ORDER BY t1.cowType
            """)
    List<DairyCowCountView> findAsViewListByBarnId(@Param("barnId") Long barnId);

    @Query("""
            SELECT new tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowResultCountView(
                        t1.cowType, t2.endYearCount, t2.averageFeedCount,
                        cast((t2.currentCount * 100) / t2.userDairyCowBarn.barnCapacity as double),
                        cast((t2.endYearCount * 100) / t2.userDairyCowBarn.barnCapacity as double),
                        CASE WHEN t1.cowType = 0
                            THEN cast((t2.currentCount * 100) / t2.userDairyCowBarn.milkingCapacity as double)
                            ELSE NULL END,
                        CASE WHEN t1.cowType = 0
                            THEN cast((t2.endYearCount * 100) / t2.userDairyCowBarn.milkingCapacity as double)
                            ELSE NULL END
                    )
            FROM DairyCowCoefficient t1
            LEFT JOIN UserDairyCowCount t2 ON t1.id = t2.dairyCowCoefficient.id AND t2.userDairyCowBarn.id = :barnId
            """)
    List<DairyCowResultCountView> findAsResultViewListByBarnId(@Param("barnId") Long barnId);

    @EntityGraph(attributePaths = {"dairyCowCoefficient"})
    List<UserDairyCowCount> findByUserDairyCowBarn_Id(Long barnId);

}

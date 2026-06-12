package tr.ozanbey.agricalc.webapp.service.repository.animal.dairycow;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.UserDairyCowFeed;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowFeedView;
import tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowResultFeedView;

import java.util.List;

public interface UserDairyCowFeedRepository extends JpaRepository<UserDairyCowFeed, Long> {

    @Query("""
            SELECT new tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowFeedView(t1.id, t1.feed.id, t1.feed, t1.amountKg, t1.buyingDate, t1.buyingPrice)
            FROM UserDairyCowFeed t1
            WHERE t1.userDairyCowBarn.id = :barnId
            AND t1.feed.status = :status
            ORDER BY t1.feed.feedCategory ASC, t1.feed.feedType ASC
            """)
    List<DairyCowFeedView> findAllAsCowFeedView(@Param("status") EnumStatus status, @Param("barnId") Long barnId);

    @Query("""
            SELECT new tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowFeedView(t1.id, t1.feed, t1.lactation, t1.roughage)
            FROM UserDairyCowFeed t1
            WHERE t1.userDairyCowBarn.id = :barnId
            AND t1.feed.status = :status
            ORDER BY t1.feed.feedCategory ASC, t1.feed.feedType ASC
            """)
    List<DairyCowFeedView> findAllAsCowRasyonView(@Param("status") EnumStatus status, @Param("barnId") Long barnId);

    @Modifying
    @Query("""
            UPDATE UserDairyCowFeed t1
            SET t1.lactation = :lactation, t1.roughage = :roughage
            WHERE t1.id = :userFeedId
            """)
    void saveRasyonValuesFromViewList(@Param("userFeedId") Long userFeedId, @Param("lactation") Double lactation, @Param("roughage") Double roughage);

    @Query("""
            SELECT new tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow.DairyCowResultFeedView(
                        t1.feed.name,
                        cast(t1.buyingPrice as bigdecimal),
                        cast((COALESCE(t1.lactation,0) + COALESCE(t1.roughage,0)) * t1.buyingPrice as bigdecimal),
                        cast(COALESCE(t1.lactation,0) * t1.buyingPrice * t1.userDairyCowBarn.lactationPeriod as bigdecimal),
                        cast(COALESCE(t1.roughage,0) * t1.buyingPrice * (365 - t1.userDairyCowBarn.lactationPeriod) as bigdecimal),
                        t1.amountKg,
                        t1.buyingDate,
                        ROUND(((COALESCE(t1.lactation,0) * t1.userDairyCowBarn.lactationPeriod) + (COALESCE(t1.roughage,0) * (365 - t1.userDairyCowBarn.lactationPeriod))) * t1.userDairyCowBarn.averageFeedTotalCount, 10),
                        ROUND((((COALESCE(t1.lactation,0) * t1.userDairyCowBarn.lactationPeriod) + (COALESCE(t1.roughage,0) * (365 - t1.userDairyCowBarn.lactationPeriod))) * t1.userDairyCowBarn.averageFeedTotalCount) / 365, 10),
                        ROUND(t1.amountKg / ((((COALESCE(t1.lactation,0) * t1.userDairyCowBarn.lactationPeriod) + (COALESCE(t1.roughage,0) * (365 - t1.userDairyCowBarn.lactationPeriod))) * t1.userDairyCowBarn.averageFeedTotalCount) / 365), 10),
                        null,
                        null
                        )
            FROM UserDairyCowFeed t1
            WHERE t1.userDairyCowBarn.id = :barnId
            ORDER BY t1.feed.feedCategory ASC, t1.feed.feedType ASC
            """)
    List<DairyCowResultFeedView> findAsResultViewListByBarnId(@Param("barnId") Long barnId);

    @EntityGraph(attributePaths = {"userDairyCowBarn"})
    List<UserDairyCowFeed> findByUserDairyCowBarn_Id(Long barnId);

}

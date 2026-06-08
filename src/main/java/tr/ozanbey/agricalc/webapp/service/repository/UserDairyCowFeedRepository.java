package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowFeed;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowFeedView;

import java.util.List;

public interface UserDairyCowFeedRepository extends JpaRepository<UserDairyCowFeed, Long> {

    @Query("""
            SELECT new tr.ozanbey.agricalc.webapp.webapp.view.DairyCowFeedView(t1.id, t1.feed.id, t1.feed, t1.amountKg, t1.buyingDate, t1.buyingPrice)
            FROM UserDairyCowFeed t1
            WHERE t1.userDairyCowBarn.id = :barnId
            AND t1.feed.status = :status
            ORDER BY t1.feed.feedCategory ASC, t1.feed.feedType ASC
            """)
    List<DairyCowFeedView> findAllAsCowFeedView(@Param("status") EnumStatus status, @Param("barnId") Long barnId);

    @Query("""
            SELECT new tr.ozanbey.agricalc.webapp.webapp.view.DairyCowFeedView(t1.id, t1.feed, t1.buyingPrice, t1.lactation, t1.roughage)
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

}

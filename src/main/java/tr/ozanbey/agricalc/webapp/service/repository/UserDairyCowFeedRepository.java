package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowFeed;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumFeedCategory;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowFeedView;

import java.util.List;

public interface UserDairyCowFeedRepository extends JpaRepository<UserDairyCowFeed, Long> {

    @Query("""
            SELECT new tr.ozanbey.agricalc.webapp.webapp.view.DairyCowFeedView(t2, t1, t2.amountKg, t2.buyingDate, t2.buyingPrice)
            FROM Feed t1
            LEFT JOIN UserDairyCowFeed t2 ON t1.id = t2.feed.id AND t2.userDairyCow.id = :dairyCowId AND t2.userDairyCow.user.id = :userId
            WHERE t1.status = :status
            ORDER BY t1.feedCategory ASC
            """)
    List<DairyCowFeedView> findAllAsCowView(@Param("status") EnumStatus status, @Param("dairyCowId") Long dairyCowId, @Param("userId") Long userId);

    @Modifying
    void deleteByUserDairyCow_IdAndFeed_FeedCategory(Long dairyCowId, EnumFeedCategory category);

    @Modifying
    void deleteByIdNotInAndUserDairyCow_IdAndFeed_FeedCategory(List<Long> idList, Long dairyCowId, EnumFeedCategory category);

}

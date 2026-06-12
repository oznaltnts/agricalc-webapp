package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowBarn;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowBarnView;

import java.util.List;
import java.util.Optional;

public interface UserDairyCowBarnRepository extends JpaRepository<UserDairyCowBarn, Long> {

    @EntityGraph(attributePaths = {"dairyCow"})
    Optional<UserDairyCowBarn> findByIdAndUser_Id(Long id, Long userId);

    @Query("""
            SELECT new tr.ozanbey.agricalc.webapp.webapp.view.DairyCowBarnView(
                t1.id, t1.dairyCow.id, t1.dairyCow.name,
                t1.barnCapacity, t1.milkingCapacity, t1.barnPrice,
                t1.birthRate, t1.deathRate, t1.inseminationRate, t1.milkYield,
                    CASE WHEN t1.birthRate IS NULL THEN true ELSE false END,
                    CASE WHEN t1.deathRate IS NULL THEN true ELSE false END,
                    CASE WHEN t1.inseminationRate IS NULL THEN true ELSE false END,
                    CASE WHEN t1.milkYield IS NULL THEN true ELSE false END,
                t1.lactationPeriod,
                t1.totalCount, t1.endYearTotalCount, t1.averageFeedTotalCount, t1.averageMilkingCount
                    )
                    FROM UserDairyCowBarn t1
                    WHERE t1.status in :statuses
                      AND t1.user.id = :userId
            """)
    List<DairyCowBarnView> findAsViewListByStatusInAndUser_Id(@Param("statuses") EnumStatus[] statuses, @Param("userId") Long userId);

    @Modifying
    @Query("""
            UPDATE UserDairyCowBarn t1
            SET t1.totalCount = :totalCount, t1.endYearTotalCount = :endYearCount, t1.averageFeedTotalCount = :averageFeedCount, t1.averageMilkingCount = :milkingCount
            WHERE t1.id = :userBarnId
            """)
    void saveAverageValuesFromCountList(@Param("userBarnId") Long userBarnId, @Param("totalCount") double totalCount, @Param("endYearCount") Double endYearCount, @Param("averageFeedCount") Double averageFeedCount, @Param("milkingCount") Double totalAverageMilkingCount);

}

package tr.ozanbey.agricalc.webapp.service.repository.plantation;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlan;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;
import java.util.Optional;


public interface UserPlantationPlanRepository extends JpaRepository<UserPlantationPlan, Long> {

    @EntityGraph(attributePaths = {"userPlantation", "primaryProduct", "userPlantation.district.city"})
    Optional<UserPlantationPlan> findByIdAndUserPlantation_User_Id(Long id, Long userId);

    @EntityGraph(attributePaths = {"userPlantation.orchardProduct", "primaryProduct", "secondaryProduct", "lastProduct"})
    List<UserPlantationPlan> findByUserPlantation_IdAndStatusInOrderByUpdateDateDesc(Long plantationId, EnumStatus[] statuses);

    @Modifying
    @Query("UPDATE UserPlantationPlan p SET p.status = :status WHERE p.userPlantation.id = :plantationId")
    int updateStatusByUserPlantationId(@Param("plantationId") Long plantationId, @Param("status") EnumStatus status);

}

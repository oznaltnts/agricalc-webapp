package tr.ozanbey.agricalc.webapp.service.repository.plantation;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlan;

import java.util.List;
import java.util.Optional;


public interface UserPlantationPlanRepository extends JpaRepository<UserPlantationPlan, Long> {

    @EntityGraph(attributePaths = {"product"})
    List<UserPlantationPlan> findByPlantParcel_IdOrderByInsertDateDesc(Long parcelId);

    @EntityGraph(attributePaths = {"product"})
    Optional<UserPlantationPlan> findByIdAndPlantParcel_User_Id(Long id, Long userId);

}

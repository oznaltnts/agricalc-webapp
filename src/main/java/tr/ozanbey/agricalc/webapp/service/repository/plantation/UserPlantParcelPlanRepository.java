package tr.ozanbey.agricalc.webapp.service.repository.plantation;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelPlan;

import java.util.List;
import java.util.Optional;


public interface UserPlantParcelPlanRepository extends JpaRepository<UserPlantParcelPlan, Long> {

    @EntityGraph(attributePaths = {"plantParcel.product"})
    List<UserPlantParcelPlan> findByPlantParcel_IdOrderByInsertDateDesc(Long parcelId);

    @EntityGraph(attributePaths = {"plantParcel.product", "plantParcel.city"})
    Optional<UserPlantParcelPlan> findByIdAndPlantParcel_User_Id(Long id, Long userId);

}

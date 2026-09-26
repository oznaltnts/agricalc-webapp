package tr.ozanbey.agricalc.webapp.service.repository.plantation;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantation;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;
import java.util.Optional;

public interface UserPlantationRepository extends JpaRepository<UserPlantation, Long> {

    @EntityGraph(attributePaths = {"district.city", "orchardProduct"})
    Optional<UserPlantation> findByIdAndUser_Id(Long id, Long userId);

    @EntityGraph(attributePaths = {"district.city", "orchardProduct", "plantationPlanList.primaryProduct"})
    List<UserPlantation> findByUser_IdAndStatusInOrderByUpdateDateDesc(Long userId, EnumStatus[] statuses);
}

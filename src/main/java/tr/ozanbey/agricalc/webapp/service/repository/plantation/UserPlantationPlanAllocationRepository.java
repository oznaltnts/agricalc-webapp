package tr.ozanbey.agricalc.webapp.service.repository.plantation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlanAllocation;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumAllocationType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.webapp.view.plantation.PlanAllocationResultView;

import java.util.List;

public interface UserPlantationPlanAllocationRepository extends JpaRepository<UserPlantationPlanAllocation, Long> {

    void deleteByUserPlantationPlan_IdAndQuestionType(Long planId, EnumPlantationQuestionType questionType);

    @Query("""
            SELECT new tr.ozanbey.agricalc.webapp.webapp.view.plantation.PlanAllocationResultView(
            upppa.userPlantationPlan.id, upppa.questionType, upppa.allocationType, SUM(upppa.calculatedValue))
            FROM UserPlantationPlanAllocation upppa
            WHERE upppa.userPlantationPlan.id = :planId
            AND upppa.allocationType IN :allocationTypes
            GROUP BY upppa.allocationType
            """)
    List<PlanAllocationResultView> findByPlanIdGroupByAllocationType(@Param("planId") Long planId,
                                                                     @Param("allocationTypes") EnumAllocationType[] allocationTypes);

}

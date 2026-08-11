package tr.ozanbey.agricalc.webapp.service.repository.plantation;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelPlanAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;

import java.util.List;


public interface UserPlantParcelPlanAnswerRepository extends JpaRepository<UserPlantParcelPlanAnswer, Long> {

    @EntityGraph(attributePaths = {"productQuestion.plantationQuestion"})
    List<UserPlantParcelPlanAnswer> findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_IdIn(Long planId, List<Long> questionIds);

    List<UserPlantParcelPlanAnswer> findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_QuestionType(Long planId, EnumPlantationQuestionType questionType);

    void deleteByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_QuestionType(Long planId, EnumPlantationQuestionType questionType);

}

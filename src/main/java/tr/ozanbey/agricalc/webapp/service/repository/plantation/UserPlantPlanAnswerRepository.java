package tr.ozanbey.agricalc.webapp.service.repository.plantation;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantPlanAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;

import java.util.List;


public interface UserPlantPlanAnswerRepository extends JpaRepository<UserPlantPlanAnswer, Long> {

    @EntityGraph(attributePaths = {"productQuestion.plantationQuestion"})
    List<UserPlantPlanAnswer> findByPlantationPlan_IdAndProductQuestion_PlantationQuestion_IdIn(Long planId, List<Long> questionIds);

    List<UserPlantPlanAnswer> findByPlantationPlan_IdAndProductQuestion_PlantationQuestion_QuestionType(Long planId, EnumPlantationQuestionType questionType);

    void deleteByPlantationPlan_IdAndProductQuestion_PlantationQuestion_QuestionType(Long planId, EnumPlantationQuestionType questionType);

}

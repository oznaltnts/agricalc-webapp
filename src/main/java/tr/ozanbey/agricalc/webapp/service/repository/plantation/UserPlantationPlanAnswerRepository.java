package tr.ozanbey.agricalc.webapp.service.repository.plantation;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlanAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;

import java.util.List;


public interface UserPlantationPlanAnswerRepository extends JpaRepository<UserPlantationPlanAnswer, Long> {

    @EntityGraph(attributePaths = {"productQuestion.plantationQuestion"})
    List<UserPlantationPlanAnswer> findByUserPlantationPlan_IdAndProductQuestion_PlantationQuestion_IdIn(Long planId, List<Long> questionIds);

    List<UserPlantationPlanAnswer> findByUserPlantationPlan_IdAndProductQuestion_PlantationQuestion_QuestionType(Long planId, EnumPlantationQuestionType questionType);

    void deleteByUserPlantationPlan_IdAndProductQuestion_PlantationQuestion_QuestionType(Long planId, EnumPlantationQuestionType questionType);

}

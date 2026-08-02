package tr.ozanbey.agricalc.webapp.service.repository.plantation;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantPlanAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;

import java.util.List;


public interface UserPlantPlanAnswerRepository extends JpaRepository<UserPlantPlanAnswer, Long> {

    List<UserPlantPlanAnswer> findByPlantationPlan_IdAndProductQuestion_PlantationQuestion_QuestionType(Long planId, EnumPlantationQuestionType questionType);

    void deleteByPlantationPlan_IdAndProductQuestion_PlantationQuestion_QuestionType(Long planId, EnumPlantationQuestionType questionType);

}

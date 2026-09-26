package tr.ozanbey.agricalc.webapp.service.repository.plantation;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;

import java.util.List;

public interface UserPlantationAnswerRepository extends JpaRepository<UserPlantationAnswer, Long> {

    List<UserPlantationAnswer> findByUserPlantation_IdAndProductQuestion_PlantationQuestion_QuestionType(Long plantationId, EnumPlantationQuestionType questionType);

    void deleteByUserPlantation_IdAndProductQuestion_PlantationQuestion_QuestionType(Long plantationId, EnumPlantationQuestionType questionType);

}

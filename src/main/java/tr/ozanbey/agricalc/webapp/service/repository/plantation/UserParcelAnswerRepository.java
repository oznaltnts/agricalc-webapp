package tr.ozanbey.agricalc.webapp.service.repository.plantation;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserParcelAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;

import java.util.List;

public interface UserParcelAnswerRepository extends JpaRepository<UserParcelAnswer, Long> {

    List<UserParcelAnswer> findByPlantParcel_IdAndProductQuestion_PlantationQuestion_QuestionType(Long parcelId, EnumPlantationQuestionType questionType);

    void deleteByPlantParcel_IdAndProductQuestion_PlantationQuestion_QuestionType(Long parcelId, EnumPlantationQuestionType questionType);


}

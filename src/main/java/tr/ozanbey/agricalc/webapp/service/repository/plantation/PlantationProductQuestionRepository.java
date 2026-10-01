package tr.ozanbey.agricalc.webapp.service.repository.plantation;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;

import java.util.List;

public interface PlantationProductQuestionRepository extends JpaRepository<PlantationProductQuestion, Long> {

    @EntityGraph(attributePaths = {"plantationQuestion", "productQuestionDiseaseList.disease"})
    List<PlantationProductQuestion> findByPlantationProduct_IdAndPlantationQuestion_IdInAndStatus(Long productId, List<Long> questionIdList, EnumStatus status);

    @Query("""
                SELECT DISTINCT ppq
                FROM PlantationProductQuestion ppq
                JOIN FETCH ppq.plantationQuestion pq
                LEFT JOIN FETCH pq.questionOptionList
                WHERE ppq.plantationProduct.id = :productId
                AND ppq.plantationQuestion.status = :status
                AND ppq.plantationQuestion.questionType = :questionType
                ORDER BY ppq.plantationQuestion.id ASC
            """)
    List<PlantationProductQuestion> getQuestionListByProductIdAndQuestionStatusAndQuestionTypeOrderByIdAsc(@Param("productId") Long productId,
                                                                                                           @Param("status") EnumStatus status,
                                                                                                           @Param("questionType") EnumPlantationQuestionType questionType);

    @Query("""
                SELECT DISTINCT ppq
                FROM PlantationProductQuestion ppq
                LEFT JOIN UserPlantationAnswer uppa ON uppa.productQuestion.id = ppq.id AND uppa.userPlantation.id = :plantationId
                WHERE ppq.plantationProduct.id = :productId
                AND ppq.plantationQuestion.status = :status
                AND ppq.plantationQuestion.questionType = :questionType
                AND uppa.answerValue IS NULL
            """)
    List<PlantationProductQuestion> nextQuestionQuery(@Param("plantationId") Long plantationId,
                                                      @Param("productId") Long productId,
                                                      @Param("status") EnumStatus status,
                                                      @Param("questionType") EnumPlantationQuestionType questionType);
}

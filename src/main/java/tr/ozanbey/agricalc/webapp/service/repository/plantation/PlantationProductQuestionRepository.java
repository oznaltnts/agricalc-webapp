package tr.ozanbey.agricalc.webapp.service.repository.plantation;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;

import java.util.List;

public interface PlantationProductQuestionRepository extends JpaRepository<PlantationProductQuestion, Long> {

    @Query("""
                SELECT DISTINCT ppq
                FROM PlantationProductQuestion ppq
                JOIN FETCH ppq.plantationQuestion pq
                LEFT JOIN FETCH pq.questionOptionList
                WHERE ppq.plantationProduct.id = :productId
                ORDER BY ppq.plantationQuestion.id ASC
            """)
    List<PlantationProductQuestion> getQuestionListByPlantationProduct_IdOrderByIdAsc(Long productId);

}

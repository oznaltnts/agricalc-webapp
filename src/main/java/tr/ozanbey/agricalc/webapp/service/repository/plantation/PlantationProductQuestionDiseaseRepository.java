package tr.ozanbey.agricalc.webapp.service.repository.plantation;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestionDisease;
import tr.ozanbey.agricalc.webapp.webapp.view.admin.ProductQuestionDiseaseView;

import java.util.List;

public interface PlantationProductQuestionDiseaseRepository extends JpaRepository<PlantationProductQuestionDisease, Long> {

    void deleteByDisease_Id(Long diseaseId);

    @Query("""
                SELECT new tr.ozanbey.agricalc.webapp.webapp.view.admin.ProductQuestionDiseaseView(pqd, pq)
                FROM PlantationProductQuestionDisease pqd
                RIGHT JOIN pqd.productQuestion pq
                LEFT JOIN FETCH pq.plantationProduct
                WHERE pq.plantationQuestion.id = :questionId
            """)
    List<ProductQuestionDiseaseView> findByPlantationQuestionId(@Param("questionId") Long questionId);
}

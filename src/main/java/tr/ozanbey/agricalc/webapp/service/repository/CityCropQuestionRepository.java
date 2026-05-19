package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropQuestion;
import tr.ozanbey.agricalc.webapp.service.dto.QuestionWithFirstValue;

import java.util.List;

public interface CityCropQuestionRepository extends JpaRepository<CityCropQuestion, Long> {

    @Query(value = """
            SELECT p.question_id AS questionId, c.value AS value
            FROM city_crop_questions p
                     JOIN (SELECT c1.*
                           FROM city_crop_question_values c1
                                    JOIN (SELECT city_crop_question_id, MAX(idate) AS idate
                                          FROM city_crop_question_values
                                          GROUP BY city_crop_question_id) x
                                         ON c1.city_crop_question_id = x.city_crop_question_id
                                             AND c1.idate = x.idate) c ON c.city_crop_question_id = p.id
            where p.status = 1
              and p.city_crop_id = :cityCropId
            """, nativeQuery = true)
    List<QuestionWithFirstValue> findActiveDTOsByCityCropId(@Param("cityCropId") Long cityCropId);
}

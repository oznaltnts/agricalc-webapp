package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropQuestion;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;

public interface CityCropQuestionRepository extends JpaRepository<CityCropQuestion, Long> {

    @Query("SELECT ccq FROM CityCropQuestion ccq " +
            "JOIN FETCH ccq.question q " +
            "JOIN FETCH ccq.cityCropQuestionAnswerList ccqa " +
            "WHERE ccq.status = :status " +
            "AND ccq.cityCrop.id = :cityCropId " +
            "ORDER BY q.id,  ccqa.insertDate desc")
    List<CityCropQuestion> findByStatusAndCityCropIdOrderByQuestionId(EnumStatus status, Long cityCropId);

}

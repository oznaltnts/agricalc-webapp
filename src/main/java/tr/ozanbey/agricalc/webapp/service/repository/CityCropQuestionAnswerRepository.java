package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropQuestionAnswer;

import java.util.List;

public interface CityCropQuestionAnswerRepository extends JpaRepository<CityCropQuestionAnswer, Long> {

    List<CityCropQuestionAnswer> findByCityCropQuestionId(Long cityCropQuestionId);
}

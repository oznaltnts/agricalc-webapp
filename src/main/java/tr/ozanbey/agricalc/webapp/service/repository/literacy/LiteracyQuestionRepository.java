package tr.ozanbey.agricalc.webapp.service.repository.literacy;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.literacy.LiteracyQuestion;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;

public interface LiteracyQuestionRepository extends JpaRepository<LiteracyQuestion, Long> {

    @EntityGraph(attributePaths = {"questionOptionList"})
    List<LiteracyQuestion> findByStatus(EnumStatus status);
}

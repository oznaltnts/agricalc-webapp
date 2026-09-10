package tr.ozanbey.agricalc.webapp.service.repository.literacy;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.literacy.UserLiteracyAssessment;

public interface UserLiteracyAssessmentRepository extends JpaRepository<UserLiteracyAssessment, Long> {
}

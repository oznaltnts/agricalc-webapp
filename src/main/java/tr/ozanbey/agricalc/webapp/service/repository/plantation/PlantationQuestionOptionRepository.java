package tr.ozanbey.agricalc.webapp.service.repository.plantation;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationQuestionOption;

public interface PlantationQuestionOptionRepository extends JpaRepository<PlantationQuestionOption, Long> {
}

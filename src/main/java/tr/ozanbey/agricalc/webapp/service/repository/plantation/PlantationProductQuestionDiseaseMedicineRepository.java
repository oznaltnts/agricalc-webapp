package tr.ozanbey.agricalc.webapp.service.repository.plantation;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestionDiseaseMedicine;

public interface PlantationProductQuestionDiseaseMedicineRepository extends JpaRepository<PlantationProductQuestionDiseaseMedicine, Long> {
}

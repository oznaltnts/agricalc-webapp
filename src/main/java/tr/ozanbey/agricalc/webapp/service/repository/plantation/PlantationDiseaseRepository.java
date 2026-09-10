package tr.ozanbey.agricalc.webapp.service.repository.plantation;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationDisease;

public interface PlantationDiseaseRepository extends JpaRepository<PlantationDisease, Long> {
}

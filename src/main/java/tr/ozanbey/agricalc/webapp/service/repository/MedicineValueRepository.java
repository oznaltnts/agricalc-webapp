package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.MedicineValue;

public interface MedicineValueRepository extends JpaRepository<MedicineValue, Long> {

}

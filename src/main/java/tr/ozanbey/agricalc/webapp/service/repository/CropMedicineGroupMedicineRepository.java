package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.CropMedicineGroupMedicine;

public interface CropMedicineGroupMedicineRepository extends JpaRepository<CropMedicineGroupMedicine, Long> {

}

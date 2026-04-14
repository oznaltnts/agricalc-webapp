package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.CropGroup;

public interface CropGroupRepository extends JpaRepository<CropGroup, Long> {
}

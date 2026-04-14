package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.Crop;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;

public interface CropRepository extends JpaRepository<Crop, Long> {

    List<Crop> findByStatus(EnumStatus status);
}

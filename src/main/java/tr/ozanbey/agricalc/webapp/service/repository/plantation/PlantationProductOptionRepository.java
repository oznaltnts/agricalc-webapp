package tr.ozanbey.agricalc.webapp.service.repository.plantation;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductOption;

import java.util.List;


public interface PlantationProductOptionRepository extends JpaRepository<PlantationProductOption, Long> {

    List<PlantationProductOption> findByPlantationProductIdOrderByNameAsc(Long productId);

}

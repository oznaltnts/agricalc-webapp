package tr.ozanbey.agricalc.webapp.service.repository.plantation;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationIrrigationValue;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumIrrigationType;

import java.util.List;

public interface PlantationIrrigationValueRepository extends JpaRepository<PlantationIrrigationValue, Long> {

    List<PlantationIrrigationValue> findByIrrigationTypeIn(EnumIrrigationType[] types);

}

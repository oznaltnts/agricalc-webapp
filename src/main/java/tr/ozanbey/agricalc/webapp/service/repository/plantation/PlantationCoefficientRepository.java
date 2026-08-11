package tr.ozanbey.agricalc.webapp.service.repository.plantation;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationCoefficient;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumCoefficientType;

import java.util.List;

public interface PlantationCoefficientRepository extends JpaRepository<PlantationCoefficient, Long> {

    List<PlantationCoefficient> findByEnumCoefficientTypeIn(EnumCoefficientType[] types);

}

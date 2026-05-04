package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.CropCoefficient;
import tr.ozanbey.agricalc.webapp.service.domain.GeneralCoefficient;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumCropCoefficientType;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumCropCoefficientValue;

import java.util.List;

public interface GeneralCoefficientRepository extends JpaRepository<GeneralCoefficient, Long> {

}

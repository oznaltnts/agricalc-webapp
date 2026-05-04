package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.GeneralCoefficient;
import tr.ozanbey.agricalc.webapp.service.domain.GeneralCoefficientValue;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

public interface GeneralCoefficientValueRepository extends JpaRepository<GeneralCoefficientValue, Long> {

    GeneralCoefficientValue findFirstByGeneralCoefficientStatusAndGeneralCoefficientNameOrderByInsertDateDesc(EnumStatus status, String name);
}

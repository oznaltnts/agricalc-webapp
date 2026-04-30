package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.CityDieselDistanceValue;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumDieselDistanceType;

public interface CityDieselDistanceValueRepository extends JpaRepository<CityDieselDistanceValue, Long> {

}

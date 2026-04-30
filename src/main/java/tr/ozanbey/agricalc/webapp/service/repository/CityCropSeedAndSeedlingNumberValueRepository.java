package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropSeedAndSeedlingNumberValue;
import tr.ozanbey.agricalc.webapp.service.domain.CityDieselDistanceValue;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumSeedAndSeedlingNumberType;

public interface CityCropSeedAndSeedlingNumberValueRepository extends JpaRepository<CityCropSeedAndSeedlingNumberValue, Long> {

}

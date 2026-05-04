package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropWateringValue;

import java.util.Optional;


public interface CityCropWateringValueRepository extends JpaRepository<CityCropWateringValue, Long> {

    Optional<CityCropWateringValue> findFirstByCityCropIdOrderByInsertDateDesc(Long cityCropId);
}

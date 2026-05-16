package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropFieldHarvestAverageValue;

import java.util.Optional;


public interface CityCropFieldHarvestAverageValueRepository extends JpaRepository<CityCropFieldHarvestAverageValue, Long> {

    @Query("SELECT AVG(fha.collectedCropCount) " +
            "FROM CityCropFieldHarvestAverageValue fha " +
            "WHERE fha.cityCrop.id = ?1 ")
    Optional<Double> findAverageValueByCityCropId(Long cityCropId);
}

package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.CityDistrict;

import java.util.List;

public interface CityDistrictRepository extends JpaRepository<CityDistrict, Long> {

    List<CityDistrict> findByCity_Id(Long cityId);
}

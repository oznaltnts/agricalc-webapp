package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.CityDieselDistance;
import tr.ozanbey.agricalc.webapp.service.dto.DieselDistanceWithFirstValue;

import java.util.List;

public interface CityDieselDistanceRepository extends JpaRepository<CityDieselDistance, Long> {

    @Query(value = """
            SELECT p.type AS enumType, c.value AS value
            FROM city_diesel_distances p
                     JOIN (SELECT c1.*
                           FROM city_diesel_distance_values c1
                                    JOIN (SELECT city_diesel_distance_id, MAX(idate) AS idate
                                          FROM city_diesel_distance_values
                                          GROUP BY city_diesel_distance_id) x
                                         ON c1.city_diesel_distance_id = x.city_diesel_distance_id
                                             AND c1.idate = x.idate) c ON c.city_diesel_distance_id = p.id
            where p.city_id = :cityCropId
            """, nativeQuery = true)
    List<DieselDistanceWithFirstValue> findDTOsByCityId(@Param("cityCropId") Long cityCropId);

}

package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.CityFertilizer;
import tr.ozanbey.agricalc.webapp.service.dto.CityFertilizerWithFirstValue;

import java.util.List;

public interface CityFertilizerRepository extends JpaRepository<CityFertilizer, Long> {

    @Query(value = """
            SELECT f.id                AS fertilizerId,
                   f.old_fertilizer_id AS oldFertilizerId,
                   f.type              AS enumType,
                   f.name              AS name,
                   f.nitrogen_percent  AS nitrogenPercent,
                   f.phosphor_percent  AS phosphorPercent,
                   f.potassium_percent AS potassiumPercent,
                   v.price             AS price
            FROM city_fertilizers cf
                     INNER JOIN fertilizers f
                                ON f.id = cf.fertilizer_id
                     INNER JOIN (SELECT v1.*
                                 FROM city_fertilizer_values v1
                                          INNER JOIN (SELECT city_fertilizer_id, MAX(idate) AS max_idate
                                                      FROM city_fertilizer_values
                                                      GROUP BY city_fertilizer_id) x
                                                     ON v1.city_fertilizer_id = x.city_fertilizer_id
                                                         AND v1.idate = x.max_idate) v
                                ON v.city_fertilizer_id = cf.id
            WHERE f.status = ?1
              AND cf.status = ?1
              AND cf.city_id = :cityCropId
            """, nativeQuery = true)
    List<CityFertilizerWithFirstValue> findDTOsByStatusAndCityId(Integer status, @Param("cityCropId") Long cityCropId);
}

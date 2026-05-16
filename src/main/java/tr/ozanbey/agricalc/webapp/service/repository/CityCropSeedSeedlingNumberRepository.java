package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropSeedSeedlingNumber;
import tr.ozanbey.agricalc.webapp.service.dto.SeedSeedlingWithFirstValue;

import java.util.List;

public interface CityCropSeedSeedlingNumberRepository extends JpaRepository<CityCropSeedSeedlingNumber, Long> {

    @Query(value = """
            SELECT p.type  as enumType,
                   c.value as value
            FROM city_crop_seed_and_seedling_numbers p
                     JOIN (SELECT c1.*
                           FROM city_crop_seed_and_seedling_number_values c1
                                    JOIN (SELECT city_crop_seed_and_seedling_number_id, MAX(idate) AS idate
                                          FROM city_crop_seed_and_seedling_number_values
                                          GROUP BY city_crop_seed_and_seedling_number_id) x
                                         ON c1.city_crop_seed_and_seedling_number_id = x.city_crop_seed_and_seedling_number_id
                                             AND c1.idate = x.idate) c ON c.city_crop_seed_and_seedling_number_id = p.id
            WHERE p.status = ?1
              AND p.city_crop_id = :cityCropId
            """, nativeQuery = true)
    List<SeedSeedlingWithFirstValue> findDTOsByStatusAndCityCropId(Integer status, @Param("cityCropId") Long cityCropId);
}

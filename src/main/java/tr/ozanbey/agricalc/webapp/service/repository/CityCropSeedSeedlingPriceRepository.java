package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.CityCropSeedSeedlingPrice;
import tr.ozanbey.agricalc.webapp.service.dto.SeedSeedlingWithFirstValue;

import java.util.List;

public interface CityCropSeedSeedlingPriceRepository extends JpaRepository<CityCropSeedSeedlingPrice, Long> {

    @Query(value = """
            SELECT p.type  as enumType,
                   c.value as value
            FROM city_crop_seed_and_seedling_prices p
                     JOIN (SELECT c1.*
                           FROM city_crop_seed_and_seedling_price_values c1
                                    JOIN (SELECT city_crop_seed_and_seedling_price_id, MAX(idate) AS idate
                                          FROM city_crop_seed_and_seedling_price_values
                                          GROUP BY city_crop_seed_and_seedling_price_id) x
                                         ON c1.city_crop_seed_and_seedling_price_id = x.city_crop_seed_and_seedling_price_id
                                             AND c1.idate = x.idate) c ON c.city_crop_seed_and_seedling_price_id = p.id
            WHERE p.status = ?1
              AND p.city_crop_id = :cityCropId
            """, nativeQuery = true)
    List<SeedSeedlingWithFirstValue> findDTOsByStatusAndCityCropId(Integer status, @Param("cityCropId") Long cityCropId);
}

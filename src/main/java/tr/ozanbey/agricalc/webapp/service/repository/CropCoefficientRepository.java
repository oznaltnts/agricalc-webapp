package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.CropCoefficient;
import tr.ozanbey.agricalc.webapp.service.dto.CropCoefficientWithFirstValue;
import tr.ozanbey.agricalc.webapp.service.dto.QuestionWithFirstAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumCropCoefficientType;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumCropCoefficientValue;

import java.util.List;

public interface CropCoefficientRepository extends JpaRepository<CropCoefficient, Long> {

    @Query(value = """
            SELECT q.value as enumValue,
                   q.type  as enumType,
                   c.value as value
            FROM agricalc.crop_coefficients p
                     INNER JOIN agricalc.coefficients q
                                ON q.id = p.coefficient_id
                     INNER JOIN (SELECT c1.*
                                 FROM agricalc.crop_coefficient_answers c1
                                          INNER JOIN (SELECT crop_coefficient_id, MAX(idate) AS idate
                                                      FROM agricalc.crop_coefficient_answers
                                                      GROUP BY crop_coefficient_id) x
                                                     ON c1.crop_coefficient_id = x.crop_coefficient_id
                                                         AND c1.idate = x.idate) c
                                ON c.crop_coefficient_id = p.id
            WHERE p.status = ?1
              AND p.crop_id = :cropId
            """, nativeQuery = true)
    List<CropCoefficientWithFirstValue> findDTOsByStatusAndCropId(Integer status, @Param("cropId") Long cropId);

}

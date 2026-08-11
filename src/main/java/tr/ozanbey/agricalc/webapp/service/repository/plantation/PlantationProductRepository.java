package tr.ozanbey.agricalc.webapp.service.repository.plantation;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProduct;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;

public interface PlantationProductRepository extends JpaRepository<PlantationProduct, Long> {

    @EntityGraph(attributePaths = {"productOptionList"})
    List<PlantationProduct> findByStatusOrderByNameAsc(EnumStatus status);

}

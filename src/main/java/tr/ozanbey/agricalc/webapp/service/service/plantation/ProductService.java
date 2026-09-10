package tr.ozanbey.agricalc.webapp.service.service.plantation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProduct;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductOption;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.PlantationProductOptionRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.PlantationProductRepository;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private PlantationProductRepository productRepository;

    @Autowired
    private PlantationProductOptionRepository productOptionRepository;

    public List<PlantationProduct> getActiveProducts(EnumStatus status) {
        return productRepository.findByStatusOrderByNameAsc(status);
    }

    public List<PlantationProductOption> getProductOption(Long productId) {
        return productOptionRepository.findByPlantationProductIdOrderByNameAsc(productId);
    }

}

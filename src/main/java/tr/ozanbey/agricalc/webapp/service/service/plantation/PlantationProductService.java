package tr.ozanbey.agricalc.webapp.service.service.plantation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProduct;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.PlantationProductQuestionRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.PlantationProductRepository;

import java.util.List;

@Service
@Slf4j
public class PlantationProductService {

    @Autowired
    private PlantationProductRepository productRepository;

    @Autowired
    private PlantationProductQuestionRepository productQuestionRepository;

    public List<PlantationProduct> getActiveProducts(EnumStatus status) {
        return productRepository.findByStatusOrderByNameAsc(status);
    }

    public List<PlantationProductQuestion> getActiveQuestionByProduct(Long productId) {
        return productQuestionRepository.getQuestionListByPlantationProduct_IdOrderByIdAsc(productId);
    }

}

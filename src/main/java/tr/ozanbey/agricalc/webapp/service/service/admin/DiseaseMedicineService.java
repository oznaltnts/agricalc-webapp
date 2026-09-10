package tr.ozanbey.agricalc.webapp.service.service.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationDisease;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestionDisease;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumDiseaseType;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.PlantationDiseaseRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.PlantationProductQuestionDiseaseRepository;
import tr.ozanbey.agricalc.webapp.webapp.view.admin.DiseaseView;
import tr.ozanbey.agricalc.webapp.webapp.view.admin.ProductQuestionDiseaseView;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DiseaseMedicineService {

    @Autowired
    private PlantationDiseaseRepository diseaseRepository;
    @Autowired
    private PlantationProductQuestionDiseaseRepository productDiseaseRepository;

    public List<DiseaseView> getAllAsDiseaseViewList() {
        return convertDiseaseEntityToView(diseaseRepository.findAll());
    }

    private List<DiseaseView> convertDiseaseEntityToView(List<PlantationDisease> diseaseList) {
        List<DiseaseView> returnList = new ArrayList<>();
        for (PlantationDisease disease : diseaseList) {
            returnList.add(new DiseaseView(disease));
        }
        return returnList;
    }

    @Transactional
    public void saveSelectedDiseaseView(Long diseaseId,
                                        EnumStatus selectedStatus, EnumDiseaseType selectedDiseaseType, String inputName,
                                        List<ProductQuestionDiseaseView> productQuestionDiseaseViewList) {
        PlantationDisease plantationDisease;
        if (diseaseId == null) {
            plantationDisease = new PlantationDisease();
        } else {
            plantationDisease = diseaseRepository.getReferenceById(diseaseId);
        }
        plantationDisease.setStatus(selectedStatus);
        plantationDisease.setType(selectedDiseaseType);
        plantationDisease.setName(inputName.trim());
        plantationDisease = diseaseRepository.save(plantationDisease);

        productDiseaseRepository.deleteByDisease_Id(plantationDisease.getId());
        productDiseaseRepository.flush();

        List<PlantationProductQuestionDisease> saveList = new ArrayList<>();
        for (ProductQuestionDiseaseView view : productQuestionDiseaseViewList) {
            PlantationProductQuestionDisease productDisease = new PlantationProductQuestionDisease();
            productDisease.setProductQuestion(view.getProductQuestionDisease().getProductQuestion());
            productDisease.setDisease(plantationDisease);
            productDisease.setUsageCoefficient(1d);
            productDisease.setAdhesivePrice(view.getInputPrice());
            productDisease.setWaterAmount(view.getInputAmount());
            saveList.add(productDisease);
        }
        productDiseaseRepository.saveAll(saveList);
    }

    public List<ProductQuestionDiseaseView> getProductListByQuestionId(EnumDiseaseType selectedType) {
        return productDiseaseRepository.findByPlantationQuestionId(selectedType.getQuestionId());
    }

}

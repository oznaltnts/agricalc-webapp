package tr.ozanbey.agricalc.webapp.webapp.view.admin;

import lombok.Getter;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationDisease;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumDiseaseType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class DiseaseView implements Serializable {

    private Long diseaseId;
    private PlantationDisease disease;
    private LocalDateTime lastUpdateTime;

    private EnumStatus selectedStatus;
    private EnumDiseaseType selectedDiseaseType;
    private String selectedDiseaseName;

    private List<ProductQuestionDiseaseView> selectedProductQuestionDiseaseViewList;
    private List<ProductQuestionDiseaseView> productQuestionDiseaseViewList;

    public DiseaseView(PlantationDisease disease) {
        this.diseaseId = disease.getId();
        this.disease = disease;
        this.lastUpdateTime = disease.getUpdateDate() != null ? disease.getUpdateDate() : disease.getInsertDate() != null ? disease.getInsertDate() : LocalDateTime.now();
        this.selectedStatus = disease.getStatus();
        this.selectedDiseaseType = disease.getType();
        this.selectedDiseaseName = disease.getName();
    }
}

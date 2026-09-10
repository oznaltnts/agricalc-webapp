package tr.ozanbey.agricalc.webapp.webapp.view.admin;

import lombok.Getter;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestionDisease;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductQuestionDiseaseView implements Serializable {

    private Long productQuestionDiseaseId;
    private PlantationProductQuestionDisease productQuestionDisease;

    private BigDecimal inputPrice;
    private Double inputAmount;

    public ProductQuestionDiseaseView(PlantationProductQuestionDisease productQuestionDisease, PlantationProductQuestion productQuestion) {
        if (productQuestionDisease == null) productQuestionDisease = new PlantationProductQuestionDisease();
        this.productQuestionDisease = productQuestionDisease;
        this.productQuestionDiseaseId = productQuestionDisease.getId();
        this.productQuestionDisease.setProductQuestion(productQuestion);
        this.inputPrice = productQuestionDisease.getAdhesivePrice();
        this.inputAmount = productQuestionDisease.getWaterAmount();
    }

}

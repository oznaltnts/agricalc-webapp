package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlan;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationPlanService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.io.IOException;

@Component
@ViewScoped
@Getter
@Setter
public class ExpenseProfileController extends BaseController {

    @Autowired
    private PlantationPlanService plantationPlanService;

    public void saveAndNext(UserPlantationPlan plantationPlan, EnumPlantationQuestionType questionType) throws IOException {
        plantationPlanService.savePlanAnswers(plantationPlan, questionType);
        //TODO sonraki sayfada soru yoksa bir sonrakini kontrol et
        super.navigationController.redirectToUrl("/secured/plantation/expense-soil-profile?parcelPlanId=" + plantationPlan.getId());
    }

}

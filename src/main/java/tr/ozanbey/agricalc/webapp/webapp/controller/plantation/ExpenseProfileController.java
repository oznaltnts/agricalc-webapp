package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelPlan;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantParcelPlanService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.io.IOException;

@Component
@ViewScoped
@Getter
@Setter
public class ExpenseProfileController extends BaseController {

    @Autowired
    private PlantParcelPlanService plantParcelPlanService;

    public void saveAndNext(UserPlantParcelPlan parcelPlan, EnumPlantationQuestionType questionType) throws IOException {
        plantParcelPlanService.savePlanAnswers(parcelPlan, questionType);
        //TODO sonraki sayfada soru yoksa bir sonrakini kontrol et
        super.navigationController.redirectToUrl("/secured/plantation/expense-soil-profile?parcelPlanId=" + parcelPlan.getId());
    }

}

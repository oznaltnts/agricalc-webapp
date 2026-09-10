package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelPlan;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionAnswerType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionRecordType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.QuestionService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantParcelPlanService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Component
@ViewScoped
public class PlanProfileController extends BaseController {

    @Autowired
    private UserPlantParcelPlanService userPlantParcelPlanService;

    private Long parcelPlanId;
    @Setter
    private UserPlantParcelPlan parcelPlan;

    public void setParcelPlanId(Long parcelPlanId) {
        if (Objects.equals(this.parcelPlanId, parcelPlanId)) {
            return;
        }
        this.parcelPlanId = parcelPlanId;
    }

    protected boolean checkPlanIdForUser(Long parcelPlanId) {
        Optional<UserPlantParcelPlan> optionalPlan = userPlantParcelPlanService.getPlantPlanByIdAndUserId(parcelPlanId, getCurrentUser().getUser().getId());
        if (optionalPlan.isPresent()) {
            parcelPlan = optionalPlan.get();
            return true;
        }
        return false;
    }

    public boolean booleanCheckboxRenderer(PlantationProductQuestion question) {
        return question.getPlantationQuestion().getRecordType().equals(EnumQuestionRecordType.ASK_USER);
    }

    protected void fillAnsweredQuestionValues(Long questionId, String answerValue, List<PlantationProductQuestion> productQuestionList, boolean dontAskValue) {
        for (PlantationProductQuestion productQuestion : productQuestionList) {
            if (Objects.equals(questionId, productQuestion.getId())) {
                productQuestion.setDontAskAgain(dontAskValue);
                if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_MENU)) {
                    productQuestion.setSelectedAnswerId(Long.valueOf(answerValue));
                } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_RADIO)) {
                    productQuestion.setSelectedAnswerId(Long.valueOf(answerValue));
                } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_MANY_CHECKBOX)) {
                    if (productQuestion.getSelectedAnswerIds() == null || productQuestion.getSelectedAnswerIds().isEmpty()) {
                        productQuestion.setSelectedAnswerIds(new ArrayList<>());
                    }
                    productQuestion.getSelectedAnswerIds().add(Long.valueOf(answerValue));
                } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_INTEGER)) {
                    productQuestion.setIntegerValue(Integer.valueOf(answerValue));
                } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DOUBLE)) {
                    productQuestion.setDoubleValue(Double.valueOf(answerValue));
                } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DECIMAL)) {
                    productQuestion.setBigDecimalValue(new BigDecimal(answerValue));
                }
            }
        }
    }

    @Autowired
    private QuestionService questionService;

    protected void goToNextPage(EnumPlantationQuestionType referenceType) throws IOException {
        userPlantParcelPlanService.savePlanAnswers(parcelPlan, referenceType);
        userPlantParcelPlanService.updatePlantParcelPlan(parcelPlan);
        for (int i = 1; i < 13 - referenceType.getValue(); i++) {
            int checkTypeValue = referenceType.getValue() + i;
            if (navigateNewPage(checkTypeValue)) return;
        }
        super.navigationController.redirectToUrl("/secured/plantation/plan-result?parcelPlanId=" + parcelPlanId);
    }

    protected void goToPreviousPage(EnumPlantationQuestionType referenceType) throws IOException {
        userPlantParcelPlanService.savePlanAnswers(parcelPlan, referenceType);
        for (int i = 1; i < referenceType.getValue() + 1; i++) {
            int checkTypeValue = referenceType.getValue() - i;
            if (navigateNewPage(checkTypeValue)) return;
        }
        super.navigationController.redirectToUrl("/secured/plantation/parcel-plan?parcelId=" + parcelPlan.getPlantParcel().getId());
    }

    protected void goToPreviousPageFromResult() throws IOException {
        for (int i = 1; i < 14; i++) {
            int checkTypeValue = 13 - i;
            if (navigateNewPage(checkTypeValue)) return;
        }
        super.navigationController.redirectToUrl("/secured/plantation/parcel-plan?parcelId=" + parcelPlan.getPlantParcel().getId());
    }

    private boolean navigateNewPage(int checkTypeValue) throws IOException {
        List<PlantationProductQuestion> productQuestionList = questionService.checkIsThereQuestionToAsk(parcelPlan.getPlantParcel().getId(),
                parcelPlan.getPlantParcel().getProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.fromValue(checkTypeValue));
        if (!productQuestionList.isEmpty()) {
            if (checkTypeValue == 0) {
                super.navigationController.redirectToUrl("/secured/plantation/income-profile?parcelPlanId=" + parcelPlanId);
            } else if (checkTypeValue == 1) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-soil-profile?parcelPlanId=" + parcelPlanId);
            } else if (checkTypeValue == 2) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-planting-profile?parcelPlanId=" + parcelPlanId);
            } else if (checkTypeValue == 3) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-fertilizer-profile?parcelPlanId=" + parcelPlanId);
            } else if (checkTypeValue == 4) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-weed-profile?parcelPlanId=" + parcelPlanId);
            } else if (checkTypeValue == 5) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-irrigation-profile?parcelPlanId=" + parcelPlanId);
            } else if (checkTypeValue == 6) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-cultural-profile?parcelPlanId=" + parcelPlanId);
            } else if (checkTypeValue == 7) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-protection-profile?parcelPlanId=" + parcelPlanId);
            } else if (checkTypeValue == 8) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-harvest-profile?parcelPlanId=" + parcelPlanId);
            } else if (checkTypeValue == 9) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-blend-profile?parcelPlanId=" + parcelPlanId);
            } else if (checkTypeValue == 10) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-drying-profile?parcelPlanId=" + parcelPlanId);
            } else if (checkTypeValue == 11) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-baling-profile?parcelPlanId=" + parcelPlanId);
            } else if (checkTypeValue == 12) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-packaging-profile?parcelPlanId=" + parcelPlanId);
            }
            return true;
        }
        return false;
    }

}

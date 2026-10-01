package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationAnswer;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlan;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlanAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionAnswerType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionRecordType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.CostAllocationService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.CostCalculationService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.QuestionService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantationPlanService;
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
public abstract class PlanProfileController extends BaseController {

    private final UserPlantationPlanService userPlantationPlanService;
    private final QuestionService questionService;
    private final CostCalculationService costCalculationService;
    private final CostAllocationService costAllocationService;

    public PlanProfileController(UserPlantationPlanService userPlantationPlanService,
                                 QuestionService questionService,
                                 CostCalculationService costCalculationService,
                                 CostAllocationService costAllocationService) {
        this.userPlantationPlanService = userPlantationPlanService;
        this.questionService = questionService;
        this.costCalculationService = costCalculationService;
        this.costAllocationService = costAllocationService;
    }

    @Setter
    private UserPlantationPlan userPlantationPlan;
    private Long plantationPlanId;

    public void setPlantationPlanId(Long plantationPlanId) {
        if (Objects.equals(this.plantationPlanId, plantationPlanId)) {
            return;
        }
        this.plantationPlanId = plantationPlanId;
    }

    protected boolean checkPlanIdForUser(Long plantationPlanId) {
        if (plantationPlanId == null)
            return false;
        Optional<UserPlantationPlan> optionalPlan = userPlantationPlanService.getPlantationPlanByIdAndUserId(plantationPlanId, getCurrentUser().getUser().getId());
        if (optionalPlan.isPresent()) {
            userPlantationPlan = optionalPlan.get();
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

    protected void goToNextPage(EnumPlantationQuestionType referenceType) throws IOException {
        userPlantationPlanService.savePlanAnswers(userPlantationPlan, referenceType);
        userPlantationPlanService.updatePlantationPlan(userPlantationPlan);
        for (int i = 1; i < 13 - referenceType.getValue(); i++) {
            int checkTypeValue = referenceType.getValue() + i;
            if (navigateNewPage(checkTypeValue)) return;
        }
        userPlantationPlan.setStatus(EnumStatus.PASSIVE);
        userPlantationPlanService.updatePlantationPlan(userPlantationPlan);
        super.navigationController.redirectToUrl("/secured/plantation/plan-result?plantationPlanId=" + plantationPlanId);
    }

    protected void goToPreviousPage(EnumPlantationQuestionType referenceType) throws IOException {
        userPlantationPlanService.savePlanAnswers(userPlantationPlan, referenceType);
        for (int i = 1; i < referenceType.getValue() + 1; i++) {
            int checkTypeValue = referenceType.getValue() - i;
            if (navigateNewPage(checkTypeValue)) return;
        }
        super.navigationController.redirectToUrl("/secured/plantation-plan-list?userPlantationId=" + userPlantationPlan.getUserPlantation().getId());
    }

    protected void goToPreviousPageFromResult() throws IOException {
        for (int i = 1; i < 14; i++) {
            int checkTypeValue = 13 - i;
            if (navigateNewPage(checkTypeValue)) return;
        }
        super.navigationController.redirectToUrl("/secured/plantation-plan-list?userPlantationId=" + userPlantationPlan.getUserPlantation().getId());
    }

    private boolean navigateNewPage(int checkTypeValue) throws IOException {
        List<PlantationProductQuestion> productQuestionList = questionService.checkIsThereQuestionToAsk(userPlantationPlan.getUserPlantation().getId(),
                userPlantationPlan.getPrimaryProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.fromValue(checkTypeValue));
        if (!productQuestionList.isEmpty()) {
            if (checkTypeValue == 0) {
                super.navigationController.redirectToUrl("/secured/plantation/income-profile?plantationPlanId=" + plantationPlanId);
            } else if (checkTypeValue == 1) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-soil-profile?plantationPlanId=" + plantationPlanId);
            } else if (checkTypeValue == 2) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-planting-profile?plantationPlanId=" + plantationPlanId);
            } else if (checkTypeValue == 3) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-fertilizer-profile?plantationPlanId=" + plantationPlanId);
            } else if (checkTypeValue == 4) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-weed-profile?plantationPlanId=" + plantationPlanId);
            } else if (checkTypeValue == 5) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-irrigation-profile?plantationPlanId=" + plantationPlanId);
            } else if (checkTypeValue == 6) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-cultural-profile?plantationPlanId=" + plantationPlanId);
            } else if (checkTypeValue == 7) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-protection-profile?plantationPlanId=" + plantationPlanId);
            } else if (checkTypeValue == 8) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-harvest-profile?plantationPlanId=" + plantationPlanId);
            } else if (checkTypeValue == 9) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-blend-profile?plantationPlanId=" + plantationPlanId);
            } else if (checkTypeValue == 10) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-drying-profile?plantationPlanId=" + plantationPlanId);
            } else if (checkTypeValue == 11) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-baling-profile?plantationPlanId=" + plantationPlanId);
            } else if (checkTypeValue == 12) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-packaging-profile?plantationPlanId=" + plantationPlanId);
            }
            return true;
        }
        return false;
    }

    protected void assignAnswerListsToProduct(List<UserPlantationPlanAnswer> planAnswerList, List<PlantationProductQuestion> productQuestionList, List<UserPlantationAnswer> plantationAnswerList) {
        for (UserPlantationPlanAnswer userPlantationPlanAnswer : planAnswerList) {
            fillAnsweredQuestionValues(userPlantationPlanAnswer.getProductQuestion().getId(), userPlantationPlanAnswer.getAnswerValue(), productQuestionList, false);
        }
        for (UserPlantationAnswer userPlantationAnswer : plantationAnswerList) {
            fillAnsweredQuestionValues(userPlantationAnswer.getProductQuestion().getId(), userPlantationAnswer.getAnswerValue(), productQuestionList, true);
        }
        userPlantationPlan.getPrimaryProduct().setProductQuestionList(productQuestionList);
    }

}

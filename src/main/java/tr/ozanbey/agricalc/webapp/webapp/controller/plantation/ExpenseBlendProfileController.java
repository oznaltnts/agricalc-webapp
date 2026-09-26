package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationAnswer;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlanAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionAnswerType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.CostAllocationService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.CostCalculationService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.QuestionService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantationPlanService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class ExpenseBlendProfileController extends PlanProfileController {

    private static final List<Long> A_BLEND_TYPE_QUESTIONS = List.of(249L, 250L, 251L);
    private static final List<Long> B_BLEND_TYPE_QUESTIONS = List.of(252L);
    private static final List<Long> BLEND_TYPE_QUESTIONS = List.of(248L);
    private static final List<Long> ONE_THRESHING_QUESTIONS = List.of(253L);
    private static final List<Long> TWO_THRESHING_QUESTIONS = List.of(254L, 255L);
    private static final List<Long> THRESHING_QUESTIONS = List.of(252L);
    public ExpenseBlendProfileController(UserPlantationPlanService userPlantationPlanService,
                                         QuestionService questionService,
                                         CostCalculationService costCalculationService,
                                         CostAllocationService costAllocationService) {
        super(userPlantationPlanService, questionService, costCalculationService, costAllocationService);
    }

    public void fillExpenseBlendQuestionList() throws IOException {
        if (super.getPlantationPlanId() == null || !checkPlanIdForUser(super.getPlantationPlanId())) {
            super.navigationController.redirectToUrl("/secured/plantation-list");
            return;
        }

        if (!Hibernate.isInitialized(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = getQuestionService().getActiveQuestionByQuestionType(super.getUserPlantationPlan().getPrimaryProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_BLEND);
            List<UserPlantationPlanAnswer> planAnswerList = getUserPlantationPlanService().fillPlanAnswerValues(super.getPlantationPlanId(), EnumPlantationQuestionType.EXPENSE_BLEND);
            List<UserPlantationAnswer> plantationAnswerList = getUserPlantationPlanService().fillPlantationAnswerValues(super.getUserPlantationPlan().getUserPlantation().getId(), EnumPlantationQuestionType.EXPENSE_BLEND);
            assignAnswerListsToProduct(planAnswerList, productQuestionList, plantationAnswerList);
        }
    }

    public boolean oneRadioRenderer(PlantationProductQuestion question) {
        if (question.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_RADIO)) {
            if (checkPreviousQuestionAnswerAccordingly(question))
                return true;
            question.setSelectedAnswerId(null);
            return false;
        }
        return false;
    }

    public boolean doubleRenderer(PlantationProductQuestion question) {
        if (question.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DOUBLE)) {
            if (checkPreviousQuestionAnswerAccordingly(question))
                return true;
            question.setDoubleValue(null);
            return false;
        }
        return false;
    }

    public boolean decimalRenderer(PlantationProductQuestion question) {
        if (question.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DECIMAL)) {
            if (checkPreviousQuestionAnswerAccordingly(question))
                return true;
            question.setBigDecimalValue(null);
            return false;
        }
        return false;
    }

    private boolean checkPreviousQuestionAnswerAccordingly(PlantationProductQuestion question) {
        Optional<PlantationProductQuestion> optionalQuestion = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream().filter(pq -> BLEND_TYPE_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (A_BLEND_TYPE_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(180L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (B_BLEND_TYPE_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(181L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        optionalQuestion = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream().filter(pq -> THRESHING_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (ONE_THRESHING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(183L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (TWO_THRESHING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(184L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        return true;
    }

    public void nextSaveExpense() throws IOException {
        super.getUserPlantationPlan().setBlendCost(getCostCalculationService().calculateBlendCost(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList(), super.getUserPlantationPlan().getId(), super.getUserPlantationPlan().getUserPlantation().getDistrict().getCity()));
        getCostAllocationService().blendAllocation(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList(), super.getUserPlantationPlan(), super.getUserPlantationPlan().getUserPlantation().getDistrict().getCity());
        goToNextPage(EnumPlantationQuestionType.EXPENSE_BLEND);
    }

    public void previousSaveExpense() throws IOException {
        goToPreviousPage(EnumPlantationQuestionType.EXPENSE_BLEND);
    }

}

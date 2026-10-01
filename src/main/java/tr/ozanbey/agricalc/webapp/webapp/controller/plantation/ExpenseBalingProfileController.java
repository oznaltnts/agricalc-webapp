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
public class ExpenseBalingProfileController extends PlanProfileController {

    private static final List<Long> X_BALING_QUESTIONS = List.of(274L);
    private static final List<Long> BALING_QUESTIONS = List.of(273L);
    private static final List<Long> BALING_OWNING_QUESTIONS = List.of(275L);
    private static final List<Long> ONE_BALING_QUESTIONS = List.of(276L, 277L, 278L, 279L);
    private static final List<Long> TWO_BALING_QUESTIONS = List.of(280L, 281L);

    private static final List<Long> X_BALING_PRODUCTS = List.of(172L);
    private static final List<Long> A_BALING_PRODUCTS = List.of(10L, 42L, 45L, 46L, 47L, 51L, 52L, 54L, 55L, 79L, 91L, 110L, 115L, 126L, 151L, 156L, 212L, 213L, 217L, 220L);
    private static final List<Long> B_BALING_PRODUCTS = List.of(9L, 11L, 12L, 21L, 23L, 40L, 41L, 43L, 44L, 50L, 218L, 219L, 221L);

    public ExpenseBalingProfileController(UserPlantationPlanService userPlantationPlanService,
                                          QuestionService questionService,
                                          CostCalculationService costCalculationService,
                                          CostAllocationService costAllocationService) {
        super(userPlantationPlanService, questionService, costCalculationService, costAllocationService);
    }

    public void fillExpenseBalingQuestionList() throws IOException {
        if (super.getPlantationPlanId() == null || !checkPlanIdForUser(super.getPlantationPlanId())) {
            super.navigationController.redirectToUrl("/plantation/field");
            return;
        }

        if (!Hibernate.isInitialized(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = super.getQuestionService().getActiveQuestionByQuestionType(super.getUserPlantationPlan().getPrimaryProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_BALING);
            List<UserPlantationPlanAnswer> planAnswerList = super.getUserPlantationPlanService().fillPlanAnswerValues(super.getPlantationPlanId(), EnumPlantationQuestionType.EXPENSE_BALING);
            List<UserPlantationAnswer> plantationAnswerList = super.getUserPlantationPlanService().fillPlantationAnswerValues(super.getUserPlantationPlan().getUserPlantation().getId(), EnumPlantationQuestionType.EXPENSE_BALING);
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
        Optional<PlantationProductQuestion> optionalQuestion = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream().filter(pq -> BALING_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (X_BALING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (X_BALING_PRODUCTS.contains(question.getPlantationProduct().getId())) {
                return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(193L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
            } else if (A_BALING_PRODUCTS.contains(question.getPlantationProduct().getId())) {
                question.setSelectedAnswerId(195L);
            } else if (B_BALING_PRODUCTS.contains(question.getPlantationProduct().getId())) {
                question.setSelectedAnswerId(196L);
            }
            return false;
        }
        optionalQuestion = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream().filter(pq -> X_BALING_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (BALING_OWNING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(195L, 196L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        optionalQuestion = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                .filter(pq -> BALING_OWNING_QUESTIONS.contains(pq.getPlantationQuestion().getId()))
                .findFirst();
        if (ONE_BALING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(197L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (TWO_BALING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(198L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        return true;
    }

    public void nextSaveExpense() throws IOException {
        super.getUserPlantationPlan().setBalingCost(getCostCalculationService().calculateBalingCost(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList(), super.getUserPlantationPlan().getId(), super.getUserPlantationPlan().getUserPlantation().getDistrict().getCity()));
        getCostAllocationService().balingAllocation(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList(), super.getUserPlantationPlan(), super.getUserPlantationPlan().getUserPlantation().getDistrict().getCity());
        goToNextPage(EnumPlantationQuestionType.EXPENSE_BALING);
    }

    public void previousSaveExpense() throws IOException {
        goToPreviousPage(EnumPlantationQuestionType.EXPENSE_BALING);
    }

}

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
import java.util.stream.Stream;

@Component
@ViewScoped
@Getter
@Setter
public class ExpenseWeedProfileController extends PlanProfileController {

    private static final List<Long> A_THROAT_QUESTIONS = List.of(119L);
    private static final List<Long> B_THROAT_QUESTIONS = List.of(120L);
    private static final List<Long> THROAT_FILLING_QUESTIONS = List.of(118L);
    private static final List<Long> A_WEED_QUESTIONS = List.of(128L, 129L, 130L, 131L, 132L, 133L, 134L, 135L);
    private static final List<Long> B_WEED_QUESTIONS = List.of(136L);
    private static final List<Long> C_WEED_QUESTIONS = List.of(137L, 138L);
    private static final List<Long> D_WEED_QUESTIONS = List.of(139L, 140L, 141L, 142L);
    private static final List<Long> E_WEED_QUESTIONS = List.of(143L, 144L, 145L);
    private static final List<Long> K_WEED_QUESTIONS = List.of(146L, 147L);
    private static final List<Long> FIRST_TILLING_QUESTIONS = List.of(129L, 130L);
    private static final List<Long> SECOND_TILLING_QUESTIONS = List.of(131L, 132L);
    private static final List<Long> THIRD_TILLING_QUESTIONS = List.of(133L, 134L, 135L);
    public ExpenseWeedProfileController(UserPlantationPlanService userPlantationPlanService,
                                        QuestionService questionService,
                                        CostCalculationService costCalculationService,
                                        CostAllocationService costAllocationService) {
        super(userPlantationPlanService, questionService, costCalculationService, costAllocationService);
    }

    public void fillExpenseWeedQuestionList() throws IOException {
        if (super.getPlantationPlanId() == null || !checkPlanIdForUser(super.getPlantationPlanId())) {
            super.navigationController.redirectToUrl("/secured/plantation-list");
            return;
        }

        if (!Hibernate.isInitialized(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = getQuestionService().getActiveQuestionByQuestionType(super.getUserPlantationPlan().getPrimaryProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_WEED);
            List<UserPlantationPlanAnswer> planAnswerList = getUserPlantationPlanService().fillPlanAnswerValues(super.getPlantationPlanId(), EnumPlantationQuestionType.EXPENSE_WEED);
            List<UserPlantationAnswer> plantationAnswerList = getUserPlantationPlanService().fillPlantationAnswerValues(super.getUserPlantationPlan().getUserPlantation().getId(), EnumPlantationQuestionType.EXPENSE_WEED);
            assignAnswerListsToProduct(planAnswerList, productQuestionList, plantationAnswerList);
        }
    }

    public boolean manyCheckboxRenderer(PlantationProductQuestion question) {
        if (question.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_MANY_CHECKBOX)) {
            if (checkPreviousQuestionAnswerAccordingly(question))
                return true;
            question.setSelectedAnswerIds(null);
            return false;
        }
        return false;
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

    public boolean integerRenderer(PlantationProductQuestion question) {
        if (question.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_INTEGER)) {
            if (checkPreviousQuestionAnswerAccordingly(question))
                return true;
            question.setIntegerValue(null);
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
        Optional<PlantationProductQuestion> optionalQuestion = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream().filter(pq -> THROAT_FILLING_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (A_THROAT_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(94L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (B_THROAT_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(95L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }

        List<PlantationProductQuestion> questionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                .filter(pq -> List.of(121L, 122L, 123L, 124L, 125L, 126L).contains(pq.getPlantationQuestion().getId())).toList();
        if (A_WEED_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) {
                questionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                        .filter(spq -> List.of(128L).contains(spq.getPlantationQuestion().getId())).toList();
                if (FIRST_TILLING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    if (questionList.isEmpty()) return true;
                    for (PlantationProductQuestion spq : questionList) {
                        if (spq.getSelectedAnswerIds() != null && Stream.of(119L).anyMatch(spq.getSelectedAnswerIds()::contains)) {
                            return true;
                        }
                    }
                    return false;
                }
                if (SECOND_TILLING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    if (questionList.isEmpty()) return true;
                    for (PlantationProductQuestion spq : questionList) {
                        if (spq.getSelectedAnswerIds() != null && Stream.of(120L).anyMatch(spq.getSelectedAnswerIds()::contains)) {
                            return true;
                        }
                    }
                    return false;
                }
                if (THIRD_TILLING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    if (questionList.isEmpty()) return true;
                    for (PlantationProductQuestion spq : questionList) {
                        if (spq.getSelectedAnswerIds() != null && Stream.of(121L).anyMatch(spq.getSelectedAnswerIds()::contains)) {
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            }
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getSelectedAnswerIds() != null && Stream.of(97L, 102L, 106L, 109L, 112L, 115L).anyMatch(pq.getSelectedAnswerIds()::contains)) {
                    questionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                            .filter(spq -> List.of(128L).contains(spq.getPlantationQuestion().getId())).toList();
                    if (FIRST_TILLING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        if (questionList.isEmpty()) return true;
                        for (PlantationProductQuestion spq : questionList) {
                            if (spq.getSelectedAnswerIds() != null && Stream.of(119L).anyMatch(spq.getSelectedAnswerIds()::contains)) {
                                return true;
                            }
                        }
                        return false;
                    }
                    if (SECOND_TILLING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        if (questionList.isEmpty()) return true;
                        for (PlantationProductQuestion spq : questionList) {
                            if (spq.getSelectedAnswerIds() != null && Stream.of(120L).anyMatch(spq.getSelectedAnswerIds()::contains)) {
                                return true;
                            }
                        }
                        return false;
                    }
                    if (THIRD_TILLING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        if (questionList.isEmpty()) return true;
                        for (PlantationProductQuestion spq : questionList) {
                            if (spq.getSelectedAnswerIds() != null && Stream.of(121L).anyMatch(spq.getSelectedAnswerIds()::contains)) {
                                return true;
                            }
                        }
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }
        questionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                .filter(pq -> List.of(121L, 122L, 123L, 124L, 125L, 126L, 127L).contains(pq.getPlantationQuestion().getId())).toList();
        if (B_WEED_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) return true;
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getSelectedAnswerIds() != null && Stream.of(98L, 103L, 107L, 110L, 113L, 116L, 117L).anyMatch(pq.getSelectedAnswerIds()::contains)) {
                    return true;
                }
            }
            return false;
        }
        questionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                .filter(pq -> List.of(121L, 123L, 127L).contains(pq.getPlantationQuestion().getId())).toList();
        if (C_WEED_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) return true;
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getSelectedAnswerIds() != null && Stream.of(99L, 108L, 118L).anyMatch(pq.getSelectedAnswerIds()::contains)) {
                    return true;
                }
            }
            return false;
        }
        questionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                .filter(pq -> List.of(121L, 125L).contains(pq.getPlantationQuestion().getId())).toList();
        if (D_WEED_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) return true;
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getSelectedAnswerIds() != null && Stream.of(100L, 114L).anyMatch(pq.getSelectedAnswerIds()::contains)) {
                    return true;
                }
            }
            return false;
        }
        questionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                .filter(pq -> List.of(121L, 122L, 124L).contains(pq.getPlantationQuestion().getId())).toList();
        if (E_WEED_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) return true;
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getSelectedAnswerIds() != null && Stream.of(101L, 104L, 111L).anyMatch(pq.getSelectedAnswerIds()::contains)) {
                    return true;
                }
            }
            return false;
        }
        questionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                .filter(pq -> List.of(122L).contains(pq.getPlantationQuestion().getId())).toList();
        if (K_WEED_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) return true;
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getSelectedAnswerIds() != null && Stream.of(105L).anyMatch(pq.getSelectedAnswerIds()::contains)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public void nextSaveExpense() throws IOException {
        super.getUserPlantationPlan().setWeedControlCost(getCostCalculationService().calculateWildGrassControlCost(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList(), super.getUserPlantationPlan().getId(), super.getUserPlantationPlan().getUserPlantation().getDistrict().getCity()));
        getCostAllocationService().wildGrassAllocation(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList(), super.getUserPlantationPlan(), super.getUserPlantationPlan().getUserPlantation().getDistrict().getCity());
        goToNextPage(EnumPlantationQuestionType.EXPENSE_WEED);
    }

    public void previousSaveExpense() throws IOException {
        goToPreviousPage(EnumPlantationQuestionType.EXPENSE_WEED);
    }

}

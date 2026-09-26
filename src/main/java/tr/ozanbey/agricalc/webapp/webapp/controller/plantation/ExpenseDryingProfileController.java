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

@Component
@ViewScoped
@Getter
@Setter
public class ExpenseDryingProfileController extends PlanProfileController {

    private static final List<Long> A_DRYING_QUESTIONS = List.of(257L, 258L, 259L, 260L, 261L, 262L, 263L, 264L, 265L, 266L, 267L, 268L, 269L, 270L, 271L, 272L);
    private static final List<Long> ONE_DRYING_QUESTIONS = List.of(259L, 260L, 261L, 262L, 263L, 264L, 265L, 266L, 269L, 270L, 271L, 272L);
    private static final List<Long> TWO_DRYING_QUESTIONS = List.of(267L, 268L);
    private static final List<Long> ONE_A_DRYING_QUESTIONS = List.of(270L, 271L, 272L);

    public ExpenseDryingProfileController(UserPlantationPlanService userPlantationPlanService,
                                          QuestionService questionService,
                                          CostCalculationService costCalculationService,
                                          CostAllocationService costAllocationService) {
        super(userPlantationPlanService, questionService, costCalculationService, costAllocationService);
    }

    public void fillExpenseDryingQuestionList() throws IOException {
        if (super.getPlantationPlanId() == null || !checkPlanIdForUser(super.getPlantationPlanId())) {
            super.navigationController.redirectToUrl("/secured/plantation-list");
            return;
        }

        if (!Hibernate.isInitialized(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = getQuestionService().getActiveQuestionByQuestionType(super.getUserPlantationPlan().getPrimaryProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_DRYING);
            List<UserPlantationPlanAnswer> planAnswerList = getUserPlantationPlanService().fillPlanAnswerValues(super.getPlantationPlanId(), EnumPlantationQuestionType.EXPENSE_DRYING);
            List<UserPlantationAnswer> plantationAnswerList = getUserPlantationPlanService().fillPlantationAnswerValues(super.getUserPlantationPlan().getUserPlantation().getId(), EnumPlantationQuestionType.EXPENSE_DRYING);
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

        List<PlantationProductQuestion> questionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                .filter(pq -> List.of(256L).contains(pq.getPlantationQuestion().getId())).toList();
        if (A_DRYING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) {
                List<PlantationProductQuestion> subQuestionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                        .filter(spq -> List.of(257L, 258L).contains(spq.getPlantationQuestion().getId())).toList();
                if (ONE_DRYING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    if (subQuestionList.isEmpty()) {
                        List<PlantationProductQuestion> subSubQuestionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                                .filter(sspq -> List.of(269L).contains(sspq.getPlantationQuestion().getId())).toList();
                        if (ONE_A_DRYING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                            if (subSubQuestionList.isEmpty()) {
                                return true;
                            }
                            for (PlantationProductQuestion sspq : subSubQuestionList) {
                                if (sspq.getSelectedAnswerId() != null && List.of(191L).contains(sspq.getSelectedAnswerId())) {
                                    return true;
                                }
                            }
                            return false;
                        }
                        return true;
                    }
                    for (PlantationProductQuestion spq : subQuestionList) {
                        if (spq.getSelectedAnswerId() != null && List.of(187L, 189L).contains(spq.getSelectedAnswerId())) {
                            List<PlantationProductQuestion> subSubQuestionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                                    .filter(sspq -> List.of(269L).contains(sspq.getPlantationQuestion().getId())).toList();
                            if (ONE_A_DRYING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                                if (subSubQuestionList.isEmpty()) {
                                    return true;
                                }
                                for (PlantationProductQuestion sspq : subSubQuestionList) {
                                    if (sspq.getSelectedAnswerId() != null && List.of(191L).contains(sspq.getSelectedAnswerId())) {
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
                if (TWO_DRYING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    if (subQuestionList.isEmpty()) {
                        return true;
                    }
                    for (PlantationProductQuestion spq : subQuestionList) {
                        if (spq.getSelectedAnswerId() != null && List.of(188L, 190L).contains(spq.getSelectedAnswerId())) {
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            }
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getSelectedAnswerId() != null && List.of(185L).contains(pq.getSelectedAnswerId())) {
                    List<PlantationProductQuestion> subQuestionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                            .filter(spq -> List.of(257L, 258L).contains(spq.getPlantationQuestion().getId())).toList();
                    if (ONE_DRYING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        if (subQuestionList.isEmpty()) {
                            List<PlantationProductQuestion> subSubQuestionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                                    .filter(sspq -> List.of(269L).contains(sspq.getPlantationQuestion().getId())).toList();
                            if (ONE_A_DRYING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                                if (subSubQuestionList.isEmpty()) {
                                    return true;
                                }
                                for (PlantationProductQuestion sspq : subSubQuestionList) {
                                    if (sspq.getSelectedAnswerId() != null && List.of(191L).contains(sspq.getSelectedAnswerId())) {
                                        return true;
                                    }
                                }
                                return false;
                            }
                            return true;
                        }
                        for (PlantationProductQuestion spq : subQuestionList) {
                            if (spq.getSelectedAnswerId() != null && List.of(187L, 189L).contains(spq.getSelectedAnswerId())) {
                                List<PlantationProductQuestion> subSubQuestionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                                        .filter(sspq -> List.of(269L).contains(sspq.getPlantationQuestion().getId())).toList();
                                if (ONE_A_DRYING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                                    if (subSubQuestionList.isEmpty()) {
                                        return true;
                                    }
                                    for (PlantationProductQuestion sspq : subSubQuestionList) {
                                        if (sspq.getSelectedAnswerId() != null && List.of(191L).contains(sspq.getSelectedAnswerId())) {
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
                    if (TWO_DRYING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        if (subQuestionList.isEmpty()) {
                            return true;
                        }
                        for (PlantationProductQuestion spq : subQuestionList) {
                            if (spq.getSelectedAnswerId() != null && List.of(188L, 190L).contains(spq.getSelectedAnswerId())) {
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
        return true;
    }

    public void nextSaveExpense() throws IOException {
        super.getUserPlantationPlan().setDryingCost(getCostCalculationService().calculateProcessDryCost(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList(), super.getUserPlantationPlan().getId(), super.getUserPlantationPlan().getUserPlantation().getDistrict().getCity()));
        getCostAllocationService().dryingAllocation(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList(), super.getUserPlantationPlan(), super.getUserPlantationPlan().getUserPlantation().getDistrict().getCity());
        goToNextPage(EnumPlantationQuestionType.EXPENSE_DRYING);
    }

    public void previousSaveExpense() throws IOException {
        goToPreviousPage(EnumPlantationQuestionType.EXPENSE_DRYING);
    }

}

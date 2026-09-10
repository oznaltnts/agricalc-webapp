package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelAnswer;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelPlanAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionAnswerType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.CostAllocationService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.CostCalculationService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.QuestionService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantParcelPlanService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

@Component
@ViewScoped
@Getter
@Setter
public class ExpensePlantingProfileController extends PlanProfileController {

    @Autowired
    private UserPlantParcelPlanService userPlantParcelPlanService;

    @Autowired
    private QuestionService questionService;


    @PostConstruct
    public void init() {
    }

    public void fillExpensePlantingQuestionList() throws IOException {
        if (super.getParcelPlanId() == null || !checkPlanIdForUser(super.getParcelPlanId())) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }

        if (!Hibernate.isInitialized(super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = questionService.getActiveQuestionByQuestionType(super.getParcelPlan().getPlantParcel().getProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_PLANTING);
            List<UserPlantParcelPlanAnswer> planAnswerList = userPlantParcelPlanService.fillPlanAnswerValues(super.getParcelPlanId(), EnumPlantationQuestionType.EXPENSE_PLANTING);
            List<UserPlantParcelAnswer> parcelAnswerList = userPlantParcelPlanService.fillParcelAnswerValues(super.getParcelPlan().getPlantParcel().getId(), EnumPlantationQuestionType.EXPENSE_PLANTING);
            for (UserPlantParcelPlanAnswer userPlantParcelPlanAnswer : planAnswerList) {
                fillAnsweredQuestionValues(userPlantParcelPlanAnswer.getProductQuestion().getId(), userPlantParcelPlanAnswer.getAnswerValue(), productQuestionList, false);
            }
            for (UserPlantParcelAnswer userPlantParcelAnswer : parcelAnswerList) {
                fillAnsweredQuestionValues(userPlantParcelAnswer.getProductQuestion().getId(), userPlantParcelAnswer.getAnswerValue(), productQuestionList, true);
            }
            super.getParcelPlan().getPlantParcel().getProduct().setProductQuestionList(productQuestionList);
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
            if (List.of(84L).contains(question.getPlantationQuestion().getId()) && question.getMaximumValue() != null && question.getMaximumValue().compareTo(BigDecimal.ONE) == 0) {
                question.setIntegerValue(1);
                return false;
            }
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

    private static final List<Long> A_PLANTING_QUESTIONS = List.of(55L, 56L, 57L, 58L, 59L, 60L, 61L, 62L, 63L, 64L, 65L, 66L, 67L, 68L, 69L);
    private static final List<Long> B_PLANTING_QUESTIONS = List.of(70L, 71L, 72L, 73L, 74L, 75L, 76L, 77L, 78L, 79L, 80L, 84L);
    private static final List<Long> C_PLANTING_QUESTIONS = List.of(81L, 82L, 83L);
    private static final List<Long> D_PLANTING_QUESTIONS = List.of(85L, 86L, 87L, 88L, 89L, 90L);
    private static final List<Long> A1_PLANTING_QUESTIONS = List.of(57L);
    private static final List<Long> A2_PLANTING_QUESTIONS = List.of(58L);
    private static final List<Long> A1_A2_PLANTING_QUESTIONS = List.of(61L);
    private static final List<Long> A3_PLANTING_QUESTIONS = List.of(59L, 62L);
    private static final List<Long> A4_PLANTING_QUESTIONS = List.of(60L, 63L);
    private static final List<Long> ONE_PLANTING_QUESTIONS = List.of(66L);
    private static final List<Long> TWO_PLANTING_QUESTIONS = List.of(67L);
    private static final List<Long> THR_PLANTING_QUESTIONS = List.of(68L);
    private static final List<Long> B1_PLANTING_QUESTIONS = List.of(76L);
    private static final List<Long> B2_PLANTING_QUESTIONS = List.of(77L, 78L, 79L, 80L);
    private static final List<Long> B2A_PLANTING_QUESTIONS = List.of(78L, 79L);
    private static final List<Long> B2B_PLANTING_QUESTIONS = List.of(80L);
    private static final List<Long> DA_PLANTING_QUESTIONS = List.of(87L);
    private static final List<Long> DB_PLANTING_QUESTIONS = List.of(88L);

    private boolean checkPreviousQuestionAnswerAccordingly(PlantationProductQuestion question) {
        List<PlantationProductQuestion> questionList = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream()
                .filter(pq -> List.of(49L, 50L, 51L, 52L, 54L).contains(pq.getPlantationQuestion().getId())).toList();
        if (A_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) {
                questionList = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream()
                        .filter(spq -> List.of(55L, 56L).contains(spq.getPlantationQuestion().getId())).toList();
                if (A1_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    return checkMinimumRequirement(questionList, List.of(51L));
                }
                if (A2_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    return checkMinimumRequirement(questionList, List.of(52L, 55L));
                }
                if (A1_A2_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    return checkMinimumRequirement(questionList, List.of(51L, 52L, 55L));
                }
                if (A3_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    return checkMinimumRequirement(questionList, List.of(53L, 56L));
                }
                if (A4_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    return checkMinimumRequirement(questionList, List.of(54L));
                }
                questionList = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream()
                        .filter(spq -> List.of(64L, 65L).contains(spq.getPlantationQuestion().getId())).toList();
                if (ONE_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    return checkMinimumRequirement(questionList, List.of(57L, 60L));
                }
                if (TWO_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    return checkMinimumRequirement(questionList, List.of(58L, 61L));
                }
                if (THR_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    return checkMinimumRequirement(questionList, List.of(59L));
                }
                return true;
            }
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getSelectedAnswerId() != null && List.of(36L, 40L, 43L, 45L, 49L).contains(pq.getSelectedAnswerId())) {
                    questionList = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream()
                            .filter(spq -> List.of(55L, 56L).contains(spq.getPlantationQuestion().getId())).toList();
                    if (A1_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        return checkMinimumRequirement(questionList, List.of(51L));
                    }
                    if (A2_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        return checkMinimumRequirement(questionList, List.of(52L, 55L));
                    }
                    if (A1_A2_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        return checkMinimumRequirement(questionList, List.of(51L, 52L, 55L));
                    }
                    if (A3_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        return checkMinimumRequirement(questionList, List.of(53L, 56L));
                    }
                    if (A4_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        return checkMinimumRequirement(questionList, List.of(54L));
                    }
                    questionList = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream()
                            .filter(spq -> List.of(64L, 65L).contains(spq.getPlantationQuestion().getId())).toList();
                    if (ONE_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        return checkMinimumRequirement(questionList, List.of(57L, 60L));
                    }
                    if (TWO_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        return checkMinimumRequirement(questionList, List.of(58L, 61L));
                    }
                    if (THR_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        return checkMinimumRequirement(questionList, List.of(59L));
                    }
                    return true;
                }
            }
            return false;
        }
        questionList = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream()
                .filter(pq -> List.of(49L, 50L, 51L, 53L).contains(pq.getPlantationQuestion().getId())).toList();
        if (B_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) {
                questionList = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream()
                        .filter(spq -> List.of(75L).contains(spq.getPlantationQuestion().getId())).toList();
                if (B1_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    if (questionList.isEmpty()) return true;
                    for (PlantationProductQuestion spq : questionList) {
                        if (spq.getSelectedAnswerIds() != null && Stream.of(65L).anyMatch(spq.getSelectedAnswerIds()::contains)) {
                            return true;
                        }
                    }
                    return false;
                }
                if (B2_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    if (questionList.isEmpty()) return true;
                    for (PlantationProductQuestion spq : questionList) {
                        if (spq.getSelectedAnswerIds() != null && Stream.of(66L).anyMatch(spq.getSelectedAnswerIds()::contains)) {
                            questionList = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream()
                                    .filter(sspq -> List.of(77L).contains(sspq.getPlantationQuestion().getId())).toList();
                            if (B2A_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                                return checkMinimumRequirement(questionList, List.of(67L));
                            }
                            if (B2B_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                                return checkMinimumRequirement(questionList, List.of(68L));
                            }
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            }
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getSelectedAnswerId() != null && List.of(37L, 41L, 44L, 47L).contains(pq.getSelectedAnswerId())) {
                    questionList = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream()
                            .filter(spq -> List.of(75L).contains(spq.getPlantationQuestion().getId())).toList();
                    if (B1_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        if (questionList.isEmpty()) return true;
                        for (PlantationProductQuestion spq : questionList) {
                            if (spq.getSelectedAnswerIds() != null && Stream.of(65L).anyMatch(spq.getSelectedAnswerIds()::contains)) {
                                return true;
                            }
                        }
                        return false;
                    }
                    if (B2_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        if (questionList.isEmpty()) return true;
                        for (PlantationProductQuestion spq : questionList) {
                            if (spq.getSelectedAnswerIds() != null && Stream.of(66L).anyMatch(spq.getSelectedAnswerIds()::contains)) {
                                questionList = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream()
                                        .filter(sspq -> List.of(77L).contains(sspq.getPlantationQuestion().getId())).toList();
                                if (B2A_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                                    return checkMinimumRequirement(questionList, List.of(67L));
                                }
                                if (B2B_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                                    return checkMinimumRequirement(questionList, List.of(68L));
                                }
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
        questionList = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream()
                .filter(pq -> List.of(49L, 50L, 52L, 53L).contains(pq.getPlantationQuestion().getId())).toList();
        if (C_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return checkMinimumRequirement(questionList, List.of(38L, 42L, 46L, 48L));
        }
        questionList = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream()
                .filter(pq -> List.of(49L, 54L).contains(pq.getPlantationQuestion().getId())).toList();
        if (D_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) {
                questionList = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream()
                        .filter(spq -> List.of(86L).contains(spq.getPlantationQuestion().getId())).toList();
                if (DA_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    return checkMinimumRequirement(questionList, List.of(69L));
                }
                if (DB_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                    return checkMinimumRequirement(questionList, List.of(70L));
                }
                return true;
            }
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getSelectedAnswerId() != null && List.of(39L, 50L).contains(pq.getSelectedAnswerId())) {
                    questionList = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream()
                            .filter(spq -> List.of(86L).contains(spq.getPlantationQuestion().getId())).toList();
                    if (DA_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        return checkMinimumRequirement(questionList, List.of(69L));
                    }
                    if (DB_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
                        return checkMinimumRequirement(questionList, List.of(70L));
                    }
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private static boolean checkMinimumRequirement(List<PlantationProductQuestion> questionList, List<Long> x) {
        if (questionList.isEmpty()) return true;
        for (PlantationProductQuestion spq : questionList) {
            if (spq.getSelectedAnswerId() != null && x.contains(spq.getSelectedAnswerId())) {
                return true;
            }
        }
        return false;
    }

    @Autowired
    private CostCalculationService costCalculationService;

    @Autowired
    private CostAllocationService costAllocationService;

    public void nextSaveExpense() throws IOException {
        super.getParcelPlan().setPlantingCost(costCalculationService.calculatePlantingCost(super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList(), super.getParcelPlan().getId(), super.getParcelPlan().getPlantParcel().getCity()));
        costAllocationService.plantingAllocation(super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList(), super.getParcelPlan(), super.getParcelPlan().getPlantParcel().getCity());
        goToNextPage(EnumPlantationQuestionType.EXPENSE_PLANTING);
    }

    public void previousSaveExpense() throws IOException {
        goToPreviousPage(EnumPlantationQuestionType.EXPENSE_PLANTING);
    }

}

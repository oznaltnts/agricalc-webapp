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
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@ViewScoped
@Getter
@Setter
public class ExpenseFertilizerProfileController extends PlanProfileController {

    @Autowired
    private UserPlantParcelPlanService userPlantParcelPlanService;

    @Autowired
    private QuestionService questionService;


    @PostConstruct
    public void init() {
    }

    public void fillExpenseFertilizerQuestionList() throws IOException {
        if (super.getParcelPlanId() == null || !checkPlanIdForUser(super.getParcelPlanId())) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }

        if (!Hibernate.isInitialized(super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = questionService.getActiveQuestionByQuestionType(super.getParcelPlan().getPlantParcel().getProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_FERTILIZER);
            List<UserPlantParcelPlanAnswer> planAnswerList = userPlantParcelPlanService.fillPlanAnswerValues(super.getParcelPlanId(), EnumPlantationQuestionType.EXPENSE_FERTILIZER);
            List<UserPlantParcelAnswer> parcelAnswerList = userPlantParcelPlanService.fillParcelAnswerValues(super.getParcelPlan().getPlantParcel().getId(), EnumPlantationQuestionType.EXPENSE_FERTILIZER);
            for (UserPlantParcelPlanAnswer userPlantParcelPlanAnswer : planAnswerList) {
                fillAnsweredQuestionValues(userPlantParcelPlanAnswer.getProductQuestion().getId(), userPlantParcelPlanAnswer.getAnswerValue(), productQuestionList, false);
            }
            for (UserPlantParcelAnswer userPlantParcelAnswer : parcelAnswerList) {
                fillAnsweredQuestionValues(userPlantParcelAnswer.getProductQuestion().getId(), userPlantParcelAnswer.getAnswerValue(), productQuestionList, true);
            }
            super.getParcelPlan().getPlantParcel().getProduct().setProductQuestionList(productQuestionList);
        }
    }

    public boolean oneMenuRenderer(PlantationProductQuestion question) {
        if (question.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_MENU)) {
            if (checkPreviousQuestionAnswerAccordingly(question))
                return true;
            question.setSelectedAnswerId(null);
            return false;
        }
        return false;
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

    private static final List<Long> A_FERTILIZER_QUESTIONS = List.of(93L, 94L);
    private static final List<Long> B_FERTILIZER_QUESTIONS = List.of(95L, 96L, 97L, 98L, 99L);
    private static final List<Long> B_C_FERTILIZER_QUESTIONS = List.of(100L);
    private static final List<Long> C_FERTILIZER_QUESTIONS = List.of(101L, 102L);
    private static final List<Long> D_FERTILIZER_QUESTIONS = List.of(103L, 104L);
    private static final List<Long> E_FERTILIZER_QUESTIONS = List.of(106L);
    private static final List<Long> F_FERTILIZER_QUESTIONS = List.of(107L, 108L, 109L);
    private static final List<Long> H_FERTILIZER_QUESTIONS = List.of(110L, 111L);
    private static final List<Long> I_FERTILIZER_QUESTIONS = List.of(112L, 113L);
    private static final List<Long> K_FERTILIZER_QUESTIONS = List.of(114L, 115L);
    private static final List<Long> L_FERTILIZER_QUESTIONS = List.of(116L, 117L);
    private static final List<Long> FERTILIZER_QUESTIONS = List.of(91L);

    private boolean checkPreviousQuestionAnswerAccordingly(PlantationProductQuestion question) {
        Optional<PlantationProductQuestion> optionalQuestion = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> FERTILIZER_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (A_FERTILIZER_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(71L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (B_FERTILIZER_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(72L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (B_C_FERTILIZER_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(72L, 73L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (C_FERTILIZER_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(73L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (D_FERTILIZER_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(74L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (E_FERTILIZER_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(75L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (F_FERTILIZER_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(76L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (H_FERTILIZER_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(77L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (I_FERTILIZER_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(78L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (K_FERTILIZER_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(79L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (L_FERTILIZER_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(80L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        }
        return true;
    }

    @Autowired
    private CostCalculationService costCalculationService;

    @Autowired
    private CostAllocationService costAllocationService;

    public void nextSaveExpense() throws IOException {
        super.getParcelPlan().setFertilizerCost(costCalculationService.calculateFertilizerCost(super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList(), super.getParcelPlan().getId(), super.getParcelPlan().getPlantParcel().getCity()));
        costAllocationService.fertilizerAllocation(super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList(), super.getParcelPlan(), super.getParcelPlan().getPlantParcel().getCity());
        goToNextPage(EnumPlantationQuestionType.EXPENSE_FERTILIZER);
    }

    public void previousSaveExpense() throws IOException {
        goToPreviousPage(EnumPlantationQuestionType.EXPENSE_FERTILIZER);
    }

}

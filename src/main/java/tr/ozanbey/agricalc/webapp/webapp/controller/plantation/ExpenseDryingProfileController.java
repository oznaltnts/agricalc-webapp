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
import tr.ozanbey.agricalc.webapp.service.service.plantation.CostCalculationService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationProductService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantParcelPlanService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class ExpenseDryingProfileController extends PlanProfileController {

    @Autowired
    private UserPlantParcelPlanService userPlantParcelPlanService;

    @Autowired
    private PlantationProductService productService;

    @PostConstruct
    public void init() {
    }

    public void fillExpenseDryingQuestionList() throws IOException {
        if (super.getParcelPlanId() == null || !checkPlanIdForUser(super.getParcelPlanId())) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }

        if (!Hibernate.isInitialized(super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = productService.getActiveQuestionByQuestionType(super.getParcelPlan().getPlantParcel().getProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_DRYING);
            List<UserPlantParcelPlanAnswer> planAnswerList = userPlantParcelPlanService.fillPlanAnswerValues(super.getParcelPlanId(), EnumPlantationQuestionType.EXPENSE_DRYING);
            List<UserPlantParcelAnswer> parcelAnswerList = userPlantParcelPlanService.fillParcelAnswerValues(super.getParcelPlan().getPlantParcel().getId(), EnumPlantationQuestionType.EXPENSE_DRYING);
            for (UserPlantParcelPlanAnswer userPlantParcelPlanAnswer : planAnswerList) {
                fillAnsweredQuestionValues(userPlantParcelPlanAnswer.getProductQuestion().getId(), userPlantParcelPlanAnswer.getAnswerValue(), productQuestionList, false);
            }
            for (UserPlantParcelAnswer userPlantParcelAnswer : parcelAnswerList) {
                fillAnsweredQuestionValues(userPlantParcelAnswer.getProductQuestion().getId(), userPlantParcelAnswer.getAnswerValue(), productQuestionList, true);
            }
            super.getParcelPlan().getPlantParcel().getProduct().setProductQuestionList(productQuestionList);
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

    private static final List<Long> DRYING_QUESTIONS = List.of(256L);

    private static final List<Long> ONE_DRYING_QUESTIONS = List.of(259L, 260L, 261L, 262L, 263L, 264L, 265L, 266L, 269L);
    private static final List<Long> TWO_DRYING_QUESTIONS = List.of(267L, 268L);
    private static final List<Long> DRYING_PERFORM_QUESTIONS = List.of(257L, 258L);

    private static final List<Long> MATERIAL_QUESTIONS = List.of(270L, 271L, 272L);
    private static final List<Long> DIPPING_MATERIAL_QUESTIONS = List.of(269L);

    private boolean checkPreviousQuestionAnswerAccordingly(PlantationProductQuestion question) {//TODO ilk soruya cevap vermediyse hiç birini sorma
        Optional<PlantationProductQuestion> optionalQuestion = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> DRYING_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (DRYING_PERFORM_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(185L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        optionalQuestion = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> DRYING_PERFORM_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (ONE_DRYING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(187L, 189L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (TWO_DRYING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(188L, 190L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        optionalQuestion = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> DIPPING_MATERIAL_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (MATERIAL_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(191L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        return true;
    }

    @Autowired
    private CostCalculationService costCalculationService;

    public void nextSaveExpense() throws IOException {
        super.getParcelPlan().setDryingCost(costCalculationService.calculateProcessDryCost(super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList(), super.getParcelPlan().getId()));
        goToNextPage(EnumPlantationQuestionType.EXPENSE_DRYING);
    }

    public void previousSaveExpense() throws IOException {
        goToPreviousPage(EnumPlantationQuestionType.EXPENSE_DRYING);
    }

}

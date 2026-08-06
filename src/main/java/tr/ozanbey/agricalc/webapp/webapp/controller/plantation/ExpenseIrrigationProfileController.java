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
public class ExpenseIrrigationProfileController extends PlanProfileController {

    @Autowired
    private UserPlantParcelPlanService userPlantParcelPlanService;

    @Autowired
    private PlantationProductService productService;

    @PostConstruct
    public void init() {
    }

    public void fillExpenseIrrigationQuestionList() throws IOException {
        if (super.getParcelPlanId() == null || !checkPlanIdForUser(super.getParcelPlanId())) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }

        if (!Hibernate.isInitialized(super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = productService.getActiveQuestionByQuestionType(super.getParcelPlan().getPlantParcel().getProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_IRRIGATION);
            List<UserPlantParcelPlanAnswer> planAnswerList = userPlantParcelPlanService.fillPlanAnswerValues(super.getParcelPlanId(), EnumPlantationQuestionType.EXPENSE_IRRIGATION);
            List<UserPlantParcelAnswer> parcelAnswerList = userPlantParcelPlanService.fillParcelAnswerValues(super.getParcelPlan().getPlantParcel().getId(), EnumPlantationQuestionType.EXPENSE_IRRIGATION);
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

    private static final List<Long> NOT_IRRIGATION_QUESTIONS = List.of(151L);
    private static final List<Long> IRRIGATION_QUESTIONS = List.of(148L, 149L, 150L);

    private static final List<Long> A_WATER_QUESTIONS = List.of(152L);
    private static final List<Long> B_WATER_QUESTIONS = List.of(163L);
    private static final List<Long> WATER_SOURCE_QUESTIONS = List.of(151L);

    private static final List<Long> PRESSURE_QUESTIONS = List.of(153L);
    private static final List<Long> WATER_PRESSURE_QUESTIONS = List.of(152L);

    private static final List<Long> FIRST_A_PAYMENT_QUESTIONS = List.of(154L, 155L, 156L);
    private static final List<Long> FIRST_B_PAYMENT_QUESTIONS = List.of(157L, 158L, 159L);
    private static final List<Long> SECOND_PAYMENT_QUESTIONS = List.of(160L, 161L, 162L);
    private static final List<Long> WATER_PAYMENT_QUESTIONS = List.of(153L);

    private static final List<Long> FIRST_PUMP_QUESTIONS = List.of(164L, 165L, 166L, 167L, 168L);
    private static final List<Long> SECOND_PUMP_QUESTIONS = List.of(169L, 170L, 171L, 172L, 173L);
    private static final List<Long> PUMP_MOTOR_QUESTIONS = List.of(163L);

    private boolean checkPreviousQuestionAnswerAccordingly(PlantationProductQuestion question) {
        Optional<PlantationProductQuestion> optionalQuestion = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> IRRIGATION_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (NOT_IRRIGATION_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(122L, 123L, 124L, 125L, 126L, 128L, 129L, 130L, 131L, 133L, 134L, 135L, 136L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        optionalQuestion = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> WATER_SOURCE_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (A_WATER_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(138L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (B_WATER_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(139L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        Optional<PlantationProductQuestion> optionalQuestion2 = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> WATER_PRESSURE_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (PRESSURE_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion2.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(140L, 141L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        optionalQuestion = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> WATER_PAYMENT_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (FIRST_A_PAYMENT_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (optionalQuestion2.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(140L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true)) {
                return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(142L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
            }
            return false;
        } else if (FIRST_B_PAYMENT_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (optionalQuestion2.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(141L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true)) {
                return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(142L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
            }
            return false;
        } else if (SECOND_PAYMENT_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(143L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        optionalQuestion = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> PUMP_MOTOR_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (FIRST_PUMP_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(144L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (SECOND_PUMP_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(145L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        return true;
    }

    @Autowired
    private CostCalculationService costCalculationService;

    public void nextSaveExpense() throws IOException {
        super.getParcelPlan().setIrrigationCost(costCalculationService.calculateIrrigationCost(super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList(), super.getParcelPlan().getId()));
        goToNextPage(EnumPlantationQuestionType.EXPENSE_IRRIGATION);
    }

    public void previousSaveExpense() throws IOException {
        goToPreviousPage(EnumPlantationQuestionType.EXPENSE_IRRIGATION);
    }

}

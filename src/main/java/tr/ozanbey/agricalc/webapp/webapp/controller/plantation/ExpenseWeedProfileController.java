package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantPlanAnswer;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlan;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionAnswerType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.CostCalculationService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationPlanService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationProductService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@ViewScoped
@Getter
@Setter
public class ExpenseWeedProfileController extends BaseController {

    @Autowired
    private PlantationPlanService plantationPlanService;

    @Autowired
    private PlantationProductService productService;

    private Long parcelPlanId;
    private UserPlantationPlan plantationPlan;

    @PostConstruct
    public void init() {
    }

    public void setParcelPlanId(Long parcelPlanId) {
        if (Objects.equals(this.parcelPlanId, parcelPlanId)) {
            return;
        }
        this.parcelPlanId = parcelPlanId;
    }

    public void fillExpenseWeedQuestionList() throws IOException {
        if (parcelPlanId == null || !checkPlanIdForUser(parcelPlanId)) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }

        if (!Hibernate.isInitialized(plantationPlan.getProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = productService.getActiveQuestionByQuestionType(plantationPlan.getProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_WEED);
            List<UserPlantPlanAnswer> planAnswerList = plantationPlanService.fillPlanAnswerValues(parcelPlanId, EnumPlantationQuestionType.EXPENSE_WEED);
            for (UserPlantPlanAnswer userPlantPlanAnswer : planAnswerList) {
                for (PlantationProductQuestion productQuestion : productQuestionList) {
                    if (Objects.equals(userPlantPlanAnswer.getProductQuestion().getId(), productQuestion.getId())) {
                        if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_MENU)) {
                            productQuestion.setSelectedAnswerId(Long.valueOf(userPlantPlanAnswer.getAnswerValue()));
                        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_RADIO)) {
                            productQuestion.setSelectedAnswerId(Long.valueOf(userPlantPlanAnswer.getAnswerValue()));
                        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_MANY_CHECKBOX)) {
                            if (productQuestion.getSelectedAnswerIds() == null || productQuestion.getSelectedAnswerIds().isEmpty()) {
                                productQuestion.setSelectedAnswerIds(new ArrayList<>());
                            }
                            productQuestion.getSelectedAnswerIds().add(Long.valueOf(userPlantPlanAnswer.getAnswerValue()));
                        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_INTEGER)) {
                            productQuestion.setIntegerValue(Integer.valueOf(userPlantPlanAnswer.getAnswerValue()));
                        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DOUBLE)) {
                            productQuestion.setDoubleValue(Double.valueOf(userPlantPlanAnswer.getAnswerValue()));
                        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DECIMAL)) {
                            productQuestion.setBigDecimalValue(new BigDecimal(userPlantPlanAnswer.getAnswerValue()));
                        }
                    }
                }
            }
            plantationPlan.getProduct().setProductQuestionList(productQuestionList);
        }
    }

    private boolean checkPlanIdForUser(Long parcelPlanId) {
        Optional<UserPlantationPlan> optionalPlan = plantationPlanService.getPlantPlanByIdAndUserId(parcelPlanId, getCurrentUser().getUser().getId());
        if (optionalPlan.isPresent()) {
            plantationPlan = optionalPlan.get();
            return true;
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


    private static final List<Long> A_THROAT_QUESTIONS = List.of(119L);
    private static final List<Long> B_THROAT_QUESTIONS = List.of(120L);
    private static final List<Long> THROAT_FILLING_QUESTIONS = List.of(118L);

    private static final List<Long> A_WEED_QUESTIONS = List.of(128L);
    private static final List<Long> B_WEED_QUESTIONS = List.of(136L);
    private static final List<Long> C_WEED_QUESTIONS = List.of(137L, 138L);
    private static final List<Long> D_WEED_QUESTIONS = List.of(139L, 140L, 141L, 142L);
    private static final List<Long> E_WEED_QUESTIONS = List.of(143L, 144L, 145L);
    private static final List<Long> K_WEED_QUESTIONS = List.of(146L, 147L);
    private static final List<Long> WEED_CONTROL_QUESTIONS = List.of(121L, 122L, 123L, 124L, 125L, 126L, 127L);

    private static final List<Long> FIRST_TILLING_QUESTIONS = List.of(129L, 130L);
    private static final List<Long> SECOND_TILLING_QUESTIONS = List.of(131L, 132L);
    private static final List<Long> THIRD_TILLING_QUESTIONS = List.of(133L, 134L, 135L);
    private static final List<Long> TILLING_QUESTIONS = List.of(128L);

    private boolean checkPreviousQuestionAnswerAccordingly(PlantationProductQuestion question) {
        Optional<PlantationProductQuestion> optionalQuestion = plantationPlan.getProduct().getProductQuestionList().stream().filter(pq -> THROAT_FILLING_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (A_THROAT_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(94L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (B_THROAT_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(95L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        optionalQuestion = plantationPlan.getProduct().getProductQuestionList().stream().filter(pq -> WEED_CONTROL_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (A_WEED_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(97L, 102L, 106L, 109L, 112L, 115L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (B_WEED_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(98L, 103L, 107L, 110L, 113L, 116L, 117L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (C_WEED_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(99L, 108L, 118L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (D_WEED_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(100L, 114L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (E_WEED_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(101L, 104L, 111L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (K_WEED_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(105L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        }
        optionalQuestion = plantationPlan.getProduct().getProductQuestionList().stream().filter(pq -> TILLING_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (FIRST_TILLING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(119L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (SECOND_TILLING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(120L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (THIRD_TILLING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(121L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        }
        return true;
    }

    @Autowired
    private CostCalculationService costCalculationService;

    public void nextSaveExpense() throws IOException {
        plantationPlanService.savePlanAnswers(plantationPlan, EnumPlantationQuestionType.EXPENSE_WEED);
        plantationPlan.setWeedControlCost(costCalculationService.calculateWildGrassControlCost(plantationPlan.getProduct().getProductQuestionList()));
        plantationPlanService.updatePlantationPlanIncome(plantationPlan);
        super.navigationController.redirectToUrl("/secured/plantation/expense-irrigation-profile?parcelPlanId=" + parcelPlanId);
    }

    public void previousSaveExpense() throws IOException {
        plantationPlanService.savePlanAnswers(plantationPlan, EnumPlantationQuestionType.EXPENSE_WEED);
        super.navigationController.redirectToUrl("/secured/plantation/expense-fertilizer-profile?parcelPlanId=" + parcelPlanId);
    }

}

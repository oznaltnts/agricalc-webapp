package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationQuestionOption;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelAnswer;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelPlanAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionAnswerType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.CostCalculationService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationProductService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantParcelPlanService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@ViewScoped
@Getter
@Setter
public class ExpenseProtectionProfileController extends PlanProfileController {

    @Autowired
    private UserPlantParcelPlanService userPlantParcelPlanService;

    @Autowired
    private PlantationProductService productService;

    @PostConstruct
    public void init() {
    }

    public void fillExpenseProtectionQuestionList() throws IOException {
        if (super.getParcelPlanId() == null || !checkPlanIdForUser(super.getParcelPlanId())) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }

        if (!Hibernate.isInitialized(super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = productService.getActiveQuestionByQuestionType(super.getParcelPlan().getPlantParcel().getProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_PROTECTION);
            List<UserPlantParcelPlanAnswer> planAnswerList = userPlantParcelPlanService.fillPlanAnswerValues(super.getParcelPlanId(), EnumPlantationQuestionType.EXPENSE_PROTECTION);
            List<UserPlantParcelAnswer> parcelAnswerList = userPlantParcelPlanService.fillParcelAnswerValues(super.getParcelPlan().getPlantParcel().getId(), EnumPlantationQuestionType.EXPENSE_PROTECTION);
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

    public Map<Long, String> manyCheckboxSelectItems(PlantationProductQuestion question) {
        Map<Long, String> returnValue = new HashMap<>();
        if (question.getPlantationQuestion().getId().equals(194L)) {
            returnValue.put(1L, "Mantari hastalık");
        } else if (question.getPlantationQuestion().getId().equals(196L)) {
            returnValue.put(1L, "Zararlı böcekler");
            returnValue.put(2L, "Kırmızı örümcek");
        } else {
            returnValue = question.getPlantationQuestion().getQuestionOptionList().stream().collect(Collectors.toMap(AbstractEntity::getId, PlantationQuestionOption::getValue));
        }
        return returnValue;
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

    public boolean decimalRenderer(PlantationProductQuestion question) {
        if (question.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DECIMAL)) {
            if (checkPreviousQuestionAnswerAccordingly(question))
                return true;
            question.setBigDecimalValue(null);
            return false;
        }
        return false;
    }


    private static final List<Long> FUNGUS_QUESTIONS = List.of(195L);
    private static final List<Long> DISEASES_QUESTIONS = List.of(194L);

    private static final List<Long> INSECT_QUESTIONS = List.of(197L);
    private static final List<Long> RED_SPIDER_QUESTIONS = List.of(198L);
    private static final List<Long> PESTS_QUESTIONS = List.of(196L);

    private static final List<Long> A_PEST_QUESTIONS = List.of(203L);
    private static final List<Long> B_PEST_QUESTIONS = List.of(204L);
    private static final List<Long> C_PEST_QUESTIONS = List.of(205L);
    private static final List<Long> D_PEST_QUESTIONS = List.of(206L, 207L);
    private static final List<Long> PEST_CONTROL_QUESTIONS = List.of(202L);

    private boolean checkPreviousQuestionAnswerAccordingly(PlantationProductQuestion question) {
        Optional<PlantationProductQuestion> optionalQuestion = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> DISEASES_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (FUNGUS_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(1L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        }

        optionalQuestion = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> PESTS_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (INSECT_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(1L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (RED_SPIDER_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(2L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        }

        optionalQuestion = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> PEST_CONTROL_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (A_PEST_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(148L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (B_PEST_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(149L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (C_PEST_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(150L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        } else if (D_PEST_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerIds() != null && Stream.of(151L).anyMatch(plantationProductQuestion.getSelectedAnswerIds()::contains)).orElse(true);
        }
        return true;
    }

    @Autowired
    private CostCalculationService costCalculationService;

    public void nextSaveExpense() throws IOException {
        super.getParcelPlan().setProtectionCost(costCalculationService.calculatePlantProtectionCost(super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList(), super.getParcelPlan().getId(), super.getParcelPlan().getPlantParcel().getCity()));
        goToNextPage(EnumPlantationQuestionType.EXPENSE_PROTECTION);
    }

    public void previousSaveExpense() throws IOException {
        goToPreviousPage(EnumPlantationQuestionType.EXPENSE_PROTECTION);
    }

}

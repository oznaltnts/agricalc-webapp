package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestionDisease;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationAnswer;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlanAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionAnswerType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@ViewScoped
@Getter
@Setter
public class ExpenseProtectionProfileController extends PlanProfileController {


    private static final List<Long> A_PEST_QUESTIONS = List.of(203L);
    private static final List<Long> B_PEST_QUESTIONS = List.of(204L);
    private static final List<Long> C_PEST_QUESTIONS = List.of(205L);
    private static final List<Long> D_PEST_QUESTIONS = List.of(206L, 207L);
    private static final List<Long> PEST_CONTROL_QUESTIONS = List.of(202L);

    private final CostMedicineService costMedicineService;

    public ExpenseProtectionProfileController(UserPlantationPlanService userPlantationPlanService,
                                              QuestionService questionService,
                                              CostCalculationService costCalculationService,
                                              CostAllocationService costAllocationService,
                                              CostMedicineService costMedicineService) {
        super(userPlantationPlanService, questionService, costCalculationService, costAllocationService);
        this.costMedicineService = costMedicineService;
    }

    private static final List<Long> DISEASE_QUESTIONS = List.of(194L, 195L, 196L, 197L, 198L, 199L, 200L, 201L);
    private List<PlantationProductQuestion> diseaseQuestionList;

    @PostConstruct
    public void init() {
    }

    public void fillExpenseProtectionQuestionList() throws IOException {
        if (super.getPlantationPlanId() == null || !checkPlanIdForUser(super.getPlantationPlanId())) {
            super.navigationController.redirectToUrl("/secured/plantation-list");
            return;
        }
        diseaseQuestionList = getQuestionService().getDiseaseListByProductIdAndQuestionIdList(super.getUserPlantationPlan().getPrimaryProduct().getId(), DISEASE_QUESTIONS, EnumStatus.ACTIVE);

        if (!Hibernate.isInitialized(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = getQuestionService().getActiveQuestionByQuestionType(super.getUserPlantationPlan().getPrimaryProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_PROTECTION);
            productQuestionList.removeIf(productQuestion -> DISEASE_QUESTIONS.contains(productQuestion.getPlantationQuestion().getId()));
            List<UserPlantationPlanAnswer> planAnswerList = getUserPlantationPlanService().fillPlanAnswerValues(super.getPlantationPlanId(), EnumPlantationQuestionType.EXPENSE_PROTECTION);
            List<UserPlantationAnswer> plantationAnswerList = getUserPlantationPlanService().fillPlantationAnswerValues(super.getUserPlantationPlan().getUserPlantation().getId(), EnumPlantationQuestionType.EXPENSE_PROTECTION);
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
        Optional<PlantationProductQuestion> optionalQuestion = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                .filter(pq -> PEST_CONTROL_QUESTIONS.contains(pq.getPlantationQuestion().getId()))
                .findFirst();
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

    public void nextSaveExpense() throws IOException {
        super.getUserPlantationPlan().setProtectionCost(getCostCalculationService().calculatePlantProtectionCost(
                super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList(),
                super.getUserPlantationPlan().getId(),
                super.getUserPlantationPlan().getUserPlantation().getDistrict().getCity()));
        getCostAllocationService().protectionAllocation(
                super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList(),
                super.getUserPlantationPlan(),
                super.getUserPlantationPlan().getUserPlantation().getDistrict().getCity());
        goToNextPage(EnumPlantationQuestionType.EXPENSE_PROTECTION);
    }

    public void previousSaveExpense() throws IOException {
        goToPreviousPage(EnumPlantationQuestionType.EXPENSE_PROTECTION);
    }

    public void handleDiseaseSelect(PlantationProductQuestion diseaseQuestion) {
        List<PlantationProductQuestionDisease> newSelectedList = new ArrayList<>();
        for (PlantationProductQuestionDisease productQuestionDisease : diseaseQuestion.getProductQuestionDiseaseList()) {
            if (diseaseQuestion.getSelectedAnswerIds().contains(productQuestionDisease.getId())) {
                newSelectedList.add(productQuestionDisease);
            }
        }
        diseaseQuestion.setSelectedDiseaseList(newSelectedList);
    }
}

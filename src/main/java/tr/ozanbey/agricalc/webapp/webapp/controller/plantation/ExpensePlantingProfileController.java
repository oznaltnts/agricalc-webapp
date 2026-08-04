package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelPlan;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelPlanAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionAnswerType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.CostCalculationService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantParcelPlanService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationProductService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class ExpensePlantingProfileController extends BaseController {

    @Autowired
    private PlantParcelPlanService plantParcelPlanService;

    @Autowired
    private PlantationProductService productService;

    private Long parcelPlanId;
    private UserPlantParcelPlan parcelPlan;

    @PostConstruct
    public void init() {
    }

    public void setParcelPlanId(Long parcelPlanId) {
        if (Objects.equals(this.parcelPlanId, parcelPlanId)) {
            return;
        }
        this.parcelPlanId = parcelPlanId;
    }

    public void fillExpensePlantingQuestionList() throws IOException {
        if (parcelPlanId == null || !checkPlanIdForUser(parcelPlanId)) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }

        if (!Hibernate.isInitialized(parcelPlan.getPlantParcel().getProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = productService.getActiveQuestionByQuestionType(parcelPlan.getPlantParcel().getProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_PLANTING);
            List<UserPlantParcelPlanAnswer> planAnswerList = plantParcelPlanService.fillPlanAnswerValues(parcelPlanId, EnumPlantationQuestionType.EXPENSE_PLANTING);
            for (UserPlantParcelPlanAnswer userPlantParcelPlanAnswer : planAnswerList) {
                for (PlantationProductQuestion productQuestion : productQuestionList) {
                    if (Objects.equals(userPlantParcelPlanAnswer.getProductQuestion().getId(), productQuestion.getId())) {
                        if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_MENU)) {
                            productQuestion.setSelectedAnswerId(Long.valueOf(userPlantParcelPlanAnswer.getAnswerValue()));
                        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_RADIO)) {
                            productQuestion.setSelectedAnswerId(Long.valueOf(userPlantParcelPlanAnswer.getAnswerValue()));
                        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_MANY_CHECKBOX)) {
                            if (productQuestion.getSelectedAnswerIds() == null || productQuestion.getSelectedAnswerIds().isEmpty()) {
                                productQuestion.setSelectedAnswerIds(new ArrayList<>());
                            }
                            productQuestion.getSelectedAnswerIds().add(Long.valueOf(userPlantParcelPlanAnswer.getAnswerValue()));
                        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_INTEGER)) {
                            productQuestion.setIntegerValue(Integer.valueOf(userPlantParcelPlanAnswer.getAnswerValue()));
                        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DOUBLE)) {
                            productQuestion.setDoubleValue(Double.valueOf(userPlantParcelPlanAnswer.getAnswerValue()));
                        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DECIMAL)) {
                            productQuestion.setBigDecimalValue(new BigDecimal(userPlantParcelPlanAnswer.getAnswerValue()));
                        }
                    }
                }
            }
            parcelPlan.getPlantParcel().getProduct().setProductQuestionList(productQuestionList);
        }
    }

    private boolean checkPlanIdForUser(Long parcelPlanId) {
        Optional<UserPlantParcelPlan> optionalPlan = plantParcelPlanService.getPlantPlanByIdAndUserId(parcelPlanId, getCurrentUser().getUser().getId());
        if (optionalPlan.isPresent()) {
            parcelPlan = optionalPlan.get();
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
            if (List.of(84L).contains(question.getPlantationQuestion().getId()) && question.getMaximumValue().compareTo(BigDecimal.ONE) == 0) {
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

    private static final List<Long> A_MATERIAL_QUESTIONS = List.of(55L, 56L, 64L, 65L, 69L);
    private static final List<Long> B_MATERIAL_QUESTIONS = List.of(70L, 71L, 72L, 73L, 74L, 75L, 84L);
    private static final List<Long> C_MATERIAL_QUESTIONS = List.of(80L, 81L, 82L);
    private static final List<Long> D_MATERIAL_QUESTIONS = List.of(85L, 86L, 89L, 90L);
    private static final List<Long> MATERIAL_QUESTIONS = List.of(49L, 50L, 51L, 52L, 53L, 54L);

    private static final List<Long> A1_USAGE_QUESTIONS = List.of(57L, 61L);
    private static final List<Long> A2_USAGE_QUESTIONS = List.of(58L, 61L);
    private static final List<Long> A3_USAGE_QUESTIONS = List.of(59L, 62L);
    private static final List<Long> A4_USAGE_QUESTIONS = List.of(60L, 63L);
    private static final List<Long> SEED_USAGE_QUESTIONS = List.of(55L);

    private static final List<Long> SOW_1_QUESTIONS = List.of(66L);
    private static final List<Long> SOW_2_QUESTIONS = List.of(67L);
    private static final List<Long> SOW_3_QUESTIONS = List.of(68L);
    private static final List<Long> SEED_SOW_QUESTIONS = List.of(64L, 65L);

    private static final List<Long> B1_PLANTING_QUESTIONS = List.of(76L);
    private static final List<Long> B2_PLANTING_QUESTIONS = List.of(77L, 78L, 79L, 80L);
    private static final List<Long> PLANTING_QUESTIONS = List.of(75L);

    private static final List<Long> A_LUMP_SUPPLY_QUESTIONS = List.of(87L);
    private static final List<Long> B_LUMP_SUPPLY_QUESTIONS = List.of(88L);
    private static final List<Long> LUMP_SUPPLY_QUESTIONS = List.of(86L);

    private boolean checkPreviousQuestionAnswerAccordingly(PlantationProductQuestion question) {
        Optional<PlantationProductQuestion> optionalQuestion = parcelPlan.getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> MATERIAL_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (A_MATERIAL_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(36L, 40L, 43L, 45L, 49L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (B_MATERIAL_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(37L, 41L, 44L, 47L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (C_MATERIAL_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(38L, 42L, 46L, 48L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (D_MATERIAL_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(39L, 50L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        optionalQuestion = parcelPlan.getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> SEED_USAGE_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (A1_USAGE_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(51L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (A2_USAGE_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(52L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (A3_USAGE_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(53L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (A4_USAGE_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(54L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        optionalQuestion = parcelPlan.getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> SEED_SOW_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (SOW_1_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(57L, 60L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (SOW_2_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(58L, 61L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (SOW_3_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(59L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        optionalQuestion = parcelPlan.getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> PLANTING_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (B1_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(65L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (B2_PLANTING_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(66L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        optionalQuestion = parcelPlan.getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> LUMP_SUPPLY_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (A_LUMP_SUPPLY_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(69L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (B_LUMP_SUPPLY_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(70L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        return true;
    }

    @Autowired
    private CostCalculationService costCalculationService;

    public void nextSaveExpense() throws IOException {
        plantParcelPlanService.savePlanAnswers(parcelPlan, EnumPlantationQuestionType.EXPENSE_PLANTING);
        parcelPlan.setPlantingCost(costCalculationService.calculatePlantingCost(parcelPlan.getPlantParcel().getProduct().getProductQuestionList(), parcelPlan.getId()));
        plantParcelPlanService.updatePlantParcelPlanIncome(parcelPlan);
        super.navigationController.redirectToUrl("/secured/plantation/expense-fertilizer-profile?parcelPlanId=" + parcelPlanId);
    }

    public void previousSaveExpense() throws IOException {
        plantParcelPlanService.savePlanAnswers(parcelPlan, EnumPlantationQuestionType.EXPENSE_PLANTING);
        super.navigationController.redirectToUrl("/secured/plantation/expense-soil-profile?parcelPlanId=" + parcelPlanId);
    }

}

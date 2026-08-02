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

@Component
@ViewScoped
@Getter
@Setter
public class ExpenseHarvestProfileController extends BaseController {

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

    public void fillExpenseHarvestQuestionList() throws IOException {
        if (parcelPlanId == null || !checkPlanIdForUser(parcelPlanId)) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }

        if (!Hibernate.isInitialized(plantationPlan.getProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = productService.getActiveQuestionByQuestionType(plantationPlan.getProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_HARVEST);
            List<UserPlantPlanAnswer> planAnswerList = plantationPlanService.fillPlanAnswerValues(parcelPlanId, EnumPlantationQuestionType.EXPENSE_HARVEST);
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


    private static final List<Long> COLLECT_QUESTIONS = List.of(210L);

    private static final List<Long> A_HARVEST_QUESTIONS = List.of(219L, 220L, 221L, 222L, 223L, 224L, 225L, 226L, 227L, 228L);
    private static final List<Long> B_HARVEST_QUESTIONS = List.of(229L, 230L, 231L, 232L, 233L, 234L);
    private static final List<Long> C_HARVEST_QUESTIONS = List.of(235L, 236L, 237L, 238L, 239L, 240L);
    private static final List<Long> D_HARVEST_QUESTIONS = List.of(241L, 242L);
    private static final List<Long> E_HARVEST_QUESTIONS = List.of(243L, 244L);
    private static final List<Long> F_HARVEST_QUESTIONS = List.of(245L, 246L);
    private static final List<Long> HARVEST_QUESTIONS = List.of(211L, 212L, 213L, 214L, 215L, 216L, 217L, 218L);

    private boolean checkPreviousQuestionAnswerAccordingly(PlantationProductQuestion question) {
        Optional<PlantationProductQuestion> optionalQuestion = plantationPlan.getProduct().getProductQuestionList().stream().filter(pq -> COLLECT_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (HARVEST_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(155L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        optionalQuestion = plantationPlan.getProduct().getProductQuestionList().stream().filter(pq -> HARVEST_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (A_HARVEST_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(157L, 162L, 166L, 169L, 172L, 174L, 176L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (B_HARVEST_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(158L, 167L, 170L, 178L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (C_HARVEST_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(159L, 163L, 171L, 173L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (D_HARVEST_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(160L, 164L, 168L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (E_HARVEST_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(161L, 175L, 179L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (F_HARVEST_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(165L, 177L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        return true;
    }

    @Autowired
    private CostCalculationService costCalculationService;

    public void nextSaveExpense() throws IOException {
        plantationPlanService.savePlanAnswers(plantationPlan, EnumPlantationQuestionType.EXPENSE_HARVEST);
        plantationPlan.setHarvestCost(costCalculationService.calculateHarvestCost(plantationPlan.getProduct().getProductQuestionList()));
        plantationPlanService.updatePlantationPlanIncome(plantationPlan);
        super.navigationController.redirectToUrl("/secured/plantation/expense-blend-profile?parcelPlanId=" + parcelPlanId);
    }

    public void previousSaveExpense() throws IOException {
        plantationPlanService.savePlanAnswers(plantationPlan, EnumPlantationQuestionType.EXPENSE_HARVEST);
        super.navigationController.redirectToUrl("/secured/plantation/expense-protection-profile?parcelPlanId=" + parcelPlanId);
    }

}

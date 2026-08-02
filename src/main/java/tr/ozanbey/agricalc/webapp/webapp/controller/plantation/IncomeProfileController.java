package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumMonth;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionAnswerType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionRecordType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.IncomeCalculationService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationPlanService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationProductService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
@ViewScoped
@Getter
@Setter
public class IncomeProfileController extends BaseController {

    @Autowired
    private PlantationPlanService plantationPlanService;

    @Autowired
    private PlantationProductService productService;

    private Long parcelPlanId;
    private UserPlantationPlan plantationPlan;

    private Map<Long, String> productionTechniqueList;

    @PostConstruct
    public void init() {
    }

    public void setParcelPlanId(Long parcelPlanId) {
        if (Objects.equals(this.parcelPlanId, parcelPlanId)) {
            return;
        }
        this.parcelPlanId = parcelPlanId;
    }

    public void fillIncomeQuestionList() throws IOException {
        if (parcelPlanId == null || !checkPlanIdForUser(parcelPlanId)) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }
        if (141L == plantationPlan.getProduct().getId()) {
            productionTechniqueList = Map.of(1L, "Erken dönem", 2L, "Orta dönem", 3L, "Geç dönem");
        } else if (208L == plantationPlan.getProduct().getId()) {
            productionTechniqueList = Map.of(1L, "Yer bağ (Goble)", 2L, "Çift kollu cordon telli terbiye", 3L, "Tek kollu cordon telli terbiye", 4L, "T telli terbiye", 5L, "Pergola (Çardak sistemi)");
        } else if (209L == plantationPlan.getProduct().getId()) {
            productionTechniqueList = Map.of(1L, "Yer bağ (Goble)", 2L, "Çift kollu cordon telli terbiye", 3L, "Tek kollu cordon telli terbiye", 4L, "T telli terbiye", 5L, "Pergola (Çardak sistemi)", 6L, "Y sistemi", 7L, "V sistemi", 8L, "GDC sistemi");
        } else if (210L == plantationPlan.getProduct().getId()) {
            productionTechniqueList = Map.of(1L, "Yer bağ (Goble)", 2L, "Çift kollu cordon telli terbiye", 3L, "Tek kollu cordon telli terbiye", 4L, "T telli terbiye", 5L, "Tek guyot sistemi", 6L, "Çift guyot sistemi");
        } else {
            productionTechniqueList = null;
        }

        if (!Hibernate.isInitialized(plantationPlan.getProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = productService.getActiveQuestionByQuestionType(plantationPlan.getProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.INCOME);
            List<UserPlantPlanAnswer> planAnswerList = plantationPlanService.fillPlanAnswerValues(parcelPlanId, EnumPlantationQuestionType.INCOME);
            List<UserParcelAnswer> parcelAnswerList = plantationPlanService.fillParcelAnswerValues(plantationPlan.getPlantParcel().getId(), EnumPlantationQuestionType.INCOME);
            for (UserPlantPlanAnswer userPlantPlanAnswer : planAnswerList) {
                fillAnsweredQuestionValues(userPlantPlanAnswer.getProductQuestion().getId(), userPlantPlanAnswer.getAnswerValue(), productQuestionList);
            }
            for (UserParcelAnswer userParcelAnswer : parcelAnswerList) {
                fillAnsweredQuestionValues(userParcelAnswer.getProductQuestion().getId(), userParcelAnswer.getAnswerValue(), productQuestionList);
            }
            plantationPlan.getProduct().setProductQuestionList(productQuestionList);
        }

    }

    private void fillAnsweredQuestionValues(Long questionId, String answerValue, List<PlantationProductQuestion> productQuestionList) {
        for (PlantationProductQuestion productQuestion : productQuestionList) {
            if (Objects.equals(questionId, productQuestion.getId())) {
                if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_MENU)) {
                    productQuestion.setSelectedAnswerId(Long.valueOf(answerValue));
                } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_RADIO)) {
                    productQuestion.setSelectedAnswerId(Long.valueOf(answerValue));
                } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_MANY_CHECKBOX)) {
                    if (productQuestion.getSelectedAnswerIds() == null || productQuestion.getSelectedAnswerIds().isEmpty()) {
                        productQuestion.setSelectedAnswerIds(new ArrayList<>());
                    }
                    productQuestion.getSelectedAnswerIds().add(Long.valueOf(answerValue));
                } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_INTEGER)) {
                    productQuestion.setIntegerValue(Integer.valueOf(answerValue));
                } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DOUBLE)) {
                    productQuestion.setDoubleValue(Double.valueOf(answerValue));
                } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DECIMAL)) {
                    productQuestion.setBigDecimalValue(new BigDecimal(answerValue));
                }
            }
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

    public boolean oneMenuRenderer(PlantationProductQuestion question) {
        if (question.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_MENU)) {
            if (checkPreviousQuestionAnswerAccordingly(question))
                return true;
            question.setSelectedAnswerId(null);
            return false;
        }
        return false;
    }

    public Map<Long, String> oneMenuSelectItems(PlantationProductQuestion question) {
        if (List.of(1L, 7L).contains(question.getPlantationQuestion().getId())) {
            if (!Hibernate.isInitialized(plantationPlan.getProduct().getProductOptionList())) {
                List<PlantationProductOption> productOptionList = productService.getProductOption(plantationPlan.getProduct().getId());
                plantationPlan.getProduct().setProductOptionList(productOptionList);
            }
            return plantationPlan.getProduct().getProductOptionList().stream().collect(Collectors.toMap(AbstractEntity::getId, PlantationProductOption::getName));
        } else if (List.of(3L).contains(question.getPlantationQuestion().getId())) {
            return productionTechniqueList;
        } else {
            return Arrays.stream(EnumMonth.values()).collect(Collectors.toMap(e -> Long.valueOf(e.getValue()), e -> e.name())); //TODO JSFUtils.getLocaleMessage()
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

    public boolean booleanCheckboxRenderer(PlantationProductQuestion question) {
        return question.getPlantationQuestion().getRecordType().equals(EnumQuestionRecordType.ASK_USER);
    }

    private static final List<Long> A_DOUBLE_YIELD_QUESTIONS = List.of(11L, 12L, 13L, 14L, 17L, 27L, 30L);
    private static final List<Long> B_DOUBLE_YIELD_QUESTIONS = List.of(15L, 28L, 31L);
    private static final List<Long> C_DOUBLE_YIELD_QUESTIONS = List.of(16L, 29L, 32L);
    private static final List<Long> SALE_QUESTIONS = List.of(8L, 9L, 10L);

    private boolean checkPreviousQuestionAnswerAccordingly(PlantationProductQuestion question) {
        Optional<PlantationProductQuestion> optionalSaleQuestion = plantationPlan.getProduct().getProductQuestionList().stream().filter(pq -> SALE_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
        if (A_DOUBLE_YIELD_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalSaleQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(17L, 21L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (B_DOUBLE_YIELD_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalSaleQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(18L, 19L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        } else if (C_DOUBLE_YIELD_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            return optionalSaleQuestion.map(plantationProductQuestion -> plantationProductQuestion.getSelectedAnswerId() != null && List.of(20L, 22L).contains(plantationProductQuestion.getSelectedAnswerId())).orElse(true);
        }
        return true;
    }

    @Autowired
    private IncomeCalculationService incomeCalculationService;

    public void nextSaveIncome() throws IOException {
        plantationPlanService.savePlanAnswers(plantationPlan, EnumPlantationQuestionType.INCOME);
        plantationPlan.setGrossIncome(incomeCalculationService.calculateIncome(plantationPlan.getProduct().getProductQuestionList()));
        plantationPlanService.updatePlantationPlanIncome(plantationPlan);
        super.navigationController.redirectToUrl("/secured/plantation/expense-soil-profile?parcelPlanId=" + parcelPlanId);
    }

    public void previousSaveIncome() throws IOException {
        plantationPlanService.savePlanAnswers(plantationPlan, EnumPlantationQuestionType.INCOME);
        super.navigationController.redirectToUrl("/secured/plantation/parcel-plan?parcelId=" + plantationPlan.getPlantParcel().getId());
    }
}

package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductOption;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelAnswer;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelPlanAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumMonth;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionAnswerType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.IncomeCalculationService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationProductService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantParcelPlanService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@ViewScoped
@Getter
@Setter
public class IncomeProfileController extends PlanProfileController {

    @Autowired
    private UserPlantParcelPlanService userPlantParcelPlanService;

    @Autowired
    private PlantationProductService productService;

    private Map<Long, String> productionTechniqueList;

    @PostConstruct
    public void init() {
    }

    public void fillIncomeQuestionList() throws IOException {
        if (super.getParcelPlanId() == null || !checkPlanIdForUser(super.getParcelPlanId())) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }
        if (141L == super.getParcelPlan().getPlantParcel().getProduct().getId()) {
            productionTechniqueList = Map.of(1L, "Erken dönem", 2L, "Orta dönem", 3L, "Geç dönem");
        } else if (208L == super.getParcelPlan().getPlantParcel().getProduct().getId()) {
            productionTechniqueList = Map.of(1L, "Yer bağ (Goble)", 2L, "Çift kollu cordon telli terbiye", 3L, "Tek kollu cordon telli terbiye", 4L, "T telli terbiye", 5L, "Pergola (Çardak sistemi)");
        } else if (209L == super.getParcelPlan().getPlantParcel().getProduct().getId()) {
            productionTechniqueList = Map.of(1L, "Yer bağ (Goble)", 2L, "Çift kollu cordon telli terbiye", 3L, "Tek kollu cordon telli terbiye", 4L, "T telli terbiye", 5L, "Pergola (Çardak sistemi)", 6L, "Y sistemi", 7L, "V sistemi", 8L, "GDC sistemi");
        } else if (210L == super.getParcelPlan().getPlantParcel().getProduct().getId()) {
            productionTechniqueList = Map.of(1L, "Yer bağ (Goble)", 2L, "Çift kollu cordon telli terbiye", 3L, "Tek kollu cordon telli terbiye", 4L, "T telli terbiye", 5L, "Tek guyot sistemi", 6L, "Çift guyot sistemi");
        } else {
            productionTechniqueList = null;
        }

        if (!Hibernate.isInitialized(super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = productService.getActiveQuestionByQuestionType(super.getParcelPlan().getPlantParcel().getProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.INCOME);
            List<UserPlantParcelPlanAnswer> planAnswerList = userPlantParcelPlanService.fillPlanAnswerValues(super.getParcelPlanId(), EnumPlantationQuestionType.INCOME);
            List<UserPlantParcelAnswer> parcelAnswerList = userPlantParcelPlanService.fillParcelAnswerValues(super.getParcelPlan().getPlantParcel().getId(), EnumPlantationQuestionType.INCOME);
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

    public Map<Long, String> oneMenuSelectItems(PlantationProductQuestion question) {
        if (List.of(1L, 7L).contains(question.getPlantationQuestion().getId())) {
            if (!Hibernate.isInitialized(super.getParcelPlan().getPlantParcel().getProduct().getProductOptionList())) {
                List<PlantationProductOption> productOptionList = productService.getProductOption(super.getParcelPlan().getPlantParcel().getProduct().getId());
                super.getParcelPlan().getPlantParcel().getProduct().setProductOptionList(productOptionList);
            }
            return super.getParcelPlan().getPlantParcel().getProduct().getProductOptionList().stream().collect(Collectors.toMap(AbstractEntity::getId, PlantationProductOption::getName));
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

    private static final List<Long> A_DOUBLE_YIELD_QUESTIONS = List.of(11L, 12L, 13L, 14L, 17L, 27L, 30L);
    private static final List<Long> B_DOUBLE_YIELD_QUESTIONS = List.of(15L, 28L, 31L);
    private static final List<Long> C_DOUBLE_YIELD_QUESTIONS = List.of(16L, 29L, 32L);
    private static final List<Long> SALE_QUESTIONS = List.of(8L, 9L, 10L);

    private boolean checkPreviousQuestionAnswerAccordingly(PlantationProductQuestion question) {
        Optional<PlantationProductQuestion> optionalSaleQuestion = super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList().stream().filter(pq -> SALE_QUESTIONS.contains(pq.getPlantationQuestion().getId())).findFirst();
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
        super.getParcelPlan().setGrossIncome(incomeCalculationService.calculateIncome(super.getParcelPlan().getPlantParcel().getProduct().getProductQuestionList()));
        goToNextPage(EnumPlantationQuestionType.INCOME);
    }

    public void previousSaveIncome() throws IOException {
        goToPreviousPage(EnumPlantationQuestionType.INCOME);
    }
}

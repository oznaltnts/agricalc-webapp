package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductOption;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationAnswer;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlanAnswer;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumMonth;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionAnswerType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.*;
import tr.ozanbey.agricalc.webapp.webapp.util.JSFUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ViewScoped
@Getter
@Setter
public class IncomeProfileController extends PlanProfileController {

    private static final List<Long> A_DOUBLE_YIELD_QUESTIONS = List.of(11L, 12L, 13L, 14L, 17L, 27L, 30L);
    private static final List<Long> B_DOUBLE_YIELD_QUESTIONS = List.of(15L, 28L, 31L);
    private static final List<Long> C_DOUBLE_YIELD_QUESTIONS = List.of(16L, 29L, 32L);
    private static final List<Long> SALE_QUESTIONS = List.of(8L, 9L, 10L);
    private static final List<Long> LOW_QUALITY_PRICE_QUESTIONS = List.of(33L);
    private static final List<Long> FRUIT_JUICE_PRICE_QUESTIONS = List.of(34L);
    private static final List<Long> SIDE_STRAW_PRICE_QUESTIONS = List.of(35L, 36L);
    private final ProductService productService;
    private final IncomeCalculationService incomeCalculationService;
    private Map<Long, String> productionTechniqueList;

    public IncomeProfileController(UserPlantationPlanService userPlantationPlanService,
                                   QuestionService questionService,
                                   CostCalculationService costCalculationService,
                                   CostAllocationService costAllocationService, ProductService productService, IncomeCalculationService incomeCalculationService) {
        super(userPlantationPlanService, questionService, costCalculationService, costAllocationService);
        this.productService = productService;
        this.incomeCalculationService = incomeCalculationService;
    }

    public void fillIncomeQuestionList() throws IOException {
        if (super.getPlantationPlanId() == null || !checkPlanIdForUser(super.getPlantationPlanId())) {
            super.navigationController.redirectToUrl("/secured/plantation-list");
            return;
        }
        if (141L == super.getUserPlantationPlan().getPrimaryProduct().getId()) {
            productionTechniqueList = Map.of(1L, "Erken dönem", 2L, "Orta dönem", 3L, "Geç dönem");
        } else if (208L == super.getUserPlantationPlan().getPrimaryProduct().getId()) {
            productionTechniqueList = Map.of(1L, "Yer bağ (Goble)", 2L, "Çift kollu cordon telli terbiye", 3L, "Tek kollu cordon telli terbiye", 4L, "T telli terbiye", 5L, "Pergola (Çardak sistemi)");
        } else if (209L == super.getUserPlantationPlan().getPrimaryProduct().getId()) {
            productionTechniqueList = Map.of(1L, "Yer bağ (Goble)", 2L, "Çift kollu cordon telli terbiye", 3L, "Tek kollu cordon telli terbiye", 4L, "T telli terbiye", 5L, "Pergola (Çardak sistemi)", 6L, "Y sistemi", 7L, "V sistemi", 8L, "GDC sistemi");
        } else if (210L == super.getUserPlantationPlan().getPrimaryProduct().getId()) {
            productionTechniqueList = Map.of(1L, "Yer bağ (Goble)", 2L, "Çift kollu cordon telli terbiye", 3L, "Tek kollu cordon telli terbiye", 4L, "T telli terbiye", 5L, "Tek guyot sistemi", 6L, "Çift guyot sistemi");
        } else {
            productionTechniqueList = null;
        }

        if (!Hibernate.isInitialized(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = getQuestionService().getActiveQuestionByQuestionType(super.getUserPlantationPlan().getPrimaryProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.INCOME);
            List<UserPlantationPlanAnswer> planAnswerList = getUserPlantationPlanService().fillPlanAnswerValues(super.getPlantationPlanId(), EnumPlantationQuestionType.INCOME);
            List<UserPlantationAnswer> plantationAnswerList = getUserPlantationPlanService().fillPlantationAnswerValues(super.getUserPlantationPlan().getUserPlantation().getId(), EnumPlantationQuestionType.INCOME);
            assignAnswerListsToProduct(planAnswerList, productQuestionList, plantationAnswerList);
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
            if (!Hibernate.isInitialized(super.getUserPlantationPlan().getPrimaryProduct().getProductOptionList())) {
                List<PlantationProductOption> productOptionList = productService.getProductOption(super.getUserPlantationPlan().getPrimaryProduct().getId());
                super.getUserPlantationPlan().getPrimaryProduct().setProductOptionList(productOptionList);
            }
            return super.getUserPlantationPlan().getPrimaryProduct().getProductOptionList().stream().collect(Collectors.toMap(AbstractEntity::getId, PlantationProductOption::getName));
        } else if (List.of(3L).contains(question.getPlantationQuestion().getId())) {
            return productionTechniqueList;
        } else {
            return Arrays.stream(EnumMonth.values()).collect(Collectors.toMap(e -> Long.valueOf(e.getValue()), e -> JSFUtils.getLocaleMessage(e.name())));
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

    private boolean checkPreviousQuestionAnswerAccordingly(PlantationProductQuestion question) {
        List<PlantationProductQuestion> questionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                .filter(pq -> SALE_QUESTIONS.contains(pq.getPlantationQuestion().getId())).toList();
        if (A_DOUBLE_YIELD_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) {
                return true;
            }
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getSelectedAnswerId() != null && List.of(17L, 21L).contains(pq.getSelectedAnswerId())) {

                }
            }
            return false;
        } else if (B_DOUBLE_YIELD_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) {
                return true;
            }
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getSelectedAnswerId() != null && List.of(18L, 19L).contains(pq.getSelectedAnswerId())) {

                }
            }
            return false;
        } else if (C_DOUBLE_YIELD_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) {
                return true;
            }
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getSelectedAnswerId() != null && List.of(20L, 22L).contains(pq.getSelectedAnswerId())) {

                }
            }
            return false;
        }

        questionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                .filter(pq -> List.of(18L).contains(pq.getPlantationQuestion().getId())).toList();
        if (LOW_QUALITY_PRICE_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) {
                return true;
            }
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getDoubleValue() != null && pq.getDoubleValue() > 0) {
                    return true;
                }
            }
            return false;
        }
        questionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                .filter(pq -> List.of(19L).contains(pq.getPlantationQuestion().getId())).toList();
        if (FRUIT_JUICE_PRICE_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) {
                return true;
            }
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getDoubleValue() != null && pq.getDoubleValue() > 0) {
                    return true;
                }
            }
            return false;
        }
        questionList = super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList().stream()
                .filter(pq -> List.of(20L, 21L, 22L, 23L, 24L, 25L, 26L).contains(pq.getPlantationQuestion().getId())).toList();
        if (SIDE_STRAW_PRICE_QUESTIONS.contains(question.getPlantationQuestion().getId())) {
            if (questionList.isEmpty()) {
                return true;
            }
            for (PlantationProductQuestion pq : questionList) {
                if (pq.getDoubleValue() != null && pq.getDoubleValue() > 0) {
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    public void nextSaveIncome() throws IOException {
        super.getUserPlantationPlan().setGrossIncome(incomeCalculationService.calculateIncome(super.getUserPlantationPlan().getPrimaryProduct().getProductQuestionList(), super.getUserPlantationPlan().getId(), super.getUserPlantationPlan().getUserPlantation().getDistrict().getCity()));
        goToNextPage(EnumPlantationQuestionType.INCOME);
    }

    public void previousSaveIncome() throws IOException {
        goToPreviousPage(EnumPlantationQuestionType.INCOME);
    }
}

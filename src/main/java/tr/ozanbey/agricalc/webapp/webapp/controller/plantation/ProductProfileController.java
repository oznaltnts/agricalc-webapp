package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProduct;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumMonth;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.service.plantation.IncomeCalculationService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationProductService;
import tr.ozanbey.agricalc.webapp.webapp.view.plantation.PlantationIncomeView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class ProductProfileController extends PlantationController {

    @Autowired
    private PlantationProductService productService;

    @Autowired
    private IncomeCalculationService incomeCalculationService;

    private List<PlantationProduct> plantationProductList;
    private List<String> productionTechniqueList;
    private EnumMonth[] months = EnumMonth.values();

    private PlantationIncomeView plantationIncomeView = new PlantationIncomeView();

    @PostConstruct
    public void init() {
        fillProductList();
    }

    private void fillProductList() {
        plantationProductList = productService.getActiveProducts(EnumStatus.ACTIVE);
    }

    public void onSelectEvent() {
        if (141L == plantationIncomeView.getSelectedProductId()) {
            productionTechniqueList = List.of(
                    "Erken dönem",
                    "Orta dönem",
                    "Geç dönem"
            );
        } else if (208L == plantationIncomeView.getSelectedProductId()) {
            productionTechniqueList = List.of(
                    "Yer bağ (Goble)",
                    "Çift kollu cordon telli terbiye",
                    "Tek kollu cordon telli terbiye",
                    "T telli terbiye",
                    "Pergola (Çardak sistemi)"
            );
        } else if (209L == plantationIncomeView.getSelectedProductId()) {
            productionTechniqueList = List.of(
                    "Yer bağ (Goble)",
                    "Çift kollu cordon telli terbiye",
                    "Tek kollu cordon telli terbiye",
                    "T telli terbiye",
                    "Pergola (Çardak sistemi)",
                    "Y sistemi",
                    "V sistemi",
                    "GDC sistemi"
            );
        } else if (210L == plantationIncomeView.getSelectedProductId()) {
            productionTechniqueList = List.of(
                    "Yer bağ (Goble)",
                    "Çift kollu cordon telli terbiye",
                    "Tek kollu cordon telli terbiye",
                    "T telli terbiye",
                    "Tek guyot sistemi",
                    "Çift guyot sistemi"
            );
        } else {
            productionTechniqueList = null;
        }

        Optional<PlantationProduct> productOptional = plantationProductList
                .stream()
                .filter(p -> p.getId().equals(plantationIncomeView.getSelectedProductId()))
                .findFirst();
        if (productOptional.isPresent()) {
            plantationIncomeView.setSelectedProduct(productOptional.get());
            if (!Hibernate.isInitialized(plantationIncomeView.getSelectedProduct().getProductQuestionList())) {
                List<PlantationProductQuestion> productQuestionList = productService.getActiveQuestionByProduct(plantationIncomeView.getSelectedProductId());
                plantationIncomeView.getSelectedProduct().setProductQuestionList(productQuestionList);
            }
        }
    }

    private static final List<Long> YIELD_QUESTIONS = List.of(9L, 10L, 11L, 12L, 13L, 14L);
    private static final List<Long> PREVIOUS_PRICE_QUESTIONS = List.of(24L, 25L, 26L);
    private static final List<Long> CURRENT_PRICE_QUESTIONS = List.of(27L, 28L, 29L);
    private static final List<Long> SIDE_PRICE_QUESTIONS = List.of(30L, 31L, 32L);

    public boolean questionRenderer(Long questionId) {
        PlantationProduct selectedProduct = plantationIncomeView.getSelectedProduct();
        if (selectedProduct == null) {
            return false;
        }
        if (!Hibernate.isInitialized(selectedProduct.getProductQuestionList())) {
            return false;
        }
        List<PlantationProductQuestion> productQuestions = selectedProduct.getProductQuestionList();
        if (37L == questionId) {
            Optional<PlantationProductQuestion> optional37L = productQuestions.stream().filter(q -> Objects.equals(q.getPlantationQuestion().getId(), questionId)).findAny();
            if (optional37L.isPresent()) {
                if (optional37L.get().getMaximumValue() != null && optional37L.get().getMaximumValue() == 1) {
                    plantationIncomeView.setHarvestCount(optional37L.get().getMaximumValue().intValue());
                    return false;
                }
                return true;
            }
            return false;
        }
        if (SIDE_PRICE_QUESTIONS.contains(questionId)) {
            return isQuestionYieldRateEntered(productQuestions, questionId);
        }
        if (!YIELD_QUESTIONS.contains(questionId) &&
                !PREVIOUS_PRICE_QUESTIONS.contains(questionId) &&
                !CURRENT_PRICE_QUESTIONS.contains(questionId)) {
            return hasQuestion(productQuestions, questionId);
        }
        if (!containsAnySellTypeQuestion(productQuestions)) {
            return hasQuestion(productQuestions, questionId);
        }
        Long sellOptionId = plantationIncomeView.getSelectedSellOptionId();
        if (sellOptionId == null) {
            return false;
        }
        if (!isQuestionAllowedForSellOption(questionId, sellOptionId)) {
            return false;
        }
        return hasQuestion(productQuestions, questionId);
    }

    private boolean hasQuestion(List<PlantationProductQuestion> questions, Long questionId) {
        return questions.stream().anyMatch(q -> Objects.equals(q.getPlantationQuestion().getId(), questionId));
    }

    private boolean containsAnySellTypeQuestion(List<PlantationProductQuestion> questions) {
        return questions.stream().map(q -> q.getPlantationQuestion().getId()).anyMatch(List.of(5L, 6L, 7L)::contains);
    }

    private boolean isQuestionAllowedForSellOption(Long questionId, Long sellOptionId) {
        if (List.of(4L, 8L).contains(sellOptionId)) {
            return List.of(9L, 10L, 11L, 14L, 24L, 27L).contains(questionId);
        }
        if (List.of(5L, 6L).contains(sellOptionId)) {
            return List.of(12L, 25L, 28L).contains(questionId);
        }
        if (List.of(7L, 9L).contains(sellOptionId)) {
            return List.of(13L, 26L, 29L).contains(questionId);
        }
        return false;
    }

    private boolean isQuestionYieldRateEntered(List<PlantationProductQuestion> questions, Long questionId) {
        if (Objects.equals(30L, questionId)) {
            if (plantationIncomeView.getLowQualityYieldRate() != null && plantationIncomeView.getLowQualityYieldRate() > 0) {
                return hasQuestion(questions, questionId);
            }
        }
        if (Objects.equals(31L, questionId)) {
            if (plantationIncomeView.getAverageJuiceYield() != null && plantationIncomeView.getAverageJuiceYield() > 0) {
                return hasQuestion(questions, questionId);
            }
        }
        if (Objects.equals(32L, questionId)) {
            if ((plantationIncomeView.getAverageSideProductYield() != null && plantationIncomeView.getAverageSideProductYield() > 0) ||
                    (plantationIncomeView.getAverageSideSafranYield() != null && plantationIncomeView.getAverageSideSafranYield() > 0)) {
                return hasQuestion(questions, questionId);
            }
        }
        return false;
    }

    public void calculationButton() {
        BigDecimal resultValue = incomeCalculationService.calculateIncome(plantationIncomeView);
        System.out.println("resultValue: " + resultValue);
    }

}

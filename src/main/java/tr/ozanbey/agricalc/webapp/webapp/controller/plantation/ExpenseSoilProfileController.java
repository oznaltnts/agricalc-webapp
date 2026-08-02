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
public class ExpenseSoilProfileController extends BaseController {

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

    public void fillExpenseSoilQuestionList() throws IOException {
        if (parcelPlanId == null || !checkPlanIdForUser(parcelPlanId)) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }

        if (!Hibernate.isInitialized(plantationPlan.getProduct().getProductQuestionList())) {
            List<PlantationProductQuestion> productQuestionList = productService.getActiveQuestionByQuestionType(plantationPlan.getProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_SOIL);
            List<UserPlantPlanAnswer> planAnswerList = plantationPlanService.fillPlanAnswerValues(parcelPlanId, EnumPlantationQuestionType.EXPENSE_SOIL);
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

    public boolean oneMenuRenderer(PlantationProductQuestion question) {
        if (question.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_MENU)) {
            return true;
        }
        return false;
    }

    public boolean manyCheckboxRenderer(PlantationProductQuestion question) {
        if (question.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_MANY_CHECKBOX)) {
            return true;
        }
        return false;
    }

    public boolean oneRadioRenderer(PlantationProductQuestion question) {
        if (question.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_RADIO)) {
            return true;
        }
        return false;
    }

    public boolean integerRenderer(PlantationProductQuestion question) {
        if (question.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_INTEGER)) {
            return true;
        }
        return false;
    }

    public boolean doubleRenderer(PlantationProductQuestion question) {
        if (question.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DOUBLE)) {
            return true;
        }
        return false;
    }

    public boolean decimalRenderer(PlantationProductQuestion question) {
        if (question.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DECIMAL)) {
            return true;
        }
        return false;
    }

    @Autowired
    private CostCalculationService costCalculationService;

    public void nextSaveExpense() throws IOException {
        plantationPlanService.savePlanAnswers(plantationPlan, EnumPlantationQuestionType.EXPENSE_SOIL);
        plantationPlan.setSoilExpense(costCalculationService.calculateSoilPrep(plantationPlan.getProduct().getProductQuestionList()));
        plantationPlanService.updatePlantationPlanIncome(plantationPlan);

        List<PlantationProductQuestion> productQuestionList = productService.getActiveQuestionByQuestionType(plantationPlan.getProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_PLANTING);
        if (!productQuestionList.isEmpty()) {
            super.navigationController.redirectToUrl("/secured/plantation/expense-planting-profile?parcelPlanId=" + parcelPlanId);
        } else {
            productQuestionList = productService.getActiveQuestionByQuestionType(plantationPlan.getProduct().getId(), EnumStatus.ACTIVE, EnumPlantationQuestionType.EXPENSE_FERTILIZER);
            if (!productQuestionList.isEmpty()) {
                super.navigationController.redirectToUrl("/secured/plantation/expense-fertilizer-profile?parcelPlanId=" + parcelPlanId);
            } else {
                super.navigationController.redirectToUrl("/secured/plantation/expense-weed-profile?parcelPlanId=" + parcelPlanId);
            }
        }
    }

    public void previousSaveExpense() throws IOException {
        plantationPlanService.savePlanAnswers(plantationPlan, EnumPlantationQuestionType.EXPENSE_SOIL);
        super.navigationController.redirectToUrl("/secured/plantation/income-profile?parcelPlanId=" + parcelPlanId);
    }

}

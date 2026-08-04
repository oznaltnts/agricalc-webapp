package tr.ozanbey.agricalc.webapp.service.service.plantation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionAnswerType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionRecordType;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PlantationPlanService {

    @Autowired
    private UserPlantationPlanRepository plantationPlanRepository;

    @Autowired
    private UserPlantParcelRepository plantParcelRepository;

    @Autowired
    private PlantationProductRepository plantationProductRepository;

    @Autowired
    private UserPlantPlanAnswerRepository planAnswerRepository;

    @Autowired
    private UserParcelAnswerRepository parcelAnswerRepository;

    public List<UserPlantationPlan> getParcelPlanList(Long parcelId) {
        return plantationPlanRepository.findByPlantParcel_IdOrderByInsertDateDesc(parcelId);
    }

    @Transactional
    public void createNewPlan(Long parcelId, Long selectedProductId, LocalDate planStartDate) {
        UserPlantationPlan plan = new UserPlantationPlan();
        plan.setStatus(EnumStatus.ACTIVE);
        plan.setPlantParcel(plantParcelRepository.getReferenceById(parcelId));
        plan.setProduct(plantationProductRepository.getReferenceById(selectedProductId));
        plan.setPlanStartDate(planStartDate);
        plantationPlanRepository.save(plan);
    }

    public Optional<UserPlantationPlan> getPlantPlanByIdAndUserId(Long parcelPlanId, Long userId) {
        return plantationPlanRepository.findByIdAndPlantParcel_User_Id(parcelPlanId, userId);
    }

    @Transactional
    public void savePlanAnswers(UserPlantationPlan plantationPlan, EnumPlantationQuestionType questionType) {
        planAnswerRepository.deleteByPlantationPlan_IdAndProductQuestion_PlantationQuestion_QuestionType(plantationPlan.getId(), questionType);
        parcelAnswerRepository.deleteByPlantParcel_IdAndProductQuestion_PlantationQuestion_QuestionType(plantationPlan.getPlantParcel().getId(), questionType);
        List<UserPlantPlanAnswer> planAnswerList = new ArrayList<>();
        List<UserParcelAnswer> parcelAnswerList = new ArrayList<>();
        for (PlantationProductQuestion productQuestion : plantationPlan.getProduct().getProductQuestionList()) {
            if (productQuestion.getPlantationQuestion().getRecordType().equals(EnumQuestionRecordType.EVERY_TIME)) {
                addAnswerToPlanList(plantationPlan, productQuestion, planAnswerList);
            } else if (productQuestion.getPlantationQuestion().getRecordType().equals(EnumQuestionRecordType.FOR_ONCE)) {
                addAnswerToParcelList(plantationPlan.getPlantParcel(), productQuestion, parcelAnswerList);
            } else if (productQuestion.getPlantationQuestion().getRecordType().equals(EnumQuestionRecordType.ASK_USER)) {
                if (productQuestion.isDontAskAgain()) {
                    addAnswerToParcelList(plantationPlan.getPlantParcel(), productQuestion, parcelAnswerList);
                } else {
                    addAnswerToPlanList(plantationPlan, productQuestion, planAnswerList);
                }
            }
        }
        planAnswerRepository.saveAll(planAnswerList);
        parcelAnswerRepository.saveAll(parcelAnswerList);
    }

    private void addAnswerToParcelList(UserPlantParcel plantParcel, PlantationProductQuestion productQuestion, List<UserParcelAnswer> parcelAnswerList) {
        if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_MENU)
                && productQuestion.getSelectedAnswerId() != null) {
            UserParcelAnswer parcelAnswer = new UserParcelAnswer();
            parcelAnswer.setPlantParcel(plantParcel);
            parcelAnswer.setProductQuestion(productQuestion);
            parcelAnswer.setAnswerValue(productQuestion.getSelectedAnswerId().toString());
            parcelAnswerList.add(parcelAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_RADIO)
                && productQuestion.getSelectedAnswerId() != null) {
            UserParcelAnswer parcelAnswer = new UserParcelAnswer();
            parcelAnswer.setPlantParcel(plantParcel);
            parcelAnswer.setProductQuestion(productQuestion);
            parcelAnswer.setAnswerValue(productQuestion.getSelectedAnswerId().toString());
            parcelAnswerList.add(parcelAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_MANY_CHECKBOX)
                && productQuestion.getSelectedAnswerIds() != null
                && !productQuestion.getSelectedAnswerIds().isEmpty()) {
            for (Long productId : productQuestion.getSelectedAnswerIds()) {
                UserParcelAnswer parcelAnswer = new UserParcelAnswer();
                parcelAnswer.setPlantParcel(plantParcel);
                parcelAnswer.setProductQuestion(productQuestion);
                parcelAnswer.setAnswerValue(productId.toString());
                parcelAnswerList.add(parcelAnswer);
            }
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_INTEGER)
                && productQuestion.getIntegerValue() != null) {
            UserParcelAnswer parcelAnswer = new UserParcelAnswer();
            parcelAnswer.setPlantParcel(plantParcel);
            parcelAnswer.setProductQuestion(productQuestion);
            parcelAnswer.setAnswerValue(productQuestion.getIntegerValue().toString());
            parcelAnswerList.add(parcelAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DOUBLE)
                && productQuestion.getDoubleValue() != null) {
            UserParcelAnswer parcelAnswer = new UserParcelAnswer();
            parcelAnswer.setPlantParcel(plantParcel);
            parcelAnswer.setProductQuestion(productQuestion);
            parcelAnswer.setAnswerValue(productQuestion.getDoubleValue().toString());
            parcelAnswerList.add(parcelAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DECIMAL)
                && productQuestion.getBigDecimalValue() != null) {
            UserParcelAnswer parcelAnswer = new UserParcelAnswer();
            parcelAnswer.setPlantParcel(plantParcel);
            parcelAnswer.setProductQuestion(productQuestion);
            parcelAnswer.setAnswerValue(productQuestion.getBigDecimalValue().toString());
            parcelAnswerList.add(parcelAnswer);
        }
    }

    private void addAnswerToPlanList(UserPlantationPlan plantationPlan, PlantationProductQuestion productQuestion, List<UserPlantPlanAnswer> planAnswerList) {
        if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_MENU)
                && productQuestion.getSelectedAnswerId() != null) {
            UserPlantPlanAnswer planAnswer = new UserPlantPlanAnswer();
            planAnswer.setPlantationPlan(plantationPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getSelectedAnswerId().toString());
            planAnswerList.add(planAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_RADIO)
                && productQuestion.getSelectedAnswerId() != null) {
            UserPlantPlanAnswer planAnswer = new UserPlantPlanAnswer();
            planAnswer.setPlantationPlan(plantationPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getSelectedAnswerId().toString());
            planAnswerList.add(planAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_MANY_CHECKBOX)
                && productQuestion.getSelectedAnswerIds() != null
                && !productQuestion.getSelectedAnswerIds().isEmpty()) {
            for (Long productId : productQuestion.getSelectedAnswerIds()) {
                UserPlantPlanAnswer planAnswer = new UserPlantPlanAnswer();
                planAnswer.setPlantationPlan(plantationPlan);
                planAnswer.setProductQuestion(productQuestion);
                planAnswer.setAnswerValue(productId.toString());
                planAnswerList.add(planAnswer);
            }
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_INTEGER)
                && productQuestion.getIntegerValue() != null) {
            UserPlantPlanAnswer planAnswer = new UserPlantPlanAnswer();
            planAnswer.setPlantationPlan(plantationPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getIntegerValue().toString());
            planAnswerList.add(planAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DOUBLE)
                && productQuestion.getDoubleValue() != null) {
            UserPlantPlanAnswer planAnswer = new UserPlantPlanAnswer();
            planAnswer.setPlantationPlan(plantationPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getDoubleValue().toString());
            planAnswerList.add(planAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DECIMAL)
                && productQuestion.getBigDecimalValue() != null) {
            UserPlantPlanAnswer planAnswer = new UserPlantPlanAnswer();
            planAnswer.setPlantationPlan(plantationPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getBigDecimalValue().toString());
            planAnswerList.add(planAnswer);
        }
    }

    public List<UserPlantPlanAnswer> fillPlanAnswerValues(Long planId, EnumPlantationQuestionType questionType) {
        return planAnswerRepository.findByPlantationPlan_IdAndProductQuestion_PlantationQuestion_QuestionType(planId, questionType);
    }

    public List<UserParcelAnswer> fillParcelAnswerValues(Long parcelId, EnumPlantationQuestionType questionType) {
        return parcelAnswerRepository.findByPlantParcel_IdAndProductQuestion_PlantationQuestion_QuestionType(parcelId, questionType);
    }

    @Transactional
    public void updatePlantationPlanIncome(UserPlantationPlan plantationPlan) {
        plantationPlanRepository.save(plantationPlan);
    }
}

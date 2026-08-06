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
public class UserPlantParcelPlanService {

    @Autowired
    private UserPlantParcelPlanRepository parcelPlanRepository;

    @Autowired
    private UserPlantParcelRepository plantParcelRepository;

    @Autowired
    private PlantationProductRepository plantationProductRepository;

    @Autowired
    private UserPlantParcelPlanAnswerRepository planAnswerRepository;

    @Autowired
    private UserPlantParcelAnswerRepository parcelAnswerRepository;

    public List<UserPlantParcelPlan> getParcelPlanList(Long parcelId) {
        return parcelPlanRepository.findByPlantParcel_IdOrderByInsertDateDesc(parcelId);
    }

    @Transactional
    public void createNewPlan(Long parcelId, LocalDate planStartDate) {
        UserPlantParcelPlan plan = new UserPlantParcelPlan();
        plan.setStatus(EnumStatus.ACTIVE);
        plan.setPlantParcel(plantParcelRepository.getReferenceById(parcelId));
        plan.setPlanStartDate(planStartDate);
        parcelPlanRepository.save(plan);
    }

    public Optional<UserPlantParcelPlan> getPlantPlanByIdAndUserId(Long parcelPlanId, Long userId) {
        return parcelPlanRepository.findByIdAndPlantParcel_User_Id(parcelPlanId, userId);
    }

    @Transactional
    public void savePlanAnswers(UserPlantParcelPlan parcelPlan, EnumPlantationQuestionType questionType) {
        planAnswerRepository.deleteByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_QuestionType(parcelPlan.getId(), questionType);
        parcelAnswerRepository.deleteByPlantParcel_IdAndProductQuestion_PlantationQuestion_QuestionType(parcelPlan.getPlantParcel().getId(), questionType);
        List<UserPlantParcelPlanAnswer> planAnswerList = new ArrayList<>();
        List<UserPlantParcelAnswer> parcelAnswerList = new ArrayList<>();
        for (PlantationProductQuestion productQuestion : parcelPlan.getPlantParcel().getProduct().getProductQuestionList()) {
            if (productQuestion.getPlantationQuestion().getRecordType().equals(EnumQuestionRecordType.EVERY_TIME)) {
                addAnswerToPlanList(parcelPlan, productQuestion, planAnswerList);
            } else if (productQuestion.getPlantationQuestion().getRecordType().equals(EnumQuestionRecordType.FOR_ONCE)) {
                addAnswerToParcelList(parcelPlan.getPlantParcel(), productQuestion, parcelAnswerList);
            } else if (productQuestion.getPlantationQuestion().getRecordType().equals(EnumQuestionRecordType.ASK_USER)) {
                if (productQuestion.isDontAskAgain()) {
                    addAnswerToParcelList(parcelPlan.getPlantParcel(), productQuestion, parcelAnswerList);
                } else {
                    addAnswerToPlanList(parcelPlan, productQuestion, planAnswerList);
                }
            }
        }
        planAnswerRepository.saveAll(planAnswerList);
        parcelAnswerRepository.saveAll(parcelAnswerList);
    }

    private void addAnswerToParcelList(UserPlantParcel plantParcel, PlantationProductQuestion productQuestion, List<UserPlantParcelAnswer> parcelAnswerList) {
        if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_MENU)
                && productQuestion.getSelectedAnswerId() != null) {
            UserPlantParcelAnswer parcelAnswer = new UserPlantParcelAnswer();
            parcelAnswer.setPlantParcel(plantParcel);
            parcelAnswer.setProductQuestion(productQuestion);
            parcelAnswer.setAnswerValue(productQuestion.getSelectedAnswerId().toString());
            parcelAnswerList.add(parcelAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_RADIO)
                && productQuestion.getSelectedAnswerId() != null) {
            UserPlantParcelAnswer parcelAnswer = new UserPlantParcelAnswer();
            parcelAnswer.setPlantParcel(plantParcel);
            parcelAnswer.setProductQuestion(productQuestion);
            parcelAnswer.setAnswerValue(productQuestion.getSelectedAnswerId().toString());
            parcelAnswerList.add(parcelAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_MANY_CHECKBOX)
                && productQuestion.getSelectedAnswerIds() != null
                && !productQuestion.getSelectedAnswerIds().isEmpty()) {
            for (Long productId : productQuestion.getSelectedAnswerIds()) {
                UserPlantParcelAnswer parcelAnswer = new UserPlantParcelAnswer();
                parcelAnswer.setPlantParcel(plantParcel);
                parcelAnswer.setProductQuestion(productQuestion);
                parcelAnswer.setAnswerValue(productId.toString());
                parcelAnswerList.add(parcelAnswer);
            }
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_INTEGER)
                && productQuestion.getIntegerValue() != null) {
            UserPlantParcelAnswer parcelAnswer = new UserPlantParcelAnswer();
            parcelAnswer.setPlantParcel(plantParcel);
            parcelAnswer.setProductQuestion(productQuestion);
            parcelAnswer.setAnswerValue(productQuestion.getIntegerValue().toString());
            parcelAnswerList.add(parcelAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DOUBLE)
                && productQuestion.getDoubleValue() != null) {
            UserPlantParcelAnswer parcelAnswer = new UserPlantParcelAnswer();
            parcelAnswer.setPlantParcel(plantParcel);
            parcelAnswer.setProductQuestion(productQuestion);
            parcelAnswer.setAnswerValue(productQuestion.getDoubleValue().toString());
            parcelAnswerList.add(parcelAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DECIMAL)
                && productQuestion.getBigDecimalValue() != null) {
            UserPlantParcelAnswer parcelAnswer = new UserPlantParcelAnswer();
            parcelAnswer.setPlantParcel(plantParcel);
            parcelAnswer.setProductQuestion(productQuestion);
            parcelAnswer.setAnswerValue(productQuestion.getBigDecimalValue().toString());
            parcelAnswerList.add(parcelAnswer);
        }
    }

    private void addAnswerToPlanList(UserPlantParcelPlan parcelPlan, PlantationProductQuestion productQuestion, List<UserPlantParcelPlanAnswer> planAnswerList) {
        if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_MENU)
                && productQuestion.getSelectedAnswerId() != null) {
            UserPlantParcelPlanAnswer planAnswer = new UserPlantParcelPlanAnswer();
            planAnswer.setPlantParcelPlan(parcelPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getSelectedAnswerId().toString());
            planAnswerList.add(planAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_RADIO)
                && productQuestion.getSelectedAnswerId() != null) {
            UserPlantParcelPlanAnswer planAnswer = new UserPlantParcelPlanAnswer();
            planAnswer.setPlantParcelPlan(parcelPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getSelectedAnswerId().toString());
            planAnswerList.add(planAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_MANY_CHECKBOX)
                && productQuestion.getSelectedAnswerIds() != null
                && !productQuestion.getSelectedAnswerIds().isEmpty()) {
            for (Long productId : productQuestion.getSelectedAnswerIds()) {
                UserPlantParcelPlanAnswer planAnswer = new UserPlantParcelPlanAnswer();
                planAnswer.setPlantParcelPlan(parcelPlan);
                planAnswer.setProductQuestion(productQuestion);
                planAnswer.setAnswerValue(productId.toString());
                planAnswerList.add(planAnswer);
            }
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_INTEGER)
                && productQuestion.getIntegerValue() != null) {
            UserPlantParcelPlanAnswer planAnswer = new UserPlantParcelPlanAnswer();
            planAnswer.setPlantParcelPlan(parcelPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getIntegerValue().toString());
            planAnswerList.add(planAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DOUBLE)
                && productQuestion.getDoubleValue() != null) {
            UserPlantParcelPlanAnswer planAnswer = new UserPlantParcelPlanAnswer();
            planAnswer.setPlantParcelPlan(parcelPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getDoubleValue().toString());
            planAnswerList.add(planAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DECIMAL)
                && productQuestion.getBigDecimalValue() != null) {
            UserPlantParcelPlanAnswer planAnswer = new UserPlantParcelPlanAnswer();
            planAnswer.setPlantParcelPlan(parcelPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getBigDecimalValue().toString());
            planAnswerList.add(planAnswer);
        }
    }

    public List<UserPlantParcelPlanAnswer> fillPlanAnswerValues(Long planId, EnumPlantationQuestionType questionType) {
        return planAnswerRepository.findByPlantParcelPlan_IdAndProductQuestion_PlantationQuestion_QuestionType(planId, questionType);
    }

    public List<UserPlantParcelAnswer> fillParcelAnswerValues(Long parcelId, EnumPlantationQuestionType questionType) {
        return parcelAnswerRepository.findByPlantParcel_IdAndProductQuestion_PlantationQuestion_QuestionType(parcelId, questionType);
    }

    @Transactional
    public void updatePlantParcelPlanIncome(UserPlantParcelPlan parcelPlan) {
        parcelPlanRepository.save(parcelPlan);
    }
}

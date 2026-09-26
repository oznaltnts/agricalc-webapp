package tr.ozanbey.agricalc.webapp.service.service.plantation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionAnswerType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumQuestionRecordType;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.UserPlantationAnswerRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.UserPlantationPlanAnswerRepository;
import tr.ozanbey.agricalc.webapp.service.repository.plantation.UserPlantationPlanRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserPlantationPlanService {

    @Autowired
    private UserPlantationPlanRepository userPlantationPlanRepository;

    @Autowired
    private UserPlantationPlanAnswerRepository planAnswerRepository;

    @Autowired
    private UserPlantationAnswerRepository userPlantationAnswerRepository;

    public Optional<UserPlantationPlan> getPlantationPlanByIdAndUserId(Long plantationPlanId, Long userId) {
        return userPlantationPlanRepository.findByIdAndUserPlantation_User_Id(plantationPlanId, userId);
    }

    @Transactional
    public void savePlanAnswers(UserPlantationPlan userPlantationPlan, EnumPlantationQuestionType questionType) {
        planAnswerRepository.deleteByUserPlantationPlan_IdAndProductQuestion_PlantationQuestion_QuestionType(userPlantationPlan.getId(), questionType);
        planAnswerRepository.flush();
        userPlantationAnswerRepository.deleteByUserPlantation_IdAndProductQuestion_PlantationQuestion_QuestionType(userPlantationPlan.getUserPlantation().getId(), questionType);
        userPlantationAnswerRepository.flush();
        List<UserPlantationPlanAnswer> planAnswerList = new ArrayList<>();
        List<UserPlantationAnswer> plantationAnswerList = new ArrayList<>();
        for (PlantationProductQuestion productQuestion : userPlantationPlan.getPrimaryProduct().getProductQuestionList()) {
            if (productQuestion.getPlantationQuestion().getRecordType().equals(EnumQuestionRecordType.EVERY_TIME)) {
                addAnswerToPlanList(userPlantationPlan, productQuestion, planAnswerList);
            } else if (productQuestion.getPlantationQuestion().getRecordType().equals(EnumQuestionRecordType.FOR_ONCE)) {
                addAnswerToPlantationList(userPlantationPlan.getUserPlantation(), productQuestion, plantationAnswerList);
            } else if (productQuestion.getPlantationQuestion().getRecordType().equals(EnumQuestionRecordType.ASK_USER)) {
                if (productQuestion.isDontAskAgain()) {
                    addAnswerToPlantationList(userPlantationPlan.getUserPlantation(), productQuestion, plantationAnswerList);
                } else {
                    addAnswerToPlanList(userPlantationPlan, productQuestion, planAnswerList);
                }
            }
        }
        planAnswerRepository.saveAll(planAnswerList);
        userPlantationAnswerRepository.saveAll(plantationAnswerList);
    }

    private void addAnswerToPlantationList(UserPlantation userPlantation, PlantationProductQuestion productQuestion, List<UserPlantationAnswer> plantationAnswerList) {
        if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_MENU)
                && productQuestion.getSelectedAnswerId() != null) {
            UserPlantationAnswer userPlantationAnswer = new UserPlantationAnswer();
            userPlantationAnswer.setUserPlantation(userPlantation);
            userPlantationAnswer.setProductQuestion(productQuestion);
            userPlantationAnswer.setAnswerValue(productQuestion.getSelectedAnswerId().toString());
            plantationAnswerList.add(userPlantationAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_RADIO)
                && productQuestion.getSelectedAnswerId() != null) {
            UserPlantationAnswer userPlantationAnswer = new UserPlantationAnswer();
            userPlantationAnswer.setUserPlantation(userPlantation);
            userPlantationAnswer.setProductQuestion(productQuestion);
            userPlantationAnswer.setAnswerValue(productQuestion.getSelectedAnswerId().toString());
            plantationAnswerList.add(userPlantationAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_MANY_CHECKBOX)
                && productQuestion.getSelectedAnswerIds() != null
                && !productQuestion.getSelectedAnswerIds().isEmpty()) {
            for (Long productId : productQuestion.getSelectedAnswerIds()) {
                UserPlantationAnswer userPlantationAnswer = new UserPlantationAnswer();
                userPlantationAnswer.setUserPlantation(userPlantation);
                userPlantationAnswer.setProductQuestion(productQuestion);
                userPlantationAnswer.setAnswerValue(productId.toString());
                plantationAnswerList.add(userPlantationAnswer);
            }
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_INTEGER)
                && productQuestion.getIntegerValue() != null) {
            UserPlantationAnswer userPlantationAnswer = new UserPlantationAnswer();
            userPlantationAnswer.setUserPlantation(userPlantation);
            userPlantationAnswer.setProductQuestion(productQuestion);
            userPlantationAnswer.setAnswerValue(productQuestion.getIntegerValue().toString());
            plantationAnswerList.add(userPlantationAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DOUBLE)
                && productQuestion.getDoubleValue() != null) {
            UserPlantationAnswer userPlantationAnswer = new UserPlantationAnswer();
            userPlantationAnswer.setUserPlantation(userPlantation);
            userPlantationAnswer.setProductQuestion(productQuestion);
            userPlantationAnswer.setAnswerValue(productQuestion.getDoubleValue().toString());
            plantationAnswerList.add(userPlantationAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DECIMAL)
                && productQuestion.getBigDecimalValue() != null) {
            UserPlantationAnswer userPlantationAnswer = new UserPlantationAnswer();
            userPlantationAnswer.setUserPlantation(userPlantation);
            userPlantationAnswer.setProductQuestion(productQuestion);
            userPlantationAnswer.setAnswerValue(productQuestion.getBigDecimalValue().toString());
            plantationAnswerList.add(userPlantationAnswer);
        }
    }

    private void addAnswerToPlanList(UserPlantationPlan userPlantationPlan, PlantationProductQuestion productQuestion, List<UserPlantationPlanAnswer> planAnswerList) {
        if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_MENU)
                && productQuestion.getSelectedAnswerId() != null) {
            UserPlantationPlanAnswer planAnswer = new UserPlantationPlanAnswer();
            planAnswer.setUserPlantationPlan(userPlantationPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getSelectedAnswerId().toString());
            planAnswerList.add(planAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_ONE_RADIO)
                && productQuestion.getSelectedAnswerId() != null) {
            UserPlantationPlanAnswer planAnswer = new UserPlantationPlanAnswer();
            planAnswer.setUserPlantationPlan(userPlantationPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getSelectedAnswerId().toString());
            planAnswerList.add(planAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.SELECT_MANY_CHECKBOX)
                && productQuestion.getSelectedAnswerIds() != null
                && !productQuestion.getSelectedAnswerIds().isEmpty()) {
            for (Long productId : productQuestion.getSelectedAnswerIds()) {
                UserPlantationPlanAnswer planAnswer = new UserPlantationPlanAnswer();
                planAnswer.setUserPlantationPlan(userPlantationPlan);
                planAnswer.setProductQuestion(productQuestion);
                planAnswer.setAnswerValue(productId.toString());
                planAnswerList.add(planAnswer);
            }
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_INTEGER)
                && productQuestion.getIntegerValue() != null) {
            UserPlantationPlanAnswer planAnswer = new UserPlantationPlanAnswer();
            planAnswer.setUserPlantationPlan(userPlantationPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getIntegerValue().toString());
            planAnswerList.add(planAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DOUBLE)
                && productQuestion.getDoubleValue() != null) {
            UserPlantationPlanAnswer planAnswer = new UserPlantationPlanAnswer();
            planAnswer.setUserPlantationPlan(userPlantationPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getDoubleValue().toString());
            planAnswerList.add(planAnswer);
        } else if (productQuestion.getPlantationQuestion().getAnswerType().equals(EnumQuestionAnswerType.INPUT_DECIMAL)
                && productQuestion.getBigDecimalValue() != null) {
            UserPlantationPlanAnswer planAnswer = new UserPlantationPlanAnswer();
            planAnswer.setUserPlantationPlan(userPlantationPlan);
            planAnswer.setProductQuestion(productQuestion);
            planAnswer.setAnswerValue(productQuestion.getBigDecimalValue().toString());
            planAnswerList.add(planAnswer);
        }
    }

    public List<UserPlantationPlanAnswer> fillPlanAnswerValues(Long planId, EnumPlantationQuestionType questionType) {
        return planAnswerRepository.findByUserPlantationPlan_IdAndProductQuestion_PlantationQuestion_QuestionType(planId, questionType);
    }

    public List<UserPlantationAnswer> fillPlantationAnswerValues(Long plantationId, EnumPlantationQuestionType questionType) {
        return userPlantationAnswerRepository.findByUserPlantation_IdAndProductQuestion_PlantationQuestion_QuestionType(plantationId, questionType);
    }

    @Transactional
    public void updatePlantationPlan(UserPlantationPlan userPlantationPlan) {
        BigDecimal totalCost = BigDecimal.ZERO;
        if (userPlantationPlan.getSoilPrepCost() != null)
            totalCost = totalCost.add(userPlantationPlan.getSoilPrepCost());
        if (userPlantationPlan.getPlantingCost() != null)
            totalCost = totalCost.add(userPlantationPlan.getPlantingCost());
        if (userPlantationPlan.getFertilizerCost() != null)
            totalCost = totalCost.add(userPlantationPlan.getFertilizerCost());
        if (userPlantationPlan.getWeedControlCost() != null)
            totalCost = totalCost.add(userPlantationPlan.getWeedControlCost());
        if (userPlantationPlan.getIrrigationCost() != null)
            totalCost = totalCost.add(userPlantationPlan.getIrrigationCost());
        if (userPlantationPlan.getCulturalCost() != null)
            totalCost = totalCost.add(userPlantationPlan.getCulturalCost());
        if (userPlantationPlan.getProtectionCost() != null)
            totalCost = totalCost.add(userPlantationPlan.getProtectionCost());
        if (userPlantationPlan.getHarvestCost() != null)
            totalCost = totalCost.add(userPlantationPlan.getHarvestCost());
        if (userPlantationPlan.getBlendCost() != null)
            totalCost = totalCost.add(userPlantationPlan.getBlendCost());
        if (userPlantationPlan.getDryingCost() != null)
            totalCost = totalCost.add(userPlantationPlan.getDryingCost());
        if (userPlantationPlan.getBalingCost() != null)
            totalCost = totalCost.add(userPlantationPlan.getBalingCost());
        if (userPlantationPlan.getTransportationCost() != null)
            totalCost = totalCost.add(userPlantationPlan.getTransportationCost());

        userPlantationPlan.setTotalExpense(totalCost);
        userPlantationPlanRepository.save(userPlantationPlan);
    }
}

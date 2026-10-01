package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.literacy.LiteracyQuestion;
import tr.ozanbey.agricalc.webapp.service.domain.literacy.LiteracyQuestionOption;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.service.LiteracyService;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class FinancialLiteracyController implements Serializable {

    @Autowired
    private LiteracyService literacyService;

    private List<LiteracyQuestion> literacyQuestionList;
    private double calculatedValue = 1d;

    @PostConstruct
    public void init() {
        literacyQuestionList = literacyService.getLiteracyQuestionListByStatus(EnumStatus.ACTIVE);
    }

    public void calculateLiteracy() {
        Optional<LiteracyQuestion> optional = literacyQuestionList.stream().filter(q -> q.getId().equals(1L)).findFirst();
        if (optional.isPresent()) {
            if (optional.get().getSelectedAnswerId().equals(1L)) {
                for (LiteracyQuestion literacyQuestion : literacyQuestionList) {
                    Optional<LiteracyQuestionOption> optionalOption = literacyQuestion.getQuestionOptionList().stream().filter(o -> o.getId().equals(literacyQuestion.getSelectedAnswerId())).findFirst();
                    if (optionalOption.isPresent()) {
                        calculatedValue = calculatedValue * optionalOption.get().getScore10Value();
                    } else {
                        System.out.println(literacyQuestion.getValue() + ": soruyu cevaplat: " + calculatedValue);
                    }
                }
            } else if (optional.get().getSelectedAnswerId().equals(2L)) {
                for (LiteracyQuestion literacyQuestion : literacyQuestionList) {
                    Optional<LiteracyQuestionOption> optionalOption = literacyQuestion.getQuestionOptionList().stream().filter(o -> o.getId().equals(literacyQuestion.getSelectedAnswerId())).findFirst();
                    if (optionalOption.isPresent()) {
                        calculatedValue = calculatedValue * optionalOption.get().getScore11Value();
                    } else {
                        System.out.println(literacyQuestion.getValue() + ": soruyu cevaplat: " + calculatedValue);
                    }
                }
            }
        } else {
            System.out.println("ilk soruyu cevaplat: " + calculatedValue);
        }
        System.out.println("calculatedValue: " + calculatedValue);
    }

}

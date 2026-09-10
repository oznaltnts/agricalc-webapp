package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.literacy.LiteracyQuestion;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.service.LiteracyService;

import java.io.Serializable;
import java.util.List;

@Component
@ViewScoped
@Getter
@Setter
public class FinancialLiteracyController implements Serializable {

    @Autowired
    private LiteracyService literacyService;

    private List<LiteracyQuestion> literacyQuestionList;

    @PostConstruct
    public void init() {
        literacyQuestionList = literacyService.getLiteracyQuestionListByStatus(EnumStatus.ACTIVE);
    }

}

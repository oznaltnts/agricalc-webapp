package tr.ozanbey.agricalc.webapp.webapp.controller.animal;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.service.animal.DairyCowResultService;
import tr.ozanbey.agricalc.webapp.webapp.view.*;

import java.util.ArrayList;
import java.util.List;

@Component
@ViewScoped
@Getter
@Setter
public class DairyCowResultController extends DairyCowController {

    @Autowired
    private DairyCowResultService cowResultService;

    private List<DairyCowResultCountView> countYearCalculationList;
    private List<DairyCowResultFeedView> feedStockCalculationList;
    private DairyCowResultFeedTotalView feedTotalView;
    private DairyCowResultDepreciationView depreciationView;
    private List<DairyCowResultIncomeView> incomeCalculationTableList;
    private List<DairyCowResultIncomeView> incomeCalculationCardList;

    @PostConstruct
    public void init() {
    }

    public void fillDataTableValues() {
        countYearCalculationList = cowResultService.calculateCountList(getBarnId());
        feedStockCalculationList = cowResultService.calculateFeedList(getBarnId());
        feedTotalView = cowResultService.calculateFeedTotalView(getBarnId());
        depreciationView = cowResultService.calculateCostTotalView(getUserDairyCowBarn(),
                feedTotalView.getTotalCostPerBarn());
        List<DairyCowResultIncomeView> incomeCalculationList = cowResultService.calculateIncomeTotalView(getBarnId(),
                feedTotalView.getTotalCostPerBarn(),
                depreciationView);
        depreciationView.setYearlyTotalCost(incomeCalculationList.get(9).getIncomeValue());
        depreciationView.setAnimalAmortisationPrice(incomeCalculationList.get(11).getIncomeValue());
        incomeCalculationTableList = new ArrayList<>(incomeCalculationList.subList(0, 8));
        incomeCalculationCardList = new ArrayList<>(incomeCalculationList.subList(8, incomeCalculationList.size()));

    }

}

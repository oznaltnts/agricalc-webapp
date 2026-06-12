package tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DairyCowResultFeedView implements Serializable {

    private String feedName;
    private BigDecimal feedPrice;
    private BigDecimal feedCostDaily;
    private BigDecimal lactationCost;
    private BigDecimal roughageCost;

    private Double feedPurchaseAmount;
    private LocalDateTime feedPurchaseDate;
    private Double feedNeedYearly;
    private Double feedNeedDaily;
    private Double feedUsageDays;
    private LocalDateTime feedStockEndDate;
    private Double leftAmount;
}

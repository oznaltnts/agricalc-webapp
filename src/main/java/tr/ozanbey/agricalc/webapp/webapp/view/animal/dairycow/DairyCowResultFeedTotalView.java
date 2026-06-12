package tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DairyCowResultFeedTotalView implements Serializable {

    private BigDecimal totalLactationCost;
    private BigDecimal totalRoughageCost;
    private BigDecimal totalCostPerAnimal;
    private BigDecimal totalCostPerBarn;
}

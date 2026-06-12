package tr.ozanbey.agricalc.webapp.webapp.view;

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
public class DairyCowResultDepreciationView implements Serializable {

    private BigDecimal yearlyFeedCost;
    private BigDecimal yearlyMaintenanceCost;
    private BigDecimal yearlyEnergyCost;
    private BigDecimal yearlyVeterinaryCost;
    private BigDecimal yearlyInseminationCost;
    private BigDecimal yearlyOtherCost;
    private BigDecimal yearlyInsuranceCost;
    private BigDecimal yearlyInterestCost;
    //Amortisman Gideri
    private BigDecimal animalAmortisationPrice;
    private BigDecimal barnAmortisationPrice;
    private BigDecimal yearlyTotalCost;

}

package tr.ozanbey.agricalc.webapp.webapp.view;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.domain.Cost;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DairyCowCostView implements Serializable {

    private Long userCostId;
    private Long selectedCostId;
    private Cost selectedCost;
    private String selectedCostName;

    private Double count;
    @NotNull(message = "Brüt gider giriniz")
    private BigDecimal totalCost;
    private Double hourlyOrInterest;

}

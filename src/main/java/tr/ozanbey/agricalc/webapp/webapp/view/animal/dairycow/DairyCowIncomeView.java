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
public class DairyCowIncomeView implements Serializable {

    private Long userIncomeId;
    private Long incomeId;
    private String incomeName;
    private String incomeUnit;
    private BigDecimal incomeValue;

}

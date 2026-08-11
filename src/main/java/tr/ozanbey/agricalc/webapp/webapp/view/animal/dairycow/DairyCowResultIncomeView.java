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
public class DairyCowResultIncomeView implements Serializable {

    private int order;
    private String groupLabel;
    private String incomeLabel;
    private BigDecimal incomeValue;
}

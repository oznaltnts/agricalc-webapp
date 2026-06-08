package tr.ozanbey.agricalc.webapp.webapp.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DairyCowCountCalculateView implements Serializable {

    private int totalCount;
    private double totalBarnUsageRate;
    private int endYearTotalCount;
    private double endYearBarnUsageRate;

}

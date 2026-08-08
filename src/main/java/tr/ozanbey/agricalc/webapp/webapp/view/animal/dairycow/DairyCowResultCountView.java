package tr.ozanbey.agricalc.webapp.webapp.view.animal.dairycow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.dairycow.EnumCowType;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DairyCowResultCountView implements Serializable {

    private EnumCowType cowType;
    private Double endYearCount;
    private Double averageFeedCount;
    private Double barnUsageRate;
    private Double endYearBarnUsageRate;
    private Double milkingUsageRate;
    private Double endYearMilkingUsageRate;

}

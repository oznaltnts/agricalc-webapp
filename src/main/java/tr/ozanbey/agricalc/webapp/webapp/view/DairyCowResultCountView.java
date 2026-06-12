package tr.ozanbey.agricalc.webapp.webapp.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumCowType;

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

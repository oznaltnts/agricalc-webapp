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
public class DairyCowCountView implements Serializable {

    private Long countId;
    private Long coefId;
    private EnumCowType cowType;
    private double coefficientValue;
    private int currentCount;
    private int purchaseCount;
    private int sellCount;
    private double endYearCount;
    private double endYearFeedCount;

}

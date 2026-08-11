package tr.ozanbey.agricalc.webapp.webapp.view.plantation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumAllocationType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlanAllocationResultView implements Serializable {

    private Long parcelPlanId;
    private EnumPlantationQuestionType questionType;
    private EnumAllocationType allocationType;
    private BigDecimal calculatedValue;

}

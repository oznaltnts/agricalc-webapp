package tr.ozanbey.agricalc.webapp.webapp.view;

import jakarta.validation.constraints.NotNull;
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
public class DairyCowBarnView implements Serializable {

    private Long barnId;
    @NotNull(message = "Hayvan ırkı seçiniz")
    private Long selectedDairyCowId;
    private String dairyCowName;
    private Integer barnCapacity;
    private Integer milkingCapacity;

    private BigDecimal barnPrice;

    private Double birthRate;
    private Double deathRate;
    private Double inseminationRate;
    private Double milkYield;

    private boolean unknownBirthRate;
    private boolean unknownDeathRate;
    private boolean unknownInseminationRate;
    private boolean unknownMilkYield;

    private Integer lactationPeriod;

    private Double totalCount;
    private Double endYearTotalCount;
    private Double averageFeedTotalCount;
    private Double averageMilkingCount;

}

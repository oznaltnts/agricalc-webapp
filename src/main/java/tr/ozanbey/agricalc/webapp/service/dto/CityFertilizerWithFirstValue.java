package tr.ozanbey.agricalc.webapp.service.dto;

import tr.ozanbey.agricalc.webapp.service.enumtype.EnumFertilizerType;

import java.math.BigDecimal;

public interface CityFertilizerWithFirstValue {
    Long getFertilizerId();
    Long getOldFertilizerId();
    EnumFertilizerType getEnumType();
    String getName();
    BigDecimal getNitrogenPercent();
    BigDecimal getPhosphorPercent();
    BigDecimal getPotassiumPercent();
    BigDecimal getPrice();

}

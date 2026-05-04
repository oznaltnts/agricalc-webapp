package tr.ozanbey.agricalc.webapp.service.dto;

import tr.ozanbey.agricalc.webapp.service.enumtype.EnumSeedSeedlingType;

import java.math.BigDecimal;

public interface SeedSeedlingWithFirstValue {
    EnumSeedSeedlingType getEnumType();
    BigDecimal getValue();

}

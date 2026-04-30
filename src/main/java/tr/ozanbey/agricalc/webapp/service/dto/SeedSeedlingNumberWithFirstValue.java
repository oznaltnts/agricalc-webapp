package tr.ozanbey.agricalc.webapp.service.dto;

import tr.ozanbey.agricalc.webapp.service.enumtype.EnumSeedAndSeedlingNumberType;

public interface SeedSeedlingNumberWithFirstValue {
    EnumSeedAndSeedlingNumberType getEnumType();
    String getValue();
}

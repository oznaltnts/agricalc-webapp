package tr.ozanbey.agricalc.webapp.service.dto;

import tr.ozanbey.agricalc.webapp.service.enumtype.EnumDieselDistanceType;

public interface DieselDistanceWithFirstValue {
    EnumDieselDistanceType getEnumType();
    String getValue();
}

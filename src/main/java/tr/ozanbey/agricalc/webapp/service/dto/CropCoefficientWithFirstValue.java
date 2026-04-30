package tr.ozanbey.agricalc.webapp.service.dto;

import tr.ozanbey.agricalc.webapp.service.enumtype.EnumCropCoefficientType;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumCropCoefficientValue;

public interface CropCoefficientWithFirstValue {
    EnumCropCoefficientValue getEnumValue();
    EnumCropCoefficientType getEnumType();
    String getValue();
}

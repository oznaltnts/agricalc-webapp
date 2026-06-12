package tr.ozanbey.agricalc.webapp.service.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumCowType;

@Converter(autoApply = true)
public class EnumCowTypeConverter implements AttributeConverter<EnumCowType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EnumCowType cowType) {
        return cowType != null ? cowType.getValue() : null;
    }

    @Override
    public EnumCowType convertToEntityAttribute(Integer cowType) {
        return cowType != null ? EnumCowType.fromValue(cowType) : null;
    }
}

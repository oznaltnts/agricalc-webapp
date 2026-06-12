package tr.ozanbey.agricalc.webapp.service.converter.animal.dairycow;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.dairycow.EnumCostType;

@Converter(autoApply = true)
public class EnumCostTypeConverter implements AttributeConverter<EnumCostType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EnumCostType costType) {
        return costType != null ? costType.getValue() : null;
    }

    @Override
    public EnumCostType convertToEntityAttribute(Integer costType) {
        return costType != null ? EnumCostType.fromValue(costType) : null;
    }
}
